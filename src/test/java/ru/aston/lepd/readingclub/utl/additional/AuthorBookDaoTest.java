package ru.aston.lepd.readingclub.utl.additional;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aston.lepd.readingclub.dao.AuthorBookDao;
import ru.aston.lepd.readingclub.exception.DaoException;
import ru.aston.lepd.readingclub.util.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorBookDaoTest {


    private static MockedStatic<DataSource> dataSource;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @InjectMocks
    private AuthorBookDao authorBookDao;



    @BeforeAll
    static void setUp() {
        dataSource = mockStatic(DataSource.class);
    }

    @BeforeEach
    void init() throws SQLException {
        dataSource.when(DataSource::getConnection).thenReturn(connection);
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
    }







    @Test
    void save_whenInvalidData_thenReturnFalse() throws SQLException {
        final Long authorId = 666L;
        final Long bookId = 666L;
        doNothing().when(preparedStatement).setLong(1, authorId);
        doNothing().when(preparedStatement).setLong(2, bookId);
        doReturn(0).when(preparedStatement).executeUpdate();

        boolean actualResult = authorBookDao.save(authorId, bookId);

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, authorId);
        verify(preparedStatement).setLong(2, bookId);
        verify(preparedStatement).executeUpdate();
        assertFalse(actualResult);
    }



    @Test
    void deleteAllByAuthorId_whenError_ThrowException() throws SQLException {
        final Long authorId = 3L;
        doNothing().when(preparedStatement).setLong(1, authorId);
        doThrow(SQLException.class).when(preparedStatement).executeUpdate();

        assertThrows(DaoException.class, () -> authorBookDao.deleteAllByAuthorId(authorId));

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, authorId);
        verify(preparedStatement).executeUpdate();
    }



    @Test
    void deleteAllByBookId_whenError_ThrowException() throws SQLException {
        final Long bookId = 3L;
        doNothing().when(preparedStatement).setLong(1, bookId);
        doThrow(SQLException.class).when(preparedStatement).executeUpdate();

        assertThrows(DaoException.class, () -> authorBookDao.deleteAllByBookId(bookId));

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, bookId);
        verify(preparedStatement).executeUpdate();
    }



    @Test
    void delete_whenError_ThrowException() throws SQLException {
        final Long authorId = 3L;
        final Long bookId = 3L;
        doNothing().when(preparedStatement).setLong(1, authorId);
        doNothing().when(preparedStatement).setLong(2, bookId);
        doThrow(SQLException.class).when(preparedStatement).executeUpdate();

        assertThrows(DaoException.class, () -> authorBookDao.delete(authorId, bookId));

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, authorId);
        verify(preparedStatement).setLong(1, bookId);
        verify(preparedStatement).executeUpdate();
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