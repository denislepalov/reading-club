package ru.aston.lepd.readingclub.utl.additional;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aston.lepd.readingclub.dao.AuthorBookDao;
import ru.aston.lepd.readingclub.dao.AuthorDao;
import ru.aston.lepd.readingclub.dao.BookDao;
import ru.aston.lepd.readingclub.exception.DaoException;
import ru.aston.lepd.readingclub.util.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static ru.aston.lepd.readingclub.Constants.AUTHOR_1;

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
    void findAll_whenError_ThrowException() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doThrow(SQLException.class).when(preparedStatement).executeQuery();

        assertThrows(DaoException.class, () -> authorDao.findAll());

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).executeQuery();
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