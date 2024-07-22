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
class AuthorDaoTest {


    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");
    private AuthorDao authorDao;




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
        this.authorDao = new ObjectContainer().getAuthorDao();
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
        authorDao.setBookDao(null);

        assertNull(authorDao.getBookDao());
    }

    @Test
    void setBookDao_whenSetObject() {
        authorDao.setBookDao(null);
        final BookDao bookDao1 = new BookDao(null);

        authorDao.setBookDao(bookDao1);

        assertNotNull(authorDao.getBookDao());
    }




    @Test
    public void findById_whenValidId_thenReturnOptionalOfAuthor() {
        final Long expectedId = 1L;
        final Long authorId = 1L;

        Optional<Author> actualResult = authorDao.findById(authorId);

        assertTrue(actualResult.isPresent());
        assertEquals(expectedId, actualResult.get().getId());
    }

    @Test
    public void findById_whenInvalidId_thenReturnEmptyOptional() {
        final Long authorId = 666L;

        Optional<Author> actualResult = authorDao.findById(authorId);

        assertTrue(actualResult.isEmpty());
    }



    @Test
    public void findAll_whenExist_thenReturnList() {
        List<Author> actualResult = authorDao.findAll();

        assertFalse(actualResult.isEmpty());
        assertEquals(3, actualResult.size());
    }

    @Test
    public void findAll_whenNotExist_thenReturnEmptyList() {
        authorDao.delete(1L);
        authorDao.delete(2L);
        authorDao.delete(3L);

        List<Author> actualResult = authorDao.findAll();

        assertTrue(actualResult.isEmpty());
    }



    @Test
    public void findAllByBookId_whenExist_thenReturnList() {
        final Long bookId = 1L;

        List<Author> actualResult = authorDao.findAllByBookId(bookId);

        assertFalse(actualResult.isEmpty());
        assertEquals(2, actualResult.size());
    }

    @Test
    public void findAllByBookId_whenNotExist_thenReturnEmptyList() {
        final Long bookId = 666L;

        List<Author> actualResult = authorDao.findAllByBookId(bookId);

        assertTrue(actualResult.isEmpty());
    }



    @Test
    void getBooksForAuthor_whenAuthorHaveBook_thenReturnList() {
        final Long authorId = 3L;

        List<Book> actualResult = authorDao.getBooksForAuthor(authorId);

        assertFalse(actualResult.isEmpty());
        assertEquals(2, actualResult.size());
    }

    @Test
    void getBooksForAuthor_whenAuthorDoNotHaveBook_thenReturnEmptyList() {
        final Author author = new Author();
        author.setFullName("some name");
        author.setPersonalInfo("some info");
        Author saved = authorDao.save(author);

        List<Book> actualResult = authorDao.getBooksForAuthor(saved.getId());

        assertTrue(actualResult.isEmpty());
    }



    @Test
    void save_whenValidData_thenSuccess() {
        final Book book = new Book();
        book.setId(1L);
        final Author author = new Author();
        author.setFullName("Steven King");
        author.setPersonalInfo("likes fish");
        author.setBooks(List.of(book));

        Author saved = authorDao.save(author);

        assertEquals(4L, saved.getId());
        assertEquals(author.getFullName(), saved.getFullName());
        assertEquals(author.getPersonalInfo(), saved.getPersonalInfo());
        assertEquals(1, author.getBooks().size());
    }

    @Test
    void save_whenFullNameIsNull_thenThrowException() {
        final Author author = new Author();
        author.setPersonalInfo("likes fish");

        assertThrows(DaoException.class, () -> authorDao.save(author));
    }

    @Test
    void save_whenPersonalInfoIsNull_thenThrowException() {
        final Author author = new Author();
        author.setFullName("Steven King");

        assertThrows(DaoException.class, () -> authorDao.save(author));
    }



    @Test
    void update_whenValidData_thenSuccess() {
        final Long authorId = 1L;
        final Author author = authorDao.findById(authorId).get();
        author.setFullName("Steven King");
        author.setPersonalInfo("likes fish");
        author.setBooks(List.of(BOOK_1));

        boolean actualResult = authorDao.update(author);

        Author updated = authorDao.findById(authorId).get();
        assertEquals(author.getFullName(), updated.getFullName());
        assertEquals(author.getPersonalInfo(), updated.getPersonalInfo());
        assertTrue(actualResult);
    }

    @Test
    void update_whenFullNameIsNull_thenThrowException() {
        final Long authorId = 1L;
        final Author author = authorDao.findById(authorId).get();
        author.setFullName(null);

        assertThrows(DaoException.class, () -> authorDao.update(author));
    }

    @Test
    void update_whenPersonalInfoIsNull_thenThrowException() {
        final Long authorId = 1L;
        final Author author = authorDao.findById(authorId).get();
        author.setPersonalInfo(null);

        assertThrows(DaoException.class, () -> authorDao.update(author));
    }



    @Test
    public void delete_whenValidId_thenTrue() {
        final Long authorId = 1L;

        boolean actualResult = authorDao.delete(authorId);

        assertEquals(2, authorDao.findAll().size());
        assertTrue(actualResult);
    }

    @Test
    public void delete_whenInvalidId_thenFalse() {
        final Long authorId = 666L;

        boolean actualResult = authorDao.delete(authorId);

        assertEquals(3, authorDao.findAll().size());
        assertFalse(actualResult);
    }



    @Test
    void isContainById_whenValidKey_thenTrue() {
        final Long authorId = 1L;

        assertTrue(authorDao.isContainById(authorId));
    }

    @Test
    void isContainById_whenInvalidKey_thenFalse() {
        final Long authorId = 666L;

        assertFalse(authorDao.isContainById(authorId));
    }



    @AfterAll
    static void afterAll() throws SQLException {
        postgreSQLContainer.stop();
    }

}