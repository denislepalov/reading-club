package ru.aston.lepd.readingclub.util;

import ru.aston.lepd.readingclub.dto.AuthorDto;
import ru.aston.lepd.readingclub.dto.BookDto;
import ru.aston.lepd.readingclub.dto.ReaderDto;
import ru.aston.lepd.readingclub.entity.Author;
import ru.aston.lepd.readingclub.entity.Book;
import ru.aston.lepd.readingclub.entity.Reader;

import java.util.ArrayList;
import java.util.List;

public class Constants {


    public static final Reader SHORT_READER_1 = new Reader();
    static {
        SHORT_READER_1.setId(1L);
        SHORT_READER_1.setName("Ivan");
        SHORT_READER_1.setSurname("Ivanov");
        SHORT_READER_1.setPhone("71111111111");
        SHORT_READER_1.setAddress("Lenina 11");
    }
    public static final Reader SHORT_READER_2 = new Reader();
    static {
        SHORT_READER_2.setId(2L);
        SHORT_READER_2.setName("Petr");
        SHORT_READER_2.setSurname("Petrov");
        SHORT_READER_2.setPhone("72222222222");
        SHORT_READER_2.setAddress("Lenina 22");
    }
    public static final Reader SHORT_READER_3 = new Reader();
    static {
        SHORT_READER_3.setId(3L);
        SHORT_READER_3.setName("Sveta");
        SHORT_READER_3.setSurname("Svetikova");
        SHORT_READER_3.setPhone("73333333333");
        SHORT_READER_3.setAddress("Lenina 33");
    }



    public static final Book SHORT_BOOK_1 = new Book();
    static {
        SHORT_BOOK_1.setId(1L);
        SHORT_BOOK_1.setTitle("Title1");
        SHORT_BOOK_1.setInventoryNumber(11111L);
        SHORT_BOOK_1.setReader(SHORT_READER_1);
    }
    public static final Book SHORT_BOOK_2 = new Book();
    static {
        SHORT_BOOK_2.setId(2L);
        SHORT_BOOK_2.setTitle("Title2");
        SHORT_BOOK_2.setInventoryNumber(22222L);
        SHORT_BOOK_2.setReader(SHORT_READER_2);
    }
    public static final Book SHORT_BOOK_3 = new Book();
    static {
        SHORT_BOOK_3.setId(3L);
        SHORT_BOOK_3.setTitle("Title3");
        SHORT_BOOK_3.setInventoryNumber(33333L);
        SHORT_BOOK_3.setReader(SHORT_READER_3);
    }



    public static final Author SHORT_AUTHOR_1 = new Author();
    static {
        SHORT_AUTHOR_1.setId(1L);
        SHORT_AUTHOR_1.setFullName("Author1");
        SHORT_AUTHOR_1.setPersonalInfo("likes dogs");
    }
    public static final Author SHORT_AUTHOR_2 = new Author();
    static {
        SHORT_AUTHOR_2.setId(2L);
        SHORT_AUTHOR_2.setFullName("Author2");
        SHORT_AUTHOR_2.setPersonalInfo("likes cats");
    }
    public static final Author SHORT_AUTHOR_3 = new Author();
    static {
        SHORT_AUTHOR_3.setId(3L);
        SHORT_AUTHOR_3.setFullName("Author3");
        SHORT_AUTHOR_3.setPersonalInfo("likes wolfs");
    }




    public static final Reader FULL_READER_1 = new Reader();
    static {
        FULL_READER_1.setId(1L);
        FULL_READER_1.setName("Ivan");
        FULL_READER_1.setSurname("Ivanov");
        FULL_READER_1.setPhone("71111111111");
        FULL_READER_1.setAddress("Lenina 11");
        FULL_READER_1.setBooks(List.of(SHORT_BOOK_1));
    }
    public static final Reader FULL_READER_2 = new Reader();
    static {
        FULL_READER_2.setId(2L);
        FULL_READER_2.setName("Petr");
        FULL_READER_2.setSurname("Petrov");
        FULL_READER_2.setPhone("72222222222");
        FULL_READER_2.setAddress("Lenina 22");
        FULL_READER_2.setBooks(List.of(SHORT_BOOK_2));
    }
    public static final Reader FULL_READER_3 = new Reader();
    static {
        FULL_READER_3.setId(3L);
        FULL_READER_3.setName("Sveta");
        FULL_READER_3.setSurname("Svetikova");
        FULL_READER_3.setPhone("73333333333");
        FULL_READER_3.setAddress("Lenina 33");
        FULL_READER_3.setBooks(List.of(SHORT_BOOK_3));
    }



    public static final Book FULL_BOOK_1 = new Book();
    static {
        FULL_BOOK_1.setId(1L);
        FULL_BOOK_1.setTitle("Title1");
        FULL_BOOK_1.setInventoryNumber(11111L);
        FULL_BOOK_1.setAuthors(new ArrayList<>(List.of(SHORT_AUTHOR_1, SHORT_AUTHOR_3)));
        FULL_BOOK_1.setReader(SHORT_READER_1);
    }
    public static final Book FULL_BOOK_2 = new Book();
    static {
        FULL_BOOK_2.setId(2L);
        FULL_BOOK_2.setTitle("Title2");
        FULL_BOOK_2.setInventoryNumber(22222L);
        FULL_BOOK_2.setAuthors(new ArrayList<>(List.of(SHORT_AUTHOR_2)));
        FULL_BOOK_2.setReader(SHORT_READER_2);
    }
    public static final Book FULL_BOOK_3 = new Book();
    static {
        FULL_BOOK_3.setId(3L);
        FULL_BOOK_3.setTitle("Title3");
        FULL_BOOK_3.setInventoryNumber(33333L);
        FULL_BOOK_3.setAuthors(new ArrayList<>(List.of(SHORT_AUTHOR_2, SHORT_AUTHOR_3)));
        FULL_BOOK_3.setReader(SHORT_READER_3);
    }



    public static final Author FULL_AUTHOR_1 = new Author();
    static {
        FULL_AUTHOR_1.setId(1L);
        FULL_AUTHOR_1.setFullName("Author1");
        FULL_AUTHOR_1.setPersonalInfo("likes dogs");
        FULL_AUTHOR_1.setBooks(List.of(SHORT_BOOK_1));
    }
    public static final Author FULL_AUTHOR_2 = new Author();
    static {
        FULL_AUTHOR_2.setId(2L);
        FULL_AUTHOR_2.setFullName("Author2");
        FULL_AUTHOR_2.setPersonalInfo("likes cats");
        FULL_AUTHOR_2.setBooks(List.of(SHORT_BOOK_2, SHORT_BOOK_3));
    }
    public static final Author FULL_AUTHOR_3 = new Author();
    static {
        FULL_AUTHOR_3.setId(3L);
        FULL_AUTHOR_3.setFullName("Author3");
        FULL_AUTHOR_3.setPersonalInfo("likes wolfs");
        FULL_AUTHOR_3.setBooks(List.of(SHORT_BOOK_1, SHORT_BOOK_3));
    }




    public static final AuthorDto AUTHOR_DTO_1 = new AuthorDto();
    static {
        AUTHOR_DTO_1.setFullName("Author1");
        AUTHOR_DTO_1.setPersonalInfo("likes dogs");
    }
    public static final AuthorDto AUTHOR_DTO_2 = new AuthorDto();
    static {
        AUTHOR_DTO_2.setFullName("Author2");
        AUTHOR_DTO_2.setPersonalInfo("likes cats");
    }
    public static final AuthorDto AUTHOR_DTO_3 = new AuthorDto();
    static {
        AUTHOR_DTO_3.setFullName("Author3");
        AUTHOR_DTO_3.setPersonalInfo("likes wolfs");
    }




    public static final BookDto BOOK_DTO_1 = new BookDto();
    static {
        BOOK_DTO_1.setTitle("Title1");
        BOOK_DTO_1.setInventoryNumber(11111L);
        BOOK_DTO_1.setAuthorIds(List.of(1L, 3L));
        BOOK_DTO_1.setReaderId(1L);
    }
    public static final BookDto BOOK_DTO_2 = new BookDto();
    static {
        BOOK_DTO_2.setTitle("Title2");
        BOOK_DTO_2.setInventoryNumber(22222L);
        BOOK_DTO_2.setAuthorIds(List.of(2L));
        BOOK_DTO_2.setReaderId(2L);
    }
    public static final BookDto BOOK_DTO_3 = new BookDto();
    static {
        BOOK_DTO_3.setTitle("Title3");
        BOOK_DTO_3.setInventoryNumber(33333L);
        BOOK_DTO_3.setAuthorIds(List.of(2L, 3L));
        BOOK_DTO_3.setReaderId(3L);
    }



    public static final ReaderDto READER_DTO_1 = new ReaderDto();
    static {
        READER_DTO_1.setName("Ivan");
        READER_DTO_1.setSurname("Ivanov");
        READER_DTO_1.setPhone("71111111111");
        READER_DTO_1.setAddress("Lenina 11");
    }
    public static final ReaderDto READER_DTO_2 = new ReaderDto();
    static {
        READER_DTO_2.setName("Petr");
        READER_DTO_2.setSurname("Petrov");
        READER_DTO_2.setPhone("72222222222");
        READER_DTO_2.setAddress("Lenina 22");
    }
    public static final ReaderDto READER_DTO_3 = new ReaderDto();
    static {
        READER_DTO_3.setName("Sveta");
        READER_DTO_3.setSurname("Svetikova");
        READER_DTO_3.setPhone("73333333333");
        READER_DTO_3.setAddress("Lenina 33");
    }



}
