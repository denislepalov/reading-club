package ru.aston.lepd.readingclub.dao;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.aston.lepd.readingclub.Constants;
import ru.aston.lepd.readingclub.exception.DaoException;
import ru.aston.lepd.readingclub.util.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;
import static ru.aston.lepd.readingclub.Constants.*;


@Testcontainers
class AuthorBookDaoTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");
    private final AuthorBookDao authorBookDao = new AuthorBookDao();




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
    void save_whenValidData_thenTrue() {
        final Long authorId = 1L;
        final Long bookId = 3L;

        boolean actualResult = authorBookDao.save(authorId, bookId);

        assertTrue(actualResult);
    }

    @Test
    void save_whenPairIsNotUnique_thenTrowException() {
        assertThrows(DaoException.class, () -> authorBookDao.save(1L, 1L));
    }

    @Test
    void save_whenColumnIsNotForeignKey_thenTrowException() {
        final Long wrongAuthorId = 666L;
        final Long bookId = 1L;
        assertThrows(DaoException.class, () -> authorBookDao.save(wrongAuthorId, bookId));
    }



    @Test
    void deleteAllByAuthorId_whenExist_thenTrue() {
        final Long authorId = 1L;

        boolean actualResult = authorBookDao.deleteAllByAuthorId(authorId);

        assertTrue(actualResult);
    }

    @Test
    void deleteAllByAuthorId_whenNotExist_thenFalse() {
        final Long authorId = 666L;

        boolean actualResult = authorBookDao.deleteAllByAuthorId(authorId);

        assertFalse(actualResult);
    }



    @Test
    void deleteAllByBookId_whenExist_thenTrue() {
        final Long bookId = 1L;

        boolean actualResult = authorBookDao.deleteAllByBookId(bookId);

        assertTrue(actualResult);
    }

    @Test
    void deleteAllByBookId_whenNotExist_thenFalse() {
        final Long bookId = 666L;

        boolean actualResult = authorBookDao.deleteAllByBookId(bookId);

        assertFalse(actualResult);
    }



    @Test
    void delete_whenValidData_thenTrue() {
        final Long authorId = 1L;
        final Long bookId = 1L;
        boolean actualResult = authorBookDao.delete(authorId, bookId);

        assertTrue(actualResult);
    }

    @Test
    void delete_whenInvalidData_thenFalse() {
        final Long authorId = 9L;
        final Long bookId = 9L;

        boolean actualResult = authorBookDao.delete(authorId, bookId);

        assertFalse(actualResult);
    }



    @AfterAll
    static void afterAll() throws SQLException {
        postgreSQLContainer.stop();
    }

}