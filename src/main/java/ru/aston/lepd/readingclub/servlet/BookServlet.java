package ru.aston.lepd.readingclub.servlet;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.aston.lepd.readingclub.dto.BookDto;
import ru.aston.lepd.readingclub.exception.DaoException;
import ru.aston.lepd.readingclub.exception.NotFoundException;
import ru.aston.lepd.readingclub.service.BookService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Stream;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static java.nio.charset.StandardCharsets.UTF_8;


@WebServlet(name = "bookServlet")
public class BookServlet extends HttpServlet {


    private final BookService bookService;
    private final ObjectMapper objectMapper;

    public BookServlet(BookService bookService, ObjectMapper objectMapper) {
        this.bookService = bookService;
        this.objectMapper = objectMapper;
    }

    private static final String TEXT_PLAIN = "text/plain";
    private static final String APPLICATION_JSON = "application/json";
    private static final String ID_ERROR = "ERROR: book ID is required";
    private static final String URL_ERROR = "ERROR: wrong URL";
    private static final String UPDATING_RESULT = "Result of updating book: ";
    private static final String DELETING_RESULT = "Result of deleting book: ";


    /**
     * To get the list of books filtered by readerId or by authorId you need to pass the parameters in url.
     * Example: localhost:8080/books?reader-id=1 or localhost:8080/books?author-id=1
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String result;
        try {
            String readerId = request.getParameter("reader-id");
            String authorId = request.getParameter("author-id");
            String pathInfo = request.getPathInfo();
            if (readerId != null) {
                List<BookDto> allByReaderId = bookService.getAllByReaderId(Long.parseLong(readerId));
                result = objectMapper.writeValueAsString(allByReaderId);
            }
            else if (authorId != null) {
                List<BookDto> allByAuthorId = bookService.getAllByAuthorId(Long.parseLong(authorId));
                result = objectMapper.writeValueAsString(allByAuthorId);
            }
            else if (pathInfo == null || pathInfo.equals("/")) {
                List<BookDto> allBooks = bookService.getAll();
                result = objectMapper.writeValueAsString(allBooks);
            }
            else {
                Long bookId = Long.parseLong(pathInfo.substring(1));
                BookDto bookDto = bookService.getById(bookId);
                result = objectMapper.writeValueAsString(bookDto);
            }
            response.setContentType(APPLICATION_JSON);
        }
          catch (NotFoundException | DaoException e) {
            result = e.getMessage();
            response.setContentType(TEXT_PLAIN);
            response.setStatus(SC_BAD_REQUEST);
        } catch (Exception e) {
            result = e.getMessage();
            response.setContentType(TEXT_PLAIN);
            response.setStatus(SC_INTERNAL_SERVER_ERROR);
        }
        try (PrintWriter writer = response.getWriter()) {
            writer.write(result);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String result ;
        try (BufferedReader bufferedReader = request.getReader();
             Stream<String> lines = bufferedReader.lines()) {

            StringBuilder body = new StringBuilder();
            lines.forEach(body::append);
            BookDto bookDto = objectMapper.readValue(body.toString(), BookDto.class);
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                BookDto savedBook = bookService.save(bookDto);
                result = objectMapper.writeValueAsString(savedBook);
                response.setContentType(APPLICATION_JSON);
            } else {
                result = URL_ERROR;
                response.setContentType(TEXT_PLAIN);
                response.setStatus(SC_BAD_REQUEST);
            }
        }
          catch (JsonMappingException | DaoException e) {
            result = e.getMessage();
            response.setContentType(TEXT_PLAIN);
            response.setStatus(SC_BAD_REQUEST);
        } catch (Exception e) {
            result = e.getMessage();
            response.setContentType(TEXT_PLAIN);
            response.setStatus(SC_INTERNAL_SERVER_ERROR);
        }
        try (PrintWriter writer = response.getWriter()) {
            writer.write(result);
        }
    }


    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String result;
        try (BufferedReader bufferedReader = request.getReader();
             Stream<String> lines = bufferedReader.lines()) {

            StringBuilder body = new StringBuilder();
            lines.forEach(body::append);
            BookDto bookDto = objectMapper.readValue(body.toString(), BookDto.class);
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                result = ID_ERROR;
                response.setStatus(SC_BAD_REQUEST);
            } else {
                Long bookId = Long.parseLong(pathInfo.substring(1));
                boolean updateResult = bookService.update(bookDto, bookId);
                result = UPDATING_RESULT + updateResult;
            }
        }
          catch (JsonMappingException | NotFoundException | DaoException e) {
            result = e.getMessage();
            response.setStatus(SC_BAD_REQUEST);
        } catch (Exception e) {
            result = e.getMessage();
            response.setStatus(SC_INTERNAL_SERVER_ERROR);
        }
        response.setContentType(TEXT_PLAIN);
        try (PrintWriter writer = response.getWriter()) {
            writer.write(result);
        }
    }


    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String result;
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                result = ID_ERROR;
                response.setStatus(SC_BAD_REQUEST);
            } else {
                Long bookId = Long.parseLong(pathInfo.substring(1));
                boolean deletedResult = bookService.delete(bookId);
                result = DELETING_RESULT + deletedResult;
            }
        } catch (NotFoundException | DaoException e) {
            result = e.getMessage();
            response.setStatus(SC_BAD_REQUEST);
        } catch (Exception e) {
            result = e.getMessage();
            response.setStatus(SC_INTERNAL_SERVER_ERROR);
        }
        response.setContentType(TEXT_PLAIN);
        try (PrintWriter writer = response.getWriter()) {
            writer.write(result);
        }
    }


}
