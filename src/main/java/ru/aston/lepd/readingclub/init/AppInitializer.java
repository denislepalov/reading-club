package ru.aston.lepd.readingclub.init;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
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
import ru.aston.lepd.readingclub.util.DataSource;
import ru.aston.lepd.readingclub.util.PropertiesUtil;

@WebListener
public class AppInitializer implements ServletContextListener {

    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";
    private static final String DRIVER_KEY = "db.driver";

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        DataSource.initialize(PropertiesUtil.getProperty(URL_KEY),
                PropertiesUtil.getProperty(USERNAME_KEY),
                PropertiesUtil.getProperty(PASSWORD_KEY),
                PropertiesUtil.getProperty(DRIVER_KEY));

        AuthorBookDao authorBookDao = new AuthorBookDao();
        AuthorDao authorDao = new AuthorDao(authorBookDao);
        BookDao bookDao = new BookDao(authorBookDao);
        ReaderDao readerDao = new ReaderDao();

        authorDao.setBookDao(bookDao);
        bookDao.setAuthorDao(authorDao);
        bookDao.setReaderDao(readerDao);
        readerDao.setBookDao(bookDao);

        CustomMapper mapper = CustomMapper.INSTANCE;
        ReaderService readerService = new ReaderService(readerDao, mapper);
        BookService bookService = new BookService(bookDao, mapper);
        AuthorService authorService = new AuthorService(authorDao, mapper);

        bookService.setAuthorService(authorService);
        bookService.setReaderService(readerService);

        ObjectMapper objectMapper = new ObjectMapper();
        ReaderServlet readerServlet = new ReaderServlet(readerService, objectMapper);
        BookServlet bookServlet = new BookServlet(bookService, objectMapper);
        AuthorServlet authorServlet = new AuthorServlet(authorService, objectMapper);

        sce.getServletContext().addServlet("readerServlet", readerServlet).addMapping("/readers/*");
        sce.getServletContext().addServlet("bookServlet", bookServlet).addMapping("/books/*");
        sce.getServletContext().addServlet("authorServlet", authorServlet).addMapping("/authors/*");

    }


}

