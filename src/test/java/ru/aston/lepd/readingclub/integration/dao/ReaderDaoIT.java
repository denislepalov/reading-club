package ru.aston.lepd.readingclub.integration.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.aston.lepd.readingclub.util.ObjectContainer;
import ru.aston.lepd.readingclub.dao.BookDao;
import ru.aston.lepd.readingclub.dao.ReaderDao;
import ru.aston.lepd.readingclub.entity.Book;
import ru.aston.lepd.readingclub.entity.Reader;
import ru.aston.lepd.readingclub.exception.DaoException;
import ru.aston.lepd.readingclub.integration.IntegrationTestBase;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static ru.aston.lepd.readingclub.util.Constants.*;

class ReaderDaoIT extends IntegrationTestBase {


    private ReaderDao readerDao;


    @BeforeEach
    void init() {
        this.readerDao = new ObjectContainer().getReaderDao();
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
        final Reader reader = new Reader();
        reader.setName("Alex");
        reader.setSurname("Smith");
        reader.setPhone("79999999999");
        reader.setAddress("Street 5");

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
        reader.setName("Alex");
        reader.setSurname("Smith");
        reader.setPhone("79999999999");
        reader.setAddress("Street 5");

        boolean actualResult = readerDao.update(reader);

        Reader updated = readerDao.findById(readerId).get();
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

        assertThrows(DaoException.class, () -> readerDao.save(reader));
    }
    @Test
    void update_whenPhoneIsNotUnique_thenThrowException() {
        final Long readerId = 1L;
        final Reader reader = readerDao.findById(readerId).get();
        reader.setName("Alex");
        reader.setSurname("Smith");
        reader.setPhone("71111111111");
        reader.setAddress("Street 5");

        assertThrows(DaoException.class, () -> readerDao.save(reader));
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


}