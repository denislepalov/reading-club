package ru.aston.lepd.readingclub.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aston.lepd.readingclub.dao.AuthorBookDao;
import ru.aston.lepd.readingclub.dao.AuthorDao;
import ru.aston.lepd.readingclub.exception.DaoException;
import ru.aston.lepd.readingclub.util.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static ru.aston.lepd.readingclub.util.Constants.FULL_AUTHOR_1;
import static ru.aston.lepd.readingclub.util.Constants.FULL_BOOK_1;

@ExtendWith(MockitoExtension.class)
class AuthorDaoTest {


    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private AuthorBookDao authorBookDao;
    @Mock
    private ResultSet resultSet;
    @InjectMocks
    private AuthorDao authorDao;





    @Test
    void save_shouldGeneratedKeysNextIsFalse() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString(), anyInt());
        doNothing().when(preparedStatement).setString(1, FULL_AUTHOR_1.getFullName());
        doNothing().when(preparedStatement).setString(2, FULL_AUTHOR_1.getPersonalInfo());
        doReturn(1).when(preparedStatement).executeUpdate();
        doReturn(resultSet).when(preparedStatement).getGeneratedKeys();
        doReturn(false).when(resultSet).next();

        try (MockedStatic<DataSource> dataSourceMock = mockStatic(DataSource.class)) {
            dataSourceMock.when(DataSource::getConnection).thenReturn(connection);
            authorDao.save(FULL_AUTHOR_1);
            dataSourceMock.verify(DataSource::getConnection);
        }

        verify(connection).prepareStatement(anyString(), anyInt());
        verify(preparedStatement).setString(1, FULL_AUTHOR_1.getFullName());
        verify(preparedStatement).setString(2, FULL_AUTHOR_1.getPersonalInfo());
        verify(preparedStatement).executeUpdate();
        verify(preparedStatement).getGeneratedKeys();
        verify(resultSet).next();
    }



    @Test
    void update_shouldUpdateResultIsZero() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setString(1, FULL_AUTHOR_1.getFullName());
        doNothing().when(preparedStatement).setString(2, FULL_AUTHOR_1.getPersonalInfo());
        doNothing().when(preparedStatement).setLong(3, FULL_AUTHOR_1.getId());
        doReturn(0).when(preparedStatement).executeUpdate();

        try (MockedStatic<DataSource> dataSourceMock = mockStatic(DataSource.class)) {
            dataSourceMock.when(DataSource::getConnection).thenReturn(connection);
            authorDao.update(FULL_AUTHOR_1);
            dataSourceMock.verify(DataSource::getConnection);
        }

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setString(1, FULL_AUTHOR_1.getFullName());
        verify(preparedStatement).setString(2, FULL_AUTHOR_1.getPersonalInfo());
        verify(preparedStatement).setLong(3, FULL_AUTHOR_1.getId());
        verify(preparedStatement).executeUpdate();
    }



    @Test
    void findById_shouldThrowException() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doThrow(SQLException.class).when(preparedStatement).executeQuery();
        doNothing().when(preparedStatement).setLong(1, 1L);

        try (MockedStatic<DataSource> dataSourceMock = mockStatic(DataSource.class)) {
            dataSourceMock.when(DataSource::getConnection).thenReturn(connection);
            assertThrows(DaoException.class, () -> authorDao.findById(1L));
            dataSourceMock.verify(DataSource::getConnection);
        }

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).executeQuery();
    }



    @Test
    void findAll_shouldThrowException() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doThrow(SQLException.class).when(preparedStatement).executeQuery();

        try (MockedStatic<DataSource> dataSourceMock = mockStatic(DataSource.class)) {
            dataSourceMock.when(DataSource::getConnection).thenReturn(connection);
            assertThrows(DaoException.class, () -> authorDao.findAll());
            dataSourceMock.verify(DataSource::getConnection);
        }

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).executeQuery();
    }



    @Test
    void findAllByBookId_shouldThrowException() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doThrow(SQLException.class).when(preparedStatement).executeQuery();
        doNothing().when(preparedStatement).setLong(1, 1L);

        try (MockedStatic<DataSource> dataSourceMock = mockStatic(DataSource.class)) {
            dataSourceMock.when(DataSource::getConnection).thenReturn(connection);
            assertThrows(DaoException.class, () -> authorDao.findAllByBookId(1L));
            dataSourceMock.verify(DataSource::getConnection);
        }

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).executeQuery();
    }



    @Test
    void delete_shouldThrowException() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doThrow(SQLException.class).when(preparedStatement).executeUpdate();
        doNothing().when(preparedStatement).setLong(1, 1L);
        doReturn(false).when(authorBookDao).deleteAllByAuthorId(1L);

        try (MockedStatic<DataSource> dataSourceMock = mockStatic(DataSource.class)) {
            dataSourceMock.when(DataSource::getConnection).thenReturn(connection);
            assertThrows(DaoException.class, () -> authorDao.delete(1L));
            dataSourceMock.verify(DataSource::getConnection);
        }

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).executeUpdate();
        verify(authorBookDao).deleteAllByAuthorId(1L);
    }



    @Test
    void isContainById_shouldThrowException() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doThrow(SQLException.class).when(preparedStatement).executeQuery();
        doNothing().when(preparedStatement).setLong(1, 1L);

        try (MockedStatic<DataSource> dataSourceMock = mockStatic(DataSource.class)) {
            dataSourceMock.when(DataSource::getConnection).thenReturn(connection);
            assertThrows(DaoException.class, () -> authorDao.isContainById(1L));
            dataSourceMock.verify(DataSource::getConnection);
        }

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).executeQuery();
    }
}