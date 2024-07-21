package ru.aston.lepd.readingclub.dao;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aston.lepd.readingclub.entity.Book;
import ru.aston.lepd.readingclub.entity.Reader;
import ru.aston.lepd.readingclub.exception.DaoException;
import ru.aston.lepd.readingclub.util.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static ru.aston.lepd.readingclub.Constants.*;


@ExtendWith(MockitoExtension.class)
class ReaderDaoTest {

    private static MockedStatic<DataSource> dataSource;
    @Mock
    private Connection connection;
    @Mock
    private ResultSet resultSet;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private BookDao bookDao;
    @InjectMocks
    private ReaderDao readerDao;


    @BeforeAll
    static void setUp() {
        dataSource = mockStatic(DataSource.class);
    }

    @BeforeEach
    void init() {
        readerDao.setBookDao(bookDao);
        dataSource.when(DataSource::getConnection).thenReturn(connection);
    }



    @Test
    void getBookDao() {
        BookDao actualResult = readerDao.getBookDao();

        assertNotNull(actualResult);
    }



    @Test
    void findById_whenValidData_thenReturnOptionalOfReader() throws SQLException {
        final Long readerId = 1L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, readerId);
        doReturn(resultSet).when(preparedStatement).executeQuery();
        doReturn(true).when(resultSet).next();
        doReturn(READER_1.getId()).when(resultSet).getLong("id");
        doReturn(READER_1.getName()).when(resultSet).getString("name");
        doReturn(READER_1.getSurname()).when(resultSet).getString("surname");
        doReturn(READER_1.getPhone()).when(resultSet).getString("phone");
        doReturn(READER_1.getAddress()).when(resultSet).getString("address");

        Optional<Reader> actualResult = readerDao.findById(readerId);

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, readerId);
        verify(preparedStatement).executeQuery();
        verify(resultSet).next();
        verify(resultSet).getLong("id");
        verify(resultSet).getString("name");
        verify(resultSet).getString("surname");
        verify(resultSet).getString("phone");
        verify(resultSet).getString("address");
        assertTrue(actualResult.isPresent());
        assertEquals(readerId, actualResult.get().getId());
    }

    @Test
    void findById_whenInvalidData_thenReturnEmptyOptional() throws SQLException {
        final Long readerId = 666L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, readerId);
        doReturn(resultSet).when(preparedStatement).executeQuery();
        doReturn(false).when(resultSet).next();

        Optional<Reader> actualResult = readerDao.findById(readerId);

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, readerId);
        verify(preparedStatement).executeQuery();
        verify(resultSet).next();
        assertFalse(actualResult.isPresent());
    }

    @Test
    void findById_whenError_thenTrowException() throws SQLException {
        final Long readerId = 3L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, readerId);
        doThrow(SQLException.class).when(preparedStatement).executeQuery();

        assertThrows(DaoException.class, () -> readerDao.findById(readerId));

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, readerId);
        verify(preparedStatement).executeQuery();
    }



    @Test
    void findAll_whenValidData_thenReturnList() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doReturn(resultSet).when(preparedStatement).executeQuery();
        doReturn(true, true, true, false).when(resultSet).next();
        doReturn(READER_1.getId()).when(resultSet).getLong("id");
        doReturn(READER_1.getName()).when(resultSet).getString("name");
        doReturn(READER_1.getSurname()).when(resultSet).getString("surname");
        doReturn(READER_1.getPhone()).when(resultSet).getString("phone");
        doReturn(READER_1.getAddress()).when(resultSet).getString("address");

        List<Reader> actualResult = readerDao.findAll();

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).executeQuery();
        verify(resultSet, times(4)).next();
        verify(resultSet, times(3)).getLong("id");
        verify(resultSet, times(3)).getString("name");
        verify(resultSet, times(3)).getString("surname");
        verify(resultSet, times(3)).getString("phone");
        verify(resultSet, times(3)).getString("address");
        assertEquals(3, actualResult.size());
    }

    @Test
    void findAll_whenInvalidData_thenReturnEmptyList() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doReturn(resultSet).when(preparedStatement).executeQuery();
        doReturn(false).when(resultSet).next();

        List<Reader> actualResult = readerDao.findAll();

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).executeQuery();
        verify(resultSet).next();
        assertTrue(actualResult.isEmpty());
    }

    @Test
    void findAll_whenError_ThrowException() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doThrow(SQLException.class).when(preparedStatement).executeQuery();

        assertThrows(DaoException.class, () -> readerDao.findAll());

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).executeQuery();
    }



    @Test
    void getBooksForReader_whenValidData_thenReturnList() {
        final Long readerId = 1L;
        final List<Book> books = List.of(BOOK_1, BOOK_2);
        doReturn(books).when(bookDao).findAllByReaderId(readerId);

        List<Book> actualResult = readerDao.getBooksForReader(readerId);

        verify(bookDao).findAllByReaderId(readerId);
        assertEquals(2, actualResult.size());
    }

    @Test
    void getBooksForReader_whenInvalidData_thenReturnEmptyList() {
        final Long readerId = 666L;
        doReturn(Collections.emptyList()).when(bookDao).findAllByReaderId(readerId);

        List<Book> actualResult = readerDao.getBooksForReader(readerId);

        assertTrue(actualResult.isEmpty());
    }



    @Test
    void save_whenValidData_thenSuccess() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString(), anyInt());
        doNothing().when(preparedStatement).setString(1, READER_1.getName());
        doNothing().when(preparedStatement).setString(2, READER_1.getSurname());
        doNothing().when(preparedStatement).setString(3, READER_1.getPhone());
        doNothing().when(preparedStatement).setString(4, READER_1.getAddress());
        doReturn(1).when(preparedStatement).executeUpdate();
        doReturn(resultSet).when(preparedStatement).getGeneratedKeys();
        doReturn(true).when(resultSet).next();
        doReturn(READER_1.getId()).when(resultSet).getLong("id");
        doReturn(BOOK_1).when(bookDao).save(any(Book.class));

        Reader actualResult = readerDao.save(READER_1);

        verify(connection).prepareStatement(anyString(), anyInt());
        verify(preparedStatement).setString(1, READER_1.getName());
        verify(preparedStatement).setString(2, READER_1.getSurname());
        verify(preparedStatement).setString(3, READER_1.getPhone());
        verify(preparedStatement).setString(4, READER_1.getAddress());
        verify(preparedStatement).executeUpdate();
        verify(preparedStatement).getGeneratedKeys();
        verify(resultSet).next();
        verify(resultSet).getLong("id");
        verify(bookDao).save(any(Book.class));
        assertEquals(BOOK_1.getId(), actualResult.getId());
    }

    @Test
    void save_whenGeneratedKeysNextIsFalse() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString(), anyInt());
        doNothing().when(preparedStatement).setString(1, READER_1.getName());
        doNothing().when(preparedStatement).setString(2, READER_1.getSurname());
        doNothing().when(preparedStatement).setString(3, READER_1.getPhone());
        doNothing().when(preparedStatement).setString(4, READER_1.getAddress());
        doReturn(1).when(preparedStatement).executeUpdate();
        doReturn(resultSet).when(preparedStatement).getGeneratedKeys();
        doReturn(false).when(resultSet).next();

        readerDao.save(READER_1);

        verify(connection).prepareStatement(anyString(), anyInt());
        verify(preparedStatement).setString(1, READER_1.getName());
        verify(preparedStatement).setString(2, READER_1.getSurname());
        verify(preparedStatement).setString(3, READER_1.getPhone());
        verify(preparedStatement).setString(4, READER_1.getAddress());
        verify(preparedStatement).executeUpdate();
        verify(preparedStatement).getGeneratedKeys();
        verify(resultSet).next();
    }

    @Test
    void save_whenError_ThrowException() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString(), anyInt());
        doNothing().when(preparedStatement).setString(1, READER_1.getName());
        doNothing().when(preparedStatement).setString(2, READER_1.getSurname());
        doNothing().when(preparedStatement).setString(3, READER_1.getPhone());
        doNothing().when(preparedStatement).setString(4, READER_1.getAddress());
        doThrow(SQLException.class).when(preparedStatement).executeUpdate();

        assertThrows(DaoException.class, () -> readerDao.save(READER_1));

        verify(connection).prepareStatement(anyString(), anyInt());
        verify(preparedStatement).setString(1, READER_1.getName());
        verify(preparedStatement).setString(2, READER_1.getSurname());
        verify(preparedStatement).setString(3, READER_1.getPhone());
        verify(preparedStatement).setString(4, READER_1.getAddress());
        verify(preparedStatement).executeUpdate();
    }



    @Test
    void update_whenValidData_thenTrue() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setString(1, READER_1.getName());
        doNothing().when(preparedStatement).setString(2, READER_1.getSurname());
        doNothing().when(preparedStatement).setString(3, READER_1.getPhone());
        doNothing().when(preparedStatement).setString(4, READER_1.getAddress());
        doReturn(1).when(preparedStatement).executeUpdate();
        doReturn(true).when(bookDao).update(any(Book.class));

        boolean actualResult = readerDao.update(READER_1);

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setString(1, READER_1.getName());
        verify(preparedStatement).setString(2, READER_1.getSurname());
        verify(preparedStatement).setString(3, READER_1.getPhone());
        verify(preparedStatement).setString(4, READER_1.getAddress());
        verify(preparedStatement).executeUpdate();
        verify(bookDao).update(any(Book.class));
        assertTrue(actualResult);
    }

    @Test
    void update_whenUpdateResultIsZero_thenReturnFalse() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setString(1, READER_1.getName());
        doNothing().when(preparedStatement).setString(2, READER_1.getSurname());
        doNothing().when(preparedStatement).setString(3, READER_1.getPhone());
        doNothing().when(preparedStatement).setString(4, READER_1.getAddress());
        doReturn(0).when(preparedStatement).executeUpdate();

        boolean actualResult = readerDao.update(READER_1);

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setString(1, READER_1.getName());
        verify(preparedStatement).setString(2, READER_1.getSurname());
        verify(preparedStatement).setString(3, READER_1.getPhone());
        verify(preparedStatement).setString(4, READER_1.getAddress());
        verify(preparedStatement).executeUpdate();
        assertFalse(actualResult);
    }

    @Test
    void update_whenBookGetIdIsNull_thenReturnTrue() throws SQLException {
        final Book book = new Book();
        final Reader reader = new Reader();
        reader.setId(1L);
        reader.setName("Ivan");
        reader.setSurname("Ivanov");
        reader.setPhone("71111111111");
        reader.setAddress("Lenina 11");
        reader.setBooks(List.of(book));
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setString(1, reader.getName());
        doNothing().when(preparedStatement).setString(2, reader.getSurname());
        doNothing().when(preparedStatement).setString(3, reader.getPhone());
        doNothing().when(preparedStatement).setString(4, reader.getAddress());
        doReturn(1).when(preparedStatement).executeUpdate();
        doReturn(book).when(bookDao).save(any(Book.class));

        boolean actualResult = readerDao.update(reader);

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setString(1, reader.getName());
        verify(preparedStatement).setString(2, reader.getSurname());
        verify(preparedStatement).setString(3, reader.getPhone());
        verify(preparedStatement).setString(4, reader.getAddress());
        verify(preparedStatement).executeUpdate();
        verify(bookDao).save(any(Book.class));
        assertTrue(actualResult);
    }

    @Test
    void update_whenError_ThrowException() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setString(1, READER_1.getName());
        doNothing().when(preparedStatement).setString(2, READER_1.getSurname());
        doNothing().when(preparedStatement).setString(3, READER_1.getPhone());
        doNothing().when(preparedStatement).setString(4, READER_1.getAddress());
        doThrow(SQLException.class).when(preparedStatement).executeUpdate();

        assertThrows(DaoException.class, () -> readerDao.update(READER_1));

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setString(1, READER_1.getName());
        verify(preparedStatement).setString(2, READER_1.getSurname());
        verify(preparedStatement).setString(3, READER_1.getPhone());
        verify(preparedStatement).setString(4, READER_1.getAddress());
        verify(preparedStatement).executeUpdate();
    }



    @Test
    void delete_whenValidData_thenReturnTrue() throws SQLException {
        final Long readerId = 1L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, readerId);
        doReturn(true).when(bookDao).deleteAllByReaderId(readerId);
        doReturn(1).when(preparedStatement).executeUpdate();

        boolean actualResult = readerDao.delete(readerId);

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, readerId);
        bookDao.deleteAllByReaderId(readerId);
        verify(preparedStatement).executeUpdate();
        assertTrue(actualResult);
    }

    @Test
    void delete_whenInvalidData_thenReturnFalse() throws SQLException {
        final Long readerId = 666L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, readerId);
        doReturn(false).when(bookDao).deleteAllByReaderId(readerId);
        doReturn(0).when(preparedStatement).executeUpdate();

        boolean actualResult = readerDao.delete(readerId);

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, readerId);
        bookDao.deleteAllByReaderId(readerId);
        verify(preparedStatement).executeUpdate();
        assertFalse(actualResult);
    }

    @Test
    void delete_whenError_ThrowException() throws SQLException {
        final Long readerId = 3L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, readerId);
        doReturn(false).when(bookDao).deleteAllByReaderId(readerId);
        doThrow(SQLException.class).when(preparedStatement).executeUpdate();

        assertThrows(DaoException.class, () -> readerDao.delete(readerId));

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, readerId);
        bookDao.deleteAllByReaderId(readerId);
        verify(preparedStatement).executeUpdate();
    }



    @Test
    void isContainById_whenValidData_thenReturnTrue() throws SQLException {
        final Long readerId = 1L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, readerId);
        doReturn(resultSet).when(preparedStatement).executeQuery();
        doReturn(true).when(resultSet).next();

        boolean actualResult = readerDao.isContainById(readerId);

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, readerId);
        verify(preparedStatement).executeQuery();
        verify(resultSet).next();
        assertTrue(actualResult);
    }

    @Test
    void isContainById_whenInvalidData_thenReturnFalse() throws SQLException {
        final Long readerId = 666L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, readerId);
        doReturn(resultSet).when(preparedStatement).executeQuery();
        doReturn(false).when(resultSet).next();

        boolean actualResult = readerDao.isContainById(readerId);

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, readerId);
        verify(preparedStatement).executeQuery();
        verify(resultSet).next();
        assertFalse(actualResult);
    }

    @Test
    void isContainById_whenError_ThrowException() throws SQLException {
        final Long readerId = 3L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, readerId);
        doThrow(SQLException.class).when(preparedStatement).executeQuery();

        assertThrows(DaoException.class, () -> readerDao.isContainById(readerId));

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, readerId);
        verify(preparedStatement).executeQuery();
    }




    @AfterEach
    void tearDown() throws SQLException {
        preparedStatement.close();
        connection.close();
    }

    @AfterAll
    static void deleteAll() {
        dataSource.close();
    }


}


















