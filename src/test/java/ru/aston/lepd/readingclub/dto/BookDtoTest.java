package ru.aston.lepd.readingclub.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookDtoTest {

    private BookDto bookDto;


    @BeforeEach
    void setUp() {
        bookDto = new BookDto();
        bookDto.setTitle("Title");
        bookDto.setAuthorIds(List.of(1L, 2L, 3L));
        bookDto.setReaderId(1L);
    }




    @Test
    void getTitle() {
        final String expectedResult = "Title";

        String actualResult = bookDto.getTitle();

        assertEquals(expectedResult, actualResult);
    }



    @Test
    void getAuthorIds() {
        final List<Long> expectedResult = List.of(1L, 2L, 3L);

        List<Long> actualResult = bookDto.getAuthorIds();

        assertEquals(expectedResult, actualResult);
    }



    @Test
    void getReaderId() {
        final Long expectedResult = 1L;

        Long actualResult = bookDto.getReaderId();

        assertEquals(expectedResult, actualResult);
    }


}