package ru.aston.lepd.readingclub.dao;

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
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static ru.aston.lepd.readingclub.util.Constants.FULL_BOOK_1;

@ExtendWith(MockitoExtension.class)
class BookDaoTest {


    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private AuthorBookDao authorBookDao;
    @Mock
    private ResultSet resultSet;
    @InjectMocks
    private BookDao bookDao;




    @Test
    void save_shouldGeneratedKeysNextIsFalse() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString(), anyInt());
        doNothing().when(preparedStatement).setString(1, FULL_BOOK_1.getTitle());
        doNothing().when(preparedStatement).setLong(2, FULL_BOOK_1.getInventoryNumber());
        doNothing().when(preparedStatement).setLong(3, FULL_BOOK_1.getReader().getId());
        doReturn(resultSet).when(preparedStatement).getGeneratedKeys();
        doReturn(false).when(resultSet).next();

        try (MockedStatic<DataSource> dataSourceMock = mockStatic(DataSource.class)) {
            dataSourceMock.when(DataSource::getConnection).thenReturn(connection);
            bookDao.save(FULL_BOOK_1);
            dataSourceMock.verify(DataSource::getConnection);
        }

        verify(connection).prepareStatement(anyString(), anyInt());
        verify(preparedStatement).setString(1, FULL_BOOK_1.getTitle());
        verify(preparedStatement).setLong(2, FULL_BOOK_1.getInventoryNumber());
        verify(preparedStatement).setLong(3, FULL_BOOK_1.getReader().getId());
        verify(preparedStatement).getGeneratedKeys();
        verify(resultSet).next();
    }



    @Test
    void update_shouldUpdateResultIsZero() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doNothing().when(preparedStatement).setString(1, FULL_BOOK_1.getTitle());
        doNothing().when(preparedStatement).setLong(2, FULL_BOOK_1.getInventoryNumber());
        doNothing().when(preparedStatement).setLong(3, FULL_BOOK_1.getReader().getId());
        doNothing().when(preparedStatement).setLong(4, FULL_BOOK_1.getId());
        doReturn(0).when(preparedStatement).executeUpdate();

        try (MockedStatic<DataSource> dataSourceMock = mockStatic(DataSource.class)) {
            dataSourceMock.when(DataSource::getConnection).thenReturn(connection);
            bookDao.update(FULL_BOOK_1);
            dataSourceMock.verify(DataSource::getConnection);
        }

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).setString(1, FULL_BOOK_1.getTitle());
        verify(preparedStatement).setLong(2, FULL_BOOK_1.getInventoryNumber());
        verify(preparedStatement).setLong(3, FULL_BOOK_1.getReader().getId());
        verify(preparedStatement).setLong(4, FULL_BOOK_1.getId());
        verify(preparedStatement).executeUpdate();
    }



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