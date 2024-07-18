package ru.aston.lepd.readingclub.integration.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.aston.lepd.readingclub.entity.Reader;
import ru.aston.lepd.readingclub.util.ObjectContainer;
import ru.aston.lepd.readingclub.dao.AuthorDao;
import ru.aston.lepd.readingclub.dao.BookDao;
import ru.aston.lepd.readingclub.dao.ReaderDao;
import ru.aston.lepd.readingclub.entity.Author;
import ru.aston.lepd.readingclub.entity.Book;
import ru.aston.lepd.readingclub.exception.DaoException;
import ru.aston.lepd.readingclub.integration.IntegrationTestBase;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static ru.aston.lepd.readingclub.util.Constants.*;


class BookDaoIT extends IntegrationTestBase {


    private BookDao bookDao;


    @BeforeEach
    void init() {
        this.bookDao = new ObjectContainer().getBookDao();
    }




    @Test
    void setAuthorDao_whenSetNull() {
        bookDao.setAuthorDao(null);

        assertNull(bookDao.getAuthorDao());
    }

    @Test
    void setAuthorDao_whenSetObject() {
        bookDao.setAuthorDao(null);
        final AuthorDao authorDao1 = new AuthorDao(null);

        bookDao.setAuthorDao(authorDao1);

        assertNotNull(bookDao.getAuthorDao());
    }


    @Test
    void setReaderDao_whenSetNull() {
        bookDao.setReaderDao(null);

        assertNull(bookDao.getReaderDao());
    }

    @Test
    void setReaderDao_whenSetObject() {
        bookDao.setReaderDao(null);
        final ReaderDao readerDao1 = new ReaderDao();

        bookDao.setReaderDao(readerDao1);

        assertNotNull(bookDao.getReaderDao());
    }


    @Test
    public void findById_whenValidId_thenReturnOptionalOfBook() {
        final Long expectedId = 1L;
        final Long bookId = 1L;

        Optional<Book> actualResult = bookDao.findById(bookId);

        assertTrue(actualResult.isPresent());
        assertEquals(expectedId, actualResult.get().getId());
    }

    @Test
    public void findById_whenInvalidId_thenReturnEmptyOptional() {
        final Long authorId = 666L;

        Optional<Book> actualResult = bookDao.findById(authorId);

        assertTrue(actualResult.isEmpty());
    }


    @Test
    public void findAll_whenExist_thenReturnList() {
        List<Book> actualResult = bookDao.findAll();

        assertFalse(actualResult.isEmpty());
        assertEquals(3, actualResult.size());
    }

    @Test
    public void findAll_whenNotExist_thenReturnEmptyList() {
        bookDao.delete(1L);
        bookDao.delete(2L);
        bookDao.delete(3L);

        List<Book> actualResult = bookDao.findAll();

        assertTrue(actualResult.isEmpty());
    }


    @Test
    public void findAllByReaderId_whenExist_thenReturnList() {
        final Long readerId = 1L;

        List<Book> actualResult = bookDao.findAllByReaderId(readerId);

        assertFalse(actualResult.isEmpty());
        assertEquals(1, actualResult.size());
    }

    @Test
    public void findAllByReaderId_whenNotExist_thenReturnEmptyList() {
        final Long readerId = 1L;
        bookDao.delete(1L);

        List<Book> actualResult = bookDao.findAllByReaderId(readerId);

        assertTrue(actualResult.isEmpty());
    }


    @Test
    public void findAllByAuthorId_whenExist_thenReturnList() {
        final Long authorId = 3L;

        List<Book> actualResult = bookDao.findAllByAuthorId(authorId);

        assertFalse(actualResult.isEmpty());
        assertEquals(2, actualResult.size());
    }

    @Test
    public void findAllByAuthorId_whenNotExist_thenReturnEmptyList() {
        final Long authorId = 3L;
        bookDao.delete(1L);
        bookDao.delete(3L);

        List<Book> actualResult = bookDao.findAllByAuthorId(authorId);

        assertTrue(actualResult.isEmpty());
    }


    @Test
    void getAuthorsForBook_whenBookHaveAuthor_thenReturnList() {
        final Long bookId = 3L;

        List<Author> actualResult = bookDao.getAuthorsForBook(bookId);

        assertFalse(actualResult.isEmpty());
        assertEquals(2, actualResult.size());
    }

    @Test
    void getAuthorsForBook_whenBookHaveNotAuthor_thenReturnEmptyList() {
        final Book book = new Book();
        book.setTitle("new title");
        book.setInventoryNumber(55555L);
        book.setAuthors(Collections.emptyList());
        book.setReader(SHORT_READER_1);
        Book saved = bookDao.save(book);

        List<Author> actualResult = bookDao.getAuthorsForBook(saved.getId());

        assertTrue(actualResult.isEmpty());
    }


    @Test
    void save_whenValidData_thenSuccess() {
        final Book book = new Book();
        book.setTitle("new title");
        book.setInventoryNumber(55555L);
        book.setAuthors(List.of(SHORT_AUTHOR_2, SHORT_AUTHOR_3));
        book.setReader(SHORT_READER_1);

        Book saved = bookDao.save(book);

        assertEquals(4L, saved.getId());
        assertEquals(book.getTitle(), saved.getTitle());
        assertEquals(book.getInventoryNumber(), saved.getInventoryNumber());
        assertEquals(2, saved.getAuthors().size());
        assertEquals(1L, saved.getReader().getId());
    }

    @Test
    void save_whenTitleIsNull_thenThrowException() {
        final Book book = new Book();
        book.setInventoryNumber(55555L);
        book.setAuthors(List.of(SHORT_AUTHOR_2, SHORT_AUTHOR_3));
        book.setReader(SHORT_READER_1);

        assertThrows(DaoException.class, () -> bookDao.save(book));
    }

    @Test
    void save_whenInventoryNumberIsNotUnique_thenThrowException() {
        final Book book = new Book();
        book.setTitle("new title");
        book.setInventoryNumber(11111L);
        book.setAuthors(List.of(SHORT_AUTHOR_2, SHORT_AUTHOR_3));
        book.setReader(SHORT_READER_1);

        assertThrows(DaoException.class, () -> bookDao.save(book));
    }


    @Test
    void update_whenValidData_thenSuccess() {
        final Long bookId = 1L;
        Book updating = bookDao.findById(bookId).get();
        updating.setTitle("new title");
        updating.setInventoryNumber(55555L);
        updating.setAuthors(List.of(SHORT_AUTHOR_2));
        updating.setReader(SHORT_READER_2);

        Book updated = bookDao.save(updating);

        assertEquals(updating.getTitle(), updated.getTitle());
        assertEquals(updating.getInventoryNumber(), updated.getInventoryNumber());
        assertEquals(1, updated.getAuthors().size());
        assertEquals(2L, updated.getReader().getId());
    }

    @Test
    void update_whenTitleIsNull_thenThrowException() {
        final Long bookId = 1L;
        Book updating = bookDao.findById(bookId).get();
        updating.setTitle(null);
        updating.setInventoryNumber(55555L);
        updating.setAuthors(List.of(SHORT_AUTHOR_2));
        updating.setReader(SHORT_READER_2);

        assertThrows(DaoException.class, () -> bookDao.update(updating));
    }

    @Test
    void update_whenInventoryNumberIsNotUnique_thenThrowException() {
        final Long bookId = 1L;
        Book updating = bookDao.findById(bookId).get();
        updating.setTitle("new title");
        updating.setInventoryNumber(33333L);
        updating.setAuthors(List.of(SHORT_AUTHOR_2));
        updating.setReader(SHORT_READER_2);

        assertThrows(DaoException.class, () -> bookDao.update(updating));
    }


    @Test
    public void delete_whenValidId_thenTrue() {
        final Long bookId = 1L;

        boolean actualResult = bookDao.delete(bookId);

        assertEquals(2, bookDao.findAll().size());
        assertTrue(actualResult);
    }

    @Test
    public void delete_whenInvalidId_thenFalse() {
        final Long bookId = 666L;

        boolean actualResult = bookDao.delete(bookId);

        assertEquals(3, bookDao.findAll().size());
        assertFalse(actualResult);
    }


    @Test
    void deleteAllByReaderId_whenExist_thenTrue() {
        final Long readerId = 1L;

        boolean actualResult = bookDao.deleteAllByReaderId(readerId);

        assertEquals(2, bookDao.findAll().size());
        assertTrue(actualResult);
    }

    @Test
    void deleteAllByReaderId_whenNotExist_thenFalse() {
        final Long readerId = 666L;

        boolean actualResult = bookDao.deleteAllByReaderId(readerId);

        assertEquals(3, bookDao.findAll().size());
        assertFalse(actualResult);
    }


    @Test
    void isContainById_whenValidKey_thenTrue() {
        final Long bookId = 1L;

        assertTrue(bookDao.isContainById(bookId));
    }

    @Test
    void isContainById_whenInvalidKey_thenFalse() {
        final Long bookId = 666L;

        assertFalse(bookDao.isContainById(bookId));
    }


}