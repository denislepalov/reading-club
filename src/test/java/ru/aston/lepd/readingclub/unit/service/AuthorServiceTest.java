package ru.aston.lepd.readingclub.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aston.lepd.readingclub.dao.AuthorDao;
import ru.aston.lepd.readingclub.dto.AuthorDto;
import ru.aston.lepd.readingclub.entity.Author;
import ru.aston.lepd.readingclub.entity.Book;
import ru.aston.lepd.readingclub.exception.NotFoundException;
import ru.aston.lepd.readingclub.service.AuthorService;
import ru.aston.lepd.readingclub.util.CustomMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.aston.lepd.readingclub.util.Constants.*;


@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    @Mock
    private AuthorDao authorDao;
    @Mock
    private CustomMapper mapper;
    @InjectMocks
    private AuthorService authorService;




    @Test
    public void getById_whenValidId_thenReturnAuthorDto() {
        final Long authorId = 1L;
        final Author author = SHORT_AUTHOR_1.clone();
        doReturn(Optional.of(author)).when(authorDao).findById(authorId);
        doReturn(FULL_AUTHOR_1.getBooks()).when(authorDao).getBooksForAuthor(authorId);
        doReturn(AUTHOR_DTO_1).when(mapper).authorToAuthorDto(any(Author.class));

        AuthorDto actualResult = authorService.getById(authorId);

        verify(authorDao).findById(authorId);
        verify(authorDao).getBooksForAuthor(authorId);
        verify(mapper).authorToAuthorDto(any(Author.class));
        assertEquals(AUTHOR_DTO_1, actualResult);
    }

    @Test
    public void getById_whenInvalidId_thenThrowException() {
        final Long authorId = 666L;
        doReturn(Optional.empty()).when(authorDao).findById(authorId);

        assertThrows(NotFoundException.class, () -> authorService.getById(authorId));

        verify(authorDao).findById(authorId);
        verify(authorDao, never()).getBooksForAuthor(authorId);
        verify(mapper, never()).authorToAuthorDto(any(Author.class));
    }



    @Test
    public void getAuthorById_whenValidId_thenReturnAuthor() {
        final Long authorId = 1L;
        final Author author = SHORT_AUTHOR_1.clone();
        doReturn(Optional.of(author)).when(authorDao).findById(authorId);
        doReturn(FULL_AUTHOR_1.getBooks()).when(authorDao).getBooksForAuthor(authorId);

        Author actualResult = authorService.getAuthorById(authorId);

        verify(authorDao).findById(authorId);
        verify(authorDao).getBooksForAuthor(authorId);
        assertEquals(FULL_AUTHOR_1.getId(), actualResult.getId());
    }

    @Test
    public void getAuthorById_whenInvalidId_thenThrowException() {
        final Long authorId = 666L;
        doReturn(Optional.empty()).when(authorDao).findById(authorId);

        assertThrows(NotFoundException.class, () -> authorService.getAuthorById(authorId));

        verify(authorDao).findById(authorId);
        verify(authorDao, never()).getBooksForAuthor(authorId);
    }



    @Test
    public void getAll_whenExist_thenReturnList() {
        doReturn(List.of(SHORT_AUTHOR_1.clone(), SHORT_AUTHOR_2.clone(), SHORT_AUTHOR_3.clone()))
                .when(authorDao).findAll();
        doReturn(FULL_AUTHOR_1.getBooks()).when(authorDao).getBooksForAuthor(anyLong());
        doReturn(AUTHOR_DTO_1).when(mapper).authorToAuthorDto(any(Author.class));

        List<AuthorDto> actualResult = authorService.getAll();

        verify(authorDao).findAll();
        verify(authorDao, times(3)).getBooksForAuthor(anyLong());
        verify(mapper, times(3)).authorToAuthorDto(any(Author.class));
        assertFalse(actualResult.isEmpty());
        assertEquals(3, actualResult.size());
    }

    @Test
    public void getAll_whenNotExist_thenReturnEmptyList() {
        doReturn(Collections.emptyList()).when(authorDao).findAll();

        List<AuthorDto> actualResult = authorService.getAll();

        verify(authorDao).findAll();
        verify(authorDao, never()).getBooksForAuthor(anyLong());
        verify(mapper, never()).authorToAuthorDto(any(Author.class));
        assertTrue(actualResult.isEmpty());
    }



    @Test
    public void save() {
        final Author author = new Author();
        author.setFullName("Author1");
        author.setPersonalInfo("likes dogs");
        doReturn(author).when(mapper).authorDtoToAuthor(AUTHOR_DTO_1);
        doReturn(SHORT_AUTHOR_1).when(authorDao).save(author);
        doReturn(AUTHOR_DTO_1).when(mapper).authorToAuthorDto(SHORT_AUTHOR_1);

        AuthorDto actualResult = authorService.save(AUTHOR_DTO_1);

        verify(mapper).authorDtoToAuthor(AUTHOR_DTO_1);
        verify(authorDao).save(author);
        verify(mapper).authorToAuthorDto(SHORT_AUTHOR_1);
        assertEquals(AUTHOR_DTO_1, actualResult);
    }



    @Test
    public void update_whenValidId_thenSuccess() {
        final Long authorId = 1L;
        final Author author = FULL_AUTHOR_1.clone();
        doReturn(Optional.of(author)).when(authorDao).findById(authorId);
        doReturn(FULL_AUTHOR_1.getBooks()).when(authorDao).getBooksForAuthor(authorId);
        doReturn(true).when(authorDao).update(author);

        boolean actualResult = authorService.update(AUTHOR_DTO_1, authorId);

        verify(authorDao).findById(authorId);
        verify(authorDao).getBooksForAuthor(authorId);
        verify(authorDao).update(author);
        assertTrue(actualResult);
    }

    @Test
    public void update_whenInvalidId_thenTrowException() {
        final Long authorId = 666L;
        doReturn(Optional.empty()).when(authorDao).findById(authorId);

        assertThrows(NotFoundException.class, () -> authorService.update(AUTHOR_DTO_1, authorId));

        verify(authorDao).findById(authorId);
        verify(authorDao, never()).getBooksForAuthor(authorId);
        verify(authorDao, never()).update(FULL_AUTHOR_1);
    }



    @Test
    public void delete_whenValidId_thenTrue() {
        final Long authorId = 1L;
        doReturn(true).when(authorDao).isContainById(authorId);
        doReturn(true).when(authorDao).delete(authorId);

        boolean actualResult = authorService.delete(authorId);

        verify(authorDao).isContainById(authorId);
        verify(authorDao).delete(authorId);
        assertTrue(actualResult);
    }

    @Test
    public void delete_whenInvalidId_thenThrowException() {
        final Long authorId = 666L;
        doReturn(false).when(authorDao).isContainById(authorId);

        assertThrows(NotFoundException.class, () -> authorService.delete(authorId));

        verify(authorDao).isContainById(authorId);
        verify(authorDao, never()).delete(authorId);
    }



    @Test
    public void isContainById_whenValidId_thenTrue() {
        final Long authorId = 1L;
        doReturn(true).when(authorDao).isContainById(authorId);

        boolean actualResult = authorService.isContainById(authorId);

        assertTrue(actualResult);
    }

    @Test
    public void isContainById_whenInvalidId_thenTrowException() {
        final Long authorId = 666L;
        doReturn(false).when(authorDao).isContainById(authorId);

        assertThrows(NotFoundException.class, () -> authorService.isContainById(authorId));
    }



}