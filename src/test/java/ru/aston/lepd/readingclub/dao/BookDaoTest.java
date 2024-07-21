package ru.aston.lepd.readingclub.dao;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aston.lepd.readingclub.entity.Author;
import ru.aston.lepd.readingclub.entity.Book;
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
import static ru.aston.lepd.readingclub.Constants.*;


@ExtendWith(MockitoExtension.class)
class BookDaoTest {


    private static MockedStatic<DataSource> dataSource;
    @Mock
    private Connection connection;
    @Mock
    private ResultSet resultSet;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private AuthorBookDao authorBookDao;
    @Mock
    private AuthorDao authorDao;
    @Mock
    private ReaderDao readerDao;
    @InjectMocks
    private BookDao bookDao;




    @BeforeAll
    static void setUp() {
        dataSource = mockStatic(DataSource.class);
    }

    @BeforeEach
    void init() {
        bookDao.setAuthorDao(authorDao);
        bookDao.setReaderDao(readerDao);
        dataSource.when(DataSource::getConnection).thenReturn(connection);
    }





    @Test
    void getAuthorDao() {
        AuthorDao actualResult = bookDao.getAuthorDao();

        assertNotNull(actualResult);
    }



    @Test
    void getReaderDao() {
        ReaderDao actualResult = bookDao.getReaderDao();

        assertNotNull(actualResult);
    }



    @Test
    void findById_whenValidData_thenReturnOptionalOfBook() throws SQLException {
        final Long bookId = 1L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, bookId);
        doReturn(resultSet).when(preparedStatement).executeQuery();
        doReturn(true).when(resultSet).next();
        doReturn(BOOK_1.getId()).when(resultSet).getLong("id");
        doReturn(BOOK_1.getTitle()).when(resultSet).getString("title");
        doReturn(BOOK_1.getInventoryNumber()).when(resultSet).getLong("inventory_number");
        doReturn(BOOK_1.getReader().getId()).when(resultSet).getLong("reader_id");
        doReturn(Optional.of(READER_1)).when(readerDao).findById(anyLong());

        Optional<Book> actualResult = bookDao.findById(bookId);

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, bookId);
        verify(preparedStatement).executeQuery();
        verify(resultSet).next();
        verify(resultSet).getLong("id");
        verify(resultSet).getString("title");
        verify(resultSet).getLong("inventory_number");
        verify(resultSet).getLong("reader_id");
        verify(readerDao).findById(anyLong());
        assertTrue(actualResult.isPresent());
        assertEquals(bookId, actualResult.get().getId());
    }

    @Test
    void findById_whenInvalidData_thenReturnEmptyOptional() throws SQLException {
        final Long bookId = 666L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, bookId);
        doReturn(resultSet).when(preparedStatement).executeQuery();
        doReturn(false).when(resultSet).next();

        Optional<Book> actualResult = bookDao.findById(bookId);

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, bookId);
        verify(preparedStatement).executeQuery();
        verify(resultSet).next();
        assertTrue(actualResult.isEmpty());
    }

    @Test
    void findById_whenError_thenTrowException() throws SQLException {
        final Long bookId = 3L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, bookId);
        doThrow(SQLException.class).when(preparedStatement).executeQuery();

        assertThrows(DaoException.class, () -> bookDao.findById(bookId));

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, bookId);
        verify(preparedStatement).executeQuery();
    }



    @Test
    void findAll_whenValidData_thenReturnList() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doReturn(resultSet).when(preparedStatement).executeQuery();
        doReturn(true, true, true, false).when(resultSet).next();
        doReturn(BOOK_1.getId()).when(resultSet).getLong("id");
        doReturn(BOOK_1.getTitle()).when(resultSet).getString("title");
        doReturn(BOOK_1.getInventoryNumber()).when(resultSet).getLong("inventory_number");
        doReturn(BOOK_1.getReader().getId()).when(resultSet).getLong("reader_id");
        doReturn(Optional.of(READER_1)).when(readerDao).findById(anyLong());

        List<Book> actualResult = bookDao.findAll();

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).executeQuery();
        verify(resultSet, times(4)).next();
        verify(resultSet, times(3)).getLong("id");
        verify(resultSet, times(3)).getString("title");
        verify(resultSet, times(3)).getLong("inventory_number");
        verify(resultSet, times(3)).getLong("reader_id");
        verify(readerDao, times(3)).findById(anyLong());
        assertEquals(3, actualResult.size());
    }

    @Test
    void findAll_whenInvalidData_thenReturnEmptyList() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doReturn(resultSet).when(preparedStatement).executeQuery();
        doReturn(false).when(resultSet).next();

        List<Book> actualResult = bookDao.findAll();

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).executeQuery();
        verify(resultSet).next();
        assertTrue(actualResult.isEmpty());
    }

    @Test
    void findAll_whenError_ThrowException() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doThrow(SQLException.class).when(preparedStatement).executeQuery();

        assertThrows(DaoException.class, () -> bookDao.findAll());

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).executeQuery();
    }



    @Test
    void findAllByReaderId_whenValidData_thenReturnList() throws SQLException {
        final Long readerId = 1L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, readerId);
        doReturn(resultSet).when(preparedStatement).executeQuery();
        doReturn(true, true, false).when(resultSet).next();
        doReturn(BOOK_1.getId()).when(resultSet).getLong("id");
        doReturn(BOOK_1.getTitle()).when(resultSet).getString("title");
        doReturn(BOOK_1.getInventoryNumber()).when(resultSet).getLong("inventory_number");
        doReturn(BOOK_1.getReader().getId()).when(resultSet).getLong("reader_id");
        doReturn(Optional.of(READER_1)).when(readerDao).findById(anyLong());

        List<Book> actualResult = bookDao.findAllByReaderId(readerId);

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).executeQuery();
        verify(resultSet, times(3)).next();
        verify(resultSet, times(2)).getLong("id");
        verify(resultSet, times(2)).getString("title");
        verify(resultSet, times(2)).getLong("inventory_number");
        verify(resultSet, times(2)).getLong("reader_id");
        verify(readerDao, times(2)).findById(anyLong());
        assertEquals(2, actualResult.size());
    }

    @Test
    void findAllByReaderId_whenInvalidData_thenReturnEmptyList() throws SQLException {
        final Long readerId = 666L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, readerId);
        doReturn(resultSet).when(preparedStatement).executeQuery();
        doReturn(false).when(resultSet).next();

        List<Book> actualResult = bookDao.findAllByReaderId(readerId);

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, readerId);
        verify(preparedStatement).executeQuery();
        verify(resultSet).next();
        assertTrue(actualResult.isEmpty());
    }

    @Test
    void findAllByReaderId_whenError_ThrowException() throws SQLException {
        final Long readerId = 3L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, readerId);
        doThrow(SQLException.class).when(preparedStatement).executeQuery();

        assertThrows(DaoException.class, () -> bookDao.findAllByReaderId(readerId));

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, readerId);
        verify(preparedStatement).executeQuery();
    }



    @Test
    void findAllByAuthorId_whenValidData_thenReturnList() throws SQLException {
        final Long authorId = 1L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, authorId);
        doReturn(resultSet).when(preparedStatement).executeQuery();
        doReturn(true, true, false).when(resultSet).next();
        doReturn(BOOK_1.getId()).when(resultSet).getLong("id");
        doReturn(BOOK_1.getTitle()).when(resultSet).getString("title");
        doReturn(BOOK_1.getInventoryNumber()).when(resultSet).getLong("inventory_number");
        doReturn(BOOK_1.getReader().getId()).when(resultSet).getLong("reader_id");
        doReturn(Optional.of(READER_1)).when(readerDao).findById(anyLong());

        List<Book> actualResult = bookDao.findAllByAuthorId(authorId);

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).executeQuery();
        verify(resultSet, times(3)).next();
        verify(resultSet, times(2)).getLong("id");
        verify(resultSet, times(2)).getString("title");
        verify(resultSet, times(2)).getLong("inventory_number");
        verify(resultSet, times(2)).getLong("reader_id");
        verify(readerDao, times(2)).findById(anyLong());
        assertEquals(2, actualResult.size());
    }

    @Test
    void findAllByAuthorId_whenInvalidData_thenReturnEmptyList() throws SQLException {
        final Long authorId = 666L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, authorId);
        doReturn(resultSet).when(preparedStatement).executeQuery();
        doReturn(false).when(resultSet).next();

        List<Book> actualResult = bookDao.findAllByAuthorId(authorId);

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, authorId);
        verify(preparedStatement).executeQuery();
        verify(resultSet).next();
        assertTrue(actualResult.isEmpty());
    }

    @Test
    void findAllByAuthorId_whenError_ThrowException() throws SQLException {
        final Long authorId = 3L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, authorId);
        doThrow(SQLException.class).when(preparedStatement).executeQuery();

        assertThrows(DaoException.class, () -> bookDao.findAllByAuthorId(authorId));

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, authorId);
        verify(preparedStatement).executeQuery();
    }



    @Test
    void getAuthorsForBook_whenValidData_thenReturnList() {
        final Long bookId = 1L;
        final List<Author> authors = List.of(AUTHOR_1, AUTHOR_2);
        doReturn(authors).when(authorDao).findAllByBookId(bookId);

        List<Author> actualResult = bookDao.getAuthorsForBook(bookId);

        verify(authorDao).findAllByBookId(bookId);
        assertEquals(2, actualResult.size());
    }

    @Test
    void getAuthorsForBook_whenInvalidData_thenReturnEmptyList() {
        final Long bookId = 666L;
        doReturn(Collections.emptyList()).when(authorDao).findAllByBookId(bookId);

        List<Author> actualResult = bookDao.getAuthorsForBook(bookId);

        assertTrue(actualResult.isEmpty());
    }



    @Test
    void save_whenValidData_thenSuccess() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString(), anyInt());
        doNothing().when(preparedStatement).setString(1, BOOK_1.getTitle());
        doNothing().when(preparedStatement).setLong(2, BOOK_1.getInventoryNumber());
        doNothing().when(preparedStatement).setLong(3, BOOK_1.getReader().getId());
        doReturn(1).when(preparedStatement).executeUpdate();
        doReturn(resultSet).when(preparedStatement).getGeneratedKeys();
        doReturn(true).when(resultSet).next();
        doReturn(BOOK_1.getId()).when(resultSet).getLong("id");
        doReturn(true).when(authorBookDao).save(anyLong(), anyLong());

        Book actualResult = bookDao.save(BOOK_1);

        verify(connection).prepareStatement(anyString(), anyInt());
        verify(preparedStatement).setString(1, BOOK_1.getTitle());
        verify(preparedStatement).setLong(2, BOOK_1.getInventoryNumber());
        verify(preparedStatement).setLong(3, BOOK_1.getReader().getId());
        verify(preparedStatement).executeUpdate();
        verify(preparedStatement).getGeneratedKeys();
        verify(resultSet).next();
        verify(resultSet).getLong("id");
        verify(authorBookDao, times(2)).save(anyLong(), anyLong());
        assertEquals(BOOK_1.getId(), actualResult.getId());
    }

    @Test
    void save_whenGeneratedKeysNextIsFalse() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString(), anyInt());
        doNothing().when(preparedStatement).setString(1, BOOK_1.getTitle());
        doNothing().when(preparedStatement).setLong(2, BOOK_1.getInventoryNumber());
        doNothing().when(preparedStatement).setLong(3, BOOK_1.getReader().getId());
        doReturn(0).when(preparedStatement).executeUpdate();
        doReturn(resultSet).when(preparedStatement).getGeneratedKeys();
        doReturn(false).when(resultSet).next();

        bookDao.save(BOOK_1);

        verify(connection).prepareStatement(anyString(), anyInt());
        verify(preparedStatement).setString(1, BOOK_1.getTitle());
        verify(preparedStatement).setLong(2, BOOK_1.getInventoryNumber());
        verify(preparedStatement).setLong(3, BOOK_1.getReader().getId());
        verify(preparedStatement).executeUpdate();
        verify(preparedStatement).getGeneratedKeys();
        verify(resultSet).next();
    }

    @Test
    void save_whenError_ThrowException() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString(), anyInt());
        doNothing().when(preparedStatement).setString(1, BOOK_1.getTitle());
        doNothing().when(preparedStatement).setLong(2, BOOK_1.getInventoryNumber());
        doNothing().when(preparedStatement).setLong(3, BOOK_1.getReader().getId());
        doThrow(SQLException.class).when(preparedStatement).executeUpdate();

        assertThrows(DaoException.class, () -> bookDao.save(BOOK_1));

        verify(connection).prepareStatement(anyString(), anyInt());
        verify(preparedStatement).setString(1, BOOK_1.getTitle());
        verify(preparedStatement).setLong(2, BOOK_1.getInventoryNumber());
        verify(preparedStatement).setLong(3, BOOK_1.getReader().getId());
        verify(preparedStatement).executeUpdate();
    }



    @Test
    void update_whenValidData_thenReturnTrue() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setString(1, BOOK_1.getTitle());
        doNothing().when(preparedStatement).setLong(2, BOOK_1.getInventoryNumber());
        doNothing().when(preparedStatement).setLong(3, BOOK_1.getReader().getId());
        doNothing().when(preparedStatement).setLong(4, BOOK_1.getId());
        doReturn(1).when(preparedStatement).executeUpdate();
        doReturn(true).when(authorBookDao).deleteAllByBookId(anyLong());
        doReturn(true).when(authorBookDao).save(anyLong(), anyLong());

        boolean actualResult = bookDao.update(BOOK_1);

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setString(1, BOOK_1.getTitle());
        verify(preparedStatement).setLong(2, BOOK_1.getInventoryNumber());
        verify(preparedStatement).setLong(3, BOOK_1.getReader().getId());
        verify(preparedStatement).setLong(4, BOOK_1.getId());
        verify(preparedStatement).executeUpdate();
        verify(authorBookDao).deleteAllByBookId(anyLong());
        verify(authorBookDao, times(2)).save(anyLong(), anyLong());
        assertTrue(actualResult);
    }

    @Test
    void update_whenUpdateResultIsZero_thenReturnFalse() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setString(1, BOOK_1.getTitle());
        doNothing().when(preparedStatement).setLong(2, BOOK_1.getInventoryNumber());
        doNothing().when(preparedStatement).setLong(3, BOOK_1.getReader().getId());
        doNothing().when(preparedStatement).setLong(4, BOOK_1.getId());
        doReturn(0).when(preparedStatement).executeUpdate();

        boolean actualResult = bookDao.update(BOOK_1);

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setString(1, BOOK_1.getTitle());
        verify(preparedStatement).setLong(2, BOOK_1.getInventoryNumber());
        verify(preparedStatement).setLong(3, BOOK_1.getReader().getId());
        verify(preparedStatement).setLong(4, BOOK_1.getId());
        verify(preparedStatement).executeUpdate();
        assertFalse(actualResult);
    }

    @Test
    void update_whenError_thenTrowException() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setString(1, BOOK_1.getTitle());
        doNothing().when(preparedStatement).setLong(2, BOOK_1.getInventoryNumber());
        doNothing().when(preparedStatement).setLong(3, BOOK_1.getReader().getId());
        doNothing().when(preparedStatement).setLong(4, BOOK_1.getId());
        doThrow(SQLException.class).when(preparedStatement).executeUpdate();

        assertThrows(DaoException.class, () -> bookDao.update(BOOK_1));

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setString(1, BOOK_1.getTitle());
        verify(preparedStatement).setLong(2, BOOK_1.getInventoryNumber());
        verify(preparedStatement).setLong(3, BOOK_1.getReader().getId());
        verify(preparedStatement).setLong(4, BOOK_1.getId());
        verify(preparedStatement).executeUpdate();
    }



    @Test
    void delete_whenValidData_thenReturnTrue() throws SQLException {
        final Long bookId = 1L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, bookId);
        doReturn(true).when(authorBookDao).deleteAllByBookId(bookId);
        doReturn(1).when(preparedStatement).executeUpdate();

        boolean actualResult = bookDao.delete(bookId);

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, bookId);
        verify(authorBookDao).deleteAllByBookId(bookId);
        verify(preparedStatement).executeUpdate();
        assertTrue(actualResult);
    }

    @Test
    void delete_whenInvalidData_thenReturnFalse() throws SQLException {
        final Long bookId = 666L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, bookId);
        doReturn(false).when(authorBookDao).deleteAllByBookId(bookId);
        doReturn(0).when(preparedStatement).executeUpdate();

        boolean actualResult = bookDao.delete(bookId);

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, bookId);
        verify(authorBookDao).deleteAllByBookId(bookId);
        verify(preparedStatement).executeUpdate();
        assertFalse(actualResult);
    }


    @Test
    void delete_whenError_thenThrowException() throws SQLException {
        final Long bookId = 3L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, bookId);
        doReturn(false).when(authorBookDao).deleteAllByBookId(bookId);
        doThrow(SQLException.class).when(preparedStatement).executeUpdate();

        assertThrows(DaoException.class, () -> bookDao.delete(bookId));

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, bookId);
        verify(authorBookDao).deleteAllByBookId(bookId);
        verify(preparedStatement).executeUpdate();
    }



    @Test
    void deleteAllByReaderId_whenValidData_thenReturnTrue() throws SQLException {
        final Long readerId = 1L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, readerId);
        doReturn(resultSet).when(preparedStatement).executeQuery();
        doReturn(true, true, false).when(resultSet).next();
        doReturn(true).when(authorBookDao).deleteAllByBookId(anyLong());
        doReturn(1).when(preparedStatement).executeUpdate();

        boolean actualResult = bookDao.deleteAllByReaderId(readerId);

        verify(connection, times(2)).prepareStatement(anyString());
        verify(preparedStatement, times(2)).setLong(1, readerId);
        verify(preparedStatement).executeQuery();
        verify(resultSet, times(3)).next();
        verify(authorBookDao, times(2)).deleteAllByBookId(anyLong());
        verify(preparedStatement).executeUpdate();
        assertTrue(actualResult);
    }

    @Test
    void deleteAllByReaderId_whenInvalidData_thenReturnFalse() throws SQLException {
        final Long readerId = 666L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, readerId);
        doReturn(resultSet).when(preparedStatement).executeQuery();
        doReturn(false).when(resultSet).next();
        doReturn(0).when(preparedStatement).executeUpdate();

        boolean actualResult = bookDao.deleteAllByReaderId(readerId);

        verify(connection, times(2)).prepareStatement(anyString());
        verify(preparedStatement, times(2)).setLong(1, readerId);
        verify(preparedStatement).executeQuery();
        verify(resultSet).next();
        verify(preparedStatement).executeUpdate();
        assertFalse(actualResult);
    }

    @Test
    void deleteAllByReaderId_whenError_thenThrowException() throws SQLException {
        final Long readerId = 3L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, readerId);
        doReturn(resultSet).when(preparedStatement).executeQuery();
        doReturn(true, true, false).when(resultSet).next();
        doReturn(true).when(authorBookDao).deleteAllByBookId(anyLong());
        doThrow(SQLException.class).when(preparedStatement).executeUpdate();

        assertThrows(DaoException.class, () -> bookDao.deleteAllByReaderId(readerId));

        verify(connection, times(2)).prepareStatement(anyString());
        verify(preparedStatement, times(2)).setLong(1, readerId);
        verify(preparedStatement).executeQuery();
        verify(resultSet, times(3)).next();
        verify(authorBookDao, times(2)).deleteAllByBookId(anyLong());
        verify(preparedStatement).executeUpdate();
    }



    @Test
    void isContainById_whenValidData_thenReturnTrue() throws SQLException {
        final Long bookId = 1L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, bookId);
        doReturn(resultSet).when(preparedStatement).executeQuery();
        doReturn(true).when(resultSet).next();

        boolean actualResult = bookDao.isContainById(bookId);

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, bookId);
        verify(preparedStatement).executeQuery();
        verify(resultSet).next();
        assertTrue(actualResult);
    }

    @Test
    void isContainById_whenInvalidData_thenReturnFalse() throws SQLException {
        final Long bookId = 666L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, bookId);
        doReturn(resultSet).when(preparedStatement).executeQuery();
        doReturn(false).when(resultSet).next();

        boolean actualResult = bookDao.isContainById(bookId);

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, bookId);
        verify(preparedStatement).executeQuery();
        verify(resultSet).next();
        assertFalse(actualResult);
    }

    @Test
    void isContainById_whenError_thenThrowException() throws SQLException {
        final Long bookId = 3L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, bookId);
        doThrow(SQLException.class).when(preparedStatement).executeQuery();

        assertThrows(DaoException.class, () -> bookDao.isContainById(bookId));

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, bookId);
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