package ru.aston.lepd.readingclub;

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




    public static final Reader READER_1 = new Reader();
    static {
        READER_1.setId(1L);
        READER_1.setName("Ivan");
        READER_1.setSurname("Ivanov");
        READER_1.setPhone("71111111111");
        READER_1.setAddress("Lenina 11");
        READER_1.setBooks(List.of(SHORT_BOOK_1));
    }
    public static final Reader READER_2 = new Reader();
    static {
        READER_2.setId(2L);
        READER_2.setName("Petr");
        READER_2.setSurname("Petrov");
        READER_2.setPhone("72222222222");
        READER_2.setAddress("Lenina 22");
        READER_2.setBooks(List.of(SHORT_BOOK_2));
    }
    public static final Reader READER_3 = new Reader();
    static {
        READER_3.setId(3L);
        READER_3.setName("Sveta");
        READER_3.setSurname("Svetikova");
        READER_3.setPhone("73333333333");
        READER_3.setAddress("Lenina 33");
        READER_3.setBooks(List.of(SHORT_BOOK_3));
    }



    public static final Book BOOK_1 = new Book();
    static {
        BOOK_1.setId(1L);
        BOOK_1.setTitle("Title1");
        BOOK_1.setInventoryNumber(11111L);
        BOOK_1.setAuthors(new ArrayList<>(List.of(SHORT_AUTHOR_1, SHORT_AUTHOR_3)));
        BOOK_1.setReader(SHORT_READER_1);
    }
    public static final Book BOOK_2 = new Book();
    static {
        BOOK_2.setId(2L);
        BOOK_2.setTitle("Title2");
        BOOK_2.setInventoryNumber(22222L);
        BOOK_2.setAuthors(new ArrayList<>(List.of(SHORT_AUTHOR_2)));
        BOOK_2.setReader(SHORT_READER_2);
    }
    public static final Book BOOK_3 = new Book();
    static {
        BOOK_3.setId(3L);
        BOOK_3.setTitle("Title3");
        BOOK_3.setInventoryNumber(33333L);
        BOOK_3.setAuthors(new ArrayList<>(List.of(SHORT_AUTHOR_2, SHORT_AUTHOR_3)));
        BOOK_3.setReader(SHORT_READER_3);
    }



    public static final Author AUTHOR_1 = new Author();
    static {
        AUTHOR_1.setId(1L);
        AUTHOR_1.setFullName("Author1");
        AUTHOR_1.setPersonalInfo("likes dogs");
        AUTHOR_1.setBooks(List.of(SHORT_BOOK_1));
    }
    public static final Author AUTHOR_2 = new Author();
    static {
        AUTHOR_2.setId(2L);
        AUTHOR_2.setFullName("Author2");
        AUTHOR_2.setPersonalInfo("likes cats");
        AUTHOR_2.setBooks(List.of(SHORT_BOOK_2, SHORT_BOOK_3));
    }
    public static final Author AUTHOR_3 = new Author();
    static {
        AUTHOR_3.setId(3L);
        AUTHOR_3.setFullName("Author3");
        AUTHOR_3.setPersonalInfo("likes wolfs");
        AUTHOR_3.setBooks(List.of(SHORT_BOOK_1, SHORT_BOOK_3));
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







    public static final String CLEAN_AUTHOR_BOOK_SQL = "DELETE FROM author_book";
    public static final String CLEAN_AUTHORS_SQL = "DELETE FROM authors";
    public static final String CLEAN_BOOKS_SQL = "DELETE FROM books";
    public static final String CLEAN_READERS_SQL = "DELETE FROM readers";

    public static final String UPDATE_AUTHOR_ID_SQL = "ALTER TABLE authors ALTER COLUMN id RESTART WITH 1";
    public static final String UPDATE_BOOKS_ID_SQL = "ALTER TABLE books ALTER COLUMN id RESTART WITH 1";
    public static final String UPDATE_READERS_ID_SQL = "ALTER TABLE readers ALTER COLUMN id RESTART WITH 1";


    public static final String CREATE_READERS_SQL = """
            CREATE TABLE IF NOT EXISTS readers (
                id      bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                name    varchar(128) NOT NULL,
                surname varchar(128) NOT NULL,
                phone   varchar(12)  NOT NULL UNIQUE,
                address varchar(256)  NOT NULL
            );
            """;

    public static final String INSERT_READERS_SQL = """
            INSERT INTO readers (name, surname, phone, address)
            VALUES ('Ivan', 'Ivanov', '71111111111', 'Lenina 11'),
                   ('Petr', 'Petrov', '72222222222', 'Lenina 22'),
                   ('Sveta', 'Svetikova', '73333333333', 'Lenina 33');
            """;

    public static final String CREATE_BOOKS_SQL = """
            CREATE TABLE IF NOT EXISTS books (
                id               bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                title            varchar(128) NOT NULL,
                inventory_number bigint NOT NULL UNIQUE,
                reader_id        bigint REFERENCES readers(id) NOT NULL
            );
            """;

    public static final String INSERT_BOOKS_SQL = """
            INSERT INTO books (title, inventory_number, reader_id)
            VALUES ('Title1', 11111, 1),
                   ('Title2', 22222, 2),
                   ('Title3', 33333, 3);
            """;

    public static final String CREATE_AUTHORS_SQL = """
            CREATE TABLE IF NOT EXISTS authors (
                id        bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                full_name varchar(128) NOT NULL,
                personal_info varchar(256) NOT NULL
            );
            """;

    public static final String INSERT_AUTHORS_SQL = """
            INSERT INTO authors (full_name, personal_info)
            VALUES ('Author1', 'likes dogs'),
                   ('Author2', 'likes cats'),
                   ('Author3', 'likes wolfs');
            """;

    public static final String CREATE_AUTHOR_BOOK_SQL = """
            CREATE TABLE IF NOT EXISTS author_book (
                author_id bigint REFERENCES authors(id),
                book_id   bigint REFERENCES books(id),
                PRIMARY KEY (author_id, book_id)
            );
            """;

    public static final String INSERT_AUTHOR_BOOK_SQL = """
            INSERT INTO author_book (author_id, book_id)
            VALUES (1, 1),
                   (3, 1),
                   (2, 3),
                   (3, 3),
                   (2, 2);
            """;

}
