package ru.aston.lepd.readingclub.integration.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.aston.lepd.readingclub.util.ObjectContainer;
import ru.aston.lepd.readingclub.dto.ReaderDto;
import ru.aston.lepd.readingclub.entity.Reader;
import ru.aston.lepd.readingclub.exception.NotFoundException;
import ru.aston.lepd.readingclub.integration.IntegrationTestBase;
import ru.aston.lepd.readingclub.service.ReaderService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.aston.lepd.readingclub.util.Constants.*;

class ReaderServiceIT extends IntegrationTestBase {


    private ReaderService readerService;


    @BeforeEach
    void init() {
        this.readerService = new ObjectContainer().getReaderService();
    }




    @Test
    public void getById_whenValidId_thenReturnReaderDto() {
        final String expectedPhone = "71111111111";
        final Long readerId = 1L;

        ReaderDto actualResult = readerService.getById(readerId);

        assertEquals(expectedPhone, actualResult.getPhone());
    }

    @Test
    public void getById_whenInvalidId_thenThrowException() {
        final Long readerId = 666L;

        assertThrows(NotFoundException.class, () -> readerService.getById(readerId));
    }



    @Test
    public void getReaderById_whenValidId_thenReturnReader() {
        final Long expectedId = 1L;
        final Long readerId = 1L;

        Reader actualResult = readerService.getReaderById(readerId);

        assertEquals(expectedId, actualResult.getId());
    }

    @Test
    public void getReaderById_whenInvalidId_thenThrowException() {
        final Long readerId = 666L;

        assertThrows(NotFoundException.class, () -> readerService.getReaderById(readerId));
    }



    @Test
    public void getAll_whenExist_thenReturnList() {
        List<ReaderDto> actualResult = readerService.getAll();

        assertFalse(actualResult.isEmpty());
        assertEquals(3, actualResult.size());
    }

    @Test
    public void getAll_whenNotExist_thenReturnEmptyList() {
        readerService.delete(1L);
        readerService.delete(2L);
        readerService.delete(3L);

        List<ReaderDto> actualResult = readerService.getAll();

        assertTrue(actualResult.isEmpty());
    }



    @Test
    public void save() {
        final ReaderDto readerDto4 = new ReaderDto();
        readerDto4.setName("Alex");
        readerDto4.setSurname("Smith");
        readerDto4.setPhone("79999999999");
        readerDto4.setAddress("Street 5");

        ReaderDto actualResult = readerService.save(readerDto4);

        ReaderDto saved = readerService.getById(4L);
        assertEquals(readerDto4.getName(), saved.getName());
        assertEquals(readerDto4.getSurname(), saved.getSurname());
        assertEquals(readerDto4.getPhone(), saved.getPhone());
        assertEquals(readerDto4.getAddress(), saved.getAddress());
    }



    @Test
    public void update_whenValidId_thenSuccess() {
        final Long readerId = 1L;
        final ReaderDto readerDto = new ReaderDto();
        readerDto.setName("Alex");
        readerDto.setSurname("Smith");
        readerDto.setPhone("79999999999");
        readerDto.setAddress("Street 5");

        boolean actualResult = readerService.update(readerDto, readerId);

        ReaderDto updated = readerService.getById(readerId);
        assertEquals(readerDto.getName(), updated.getName());
        assertEquals(readerDto.getSurname(), updated.getSurname());
        assertEquals(readerDto.getPhone(), updated.getPhone());
        assertEquals(readerDto.getAddress(), updated.getAddress());
        assertTrue(actualResult);
    }

    @Test
    public void update_whenInvalidId_thenTrowException() {
        final Long readerId = 666L;

        assertThrows(NotFoundException.class, () -> readerService.update(READER_DTO_1, readerId));
    }



    @Test
    public void delete_whenValidId_thenTrue() {
        final Long readerId = 1L;

        boolean actualResult = readerService.delete(readerId);

        assertEquals(2, readerService.getAll().size());
        assertTrue(actualResult);
    }

    @Test
    public void delete_whenInvalidId_thenThrowException() {
        final Long authorId = 666L;

        assertThrows(NotFoundException.class, () -> readerService.delete(authorId));
    }



    @Test
    public void isContainById_whenValidId_thenTrue() {
        final Long authorId = 1L;

        boolean actualResult = readerService.isContainById(authorId);

        assertTrue(actualResult);
    }

    @Test
    public void isContainById_whenInvalidId_thenTrowException() {
        final Long authorId = 666L;

        assertThrows(NotFoundException.class, () -> readerService.isContainById(authorId));
    }



}