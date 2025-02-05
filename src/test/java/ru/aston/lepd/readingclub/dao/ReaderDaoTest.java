package ru.aston.lepd.readingclub.dao;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.aston.lepd.readingclub.ObjectContainer;
import ru.aston.lepd.readingclub.entity.Author;
import ru.aston.lepd.readingclub.entity.Book;
import ru.aston.lepd.readingclub.entity.Reader;
import ru.aston.lepd.readingclub.exception.DaoException;
import ru.aston.lepd.readingclub.util.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static ru.aston.lepd.readingclub.Constants.*;
import static ru.aston.lepd.readingclub.Constants.INSERT_AUTHOR_BOOK_SQL;

@Testcontainers
class ReaderDaoTest {


    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");
    private ReaderDao readerDao;




    @BeforeAll
    static void prepareDatabase() throws SQLException {
        postgreSQLContainer.start();
        DataSource.initialize(postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword(),
                postgreSQLContainer.getDriverClassName());

        try (Connection connection = DataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(CREATE_READERS_SQL);
            statement.execute(CREATE_BOOKS_SQL);
            statement.execute(CREATE_AUTHORS_SQL);
            statement.execute(CREATE_AUTHOR_BOOK_SQL);
        }
    }


    @BeforeEach
    void cleanData() throws SQLException {
        this.readerDao = new ObjectContainer().getReaderDao();
        try (var connection = DataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(CLEAN_AUTHOR_BOOK_SQL);
            statement.execute(CLEAN_AUTHORS_SQL);
            statement.execute(CLEAN_BOOKS_SQL);
            statement.execute(CLEAN_READERS_SQL);

            statement.execute(UPDATE_AUTHOR_ID_SQL);
            statement.execute(UPDATE_BOOKS_ID_SQL);
            statement.execute(UPDATE_READERS_ID_SQL);

            statement.execute(INSERT_READERS_SQL);
            statement.execute(INSERT_BOOKS_SQL);
            statement.execute(INSERT_AUTHORS_SQL);
            statement.execute(INSERT_AUTHOR_BOOK_SQL);
        }
    }





    @Test
    void setBookDao_whenSetNull() {
        readerDao.setBookDao(null);

        assertNull(readerDao.getBookDao());
    }

    @Test
    void setBookDao_whenSetObject() {
        readerDao.setBookDao(null);
        final BookDao bookDao1 = new BookDao(null);

        readerDao.setBookDao(bookDao1);

        assertNotNull(readerDao.getBookDao());
    }


    @Test
    public void findById_whenValidId_thenReturnOptionalOfReader() {
        final Long expectedId = 1L;
        final Long readerId = 1L;

        Optional<Reader> actualResult = readerDao.findById(readerId);

        assertTrue(actualResult.isPresent());
        assertEquals(expectedId, actualResult.get().getId());
    }

    @Test
    public void findById_whenInvalidId_thenReturnEmptyOptional() {
        final Long readerId = 666L;

        Optional<Reader> actualResult = readerDao.findById(readerId);

        assertTrue(actualResult.isEmpty());
    }


    @Test
    public void findAll_whenExist_thenReturnList() {
        List<Reader> actualResult = readerDao.findAll();

        assertFalse(actualResult.isEmpty());
        assertEquals(3, actualResult.size());
    }

    @Test
    public void findAll_whenNotExist_thenReturnEmptyList() {
        readerDao.delete(1L);
        readerDao.delete(2L);
        readerDao.delete(3L);

        List<Reader> actualResult = readerDao.findAll();

        assertTrue(actualResult.isEmpty());
    }


    @Test
    void getBooksForReader_whenReaderHaveBook_thenReturnList() {
        final int expectedSize = 1;
        final Long readerId = 1L;

        List<Book> actualResult = readerDao.getBooksForReader(readerId);

        assertFalse(actualResult.isEmpty());
        assertEquals(expectedSize, actualResult.size());
    }

    @Test
    void getBooksForReader_whenReaderDoNotHaveBook_thenReturnEmptyList() {
        final Reader reader = new Reader();
        reader.setName("Alex");
        reader.setSurname("Smith");
        reader.setPhone("79999999999");
        reader.setAddress("Street 5");
        Reader saved = readerDao.save(reader);

        List<Book> actualResult = readerDao.getBooksForReader(saved.getId());

        assertTrue(actualResult.isEmpty());
    }


    @Test
    void save_whenValidData_thenSuccess() {
        final Author author = new Author();
        author.setId(1L);
        final Book book = new Book();
        book.setTitle("Title");
        book.setInventoryNumber(55555L);
        book.setAuthors(List.of(author));
        final Reader reader = new Reader();
        reader.setName("Alex");
        reader.setSurname("Smith");
        reader.setPhone("79999999999");
        reader.setAddress("Street 5");
        reader.setBooks(List.of(book));

        Reader saved = readerDao.save(reader);

        assertEquals(4L, saved.getId());
        assertEquals(reader.getName(), saved.getName());
        assertEquals(reader.getSurname(), saved.getSurname());
        assertEquals(reader.getPhone(), saved.getPhone());
        assertEquals(reader.getAddress(), saved.getAddress());
    }

    @Test
    void save_whenNameIsNull_thenThrowException() {
        final Reader reader = new Reader();
        reader.setSurname("Smith");
        reader.setPhone("79999999999");
        reader.setAddress("Street 5");

        assertThrows(DaoException.class, () -> readerDao.save(reader));
    }

    @Test
    void save_whenPhoneIsNotUnique_thenThrowException() {
        final Reader reader = new Reader();
        reader.setName("Alex");
        reader.setSurname("Smith");
        reader.setPhone("71111111111");
        reader.setAddress("Street 5");

        assertThrows(DaoException.class, () -> readerDao.save(reader));
    }


    @Test
    void update_whenValidData_thenSuccess() {
        final Long readerId = 1L;
        final Reader reader = readerDao.findById(readerId).get();
        reader.addBook(BOOK_1);
        reader.setName("Alex");
        reader.setSurname("Smith");
        reader.setPhone("79999999999");
        reader.setAddress("Street 5");

        boolean actualResult = readerDao.update(reader);

        Reader updated = readerDao.findById(reader.getId()).get();
        assertEquals(reader.getName(), updated.getName());
        assertEquals(reader.getSurname(), updated.getSurname());
        assertEquals(reader.getPhone(), updated.getPhone());
        assertEquals(reader.getAddress(), updated.getAddress());
        assertTrue(actualResult);
    }

    @Test
    void update_whenValidData2_thenSuccess() {
        final Author author = new Author();
        author.setId(1L);
        final Book book = new Book();
        book.setTitle("Title");
        book.setInventoryNumber(55555L);
        book.setAuthors(List.of(author));
        final Long readerId = 1L;
        final Reader reader = readerDao.findById(readerId).get();
        reader.addBook(book);
        reader.setName("Alex");
        reader.setSurname("Smith");
        reader.setPhone("79999999999");
        reader.setAddress("Street 5");

        boolean actualResult = readerDao.update(reader);

        Reader updated = readerDao.findById(reader.getId()).get();
        assertEquals(reader.getName(), updated.getName());
        assertEquals(reader.getSurname(), updated.getSurname());
        assertEquals(reader.getPhone(), updated.getPhone());
        assertEquals(reader.getAddress(), updated.getAddress());
        assertTrue(actualResult);
    }

    @Test
    void update_whenNameIsNull_thenThrowException() {
        final Long readerId = 1L;
        final Reader reader = readerDao.findById(readerId).get();
        reader.setName(null);
        reader.setSurname("Smith");
        reader.setPhone("79999999999");
        reader.setAddress("Street 5");

        assertThrows(DaoException.class, () -> readerDao.update(reader));
    }

    @Test
    void update_whenPhoneIsNotUnique_thenThrowException() {
        final Long readerId = 1L;
        final Reader reader = readerDao.findById(readerId).get();
        reader.setId(2L);
        reader.setName("Alex");
        reader.setSurname("Smith");
        reader.setPhone("71111111111");
        reader.setAddress("Street 5");

        assertThrows(DaoException.class, () -> readerDao.update(reader));
    }


    @Test
    public void delete_whenValidId_thenTrue() {
        final Long readerId = 1L;

        boolean actualResult = readerDao.delete(readerId);

        assertEquals(2, readerDao.findAll().size());
        assertTrue(actualResult);
    }

    @Test
    public void delete_whenInvalidId_thenFalse() {
        final Long readerId = 666L;

        boolean actualResult = readerDao.delete(readerId);

        assertEquals(3, readerDao.findAll().size());
        assertFalse(actualResult);
    }


    @Test
    void isContainById_whenValidKey_thenTrue() {
        final Long readerId = 1L;

        assertTrue(readerDao.isContainById(readerId));
    }

    @Test
    void isContainById_whenInvalidKey_thenFalse() {
        final Long readerId = 666L;

        assertFalse(readerDao.isContainById(readerId));
    }





    @AfterAll
    static void afterAll() throws SQLException {
        postgreSQLContainer.stop();
    }


}