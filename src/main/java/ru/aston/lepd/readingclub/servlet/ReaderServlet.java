package ru.aston.lepd.readingclub.servlet;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.aston.lepd.readingclub.dto.ReaderDto;
import ru.aston.lepd.readingclub.exception.DaoException;
import ru.aston.lepd.readingclub.exception.NotFoundException;
import ru.aston.lepd.readingclub.service.ReaderService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Stream;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static java.nio.charset.StandardCharsets.UTF_8;


@WebServlet(name = "readerServlet")
public class ReaderServlet extends HttpServlet {


    private final ReaderService readerService;
    private final ObjectMapper objectMapper;

    public ReaderServlet(ReaderService readerService, ObjectMapper objectMapper) {
        this.readerService = readerService;
        this.objectMapper = objectMapper;
    }


    private static final String TEXT_PLAIN = "text/plain";
    private static final String APPLICATION_JSON = "application/json";
    private static final String ID_ERROR = "ERROR: reader ID is required";
    private static final String URL_ERROR = "ERROR: wrong URL";
    private static final String UPDATING_RESULT = "Result of updating reader: ";
    private static final String DELETING_RESULT = "Result of deleting reader: ";


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String result;
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                List<ReaderDto> allReaders = readerService.getAll();
                result = objectMapper.writeValueAsString(allReaders);
            } else {
                Long readerId = Long.parseLong(pathInfo.substring(1));
                ReaderDto readerDto = readerService.getById(readerId);
                result = objectMapper.writeValueAsString(readerDto);
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

        String result = "";
        try (BufferedReader bufferedReader = request.getReader();
             Stream<String> lines = bufferedReader.lines()) {

            StringBuilder body = new StringBuilder();
            lines.forEach(body::append);
            ReaderDto readerDto = objectMapper.readValue(body.toString(), ReaderDto.class);
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                ReaderDto savedReader = readerService.save(readerDto);
                result = objectMapper.writeValueAsString(savedReader);
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
            ReaderDto readerDto = objectMapper.readValue(body.toString(), ReaderDto.class);
            String pathInfo = request.getPathInfo();

            if (pathInfo == null || pathInfo.equals("/")) {
                result = ID_ERROR;
                response.setStatus(SC_BAD_REQUEST);
            } else {
                Long readerId = Long.parseLong(pathInfo.substring(1));
                boolean updateResult = readerService.update(readerDto, readerId);
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
                Long readerId = Long.parseLong(pathInfo.substring(1));
                boolean deletedResult = readerService.delete(readerId);
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
