package ru.aston.lepd.readingclub.unit.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aston.lepd.readingclub.dao.BookDao;
import ru.aston.lepd.readingclub.dao.ReaderDao;
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
import static ru.aston.lepd.readingclub.util.Constants.FULL_BOOK_1;
import static ru.aston.lepd.readingclub.util.Constants.FULL_READER_1;


@ExtendWith(MockitoExtension.class)
class ReaderDaoTest {



    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private BookDao bookDao;
    @Mock
    private ResultSet resultSet;
    @InjectMocks
    private ReaderDao readerDao;





    @Test
    void save_shouldGeneratedKeysNextIsFalse() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString(), anyInt());
        doNothing().when(preparedStatement).setString(1, FULL_READER_1.getName());
        doNothing().when(preparedStatement).setString(2, FULL_READER_1.getSurname());
        doNothing().when(preparedStatement).setString(3, FULL_READER_1.getPhone());
        doNothing().when(preparedStatement).setString(4, FULL_READER_1.getAddress());
        doReturn(resultSet).when(preparedStatement).getGeneratedKeys();
        doReturn(false).when(resultSet).next();

        try (MockedStatic<DataSource> dataSourceMock = mockStatic(DataSource.class)) {
            dataSourceMock.when(DataSource::getConnection).thenReturn(connection);
            readerDao.save(FULL_READER_1);
            dataSourceMock.verify(DataSource::getConnection);
        }

        verify(connection).prepareStatement(anyString(), anyInt());
        verify(preparedStatement).setString(1, FULL_READER_1.getName());
        verify(preparedStatement).setString(2, FULL_READER_1.getSurname());
        verify(preparedStatement).setString(3, FULL_READER_1.getPhone());
        verify(preparedStatement).setString(4, FULL_READER_1.getAddress());
        verify(preparedStatement).getGeneratedKeys();
        verify(resultSet).next();
    }



    @Test
    void update_shouldUpdateResultIsZero() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setString(1, FULL_READER_1.getName());
        doNothing().when(preparedStatement).setString(2, FULL_READER_1.getSurname());
        doNothing().when(preparedStatement).setString(3, FULL_READER_1.getPhone());
        doNothing().when(preparedStatement).setString(4, FULL_READER_1.getAddress());
        doReturn(0).when(preparedStatement).executeUpdate();

        try (MockedStatic<DataSource> dataSourceMock = mockStatic(DataSource.class)) {
            dataSourceMock.when(DataSource::getConnection).thenReturn(connection);
            readerDao.update(FULL_READER_1);
            dataSourceMock.verify(DataSource::getConnection);
        }

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setString(1, FULL_READER_1.getName());
        verify(preparedStatement).setString(2, FULL_READER_1.getSurname());
        verify(preparedStatement).setString(3, FULL_READER_1.getPhone());
        verify(preparedStatement).setString(4, FULL_READER_1.getAddress());
        verify(preparedStatement).executeUpdate();
    }



    @Test
    void findById_shouldTrowException() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doThrow(SQLException.class).when(preparedStatement).executeQuery();
        doNothing().when(preparedStatement).setLong(1, 1L);

        try (MockedStatic<DataSource> dataSourceMock = mockStatic(DataSource.class)) {
            dataSourceMock.when(DataSource::getConnection).thenReturn(connection);
            assertThrows(DaoException.class, () -> readerDao.findById(1L));
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
            assertThrows(DaoException.class, () -> readerDao.findAll());
            dataSourceMock.verify(DataSource::getConnection);
        }

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).executeQuery();
    }



    @Test
    void delete_shouldTrowException() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doThrow(SQLException.class).when(preparedStatement).executeUpdate();
        doNothing().when(preparedStatement).setLong(1, 1L);
        doReturn(false).when(bookDao).deleteAllByReaderId(1L);

        try (MockedStatic<DataSource> dataSourceMock = mockStatic(DataSource.class)) {
            dataSourceMock.when(DataSource::getConnection).thenReturn(connection);
            assertThrows(DaoException.class, () -> readerDao.delete(1L));
            dataSourceMock.verify(DataSource::getConnection);
        }

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).executeUpdate();
        bookDao.deleteAllByReaderId(1L);
    }



    @Test
    void isContainById_shouldTrowException() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doThrow(SQLException.class).when(preparedStatement).executeQuery();
        doNothing().when(preparedStatement).setLong(1, 1L);

        try (MockedStatic<DataSource> dataSourceMock = mockStatic(DataSource.class)) {
            dataSourceMock.when(DataSource::getConnection).thenReturn(connection);
            assertThrows(DaoException.class, () -> readerDao.isContainById(1L));
            dataSourceMock.verify(DataSource::getConnection);
        }

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).executeQuery();
    }
}