package ru.aston.lepd.readingclub.unit.dao;

import org.junit.jupiter.api.Test;
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorBookDaoTest {


    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @InjectMocks
    private AuthorBookDao authorBookDao;





    @Test
    void save_shouldReturnFalse() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doReturn(0).when(preparedStatement).executeUpdate();
        doNothing().when(preparedStatement).setLong(1, 1L);
        doNothing().when(preparedStatement).setLong(2, 2L);
        boolean actualResult;

        try (MockedStatic<DataSource> dataSourceMock = mockStatic(DataSource.class)) {
            dataSourceMock.when(DataSource::getConnection).thenReturn(connection);
            actualResult = authorBookDao.save(1L, 2L);
            dataSourceMock.verify(DataSource::getConnection);
        }

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).setLong(2, 2L);
        verify(preparedStatement).executeUpdate();
        assertFalse(actualResult);
    }

    @Test
    void save_shouldTrowException() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doThrow(SQLException.class).when(preparedStatement).executeUpdate();
        doNothing().when(preparedStatement).setLong(1, 1L);
        doNothing().when(preparedStatement).setLong(2, 2L);

        try (MockedStatic<DataSource> dataSourceMock = mockStatic(DataSource.class)) {
            dataSourceMock.when(DataSource::getConnection).thenReturn(connection);
            assertThrows(DaoException.class, () -> authorBookDao.save(1L, 2L));
            dataSourceMock.verify(DataSource::getConnection);
        }

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).setLong(2, 2L);
        verify(preparedStatement).executeUpdate();
    }



    @Test
    void deleteAllByAuthorId_shouldThrowException() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doThrow(SQLException.class).when(preparedStatement).executeUpdate();
        doNothing().when(preparedStatement).setLong(1, 1L);

        try (MockedStatic<DataSource> dataSourceMock = mockStatic(DataSource.class)) {
            dataSourceMock.when(DataSource::getConnection).thenReturn(connection);
            assertThrows(DaoException.class, () -> authorBookDao.deleteAllByAuthorId(1L));
            dataSourceMock.verify(DataSource::getConnection);
        }

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).executeUpdate();
    }



    @Test
    void deleteAllByBookId_shouldThrowException() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doThrow(SQLException.class).when(preparedStatement).executeUpdate();
        doNothing().when(preparedStatement).setLong(1, 1L);

        try (MockedStatic<DataSource> dataSourceMock = mockStatic(DataSource.class)) {
            dataSourceMock.when(DataSource::getConnection).thenReturn(connection);
            assertThrows(DaoException.class, () -> authorBookDao.deleteAllByBookId(1L));
            dataSourceMock.verify(DataSource::getConnection);
        }

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).executeUpdate();
    }


    @Test
    void delete_shouldTrowException() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doThrow(SQLException.class).when(preparedStatement).executeUpdate();
        doNothing().when(preparedStatement).setLong(1, 1L);
        doNothing().when(preparedStatement).setLong(2, 2L);

        try (MockedStatic<DataSource> dataSourceMock = mockStatic(DataSource.class)) {
            dataSourceMock.when(DataSource::getConnection).thenReturn(connection);
            assertThrows(DaoException.class, () -> authorBookDao.delete(1L, 2L));
            dataSourceMock.verify(DataSource::getConnection);
        }

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).setLong(2, 2L);
        verify(preparedStatement).executeUpdate();
    }



}