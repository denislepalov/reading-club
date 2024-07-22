package ru.aston.lepd.readingclub;

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
import ru.aston.lepd.readingclub.util.CustomMapper;

public class ObjectContainer {

    AuthorBookDao authorBookDao = new AuthorBookDao();
    AuthorDao authorDao = new AuthorDao(authorBookDao);
    BookDao bookDao = new BookDao(authorBookDao);
    ReaderDao readerDao = new ReaderDao();

    {
        authorDao.setBookDao(bookDao);
        bookDao.setAuthorDao(authorDao);
        bookDao.setReaderDao(readerDao);
        readerDao.setBookDao(bookDao);
    }

    CustomMapper mapper = CustomMapper.INSTANCE;
    ReaderService readerService = new ReaderService(readerDao, mapper);
    BookService bookService = new BookService(bookDao, mapper);
    AuthorService authorService = new AuthorService(authorDao, mapper);

    {
        bookService.setAuthorService(authorService);
        bookService.setReaderService(readerService);
    }

    ObjectMapper objectMapper = new ObjectMapper();
    ReaderServlet readerServlet = new ReaderServlet(readerService, objectMapper);
    BookServlet bookServlet = new BookServlet(bookService, objectMapper);
    AuthorServlet authorServlet = new AuthorServlet(authorService, objectMapper);


    public AuthorBookDao getAuthorBookDao() {
        return authorBookDao;
    }

    public AuthorDao getAuthorDao() {
        return authorDao;
    }

    public BookDao getBookDao() {
        return bookDao;
    }

    public ReaderDao getReaderDao() {
        return readerDao;
    }

    public CustomMapper getCustomMapper() {
        return mapper;
    }

    public ReaderService getReaderService() {
        return readerService;
    }

    public BookService getBookService() {
        return bookService;
    }

    public AuthorService getAuthorService() {
        return authorService;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public ReaderServlet getReaderServlet() {
        return readerServlet;
    }

    public BookServlet getBookServlet() {
        return bookServlet;
    }

    public AuthorServlet getAuthorServlet() {
        return authorServlet;
    }


}
