package ru.aston.lepd.readingclub.integration.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.aston.lepd.readingclub.util.ObjectContainer;
import ru.aston.lepd.readingclub.dto.BookDto;
import ru.aston.lepd.readingclub.entity.Book;
import ru.aston.lepd.readingclub.exception.NotFoundException;
import ru.aston.lepd.readingclub.integration.IntegrationTestBase;
import ru.aston.lepd.readingclub.service.BookService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.aston.lepd.readingclub.util.Constants.*;

class BookServiceIT extends IntegrationTestBase {


    private BookService bookService;


    @BeforeEach
    void init() {
        this.bookService = new ObjectContainer().getBookService();
    }


    @Test
    public void getById_whenValidId_thenReturnBookDto() {
        final Long expectedInventoryNumber = 11111L;
        final Long bookId = 1L;

        BookDto actualResult = bookService.getById(bookId);

        assertEquals(actualResult.getInventoryNumber(), expectedInventoryNumber);
    }

    @Test
    public void getById_whenInvalidId_thenThrowException() {
        final Long bookId = 666L;

        assertThrows(NotFoundException.class, () -> bookService.getById(bookId));
    }


    @Test
    public void getBookById_whenValidId_thenReturnBook() {
        final Long expectedId = 1L;
        final Long bookId = 1L;

        Book actualResult = bookService.getBookById(bookId);

        assertEquals(expectedId, actualResult.getId());
    }

    @Test
    public void getBookById_whenInvalidId_thenThrowException() {
        final Long bookId = 666L;

        assertThrows(NotFoundException.class, () -> bookService.getBookById(bookId));
    }


    @Test
    public void getAll_whenExist_thenReturnList() {
        List<BookDto> actualResult = bookService.getAll();

        assertFalse(actualResult.isEmpty());
        assertEquals(3, actualResult.size());
    }

    @Test
    public void getAll_whenNotExist_thenReturnEmptyList() {
        bookService.delete(1L);
        bookService.delete(2L);
        bookService.delete(3L);

        List<BookDto> actualResult = bookService.getAll();

        assertTrue(actualResult.isEmpty());
    }


    @Test
    void getAllByReaderId_whenExist_thenReturnList() {
        final Long readerId = 1L;

        List<BookDto> actualResult = bookService.getAllByReaderId(readerId);

        assertFalse(actualResult.isEmpty());
        assertEquals(1, actualResult.size());
    }

    @Test
    void getAllByReaderId_whenNotExist_thenReturnEmptyList() {
        final Long readerId = 666L;

        List<BookDto> actualResult = bookService.getAllByReaderId(readerId);

        assertTrue(actualResult.isEmpty());
    }


    @Test
    void getAllByAuthorId_whenExist_thenReturnList() {
        final Long authorId = 3L;

        List<BookDto> actualResult = bookService.getAllByAuthorId(authorId);

        assertFalse(actualResult.isEmpty());
        assertEquals(2, actualResult.size());
    }

    @Test
    void getAllByAuthorId_whenNotExist_thenReturnEmptyList() {
        final Long authorId = 666L;

        List<BookDto> actualResult = bookService.getAllByAuthorId(authorId);

        assertTrue(actualResult.isEmpty());
    }


    @Test
    public void save() {
        final BookDto bookDto4 = new BookDto();
        bookDto4.setTitle("new title");
        bookDto4.setInventoryNumber(55555L);
        bookDto4.setAuthorIds(List.of(2L, 3L));
        bookDto4.setReaderId(1L);

        BookDto actualResult = bookService.save(bookDto4);

        BookDto saved = bookService.getById(4L);
        assertEquals(bookDto4.getTitle(), saved.getTitle());
        assertEquals(bookDto4.getInventoryNumber(), saved.getInventoryNumber());
        assertEquals(bookDto4.getAuthorIds(), saved.getAuthorIds());
        assertEquals(bookDto4.getReaderId(), saved.getReaderId());
    }


    @Test
    public void update_whenValidId_thenSuccess() {
        final Long bookId = 1L;
        final BookDto bookDto = new BookDto();
        bookDto.setTitle("new title");
        bookDto.setInventoryNumber(55555L);
        bookDto.setAuthorIds(List.of(2L, 3L));
        bookDto.setReaderId(1L);

        boolean actualResult = bookService.update(bookDto, bookId);

        BookDto updated = bookService.getById(bookId);
        assertEquals(bookDto.getTitle(), updated.getTitle());
        assertEquals(bookDto.getInventoryNumber(), updated.getInventoryNumber());
        assertEquals(bookDto.getAuthorIds(), updated.getAuthorIds());
        assertEquals(bookDto.getReaderId(), updated.getReaderId());
        assertTrue(actualResult);
    }

    @Test
    public void update_whenValidIdButAuthorIdsAreEmpty_thenSuccess() {
        final Long bookId = 1L;
        final BookDto bookDto = new BookDto();
        bookDto.setTitle("new title");
        bookDto.setInventoryNumber(55555L);
        bookDto.setReaderId(1L);

        boolean actualResult = bookService.update(bookDto, bookId);

        BookDto updated = bookService.getById(bookId);
        assertEquals(bookDto.getTitle(), updated.getTitle());
        assertEquals(bookDto.getInventoryNumber(), updated.getInventoryNumber());
        assertEquals(bookDto.getReaderId(), updated.getReaderId());
        assertTrue(actualResult);
    }

    @Test
    public void update_whenValidIdButReaderIdIsNull_thenSuccess() {
        final Long bookId = 1L;
        final BookDto bookDto = new BookDto();
        bookDto.setTitle("new title");
        bookDto.setInventoryNumber(55555L);
        bookDto.setAuthorIds(List.of(2L, 3L));

        boolean actualResult = bookService.update(bookDto, bookId);

        BookDto updated = bookService.getById(bookId);
        assertEquals(bookDto.getTitle(), updated.getTitle());
        assertEquals(bookDto.getInventoryNumber(), updated.getInventoryNumber());
        assertEquals(bookDto.getAuthorIds(), updated.getAuthorIds());
        assertTrue(actualResult);
    }

    @Test
    public void update_whenInvalidId_thenTrowException() {
        final Long bookId = 666L;

        assertThrows(NotFoundException.class, () -> bookService.update(BOOK_DTO_1, bookId));
    }


    @Test
    public void delete_whenValidId_thenTrue() {
        final Long bookId = 1L;

        boolean actualResult = bookService.delete(bookId);

        assertEquals(2, bookService.getAll().size());
        assertTrue(actualResult);
    }

    @Test
    public void delete_whenInvalidId_thenThrowException() {
        final Long bookId = 666L;

        assertThrows(NotFoundException.class, () -> bookService.delete(bookId));
    }


    @Test
    public void isContainById_whenValidId_thenTrue() {
        final Long bookId = 1L;

        boolean actualResult = bookService.isContainById(bookId);

        assertTrue(actualResult);
    }

    @Test
    public void isContainById_whenInvalidId_thenTrowException() {
        final Long authorId = 666L;

        assertThrows(NotFoundException.class, () -> bookService.isContainById(authorId));
    }


}