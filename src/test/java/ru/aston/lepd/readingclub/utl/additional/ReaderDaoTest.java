package ru.aston.lepd.readingclub.utl.additional;

import org.junit.jupiter.api.*;
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static ru.aston.lepd.readingclub.Constants.READER_1;


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
    void findAll_whenError_ThrowException() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doThrow(SQLException.class).when(preparedStatement).executeQuery();

        assertThrows(DaoException.class, () -> readerDao.findAll());

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).executeQuery();
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


















