package ru.aston.lepd.readingclub.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.aston.lepd.readingclub.dao.AuthorBookDao;
import ru.aston.lepd.readingclub.dao.AuthorDao;
import ru.aston.lepd.readingclub.dao.BookDao;
import ru.aston.lepd.readingclub.dao.ReaderDao;
import ru.aston.lepd.readingclub.service.AuthorService;
import ru.aston.lepd.readingclub.service.BookService;
import ru.aston.lepd.readingclub.service.ReaderService;
import ru.aston.lepd.readingclub.servlet.AuthorServlet;
import ru.aston.lepd.readingclub.servlet.BookServlet;
import ru.aston.lepd.readingclub.servlet.ReaderServlet;

public class ObjectContainerStatic {

    static AuthorBookDao authorBookDao = new AuthorBookDao();
    static AuthorDao authorDao = new AuthorDao(authorBookDao);
    static BookDao bookDao = new BookDao(authorBookDao);
    static ReaderDao readerDao = new ReaderDao();

    static {
        authorDao.setBookDao(bookDao);
        bookDao.setAuthorDao(authorDao);
        bookDao.setReaderDao(readerDao);
        readerDao.setBookDao(bookDao);
    }

    static CustomMapper mapper = CustomMapper.INSTANCE;
    static ReaderService readerService = new ReaderService(readerDao, mapper);
    static BookService bookService = new BookService(bookDao, mapper);
    static AuthorService authorService = new AuthorService(authorDao, mapper);

    static ObjectMapper objectMapper = new ObjectMapper();
    static ReaderServlet readerServlet = new ReaderServlet(readerService, objectMapper);
    static BookServlet bookServlet = new BookServlet(bookService, objectMapper);
    static AuthorServlet authorServlet = new AuthorServlet(authorService, objectMapper);


    public static AuthorBookDao getAuthorBookDao() {
        return authorBookDao;
    }

    public static AuthorDao getAuthorDao() {
        return authorDao;
    }

    public static BookDao getBookDao() {
        return bookDao;
    }

    public static ReaderDao getReaderDao() {
        return readerDao;
    }

    public static CustomMapper getCustomMapper() {
        return mapper;
    }

    public static ReaderService getReaderService() {
        return readerService;
    }

    public static BookService getBookService() {
        return bookService;
    }

    public static AuthorService getAuthorService() {
        return authorService;
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static ReaderServlet getReaderServlet() {
        return readerServlet;
    }

    public static BookServlet getBookServlet() {
        return bookServlet;
    }

    public static AuthorServlet getAuthorServlet() {
        return authorServlet;
    }


}
