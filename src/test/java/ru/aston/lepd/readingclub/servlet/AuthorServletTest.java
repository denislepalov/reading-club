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
import ru.aston.lepd.readingclub.dto.AuthorDto;
import ru.aston.lepd.readingclub.exception.NotFoundException;
import ru.aston.lepd.readingclub.service.AuthorService;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Stream;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static ru.aston.lepd.readingclub.Constants.*;


@ExtendWith(MockitoExtension.class)
class AuthorServletTest {

    @Mock
    private AuthorService authorService;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private PrintWriter printWriter;
    @InjectMocks
    private AuthorServlet authorServlet;

    private static final String TEXT_PLAIN = "text/plain";
    private static final String APPLICATION_JSON = "application/json";
    private static final String ID_ERROR = "ERROR: author ID is required";
    private static final String URL_ERROR = "ERROR: wrong URL";
    private static final String UPDATING_RESULT = "Result of updating author: ";
    private static final String DELETING_RESULT = "Result of deleting author: ";
    private static final String NOT_FOUNT_STRING = "There is no author with id=666 in database";
    private static final String EXCEPTION_STRING = "Exception occurred";
    private static final String JSON_EXCEPTION_STRING = "Wrong json";




    @Test
    void doGet_whenExistAndPathInfoIsNull_thenReturnList() throws Exception {
        final String result = "all authors";
        final List<AuthorDto> authors = List.of(AUTHOR_DTO_1, AUTHOR_DTO_2, AUTHOR_DTO_3);
        doReturn(null).when(request).getPathInfo();
        doReturn(authors).when(authorService).getAll();
        doReturn(result).when(objectMapper).writeValueAsString(authors);
        doNothing().when(response).setContentType(APPLICATION_JSON);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(result);

        authorServlet.doGet(request, response);

        verify(request).getPathInfo();
        verify(authorService).getAll();
        verify(objectMapper).writeValueAsString(authors);
        verify(response).setContentType(APPLICATION_JSON);
        verify(response).getWriter();
        verify(printWriter).write(result);
    }

    @Test
    void doGet_whenExistAndPathInfoIsSlash_thenReturnList() throws Exception {
        final String result = "all authors";
        final List<AuthorDto> authors = List.of(AUTHOR_DTO_1, AUTHOR_DTO_2, AUTHOR_DTO_3);
        doReturn("/").when(request).getPathInfo();
        doReturn(authors).when(authorService).getAll();
        doReturn(result).when(objectMapper).writeValueAsString(authors);
        doNothing().when(response).setContentType(APPLICATION_JSON);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(result);

        authorServlet.doGet(request, response);

        verify(request).getPathInfo();
        verify(authorService).getAll();
        verify(objectMapper).writeValueAsString(authors);
        verify(response).setContentType(APPLICATION_JSON);
        verify(response).getWriter();
        verify(printWriter).write(result);
    }

    @Test
    void doGet_whenValidId_thenAuthorDto() throws Exception {
        final Long authorId = 1L;
        final String result = "authorDto 1";
        doReturn("/1").when(request).getPathInfo();
        doReturn(AUTHOR_DTO_1).when(authorService).getById(authorId);
        doReturn(result).when(objectMapper).writeValueAsString(AUTHOR_DTO_1);
        doNothing().when(response).setContentType(APPLICATION_JSON);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(result);

        authorServlet.doGet(request, response);

        verify(request).getPathInfo();
        verify(authorService).getById(authorId);
        verify(objectMapper).writeValueAsString(AUTHOR_DTO_1);
        verify(response).setContentType(APPLICATION_JSON);
        verify(response).getWriter();
        verify(printWriter).write(result);
    }

    @Test
    void doGet_whenInvalidId_thenThrowNotFoundException() throws Exception {
        final Long authorId = 666L;
        final NotFoundException notFoundException = new NotFoundException(NOT_FOUNT_STRING);
        doReturn("/666").when(request).getPathInfo();
        doThrow(notFoundException).when(authorService).getById(authorId);
        doNothing().when(response).setContentType(TEXT_PLAIN);
        doNothing().when(response).setStatus(SC_BAD_REQUEST);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(NOT_FOUNT_STRING);

        authorServlet.doGet(request, response);

        verify(request).getPathInfo();
        verify(authorService).getById(authorId);
        verify(response).setContentType(TEXT_PLAIN);
        verify(response).setStatus(SC_BAD_REQUEST);
        verify(response).getWriter();
        verify(printWriter).write(NOT_FOUNT_STRING);
    }

    @Test
    void doGet_whenInternalError_thenThrowException() throws Exception {
        final Long authorId = 2L;
        final RuntimeException exception = new RuntimeException(EXCEPTION_STRING);
        doReturn("/2").when(request).getPathInfo();
        doThrow(exception).when(authorService).getById(authorId);
        doNothing().when(response).setContentType(TEXT_PLAIN);
        doNothing().when(response).setStatus(SC_INTERNAL_SERVER_ERROR);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(EXCEPTION_STRING);

        authorServlet.doGet(request, response);

        verify(request).getPathInfo();
        verify(authorService).getById(authorId);
        verify(response).setContentType(TEXT_PLAIN);
        verify(response).setStatus(SC_INTERNAL_SERVER_ERROR);
        verify(response).getWriter();
        verify(printWriter).write(EXCEPTION_STRING);
    }



    @Test
    void doPost_whenValidJsonAndPathInfoIsNull_thenAuthorDto() throws Exception {
        BufferedReader bufferedReader = mock(BufferedReader.class);
        final Stream<String> stream = Stream.of("{", "valid", "json", "of", "authorDto", "}");
        final String result = "new authorDto";
        doReturn(bufferedReader).when(request).getReader();
        doReturn(stream).when(bufferedReader).lines();
        doReturn(AUTHOR_DTO_1).when(objectMapper).readValue(anyString(), eq(AuthorDto.class));
        doReturn(null).when(request).getPathInfo();
        doReturn(AUTHOR_DTO_1).when(authorService).save(AUTHOR_DTO_1);
        doReturn(result).when(objectMapper).writeValueAsString(AUTHOR_DTO_1);
        doNothing().when(response).setContentType(APPLICATION_JSON);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(result);

        authorServlet.doPost(request, response);

        verify(objectMapper).readValue(anyString(), eq(AuthorDto.class));
        verify(request).getPathInfo();
        verify(authorService).save(AUTHOR_DTO_1);
        verify(objectMapper).writeValueAsString(AUTHOR_DTO_1);
        verify(response).setContentType(APPLICATION_JSON);
        verify(response).getWriter();
        verify(printWriter).write(result);
    }

    @Test
    void doPost_whenValidJsonAndPathInfoIsSlash_thenAuthorDto() throws Exception {
        BufferedReader bufferedReader = mock(BufferedReader.class);
        final Stream<String> stream = Stream.of("{", "valid", "json", "of", "authorDto", "}");
        final String result = "new authorDto";
        doReturn(bufferedReader).when(request).getReader();
        doReturn(stream).when(bufferedReader).lines();
        doReturn(AUTHOR_DTO_1).when(objectMapper).readValue(anyString(), eq(AuthorDto.class));
        doReturn("/").when(request).getPathInfo();
        doReturn(AUTHOR_DTO_1).when(authorService).save(AUTHOR_DTO_1);
        doReturn(result).when(objectMapper).writeValueAsString(AUTHOR_DTO_1);
        doNothing().when(response).setContentType(APPLICATION_JSON);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(result);

        authorServlet.doPost(request, response);

        verify(objectMapper).readValue(anyString(), eq(AuthorDto.class));
        verify(request).getPathInfo();
        verify(authorService).save(AUTHOR_DTO_1);
        verify(objectMapper).writeValueAsString(AUTHOR_DTO_1);
        verify(response).setContentType(APPLICATION_JSON);
        verify(response).getWriter();
        verify(printWriter).write(result);
    }

    @Test
    void doPost_whenWrongUrl_thenUrlErrorString() throws Exception {
        BufferedReader bufferedReader = mock(BufferedReader.class);
        final Stream<String> stream = Stream.of("{", "valid", "json", "of", "authorDto", "}");
        doReturn(bufferedReader).when(request).getReader();
        doReturn(stream).when(bufferedReader).lines();
        doReturn(AUTHOR_DTO_1).when(objectMapper).readValue(anyString(), eq(AuthorDto.class));
        doReturn("/wrong").when(request).getPathInfo();
        doNothing().when(response).setContentType(TEXT_PLAIN);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(URL_ERROR);

        authorServlet.doPost(request, response);

        verify(objectMapper).readValue(anyString(), eq(AuthorDto.class));
        verify(request).getPathInfo();
        verify(response).setContentType(TEXT_PLAIN);
        verify(response).getWriter();
        verify(printWriter).write(URL_ERROR);
    }

    @Test
    void doPost_whenInvalidJson_thenThrowJsonMappingException() throws Exception {
        BufferedReader bufferedReader = mock(BufferedReader.class);
        final JsonMappingException jsonMappingException = new JsonMappingException(JSON_EXCEPTION_STRING);
        final Stream<String> stream = Stream.of("{", "invalid", "json", "of", "authorDto", "}");
        doReturn(bufferedReader).when(request).getReader();
        doReturn(stream).when(bufferedReader).lines();
        doThrow(jsonMappingException).when(objectMapper).readValue(anyString(), eq(AuthorDto.class));
        doNothing().when(response).setContentType(TEXT_PLAIN);
        doNothing().when(response).setStatus(SC_BAD_REQUEST);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(JSON_EXCEPTION_STRING);

        authorServlet.doPost(request, response);

        verify(request).getReader();
        verify(bufferedReader).lines();
        verify(objectMapper).readValue(anyString(), eq(AuthorDto.class));
        verify(response).setContentType(TEXT_PLAIN);
        verify(response).setStatus(SC_BAD_REQUEST);
        verify(response).getWriter();
        verify(printWriter).write(JSON_EXCEPTION_STRING);

    }

    @Test
    void doPost_whenInternalError_thenThrowException() throws Exception {
        BufferedReader bufferedReader = mock(BufferedReader.class);
        final RuntimeException exception = new RuntimeException(EXCEPTION_STRING);
        final Stream<String> stream = Stream.of("{", "valid", "json", "of", "authorDto", "}");
        doReturn(bufferedReader).when(request).getReader();
        doReturn(stream).when(bufferedReader).lines();
        doReturn(AUTHOR_DTO_1).when(objectMapper).readValue(anyString(), eq(AuthorDto.class));
        doReturn(null).when(request).getPathInfo();
        doThrow(exception).when(authorService).save(AUTHOR_DTO_1);
        doNothing().when(response).setContentType(TEXT_PLAIN);
        doNothing().when(response).setStatus(SC_INTERNAL_SERVER_ERROR);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(EXCEPTION_STRING);

        authorServlet.doPost(request, response);

        verify(objectMapper).readValue(anyString(), eq(AuthorDto.class));
        verify(request).getPathInfo();
        verify(authorService).save(AUTHOR_DTO_1);
        verify(response).setContentType(TEXT_PLAIN);
        verify(response).setStatus(SC_INTERNAL_SERVER_ERROR);
        verify(response).getWriter();
        verify(printWriter).write(EXCEPTION_STRING);
    }



    @Test
    void doPut_whenValidJsonAndId_thenReturnTrue() throws Exception {
        BufferedReader bufferedReader = mock(BufferedReader.class);
        final Stream<String> stream = Stream.of("{", "valid", "json", "of", "authorDto", "}");
        final Long authorId = 1L;
        doReturn(bufferedReader).when(request).getReader();
        doReturn(stream).when(bufferedReader).lines();
        doReturn(AUTHOR_DTO_1).when(objectMapper).readValue(anyString(), eq(AuthorDto.class));
        doReturn("/1").when(request).getPathInfo();
        doReturn(true).when(authorService).update(AUTHOR_DTO_1, authorId);
        doNothing().when(response).setContentType(TEXT_PLAIN);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(UPDATING_RESULT + true);

        authorServlet.doPut(request, response);

        verify(request).getReader();
        verify(bufferedReader).lines();
        verify(objectMapper).readValue(anyString(), eq(AuthorDto.class));
        verify(request).getPathInfo();
        verify(authorService).update(AUTHOR_DTO_1, authorId);
        verify(response).setContentType(TEXT_PLAIN);
        verify(response).getWriter();
        verify(printWriter).write(UPDATING_RESULT + true);
    }

    @Test
    void doPut_whenValidJsonButPathInfoIsNull_thenIdErrorString() throws Exception {
        BufferedReader bufferedReader = mock(BufferedReader.class);
        final Stream<String> stream = Stream.of("{", "valid", "json", "of", "authorDto", "}");
        doReturn(bufferedReader).when(request).getReader();
        doReturn(stream).when(bufferedReader).lines();
        doReturn(AUTHOR_DTO_1).when(objectMapper).readValue(anyString(), eq(AuthorDto.class));
        doReturn(null).when(request).getPathInfo();
        doNothing().when(response).setStatus(SC_BAD_REQUEST);
        doNothing().when(response).setContentType(TEXT_PLAIN);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(ID_ERROR);

        authorServlet.doPut(request, response);

        verify(request).getReader();
        verify(bufferedReader).lines();
        verify(objectMapper).readValue(anyString(), eq(AuthorDto.class));
        verify(request).getPathInfo();
        verify(response).setStatus(SC_BAD_REQUEST);
        verify(response).setContentType(TEXT_PLAIN);
        verify(response).getWriter();
        verify(printWriter).write(ID_ERROR);
    }

    @Test
    void doPut_whenValidJsonButPathInfoIsSlash_thenIdErrorString() throws Exception {
        BufferedReader bufferedReader = mock(BufferedReader.class);
        final Stream<String> stream = Stream.of("{", "valid", "json", "of", "authorDto", "}");
        doReturn(bufferedReader).when(request).getReader();
        doReturn(stream).when(bufferedReader).lines();
        doReturn(AUTHOR_DTO_1).when(objectMapper).readValue(anyString(), eq(AuthorDto.class));
        doReturn("/").when(request).getPathInfo();
        doNothing().when(response).setStatus(SC_BAD_REQUEST);
        doNothing().when(response).setContentType(TEXT_PLAIN);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(ID_ERROR);

        authorServlet.doPut(request, response);

        verify(request).getReader();
        verify(bufferedReader).lines();
        verify(objectMapper).readValue(anyString(), eq(AuthorDto.class));
        verify(request).getPathInfo();
        verify(response).setStatus(SC_BAD_REQUEST);
        verify(response).setContentType(TEXT_PLAIN);
        verify(response).getWriter();
        verify(printWriter).write(ID_ERROR);
    }

    @Test
    void doPut_whenInvalidJson_thenThrowJsonMappingException() throws Exception {
        BufferedReader bufferedReader = mock(BufferedReader.class);
        final Stream<String> stream = Stream.of("{", "invalid", "json", "of", "authorDto", "}");
        final JsonMappingException jsonMappingException = new JsonMappingException(JSON_EXCEPTION_STRING);
        doReturn(bufferedReader).when(request).getReader();
        doReturn(stream).when(bufferedReader).lines();
        doThrow(jsonMappingException).when(objectMapper).readValue(anyString(), eq(AuthorDto.class));
        doNothing().when(response).setStatus(SC_BAD_REQUEST);
        doNothing().when(response).setContentType(TEXT_PLAIN);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(JSON_EXCEPTION_STRING);

        authorServlet.doPut(request, response);

        verify(request).getReader();
        verify(bufferedReader).lines();
        verify(objectMapper).readValue(anyString(), eq(AuthorDto.class));
        verify(response).setStatus(SC_BAD_REQUEST);
        verify(response).setContentType(TEXT_PLAIN);
        verify(response).getWriter();
        verify(printWriter).write(JSON_EXCEPTION_STRING);
    }

    @Test
    void doPut_whenInvalidId_thenThrowNotFoundException() throws Exception {
        BufferedReader bufferedReader = mock(BufferedReader.class);
        final Long authorId = 666L;
        final Stream<String> stream = Stream.of("{", "valid", "json", "of", "authorDto", "}");
        final NotFoundException notFoundException = new NotFoundException(NOT_FOUNT_STRING);
        doReturn(bufferedReader).when(request).getReader();
        doReturn(stream).when(bufferedReader).lines();
        doReturn(AUTHOR_DTO_1).when(objectMapper).readValue(anyString(), eq(AuthorDto.class));
        doReturn("/666").when(request).getPathInfo();
        doThrow(notFoundException).when(authorService).update(AUTHOR_DTO_1, authorId);
        doNothing().when(response).setStatus(SC_BAD_REQUEST);
        doNothing().when(response).setContentType(TEXT_PLAIN);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(NOT_FOUNT_STRING);

        authorServlet.doPut(request, response);

        verify(request).getReader();
        verify(bufferedReader).lines();
        verify(objectMapper).readValue(anyString(), eq(AuthorDto.class));
        verify(request).getPathInfo();
        verify(authorService).update(AUTHOR_DTO_1, authorId);
        verify(response).setStatus(SC_BAD_REQUEST);
        verify(response).setContentType(TEXT_PLAIN);
        verify(response).getWriter();
        verify(printWriter).write(NOT_FOUNT_STRING);
    }

    @Test
    void doPut_whenInternalError_thenThrowException() throws Exception {
        BufferedReader bufferedReader = mock(BufferedReader.class);
        final Long authorId = 2L;
        final Stream<String> stream = Stream.of("{", "valid", "json", "of", "authorDto", "}");
        final RuntimeException exception = new RuntimeException(EXCEPTION_STRING);
        doReturn(bufferedReader).when(request).getReader();
        doReturn(stream).when(bufferedReader).lines();
        doReturn(AUTHOR_DTO_1).when(objectMapper).readValue(anyString(), eq(AuthorDto.class));
        doReturn("/2").when(request).getPathInfo();
        doThrow(exception).when(authorService).update(AUTHOR_DTO_1, authorId);
        doNothing().when(response).setStatus(SC_INTERNAL_SERVER_ERROR);
        doNothing().when(response).setContentType(TEXT_PLAIN);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(EXCEPTION_STRING);

        authorServlet.doPut(request, response);

        verify(request).getReader();
        verify(bufferedReader).lines();
        verify(objectMapper).readValue(anyString(), eq(AuthorDto.class));
        verify(request).getPathInfo();
        verify(authorService).update(AUTHOR_DTO_1, authorId);
        verify(response).setStatus(SC_INTERNAL_SERVER_ERROR);
        verify(response).setContentType(TEXT_PLAIN);
        verify(response).getWriter();
        verify(printWriter).write(EXCEPTION_STRING);
    }



    @Test
    void doDelete_whenValidId_thenSuccessString() throws Exception {
        final Long authorId = 1L;
        doReturn("/1").when(request).getPathInfo();
        doReturn(true).when(authorService).delete(authorId);
        doNothing().when(response).setContentType(TEXT_PLAIN);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(DELETING_RESULT + true);

        authorServlet.doDelete(request, response);

        verify(request).getPathInfo();
        verify(authorService).delete(authorId);
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

        authorServlet.doDelete(request, response);

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

        authorServlet.doDelete(request, response);

        verify(request).getPathInfo();
        verify(response).setStatus(SC_BAD_REQUEST);
        verify(response).setContentType(TEXT_PLAIN);
        verify(response).getWriter();
        verify(printWriter).write(ID_ERROR);
    }

    @Test
    void doDelete_whenInvalidId_thenThrowNotFoundException() throws Exception {
        final Long authorId = 666L;
        final NotFoundException notFoundException = new NotFoundException(NOT_FOUNT_STRING);
        doReturn("/666").when(request).getPathInfo();
        doThrow(notFoundException).when(authorService).delete(authorId);
        doNothing().when(response).setStatus(SC_BAD_REQUEST);
        doNothing().when(response).setContentType(TEXT_PLAIN);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(NOT_FOUNT_STRING);

        authorServlet.doDelete(request, response);

        verify(request).getPathInfo();
        verify(authorService).delete(authorId);
        verify(response).setStatus(SC_BAD_REQUEST);
        verify(response).setContentType(TEXT_PLAIN);
        verify(response).getWriter();
        verify(printWriter).write(NOT_FOUNT_STRING);
    }

    @Test
    void doDelete_whenInternalError_thenThrowException() throws Exception {
        final Long authorId = 2L;
        final RuntimeException exception = new RuntimeException(EXCEPTION_STRING);
        doReturn("/2").when(request).getPathInfo();
        doThrow(exception).when(authorService).delete(authorId);
        doNothing().when(response).setStatus(SC_INTERNAL_SERVER_ERROR);
        doNothing().when(response).setContentType(TEXT_PLAIN);
        doReturn(printWriter).when(response).getWriter();
        doNothing().when(printWriter).write(EXCEPTION_STRING);

        authorServlet.doDelete(request, response);

        verify(request).getPathInfo();
        verify(authorService).delete(authorId);
        verify(response).setStatus(SC_INTERNAL_SERVER_ERROR);
        verify(response).setContentType(TEXT_PLAIN);
        verify(response).getWriter();
        verify(printWriter).write(EXCEPTION_STRING);
    }


}