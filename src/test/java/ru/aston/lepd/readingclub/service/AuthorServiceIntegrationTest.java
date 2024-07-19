package ru.aston.lepd.readingclub.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.aston.lepd.readingclub.util.ObjectContainer;
import ru.aston.lepd.readingclub.dto.AuthorDto;
import ru.aston.lepd.readingclub.entity.Author;
import ru.aston.lepd.readingclub.exception.NotFoundException;
import ru.aston.lepd.readingclub.util.IntegrationTestBase;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.aston.lepd.readingclub.util.Constants.*;


class AuthorServiceIntegrationTest extends IntegrationTestBase {

    private AuthorService authorService;


    @BeforeEach
    void init() {
        this.authorService = new ObjectContainer().getAuthorService();
    }



    @Test
    public void getById_whenValidId_thenReturnAuthorDto() {
        AuthorDto expectedAuthorDto = new AuthorDto();
        expectedAuthorDto.setFullName("Author1");
        expectedAuthorDto.setPersonalInfo("likes dogs");
        final Long expectedId = 1L;
        final Long authorId = 1L;

        AuthorDto actualResult = authorService.getById(authorId);

        assertEquals(expectedAuthorDto.getFullName(), actualResult.getFullName());
        assertEquals(expectedAuthorDto.getPersonalInfo(), actualResult.getPersonalInfo());
    }

    @Test
    public void getById_whenInvalidId_thenThrowException() {
        final Long authorId = 666L;

        assertThrows(NotFoundException.class, () -> authorService.getById(authorId));
    }



    @Test
    public void getAuthorById_whenValidId_thenReturnAuthor() {
        final Long expectedId = 1L;
        final Long authorId = 1L;

        Author actualResult = authorService.getAuthorById(authorId);

        assertEquals(expectedId, actualResult.getId());
    }

    @Test
    public void getAuthorById_whenInvalidId_thenThrowException() {
        final Long authorId = 666L;

        assertThrows(NotFoundException.class, () -> authorService.getAuthorById(authorId));
    }



    @Test
    public void getAll_whenExist_thenReturnList() {
        List<AuthorDto> actualResult = authorService.getAll();

        assertFalse(actualResult.isEmpty());
        assertEquals(3, actualResult.size());
    }

    @Test
    public void getAll_whenNotExist_thenReturnEmptyList() {
        authorService.delete(1L);
        authorService.delete(2L);
        authorService.delete(3L);

        List<AuthorDto> actualResult = authorService.getAll();

        assertTrue(actualResult.isEmpty());
    }



    @Test
    public void save() {
        final AuthorDto authorDto4 = new AuthorDto();
        authorDto4.setFullName("Steven King");
        authorDto4.setPersonalInfo("likes fish");

        AuthorDto actualResult = authorService.save(authorDto4);

        assertEquals(authorDto4.getFullName(), actualResult.getFullName());
        assertEquals(authorDto4.getPersonalInfo(), actualResult.getPersonalInfo());
    }



    @Test
    public void update_whenValidId_thenSuccess() {
        final Long authorId = 1L;
        final AuthorDto authorDto = new AuthorDto();
        authorDto.setFullName("Steven King");
        authorDto.setPersonalInfo("likes fish");

        boolean actualResult = authorService.update(authorDto, authorId);

        AuthorDto updated = authorService.getById(authorId);
        assertEquals(authorDto.getFullName(), updated.getFullName());
        assertEquals(authorDto.getPersonalInfo(), updated.getPersonalInfo());
        assertTrue(actualResult);
    }

    @Test
    public void update_whenInvalidId_thenTrowException() {
        final Long authorId = 666L;

        assertThrows(NotFoundException.class, () -> authorService.update(AUTHOR_DTO_1, authorId));
    }



    @Test
    public void delete_whenValidId_thenTrue() {
        final Long authorId = 1L;

        boolean actualResult = authorService.delete(authorId);

        assertEquals(2, authorService.getAll().size());
        assertTrue(actualResult);
    }

    @Test
    public void delete_whenInvalidId_thenThrowException() {
        final Long authorId = 666L;

        assertThrows(NotFoundException.class, () -> authorService.delete(authorId));
    }



    @Test
    public void isContainById_whenValidId_thenTrue() {
        final Long authorId = 1L;

        boolean actualResult = authorService.isContainById(authorId);

        assertTrue(actualResult);
    }

    @Test
    public void isContainById_whenInvalidId_thenTrowException() {
        final Long authorId = 666L;

        assertThrows(NotFoundException.class, () -> authorService.isContainById(authorId));
    }



}