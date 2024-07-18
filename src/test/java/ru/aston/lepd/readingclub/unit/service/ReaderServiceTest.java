package ru.aston.lepd.readingclub.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aston.lepd.readingclub.dao.ReaderDao;
import ru.aston.lepd.readingclub.dto.ReaderDto;
import ru.aston.lepd.readingclub.entity.Reader;
import ru.aston.lepd.readingclub.exception.NotFoundException;
import ru.aston.lepd.readingclub.service.ReaderService;
import ru.aston.lepd.readingclub.util.CustomMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ru.aston.lepd.readingclub.util.Constants.*;


@ExtendWith(MockitoExtension.class)
class ReaderServiceTest {


    @Mock
    private ReaderDao readerDao;
    @Mock
    private CustomMapper mapper;
    @InjectMocks
    private ReaderService readerService;




    @Test
    public void getById_whenValidId_thenReturnReaderDto() {
        final Long readerId = 1L;
        final Reader reader = SHORT_READER_1.clone();
        doReturn(Optional.of(reader)).when(readerDao).findById(readerId);
        doReturn(FULL_READER_1.getBooks()).when(readerDao).getBooksForReader(readerId);
        doReturn(READER_DTO_1).when(mapper).readerToReaderDto(any(Reader.class));

        ReaderDto actualResult = readerService.getById(readerId);

        verify(readerDao).findById(readerId);
        verify(readerDao).getBooksForReader(readerId);
        verify(mapper).readerToReaderDto(any(Reader.class));
        assertEquals(READER_DTO_1, actualResult);
    }

    @Test
    public void getById_whenInvalidId_thenThrowException() {
        final Long readerId = 666L;
        doReturn(Optional.empty()).when(readerDao).findById(readerId);

        assertThrows(NotFoundException.class, () -> readerService.getById(readerId));

        verify(readerDao).findById(readerId);
        verify(readerDao, never()).getBooksForReader(readerId);
        verify(mapper, never()).readerToReaderDto(any(Reader.class));
    }



    @Test
    public void getReaderById_whenValidId_thenReturnReader() {
        final Long readerId = 1L;
        final Reader reader = SHORT_READER_1.clone();
        doReturn(Optional.of(reader)).when(readerDao).findById(readerId);
        doReturn(FULL_READER_1.getBooks()).when(readerDao).getBooksForReader(readerId);

        Reader actualResult = readerService.getReaderById(readerId);

        verify(readerDao).findById(readerId);
        verify(readerDao).getBooksForReader(readerId);
        assertEquals(FULL_READER_1.getId(), actualResult.getId());
    }

    @Test
    public void getReaderById_whenInvalidId_thenThrowException() {
        final Long readerId = 666L;
        doReturn(Optional.empty()).when(readerDao).findById(readerId);

        assertThrows(NotFoundException.class, () -> readerService.getReaderById(readerId));

        verify(readerDao).findById(readerId);
        verify(readerDao, never()).getBooksForReader(readerId);
    }



    @Test
    public void getAll_whenExist_thenReturnList() {
        doReturn(List.of(SHORT_READER_1.clone(), SHORT_READER_2.clone(), SHORT_READER_3.clone()))
                .when(readerDao).findAll();
        doReturn(FULL_READER_1.getBooks()).when(readerDao).getBooksForReader(anyLong());
        doReturn(READER_DTO_1).when(mapper).readerToReaderDto(any(Reader.class));

        List<ReaderDto> actualResult = readerService.getAll();

        verify(readerDao).findAll();
        verify(readerDao, times(3)).getBooksForReader(anyLong());
        verify(mapper, times(3)).readerToReaderDto(any(Reader.class));
        assertFalse(actualResult.isEmpty());
        assertEquals(3, actualResult.size());
    }

    @Test
    public void getAll_whenNotExist_thenReturnEmptyList() {
        doReturn(Collections.emptyList()).when(readerDao).findAll();

        List<ReaderDto> actualResult = readerService.getAll();

        verify(readerDao).findAll();
        verify(readerDao, never()).getBooksForReader(anyLong());
        verify(mapper, never()).readerToReaderDto(any(Reader.class));
        assertTrue(actualResult.isEmpty());
    }



    @Test
    public void save() {
        final Reader reader = new Reader();
        reader.setName("Ivan");
        reader.setSurname("Ivanov");
        reader.setPhone("71111111111");
        reader.setAddress("Lenina 11");
        doReturn(reader).when(mapper).readerDtoToReader(READER_DTO_1);
        doReturn(SHORT_READER_1).when(readerDao).save(reader);
        doReturn(READER_DTO_1).when(mapper).readerToReaderDto(SHORT_READER_1);

        ReaderDto actualResult = readerService.save(READER_DTO_1);

        verify(mapper).readerDtoToReader(READER_DTO_1);
        verify(readerDao).save(reader);
        verify(mapper).readerToReaderDto(SHORT_READER_1);
        assertEquals(READER_DTO_1, actualResult);
    }



    @Test
    public void update_whenValidId_thenSuccess() {
        final Long readerId = 1L;
        final Reader author = FULL_READER_1.clone();
        doReturn(Optional.of(author)).when(readerDao).findById(readerId);
        doReturn(FULL_READER_1.getBooks()).when(readerDao).getBooksForReader(readerId);
        doReturn(true).when(readerDao).update(author);

        boolean actualResult = readerService.update(READER_DTO_1, readerId);

        verify(readerDao).findById(readerId);
        verify(readerDao).getBooksForReader(readerId);
        verify(readerDao).update(author);
        assertTrue(actualResult);
    }

    @Test
    public void update_whenInvalidId_thenTrowException() {
        final Long readerId = 666L;
        doReturn(Optional.empty()).when(readerDao).findById(readerId);

        assertThrows(NotFoundException.class, () -> readerService.update(READER_DTO_1, readerId));

        verify(readerDao).findById(readerId);
        verify(readerDao, never()).getBooksForReader(readerId);
        verify(readerDao, never()).update(FULL_READER_1);
    }



    @Test
    public void delete_whenValidId_thenTrue() {
        final Long readerId = 1L;
        doReturn(true).when(readerDao).isContainById(readerId);
        doReturn(true).when(readerDao).delete(readerId);

        boolean actualResult = readerService.delete(readerId);

        verify(readerDao).isContainById(readerId);
        verify(readerDao).delete(readerId);
        assertTrue(actualResult);
    }

    @Test
    public void delete_whenInvalidId_thenThrowException() {
        final Long authorId = 666L;
        doReturn(false).when(readerDao).isContainById(authorId);

        assertThrows(NotFoundException.class, () -> readerService.delete(authorId));

        verify(readerDao).isContainById(authorId);
        verify(readerDao, never()).delete(authorId);
    }



    @Test
    public void isContainById_whenValidId_thenTrue() {
        final Long authorId = 1L;
        doReturn(true).when(readerDao).isContainById(authorId);

        boolean actualResult = readerService.isContainById(authorId);

        assertTrue(actualResult);
    }

    @Test
    public void isContainById_whenInvalidId_thenTrowException() {
        final Long authorId = 666L;
        doReturn(false).when(readerDao).isContainById(authorId);

        assertThrows(NotFoundException.class, () -> readerService.isContainById(authorId));
    }



}