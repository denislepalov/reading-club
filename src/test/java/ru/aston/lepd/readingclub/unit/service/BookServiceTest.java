package ru.aston.lepd.readingclub.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aston.lepd.readingclub.dao.BookDao;
import ru.aston.lepd.readingclub.dto.BookDto;
import ru.aston.lepd.readingclub.entity.Author;
import ru.aston.lepd.readingclub.entity.Book;
import ru.aston.lepd.readingclub.entity.Reader;
import ru.aston.lepd.readingclub.exception.NotFoundException;
import ru.aston.lepd.readingclub.service.BookService;
import ru.aston.lepd.readingclub.util.CustomMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.aston.lepd.readingclub.util.Constants.*;


@ExtendWith(MockitoExtension.class)
class BookServiceTest {


    @Mock
    private BookDao bookDao;
    @Mock
    private CustomMapper mapper;
    @InjectMocks
    private BookService bookService;




    @Test
    public void getById_whenValidId_thenReturnBookDto() {
        final Long bookId = 1L;
        final Book book = SHORT_BOOK_1.clone();
        doReturn(Optional.of(book)).when(bookDao).findById(bookId);
        doReturn(FULL_BOOK_1.getAuthors()).when(bookDao).getAuthorsForBook(bookId);
        doReturn(BOOK_DTO_1).when(mapper).bookToBookDto(any(Book.class));

        BookDto actualResult = bookService.getById(bookId);

        verify(bookDao).findById(bookId);
        verify(bookDao).getAuthorsForBook(bookId);
        verify(mapper).bookToBookDto(any(Book.class));
        assertEquals(BOOK_DTO_1.getInventoryNumber(), actualResult.getInventoryNumber());
    }

    @Test
    public void getById_whenInvalidId_thenThrowException() {
        final Long bookId = 666L;
        doReturn(Optional.empty()).when(bookDao).findById(bookId);

        assertThrows(NotFoundException.class, () -> bookService.getById(bookId));

        verify(bookDao).findById(bookId);
        verify(bookDao, never()).getAuthorsForBook(bookId);
        verify(mapper, never()).bookToBookDto(any(Book.class));
    }



    @Test
    public void getBookById_whenValidId_thenReturnBook() {
        final Long bookId = 1L;
        final Book book = SHORT_BOOK_1.clone();
        doReturn(Optional.of(book)).when(bookDao).findById(bookId);
        doReturn(FULL_BOOK_1.getAuthors()).when(bookDao).getAuthorsForBook(bookId);

        Book actualResult = bookService.getBookById(bookId);

        verify(bookDao).findById(bookId);
        verify(bookDao).getAuthorsForBook(bookId);
        assertEquals(FULL_BOOK_1.getId(), actualResult.getId());
    }

    @Test
    public void getBookById_whenInvalidId_thenThrowException() {
        final Long bookId = 666L;
        doReturn(Optional.empty()).when(bookDao).findById(bookId);

        assertThrows(NotFoundException.class, () -> bookService.getBookById(bookId));

        verify(bookDao).findById(bookId);
        verify(bookDao, never()).getAuthorsForBook(bookId);
    }



    @Test
    public void getAll_whenExist_thenReturnList() {
        doReturn(List.of(SHORT_BOOK_1, SHORT_BOOK_2, SHORT_BOOK_3)).when(bookDao).findAll();
        doReturn(FULL_BOOK_1.getAuthors()).when(bookDao).getAuthorsForBook(anyLong());
        doReturn(BOOK_DTO_1).when(mapper).bookToBookDto(any(Book.class));

        List<BookDto> actualResult = bookService.getAll();

        verify(bookDao).findAll();
        verify(bookDao, times(3)).getAuthorsForBook(anyLong());
        verify(mapper, times(3)).bookToBookDto(any(Book.class));
        assertFalse(actualResult.isEmpty());
        assertEquals(3, actualResult.size());
    }

    @Test
    public void getAll_whenNotExist_thenReturnEmptyList() {
        doReturn(Collections.emptyList()).when(bookDao).findAll();

        List<BookDto> actualResult = bookService.getAll();

        verify(bookDao).findAll();
        verify(bookDao, never()).getAuthorsForBook(anyLong());
        verify(mapper, never()).bookToBookDto(any(Book.class));
        assertTrue(actualResult.isEmpty());
    }



    @Test
    void getAllByReaderId_whenExist_thenReturnList() {
        final Long readerId = 1L;
        doReturn(List.of(SHORT_BOOK_1)).when(bookDao).findAllByReaderId(readerId);
        doReturn(List.of(SHORT_AUTHOR_1, SHORT_AUTHOR_3)).when(bookDao).getAuthorsForBook(anyLong());
        doReturn(BOOK_DTO_1).when(mapper).bookToBookDto(any(Book.class));

        List<BookDto> actualResult = bookService.getAllByReaderId(readerId);

        verify(bookDao).findAllByReaderId(readerId);
        verify(bookDao).getAuthorsForBook(anyLong());
        verify(mapper).bookToBookDto(any(Book.class));
        assertFalse(actualResult.isEmpty());
        assertEquals(1, actualResult.size());
    }

    @Test
    void getAllByReaderId_whenNotExist_thenReturnEmptyList() {
        final Long readerId = 666L;
        doReturn(Collections.emptyList()).when(bookDao).findAllByReaderId(readerId);

        List<BookDto> actualResult = bookService.getAllByReaderId(readerId);

        verify(bookDao).findAllByReaderId(readerId);
        verify(bookDao, never()).getAuthorsForBook(anyLong());
        verify(mapper, never()).bookToBookDto(any(Book.class));
        assertTrue(actualResult.isEmpty());
    }



    @Test
    void getAllByAuthorId_whenExist_thenReturnList() {
        final Long authorId = 3L;
        doReturn(List.of(SHORT_BOOK_1, SHORT_BOOK_3)).when(bookDao).findAllByAuthorId(authorId);
        doReturn(List.of(SHORT_AUTHOR_1, SHORT_AUTHOR_3)).when(bookDao).getAuthorsForBook(anyLong());
        doReturn(BOOK_DTO_1).when(mapper).bookToBookDto(any(Book.class));

        List<BookDto> actualResult = bookService.getAllByAuthorId(authorId);

        verify(bookDao).findAllByAuthorId(authorId);
        verify(bookDao, times(2)).getAuthorsForBook(anyLong());
        verify(mapper, times(2)).bookToBookDto(any(Book.class));
        assertFalse(actualResult.isEmpty());
        assertEquals(2, actualResult.size());
    }

    @Test
    void getAllByAuthorId_whenNotExist_thenReturnEmptyList() {
        final Long authorId = 666L;
        doReturn(Collections.emptyList()).when(bookDao).findAllByAuthorId(authorId);

        List<BookDto> actualResult = bookService.getAllByAuthorId(authorId);

        verify(bookDao).findAllByAuthorId(authorId);
        verify(bookDao, never()).getAuthorsForBook(anyLong());
        verify(mapper, never()).bookToBookDto(any(Book.class));
        assertTrue(actualResult.isEmpty());
    }



    @Test
    public void save() {
        final Reader reader = new Reader();
        reader.setId(2L);
        final Author author = new Author();
        author.setId(2L);
        final Book book = new Book();
        book.setTitle("Title2");
        book.setInventoryNumber(22222L);
        book.setAuthors(List.of(author));
        book.setReader(reader);
        doReturn(book).when(mapper).bookDtoToBook(BOOK_DTO_2);
        doReturn(FULL_BOOK_2).when(bookDao).save(book);
        doReturn(BOOK_DTO_2).when(mapper).bookToBookDto(FULL_BOOK_2);

        BookDto actualResult = bookService.save(BOOK_DTO_2);

        verify(mapper).bookDtoToBook(BOOK_DTO_2);
        verify(bookDao).save(book);
        verify(mapper).bookToBookDto(FULL_BOOK_2);
        assertEquals(BOOK_DTO_2, actualResult);
    }



    @Test
    public void update_whenValidId_thenSuccess() {
        final Long bookId = 1L;
        final Book book = FULL_BOOK_1.clone();
        doReturn(Optional.of(book)).when(bookDao).findById(bookId);
        doReturn(FULL_BOOK_1.getAuthors()).when(bookDao).getAuthorsForBook(bookId);
        doReturn(true).when(bookDao).update(book);

        boolean actualResult = bookService.update(BOOK_DTO_1, bookId);

        verify(bookDao).findById(bookId);
        verify(bookDao).getAuthorsForBook(bookId);
        verify(bookDao).update(book);
        assertTrue(actualResult);;
    }

    @Test
    public void update_whenInvalidId_thenTrowException() {
        final Long bookId = 666L;
        doReturn(Optional.empty()).when(bookDao).findById(bookId);

        assertThrows(NotFoundException.class, () -> bookService.update(BOOK_DTO_1, bookId));

        verify(bookDao).findById(bookId);
        verify(bookDao, never()).getAuthorsForBook(bookId);
        verify(bookDao, never()).update(FULL_BOOK_1);
    }



    @Test
    public void delete_whenValidId_thenTrue() {
        final Long bookId = 1L;
        doReturn(true).when(bookDao).isContainById(bookId);
        doReturn(true).when(bookDao).delete(bookId);

        boolean actualResult = bookService.delete(bookId);

        verify(bookDao).isContainById(bookId);
        verify(bookDao).delete(bookId);
        assertTrue(actualResult);
    }

    @Test
    public void delete_whenInvalidId_thenThrowException() {
        final Long bookId = 666L;
        doReturn(false).when(bookDao).isContainById(bookId);

        assertThrows(NotFoundException.class, () -> bookService.delete(bookId));

        verify(bookDao).isContainById(bookId);
        verify(bookDao, never()).delete(bookId);
    }



    @Test
    public void isContainById_whenValidId_thenTrue() {
        final Long bookId = 1L;
        doReturn(true).when(bookDao).isContainById(bookId);

        boolean actualResult = bookService.isContainById(bookId);

        assertTrue(actualResult);
    }

    @Test
    public void isContainById_whenInvalidId_thenTrowException() {
        final Long authorId = 666L;
        doReturn(false).when(bookDao).isContainById(authorId);

        assertThrows(NotFoundException.class, () -> bookService.isContainById(authorId));
    }


}