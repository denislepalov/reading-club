package ru.aston.lepd.readingclub.servlet;


import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.aston.lepd.readingclub.dto.AuthorDto;
import ru.aston.lepd.readingclub.exception.DaoException;
import ru.aston.lepd.readingclub.exception.NotFoundException;
import ru.aston.lepd.readingclub.service.AuthorService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Stream;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

@WebServlet(name = "authorServlet")
public class AuthorServlet extends HttpServlet {

    private final AuthorService authorService;
    private final ObjectMapper objectMapper;

    public AuthorServlet(AuthorService authorService, ObjectMapper objectMapper) {
        this.authorService = authorService;
        this.objectMapper = objectMapper;
    }


    private static final String TEXT_PLAIN = "text/plain";
    private static final String APPLICATION_JSON = "application/json";
    private static final String ID_ERROR = "ERROR: author ID is required";
    private static final String URL_ERROR = "ERROR: wrong URL";
    private static final String UPDATING_RESULT = "Result of updating author: ";
    private static final String DELETING_RESULT = "Result of deleting author: ";


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String result;
        String pathInfo = request.getPathInfo();
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                List<AuthorDto> allAuthors = authorService.getAll();
                result = objectMapper.writeValueAsString(allAuthors);
            } else {
                Long authorId = Long.parseLong(pathInfo.substring(1));
                AuthorDto authorDto = authorService.getById(authorId);
                result = objectMapper.writeValueAsString(authorDto);
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

        String result;
        try (BufferedReader bufferedReader = request.getReader();
             Stream<String> lines = bufferedReader.lines()) {

            StringBuilder body = new StringBuilder();
            lines.forEach(body::append);
            AuthorDto authorDto = objectMapper.readValue(body.toString(), AuthorDto.class);
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                AuthorDto savedAuthor = authorService.save(authorDto);
                result = objectMapper.writeValueAsString(savedAuthor);
                response.setContentType(APPLICATION_JSON);
            } else {
                result = URL_ERROR;
                response.setContentType(TEXT_PLAIN);
                response.setStatus(SC_BAD_REQUEST);
            }
        } catch (JsonMappingException | DaoException e) {
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
            AuthorDto authorDto = objectMapper.readValue(body.toString(), AuthorDto.class);
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                result = ID_ERROR;
                response.setStatus(SC_BAD_REQUEST);
            } else {
                Long authorId = Long.parseLong(pathInfo.substring(1));
                boolean updateResult = authorService.update(authorDto, authorId);
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
                Long authorId = Long.parseLong(pathInfo.substring(1));
                boolean deletedResult = authorService.delete(authorId);
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
