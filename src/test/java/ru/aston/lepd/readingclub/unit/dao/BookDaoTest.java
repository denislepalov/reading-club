package ru.aston.lepd.readingclub.unit.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aston.lepd.readingclub.dao.AuthorBookDao;
import ru.aston.lepd.readingclub.dao.BookDao;
import ru.aston.lepd.readingclub.exception.DaoException;
import ru.aston.lepd.readingclub.util.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookDaoTest {



    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private AuthorBookDao authorBookDao;
    @InjectMocks
    private BookDao bookDao;




    @Test
    void findById_shouldTrowException() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doThrow(SQLException.class).when(preparedStatement).executeQuery();
        doNothing().when(preparedStatement).setLong(1, 1L);

        try (MockedStatic<DataSource> dataSourceMock = mockStatic(DataSource.class)) {
            dataSourceMock.when(DataSource::getConnection).thenReturn(connection);
            assertThrows(DaoException.class, () -> bookDao.findById(1L));
            dataSourceMock.verify(DataSource::getConnection);
        }

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).executeQuery();
    }



    @Test
    void findAll_shouldTrowException() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doThrow(SQLException.class).when(preparedStatement).executeQuery();

        try (MockedStatic<DataSource> dataSourceMock = mockStatic(DataSource.class)) {
            dataSourceMock.when(DataSource::getConnection).thenReturn(connection);
            assertThrows(DaoException.class, () -> bookDao.findAll());
            dataSourceMock.verify(DataSource::getConnection);
        }

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).executeQuery();
    }



    @Test
    void findAllByReaderId_shouldTrowException() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doThrow(SQLException.class).when(preparedStatement).executeQuery();
        doNothing().when(preparedStatement).setLong(1, 1L);

        try (MockedStatic<DataSource> dataSourceMock = mockStatic(DataSource.class)) {
            dataSourceMock.when(DataSource::getConnection).thenReturn(connection);
            assertThrows(DaoException.class, () -> bookDao.findAllByReaderId(1L));
            dataSourceMock.verify(DataSource::getConnection);
        }

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).executeQuery();
    }



    @Test
    void findAllByAuthorId_shouldTrowException() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doThrow(SQLException.class).when(preparedStatement).executeQuery();
        doNothing().when(preparedStatement).setLong(1, 1L);

        try (MockedStatic<DataSource> dataSourceMock = mockStatic(DataSource.class)) {
            dataSourceMock.when(DataSource::getConnection).thenReturn(connection);
            assertThrows(DaoException.class, () -> bookDao.findAllByAuthorId(1L));
            dataSourceMock.verify(DataSource::getConnection);
        }

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).executeQuery();
    }



    @Test
    void delete_shouldTrowException() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doThrow(SQLException.class).when(preparedStatement).executeUpdate();
        doNothing().when(preparedStatement).setLong(1, 1L);
        doReturn(false).when(authorBookDao).deleteAllByBookId(1L);

        try (MockedStatic<DataSource> dataSourceMock = mockStatic(DataSource.class)) {
            dataSourceMock.when(DataSource::getConnection).thenReturn(connection);
            assertThrows(DaoException.class, () -> bookDao.delete(1L));
            dataSourceMock.verify(DataSource::getConnection);
        }

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).executeUpdate();
        verify(authorBookDao).deleteAllByBookId(1L);
    }


    @Test
    void isContainById_shouldTrowException() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doThrow(SQLException.class).when(preparedStatement).executeQuery();
        doNothing().when(preparedStatement).setLong(1, 1L);

        try (MockedStatic<DataSource> dataSourceMock = mockStatic(DataSource.class)) {
            dataSourceMock.when(DataSource::getConnection).thenReturn(connection);
            assertThrows(DaoException.class, () -> bookDao.isContainById(1L));
            dataSourceMock.verify(DataSource::getConnection);
        }

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).executeQuery();
    }
}