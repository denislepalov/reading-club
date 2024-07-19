package ru.aston.lepd.readingclub.dao;

import org.junit.jupiter.api.Test;
import ru.aston.lepd.readingclub.exception.DaoException;
import ru.aston.lepd.readingclub.util.IntegrationTestBase;

import static org.junit.jupiter.api.Assertions.*;


class AuthorBookDaoIntegrationTest extends IntegrationTestBase {


    private final AuthorBookDao authorBookDao = new AuthorBookDao();




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





}