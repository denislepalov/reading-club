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
import ru.aston.lepd.readingclub.dao.ReaderDao;
import ru.aston.lepd.readingclub.exception.DaoException;
import ru.aston.lepd.readingclub.util.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static ru.aston.lepd.readingclub.Constants.BOOK_1;


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
    void findAll_whenError_ThrowException() throws SQLException {
        doReturn(preparedStatement).when(connection).prepareStatement(anyString());
        doThrow(SQLException.class).when(preparedStatement).executeQuery();

        assertThrows(DaoException.class, () -> bookDao.findAll());

        verify(connection).prepareStatement(anyString());
        verify(preparedStatement).executeQuery();
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