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
class AuthorDaoTest {


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
    private BookDao bookDao;

    @InjectMocks
    private AuthorDao authorDao;



    @BeforeAll
    static void setUp() {
        dataSource = mockStatic(DataSource.class);
    }

    @BeforeEach
    void init() {
        authorDao.setBookDao(bookDao);
        dataSource.when(DataSource::getConnection).thenReturn(connection);
    }





    @Test
    void getBookDao() {
        BookDao actualResult = authorDao.getBookDao();

        assertNotNull(actualResult);
    }



    @Test
    void findById_whenValidData_thenReturnOptionalOfAuthor() throws SQLException {
        final Long authorId = 1L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, authorId);
        doReturn(resultSet).when(preparedStatement).executeQuery();
        doReturn(true).when(resultSet).next();
        doReturn(READER_1.getId()).when(resultSet).getLong("id");
        doReturn(READER_1.getName()).when(resultSet).getString("full_name");
        doReturn(READER_1.getSurname()).when(resultSet).getString("personal_info");

        Optional<Author> actualResult = authorDao.findById(authorId);

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, authorId);
        verify(preparedStatement).executeQuery();
        verify(resultSet).next();
        verify(resultSet).getLong("id");
        verify(resultSet).getString("full_name");
        verify(resultSet).getString("personal_info");
        assertTrue(actualResult.isPresent());
        assertEquals(authorId, actualResult.get().getId());
    }

    @Test
    void findById_whenInvalidData_thenReturnEmptyOptional() throws SQLException {
        final Long authorId = 666L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, authorId);
        doReturn(resultSet).when(preparedStatement).executeQuery();
        doReturn(false).when(resultSet).next();

        Optional<Author> actualResult = authorDao.findById(authorId);

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, authorId);
        verify(preparedStatement).executeQuery();
        verify(resultSet).next();
        assertFalse(actualResult.isPresent());
    }

    @Test
    void findById_whenError_thenTrowException() throws SQLException {
        final Long authorId = 3L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, authorId);
        doThrow(SQLException.class).when(preparedStatement).executeQuery();

        assertThrows(DaoException.class, () -> authorDao.findById(authorId));

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, authorId);
        verify(preparedStatement).executeQuery();
    }



    @Test
    void findAll_whenValidData_thenReturnList() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doReturn(resultSet).when(preparedStatement).executeQuery();
        doReturn(true, true, true, false).when(resultSet).next();
        doReturn(READER_1.getId()).when(resultSet).getLong("id");
        doReturn(READER_1.getName()).when(resultSet).getString("full_name");
        doReturn(READER_1.getSurname()).when(resultSet).getString("personal_info");

        List<Author> actualResult = authorDao.findAll();

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).executeQuery();
        verify(resultSet, times(4)).next();
        verify(resultSet, times(3)).getLong("id");
        verify(resultSet, times(3)).getString("full_name");
        verify(resultSet, times(3)).getString("personal_info");
        assertEquals(3, actualResult.size());
    }

    @Test
    void findAll_whenInvalidData_thenReturnEmptyList() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doReturn(resultSet).when(preparedStatement).executeQuery();
        doReturn(false).when(resultSet).next();

        List<Author> actualResult = authorDao.findAll();

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).executeQuery();
        verify(resultSet).next();
        assertTrue(actualResult.isEmpty());
    }

    @Test
    void findAll_whenError_ThrowException() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doThrow(SQLException.class).when(preparedStatement).executeQuery();

        assertThrows(DaoException.class, () -> authorDao.findAll());

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).executeQuery();
    }



    @Test
    void findAllByBookId_whenValidData_thenReturnList() throws SQLException {
        final Long bookId = 1L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, bookId);
        doReturn(resultSet).when(preparedStatement).executeQuery();
        doReturn(true, true, false).when(resultSet).next();
        doReturn(READER_1.getId()).when(resultSet).getLong("id");
        doReturn(READER_1.getName()).when(resultSet).getString("full_name");
        doReturn(READER_1.getSurname()).when(resultSet).getString("personal_info");

        List<Author> actualResult = authorDao.findAllByBookId(bookId);

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, bookId);
        verify(preparedStatement).executeQuery();
        verify(resultSet, times(3)).next();
        verify(resultSet, times(2)).getLong("id");
        verify(resultSet, times(2)).getString("full_name");
        verify(resultSet, times(2)).getString("personal_info");
        assertEquals(2, actualResult.size());
    }

    @Test
    void findAllByBookId_whenInvalidData_thenReturnEmptyList() throws SQLException {
        final Long bookId = 666L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, bookId);
        doReturn(resultSet).when(preparedStatement).executeQuery();
        doReturn(false).when(resultSet).next();

        List<Author> actualResult = authorDao.findAllByBookId(bookId);

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, bookId);
        verify(preparedStatement).executeQuery();
        verify(resultSet).next();
        assertTrue(actualResult.isEmpty());
    }

    @Test
    void findAllByBookId_whenError_ThrowException() throws SQLException {
        final Long bookId = 3L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, bookId);
        doThrow(SQLException.class).when(preparedStatement).executeQuery();

        assertThrows(DaoException.class, () -> authorDao.findAllByBookId(bookId));

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, bookId);
        verify(preparedStatement).executeQuery();
    }



    @Test
    void getBooksForAuthor_whenValidData_thenReturnList() {
        final Long authorId = 1L;
        final List<Book> books = List.of(BOOK_1, BOOK_2);
        doReturn(books).when(bookDao).findAllByAuthorId(authorId);

        List<Book> actualResult = authorDao.getBooksForAuthor(authorId);

        verify(bookDao).findAllByAuthorId(authorId);
        assertEquals(2, actualResult.size());
    }

    @Test
    void getBooksForAuthor_whenInvalidData_thenReturnEmptyList() {
        final Long readerId = 666L;
        doReturn(Collections.emptyList()).when(bookDao).findAllByAuthorId(readerId);

        List<Book> actualResult = authorDao.getBooksForAuthor(readerId);

        assertTrue(actualResult.isEmpty());
    }



    @Test
    void save_whenValidData_thenSuccess() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString(), anyInt());
        doNothing().when(preparedStatement).setString(1, AUTHOR_1.getFullName());
        doNothing().when(preparedStatement).setString(2, AUTHOR_1.getPersonalInfo());
        doReturn(1).when(preparedStatement).executeUpdate();
        doReturn(resultSet).when(preparedStatement).getGeneratedKeys();
        doReturn(true).when(resultSet).next();
        doReturn(AUTHOR_1.getId()).when(resultSet).getLong("id");
        doReturn(true).when(authorBookDao).save(anyLong(), anyLong());

        Author actualResult = authorDao.save(AUTHOR_1);

        verify(connection).prepareStatement(anyString(), anyInt());
        verify(preparedStatement).setString(1, AUTHOR_1.getFullName());
        verify(preparedStatement).setString(2, AUTHOR_1.getPersonalInfo());
        verify(preparedStatement).executeUpdate();
        verify(preparedStatement).getGeneratedKeys();
        verify(resultSet).next();
        verify(resultSet).getLong("id");
        verify(authorBookDao).save(anyLong(), anyLong());
        assertEquals(AUTHOR_1.getPersonalInfo(), actualResult.getPersonalInfo());
    }

    @Test
    void save_whenGeneratedKeysNextIsFalse() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString(), anyInt());
        doNothing().when(preparedStatement).setString(1, AUTHOR_1.getFullName());
        doNothing().when(preparedStatement).setString(2, AUTHOR_1.getPersonalInfo());
        doReturn(1).when(preparedStatement).executeUpdate();
        doReturn(resultSet).when(preparedStatement).getGeneratedKeys();
        doReturn(false).when(resultSet).next();

        authorDao.save(AUTHOR_1);

        verify(connection).prepareStatement(anyString(), anyInt());
        verify(preparedStatement).setString(1, AUTHOR_1.getFullName());
        verify(preparedStatement).setString(2, AUTHOR_1.getPersonalInfo());
        verify(preparedStatement).executeUpdate();
        verify(preparedStatement).getGeneratedKeys();
        verify(resultSet).next();
    }

    @Test
    void save_whenError_ThrowException() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString(), anyInt());
        doNothing().when(preparedStatement).setString(1, AUTHOR_1.getFullName());
        doNothing().when(preparedStatement).setString(2, AUTHOR_1.getPersonalInfo());
        doThrow(SQLException.class).when(preparedStatement).executeUpdate();

        assertThrows(DaoException.class, () -> authorDao.save(AUTHOR_1));

        verify(connection).prepareStatement(anyString(), anyInt());
        verify(preparedStatement).setString(1, AUTHOR_1.getFullName());
        verify(preparedStatement).setString(2, AUTHOR_1.getPersonalInfo());
        verify(preparedStatement).executeUpdate();
    }



    @Test
    void update_whenValidData_thenReturnTrue() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setString(1, AUTHOR_1.getFullName());
        doNothing().when(preparedStatement).setString(2, AUTHOR_1.getPersonalInfo());
        doNothing().when(preparedStatement).setLong(3, AUTHOR_1.getId());
        doReturn(1).when(preparedStatement).executeUpdate();

        boolean actualResult = authorDao.update(AUTHOR_1);

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setString(1, AUTHOR_1.getFullName());
        verify(preparedStatement).setString(2, AUTHOR_1.getPersonalInfo());
        verify(preparedStatement).setLong(3, AUTHOR_1.getId());
        verify(preparedStatement).executeUpdate();
        assertTrue(actualResult);
    }

    @Test
    void update_whenUpdateResultIsZero_thenReturnFalse() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setString(1, AUTHOR_1.getFullName());
        doNothing().when(preparedStatement).setString(2, AUTHOR_1.getPersonalInfo());
        doNothing().when(preparedStatement).setLong(3, AUTHOR_1.getId());
        doReturn(0).when(preparedStatement).executeUpdate();

        boolean actualResult = authorDao.update(AUTHOR_1);

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setString(1, AUTHOR_1.getFullName());
        verify(preparedStatement).setString(2, AUTHOR_1.getPersonalInfo());
        verify(preparedStatement).setLong(3, AUTHOR_1.getId());
        verify(preparedStatement).executeUpdate();
        assertFalse(actualResult);
    }

    @Test
    void update_whenError_thenTrowException() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setString(1, AUTHOR_1.getFullName());
        doNothing().when(preparedStatement).setString(2, AUTHOR_1.getPersonalInfo());
        doNothing().when(preparedStatement).setLong(3, AUTHOR_1.getId());
        doThrow(SQLException.class).when(preparedStatement).executeUpdate();

        assertThrows(DaoException.class, () -> authorDao.update(AUTHOR_1));

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setString(1, AUTHOR_1.getFullName());
        verify(preparedStatement).setString(2, AUTHOR_1.getPersonalInfo());
        verify(preparedStatement).setLong(3, AUTHOR_1.getId());
        verify(preparedStatement).executeUpdate();
    }



    @Test
    void delete_whenValidData_thenReturnTrue() throws SQLException {
        final Long authorId = 1L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, authorId);
        doReturn(true).when(authorBookDao).deleteAllByAuthorId(authorId);
        doReturn(1).when(preparedStatement).executeUpdate();

        boolean actualResult = authorDao.delete(authorId);

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, authorId);
        verify(authorBookDao).deleteAllByAuthorId(authorId);
        verify(preparedStatement).executeUpdate();
        assertTrue(actualResult);
    }

    @Test
    void delete_whenInvalidData_thenReturnFalse() throws SQLException {
        final Long authorId = 666L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, authorId);
        doReturn(false).when(authorBookDao).deleteAllByAuthorId(authorId);
        doReturn(0).when(preparedStatement).executeUpdate();

        boolean actualResult = authorDao.delete(authorId);

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, authorId);
        verify(authorBookDao).deleteAllByAuthorId(authorId);
        verify(preparedStatement).executeUpdate();
        assertFalse(actualResult);
    }

    @Test
    void delete_whenError_thenThrowException() throws SQLException {
        final Long authorId = 3L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, authorId);
        doReturn(false).when(authorBookDao).deleteAllByAuthorId(authorId);
        doThrow(SQLException.class).when(preparedStatement).executeUpdate();

        assertThrows(DaoException.class, () -> authorDao.delete(authorId));

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, authorId);
        verify(authorBookDao).deleteAllByAuthorId(authorId);
        verify(preparedStatement).executeUpdate();
    }



    @Test
    void isContainById_whenValidData_thenReturnTrue() throws SQLException {
        final Long authorId = 1L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, authorId);
        doReturn(resultSet).when(preparedStatement).executeQuery();
        doReturn(true).when(resultSet).next();

        boolean actualResult = authorDao.isContainById(authorId);

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, authorId);
        verify(preparedStatement).executeQuery();
        verify(resultSet).next();
        assertTrue(actualResult);
    }
    @Test
    void isContainById_whenInvalidData_thenReturnFalse() throws SQLException {
        final Long authorId = 666L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, authorId);
        doReturn(resultSet).when(preparedStatement).executeQuery();
        doReturn(false).when(resultSet).next();

        boolean actualResult = authorDao.isContainById(authorId);

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, authorId);
        verify(preparedStatement).executeQuery();
        verify(resultSet).next();
        assertFalse(actualResult);
    }

    @Test
    void isContainById_whenError_thenThrowException() throws SQLException {
        final Long authorId = 3L;
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setLong(1, authorId);
        doThrow(SQLException.class).when(preparedStatement).executeQuery();

        assertThrows(DaoException.class, () -> authorDao.isContainById(authorId));

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, authorId);
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