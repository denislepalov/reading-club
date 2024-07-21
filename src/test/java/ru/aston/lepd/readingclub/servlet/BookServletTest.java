package ru.aston.lepd.readingclub.servlet;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.aston.lepd.readingclub.dto.BookDto;
import ru.aston.lepd.readingclub.exception.NotFoundException;
import ru.aston.lepd.readingclub.service.BookService;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Stream;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static org.mockito.Mockito.*;
import static ru.aston.lepd.readingclub.Constants.*;

@ExtendWith(MockitoExtension.class)
class BookServletTest {


    @Mock
    private BookService bookService;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private PrintWriter printWriter;
    @InjectMocks
    private BookServlet bookServlet;

    private static final String TEXT_PLAIN = "text/plain";
    private static final String APPLICATION_JSON = "application/json";
    private static final String ID_ERROR = "ERROR: book ID is required";
    private static final String URL_ERROR = "ERROR: wrong URL";
    private static final String UPDATING_RESULT = "Result of updating book: ";
    private static final String DELETING_RESULT = "Result of deleting book: ";
    private static final String NOT_FOUNT_STRING = "There is no author with id=666 in database";
    private static final String EXCEPTION_STRING = "Exception occurred";
    private static final String JSON_EXCEPTION_STRING = "Wrong json";




    @Test
    void doGet_whenExistReaderIdParameter_thenReturnList() throws Exception {
        final Long readerId = 1L;
        final String result = "books by readerId";
        final List<BookDto> books = List.of(BOOK_DTO_1, BOOK_DTO_2, BOOK_DTO_3);
        doReturn("1").when(request).getParameter("reader-id");
        doReturn(null).when(request).getParameter("author-id");
        doReturn(null).when(request).getPathInfo();
        doReturn(books).when(bookService).getAllByReaderId(readerId);
        doReturn(result).when(objectMapper).writeValueAsString(books);
        doNothing().when(response).setContentType(APPLICATION_JSON);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(result);

        bookServlet.doGet(request, response);

        verify(request).getParameter("reader-id");
        verify(request).getParameter("author-id");
        verify(request).getPathInfo();
        verify(bookService).getAllByReaderId(readerId);
        verify(objectMapper).writeValueAsString(books);
        verify(response).setContentType(APPLICATION_JSON);
        verify(response).getWriter();
        verify(printWriter).write(result);
    }

    @Test
    void doGet_whenExistAuthorIdParameter_thenReturnList() throws Exception {
        final Long authorId = 1L;
        final String result = "books by authorId";
        final List<BookDto> books = List.of(BOOK_DTO_1, BOOK_DTO_2, BOOK_DTO_3);
        doReturn(null).when(request).getParameter("reader-id");
        doReturn("1").when(request).getParameter("author-id");
        doReturn(null).when(request).getPathInfo();
        doReturn(books).when(bookService).getAllByAuthorId(authorId);
        doReturn(result).when(objectMapper).writeValueAsString(books);
        doNothing().when(response).setContentType(APPLICATION_JSON);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(result);

        bookServlet.doGet(request, response);

        verify(request).getParameter("reader-id");
        verify(request).getParameter("author-id");
        verify(request).getPathInfo();
        verify(bookService).getAllByAuthorId(authorId);
        verify(objectMapper).writeValueAsString(books);
        verify(response).setContentType(APPLICATION_JSON);
        verify(response).getWriter();
        verify(printWriter).write(result);
    }

    @Test
    void doGet_whenExistAndPathInfoIsNull_thenReturnList() throws Exception {
        final String result = "all books";
        final List<BookDto> books = List.of(BOOK_DTO_1, BOOK_DTO_2, BOOK_DTO_3);
        doReturn(null).when(request).getParameter("reader-id");
        doReturn(null).when(request).getParameter("author-id");
        doReturn(null).when(request).getPathInfo();
        doReturn(books).when(bookService).getAll();
        doReturn(result).when(objectMapper).writeValueAsString(books);
        doNothing().when(response).setContentType(APPLICATION_JSON);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(result);

        bookServlet.doGet(request, response);

        verify(request).getParameter("reader-id");
        verify(request).getParameter("author-id");
        verify(request).getPathInfo();
        verify(bookService).getAll();
        verify(objectMapper).writeValueAsString(books);
        verify(response).setContentType(APPLICATION_JSON);
        verify(response).getWriter();
        verify(printWriter).write(result);
    }

    @Test
    void doGet_whenExistAndPathInfoIsSlash_thenReturnList() throws Exception {
        final String result = "all books";
        final List<BookDto> books = List.of(BOOK_DTO_1, BOOK_DTO_2, BOOK_DTO_3);
        doReturn(null).when(request).getParameter("reader-id");
        doReturn(null).when(request).getParameter("author-id");
        doReturn("/").when(request).getPathInfo();
        doReturn(books).when(bookService).getAll();
        doReturn(result).when(objectMapper).writeValueAsString(books);
        doNothing().when(response).setContentType(APPLICATION_JSON);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(result);

        bookServlet.doGet(request, response);

        verify(request).getParameter("reader-id");
        verify(request).getParameter("author-id");
        verify(request).getPathInfo();
        verify(bookService).getAll();
        verify(objectMapper).writeValueAsString(books);
        verify(response).setContentType(APPLICATION_JSON);
        verify(response).getWriter();
        verify(printWriter).write(result);
    }

    @Test
    void doGet_whenValidId_thenBookDto() throws Exception {
        final Long bookId = 1L;
        final String result = "bookDto 1";
        doReturn(null).when(request).getParameter("reader-id");
        doReturn(null).when(request).getParameter("author-id");
        doReturn("/1").when(request).getPathInfo();
        doReturn(BOOK_DTO_1).when(bookService).getById(bookId);
        doReturn(result).when(objectMapper).writeValueAsString(BOOK_DTO_1);
        doNothing().when(response).setContentType(APPLICATION_JSON);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(result);

        bookServlet.doGet(request, response);

        verify(request).getParameter("reader-id");
        verify(request).getParameter("author-id");
        verify(request).getPathInfo();
        verify(bookService).getById(bookId);
        verify(objectMapper).writeValueAsString(BOOK_DTO_1);
        verify(response).setContentType(APPLICATION_JSON);
        verify(response).getWriter();
        verify(printWriter).write(result);
    }

    @Test
    void doGet_whenInvalidId_thenThrowNotFoundException() throws Exception {
        final Long bookId = 666L;
        final NotFoundException notFoundException = new NotFoundException(NOT_FOUNT_STRING);
        doReturn(null).when(request).getParameter("reader-id");
        doReturn(null).when(request).getParameter("author-id");
        doReturn("/666").when(request).getPathInfo();
        doThrow(notFoundException).when(bookService).getById(bookId);
        doNothing().when(response).setContentType(TEXT_PLAIN);
        doNothing().when(response).setStatus(SC_BAD_REQUEST);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(NOT_FOUNT_STRING);

        bookServlet.doGet(request, response);

        verify(request).getParameter("reader-id");
        verify(request).getParameter("author-id");
        verify(request).getPathInfo();
        verify(bookService).getById(bookId);
        verify(response).setContentType(TEXT_PLAIN);
        verify(response).setStatus(SC_BAD_REQUEST);
        verify(response).getWriter();
        verify(printWriter).write(NOT_FOUNT_STRING);
    }

    @Test
    void doGet_whenInternalError_thenThrowException() throws Exception {
        final Long bookId = 2L;
        final RuntimeException exception = new RuntimeException(EXCEPTION_STRING);
        doReturn(null).when(request).getParameter("reader-id");
        doReturn(null).when(request).getParameter("author-id");
        doReturn("/2").when(request).getPathInfo();
        doThrow(exception).when(bookService).getById(bookId);
        doNothing().when(response).setContentType(TEXT_PLAIN);
        doNothing().when(response).setStatus(SC_INTERNAL_SERVER_ERROR);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(EXCEPTION_STRING);

        bookServlet.doGet(request, response);

        verify(request).getParameter("reader-id");
        verify(request).getParameter("author-id");
        verify(request).getPathInfo();
        verify(bookService).getById(bookId);
        verify(response).setContentType(TEXT_PLAIN);
        verify(response).setStatus(SC_INTERNAL_SERVER_ERROR);
        verify(response).getWriter();
        verify(printWriter).write(EXCEPTION_STRING);
    }



    @Test
    void doPost_whenValidJsonAndPathInfoIsNull_thenBookDto() throws Exception {
        BufferedReader bufferedReader = mock(BufferedReader.class);
        final Stream<String> stream = Stream.of("{", "valid", "json", "of", "bookDto", "}");
        final String result = "new bookDto";
        doReturn(bufferedReader).when(request).getReader();
        doReturn(stream).when(bufferedReader).lines();
        doReturn(BOOK_DTO_1).when(objectMapper).readValue(anyString(), eq(BookDto.class));
        doReturn(null).when(request).getPathInfo();
        doReturn(BOOK_DTO_1).when(bookService).save(BOOK_DTO_1);
        doReturn(result).when(objectMapper).writeValueAsString(BOOK_DTO_1);
        doNothing().when(response).setContentType(APPLICATION_JSON);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(result);

        bookServlet.doPost(request, response);

        verify(objectMapper).readValue(anyString(), eq(BookDto.class));
        verify(request).getPathInfo();
        verify(bookService).save(BOOK_DTO_1);
        verify(objectMapper).writeValueAsString(BOOK_DTO_1);
        verify(response).setContentType(APPLICATION_JSON);
        verify(response).getWriter();
        verify(printWriter).write(result);
    }

    @Test
    void doPost_whenValidJsonAndPathInfoIsSlash_thenBookDto() throws Exception {
        BufferedReader bufferedReader = mock(BufferedReader.class);
        final Stream<String> stream = Stream.of("{", "valid", "json", "of", "bookDto", "}");
        final String result = "new bookDto";
        doReturn(bufferedReader).when(request).getReader();
        doReturn(stream).when(bufferedReader).lines();
        doReturn(BOOK_DTO_1).when(objectMapper).readValue(anyString(), eq(BookDto.class));
        doReturn("/").when(request).getPathInfo();
        doReturn(BOOK_DTO_1).when(bookService).save(BOOK_DTO_1);
        doReturn(result).when(objectMapper).writeValueAsString(BOOK_DTO_1);
        doNothing().when(response).setContentType(APPLICATION_JSON);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(result);

        bookServlet.doPost(request, response);

        verify(objectMapper).readValue(anyString(), eq(BookDto.class));
        verify(request).getPathInfo();
        verify(bookService).save(BOOK_DTO_1);
        verify(objectMapper).writeValueAsString(BOOK_DTO_1);
        verify(response).setContentType(APPLICATION_JSON);
        verify(response).getWriter();
        verify(printWriter).write(result);
    }

    @Test
    void doPost_whenWrongUrl_thenUrlErrorString() throws Exception {
        BufferedReader bufferedReader = mock(BufferedReader.class);
        final Stream<String> stream = Stream.of("{", "valid", "json", "of", "bookDto", "}");
        doReturn(bufferedReader).when(request).getReader();
        doReturn(stream).when(bufferedReader).lines();
        doReturn(BOOK_DTO_1).when(objectMapper).readValue(anyString(), eq(BookDto.class));
        doReturn("/wrong").when(request).getPathInfo();
        doNothing().when(response).setContentType(TEXT_PLAIN);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(URL_ERROR);

        bookServlet.doPost(request, response);

        verify(objectMapper).readValue(anyString(), eq(BookDto.class));
        verify(request).getPathInfo();
        verify(response).setContentType(TEXT_PLAIN);
        verify(response).getWriter();
        verify(printWriter).write(URL_ERROR);
    }

    @Test
    void doPost_whenInvalidJson_thenThrowJsonMappingException() throws Exception {
        BufferedReader bufferedReader = mock(BufferedReader.class);
        final JsonMappingException jsonMappingException = new JsonMappingException(JSON_EXCEPTION_STRING);
        final Stream<String> stream = Stream.of("{", "invalid", "json", "of", "bookDto", "}");
        doReturn(bufferedReader).when(request).getReader();
        doReturn(stream).when(bufferedReader).lines();
        doThrow(jsonMappingException).when(objectMapper).readValue(anyString(), eq(BookDto.class));
        doNothing().when(response).setContentType(TEXT_PLAIN);
        doNothing().when(response).setStatus(SC_BAD_REQUEST);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(JSON_EXCEPTION_STRING);

        bookServlet.doPost(request, response);

        verify(request).getReader();
        verify(bufferedReader).lines();
        verify(objectMapper).readValue(anyString(), eq(BookDto.class));
        verify(response).setContentType(TEXT_PLAIN);
        verify(response).setStatus(SC_BAD_REQUEST);
        verify(response).getWriter();
        verify(printWriter).write(JSON_EXCEPTION_STRING);

    }

    @Test
    void doPost_whenInternalError_thenThrowException() throws Exception {
        BufferedReader bufferedReader = mock(BufferedReader.class);
        final RuntimeException exception = new RuntimeException(EXCEPTION_STRING);
        final Stream<String> stream = Stream.of("{", "valid", "json", "of", "bookDto", "}");
        doReturn(bufferedReader).when(request).getReader();
        doReturn(stream).when(bufferedReader).lines();
        doReturn(BOOK_DTO_1).when(objectMapper).readValue(anyString(), eq(BookDto.class));
        doReturn(null).when(request).getPathInfo();
        doThrow(exception).when(bookService).save(BOOK_DTO_1);
        doNothing().when(response).setContentType(TEXT_PLAIN);
        doNothing().when(response).setStatus(SC_INTERNAL_SERVER_ERROR);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(EXCEPTION_STRING);

        bookServlet.doPost(request, response);

        verify(objectMapper).readValue(anyString(), eq(BookDto.class));
        verify(request).getPathInfo();
        verify(bookService).save(BOOK_DTO_1);
        verify(response).setContentType(TEXT_PLAIN);
        verify(response).setStatus(SC_INTERNAL_SERVER_ERROR);
        verify(response).getWriter();
        verify(printWriter).write(EXCEPTION_STRING);
    }



    @Test
    void doPut_whenValidJsonAndId_thenReturnTrue() throws Exception {
        BufferedReader bufferedReader = mock(BufferedReader.class);
        final Stream<String> stream = Stream.of("{", "valid", "json", "of", "bookDto", "}");
        final Long bookId = 1L;
        doReturn(bufferedReader).when(request).getReader();
        doReturn(stream).when(bufferedReader).lines();
        doReturn(BOOK_DTO_1).when(objectMapper).readValue(anyString(), eq(BookDto.class));
        doReturn("/1").when(request).getPathInfo();
        doReturn(true).when(bookService).update(BOOK_DTO_1, bookId);
        doNothing().when(response).setContentType(TEXT_PLAIN);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(UPDATING_RESULT + true);

        bookServlet.doPut(request, response);

        verify(request).getReader();
        verify(bufferedReader).lines();
        verify(objectMapper).readValue(anyString(), eq(BookDto.class));
        verify(request).getPathInfo();
        verify(bookService).update(BOOK_DTO_1, bookId);
        verify(response).setContentType(TEXT_PLAIN);
        verify(response).getWriter();
        verify(printWriter).write(UPDATING_RESULT + true);
    }

    @Test
    void doPut_whenValidJsonButPathInfoIsNull_thenIdErrorString() throws Exception {
        BufferedReader bufferedReader = mock(BufferedReader.class);
        final Stream<String> stream = Stream.of("{", "valid", "json", "of", "bookDto", "}");
        doReturn(bufferedReader).when(request).getReader();
        doReturn(stream).when(bufferedReader).lines();
        doReturn(BOOK_DTO_1).when(objectMapper).readValue(anyString(), eq(BookDto.class));
        doReturn(null).when(request).getPathInfo();
        doNothing().when(response).setStatus(SC_BAD_REQUEST);
        doNothing().when(response).setContentType(TEXT_PLAIN);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(ID_ERROR);

        bookServlet.doPut(request, response);

        verify(request).getReader();
        verify(bufferedReader).lines();
        verify(objectMapper).readValue(anyString(), eq(BookDto.class));
        verify(request).getPathInfo();
        verify(response).setStatus(SC_BAD_REQUEST);
        verify(response).setContentType(TEXT_PLAIN);
        verify(response).getWriter();
        verify(printWriter).write(ID_ERROR);
    }

    @Test
    void doPut_whenValidJsonButPathInfoIsSlash_thenIdErrorString() throws Exception {
        BufferedReader bufferedReader = mock(BufferedReader.class);
        final Stream<String> stream = Stream.of("{", "valid", "json", "of", "bookDto", "}");
        doReturn(bufferedReader).when(request).getReader();
        doReturn(stream).when(bufferedReader).lines();
        doReturn(BOOK_DTO_1).when(objectMapper).readValue(anyString(), eq(BookDto.class));
        doReturn("/").when(request).getPathInfo();
        doNothing().when(response).setStatus(SC_BAD_REQUEST);
        doNothing().when(response).setContentType(TEXT_PLAIN);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(ID_ERROR);

        bookServlet.doPut(request, response);

        verify(request).getReader();
        verify(bufferedReader).lines();
        verify(objectMapper).readValue(anyString(), eq(BookDto.class));
        verify(request).getPathInfo();
        verify(response).setStatus(SC_BAD_REQUEST);
        verify(response).setContentType(TEXT_PLAIN);
        verify(response).getWriter();
        verify(printWriter).write(ID_ERROR);
    }

    @Test
    void doPut_whenInvalidJson_thenThrowJsonMappingException() throws Exception {
        BufferedReader bufferedReader = mock(BufferedReader.class);
        final Stream<String> stream = Stream.of("{", "invalid", "json", "of", "bookDto", "}");
        final JsonMappingException jsonMappingException = new JsonMappingException(JSON_EXCEPTION_STRING);
        doReturn(bufferedReader).when(request).getReader();
        doReturn(stream).when(bufferedReader).lines();
        doThrow(jsonMappingException).when(objectMapper).readValue(anyString(), eq(BookDto.class));
        doNothing().when(response).setStatus(SC_BAD_REQUEST);
        doNothing().when(response).setContentType(TEXT_PLAIN);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(JSON_EXCEPTION_STRING);

        bookServlet.doPut(request, response);

        verify(request).getReader();
        verify(bufferedReader).lines();
        verify(objectMapper).readValue(anyString(), eq(BookDto.class));
        verify(response).setStatus(SC_BAD_REQUEST);
        verify(response).setContentType(TEXT_PLAIN);
        verify(response).getWriter();
        verify(printWriter).write(JSON_EXCEPTION_STRING);
    }

    @Test
    void doPut_whenInvalidId_thenThrowNotFoundException() throws Exception {
        BufferedReader bufferedReader = mock(BufferedReader.class);
        final Long bookId = 666L;
        final Stream<String> stream = Stream.of("{", "valid", "json", "of", "bookDto", "}");
        final NotFoundException notFoundException = new NotFoundException(NOT_FOUNT_STRING);
        doReturn(bufferedReader).when(request).getReader();
        doReturn(stream).when(bufferedReader).lines();
        doReturn(BOOK_DTO_1).when(objectMapper).readValue(anyString(), eq(BookDto.class));
        doReturn("/666").when(request).getPathInfo();
        doThrow(notFoundException).when(bookService).update(BOOK_DTO_1, bookId);
        doNothing().when(response).setStatus(SC_BAD_REQUEST);
        doNothing().when(response).setContentType(TEXT_PLAIN);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(NOT_FOUNT_STRING);

        bookServlet.doPut(request, response);

        verify(request).getReader();
        verify(bufferedReader).lines();
        verify(objectMapper).readValue(anyString(), eq(BookDto.class));
        verify(request).getPathInfo();
        verify(bookService).update(BOOK_DTO_1, bookId);
        verify(response).setStatus(SC_BAD_REQUEST);
        verify(response).setContentType(TEXT_PLAIN);
        verify(response).getWriter();
        verify(printWriter).write(NOT_FOUNT_STRING);
    }

    @Test
    void doPut_whenInternalError_thenThrowException() throws Exception {
        BufferedReader bufferedReader = mock(BufferedReader.class);
        final Long bookId = 2L;
        final Stream<String> stream = Stream.of("{", "valid", "json", "of", "bookDto", "}");
        final RuntimeException exception = new RuntimeException(EXCEPTION_STRING);
        doReturn(bufferedReader).when(request).getReader();
        doReturn(stream).when(bufferedReader).lines();
        doReturn(BOOK_DTO_1).when(objectMapper).readValue(anyString(), eq(BookDto.class));
        doReturn("/2").when(request).getPathInfo();
        doThrow(exception).when(bookService).update(BOOK_DTO_1, bookId);
        doNothing().when(response).setStatus(SC_INTERNAL_SERVER_ERROR);
        doNothing().when(response).setContentType(TEXT_PLAIN);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(EXCEPTION_STRING);

        bookServlet.doPut(request, response);

        verify(request).getReader();
        verify(bufferedReader).lines();
        verify(objectMapper).readValue(anyString(), eq(BookDto.class));
        verify(request).getPathInfo();
        verify(bookService).update(BOOK_DTO_1, bookId);
        verify(response).setStatus(SC_INTERNAL_SERVER_ERROR);
        verify(response).setContentType(TEXT_PLAIN);
        verify(response).getWriter();
        verify(printWriter).write(EXCEPTION_STRING);
    }



    @Test
    void doDelete_whenValidId_thenSuccessString() throws Exception {
        final Long bookId = 1L;
        doReturn("/1").when(request).getPathInfo();
        doReturn(true).when(bookService).delete(bookId);
        doNothing().when(response).setContentType(TEXT_PLAIN);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(DELETING_RESULT + true);

        bookServlet.doDelete(request, response);

        verify(request).getPathInfo();
        verify(bookService).delete(bookId);
        verify(response).setContentType(TEXT_PLAIN);
        verify(response).getWriter();
        verify(printWriter).write(DELETING_RESULT + true);
    }

    @Test
    void doDelete_whenValidIdButPathInfoIsNull_thenIdErrorString() throws Exception {
        doReturn(null).when(request).getPathInfo();
        doNothing().when(response).setStatus(SC_BAD_REQUEST);
        doNothing().when(response).setContentType(TEXT_PLAIN);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(ID_ERROR);

        bookServlet.doDelete(request, response);

        verify(request).getPathInfo();
        verify(response).setStatus(SC_BAD_REQUEST);
        verify(response).setContentType(TEXT_PLAIN);
        verify(response).getWriter();
        verify(printWriter).write(ID_ERROR);
    }

    @Test
    void doDelete_whenValidIdButPathInfoIsSlash_thenIdErrorString() throws Exception {
        doReturn("/").when(request).getPathInfo();
        doNothing().when(response).setStatus(SC_BAD_REQUEST);
        doNothing().when(response).setContentType(TEXT_PLAIN);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(ID_ERROR);

        bookServlet.doDelete(request, response);

        verify(request).getPathInfo();
        verify(response).setStatus(SC_BAD_REQUEST);
        verify(response).setContentType(TEXT_PLAIN);
        verify(response).getWriter();
        verify(printWriter).write(ID_ERROR);
    }

    @Test
    void doDelete_whenInvalidId_thenThrowNotFoundException() throws Exception {
        final Long bookId = 666L;
        final NotFoundException notFoundException = new NotFoundException(NOT_FOUNT_STRING);
        doReturn("/666").when(request).getPathInfo();
        doThrow(notFoundException).when(bookService).delete(bookId);
        doNothing().when(response).setStatus(SC_BAD_REQUEST);
        doNothing().when(response).setContentType(TEXT_PLAIN);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(NOT_FOUNT_STRING);

        bookServlet.doDelete(request, response);

        verify(request).getPathInfo();
        verify(bookService).delete(bookId);
        verify(response).setStatus(SC_BAD_REQUEST);
        verify(response).setContentType(TEXT_PLAIN);
        verify(response).getWriter();
        verify(printWriter).write(NOT_FOUNT_STRING);
    }

    @Test
    void doDelete_whenInternalError_thenThrowException() throws Exception {
        final Long bookId = 2L;
        final RuntimeException exception = new RuntimeException(EXCEPTION_STRING);
        doReturn("/2").when(request).getPathInfo();
        doThrow(exception).when(bookService).delete(bookId);
        doNothing().when(response).setStatus(SC_INTERNAL_SERVER_ERROR);
        doNothing().when(response).setContentType(TEXT_PLAIN);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(EXCEPTION_STRING);

        bookServlet.doDelete(request, response);

        verify(request).getPathInfo();
        verify(bookService).delete(bookId);
        verify(response).setStatus(SC_INTERNAL_SERVER_ERROR);
        verify(response).setContentType(TEXT_PLAIN);
        verify(response).getWriter();
        verify(printWriter).write(EXCEPTION_STRING);
    }




}