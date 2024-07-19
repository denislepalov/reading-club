package ru.aston.lepd.readingclub.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.aston.lepd.readingclub.dto.AuthorDto;
import ru.aston.lepd.readingclub.servlet.AuthorServlet;
import ru.aston.lepd.readingclub.util.ObjectContainerStatic;
import ru.aston.lepd.readingclub.dao.AuthorDao;
import ru.aston.lepd.readingclub.util.IntegrationTestBase;

import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static ru.aston.lepd.readingclub.util.Constants.*;


class AuthorServletIntegrationTest extends IntegrationTestBase {

    private static Server server;
    private static ObjectMapper objectMapper = ObjectContainerStatic.getObjectMapper();
    private static AuthorDao authorDao = ObjectContainerStatic.getAuthorDao();
    private static final String TEXT_PLAIN = "text/plain;charset=iso-8859-1";
    private static final String APPLICATION_JSON = "application/json";
    private static final String ID_ERROR = "ERROR: author ID is required";
    private static final String URL_ERROR = "ERROR: wrong URL";
    private static final String UPDATING_RESULT = "Result of updating author: ";
    private static final String DELETING_RESULT = "Result of deleting author: ";
    private static final String NOT_FOUNT_STRING = "There is no author with id=666 in database";


    @BeforeAll
    public static void startServer() throws Exception {
        AuthorServlet authorServlet = ObjectContainerStatic.getAuthorServlet();
        server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(authorServlet), "/authors/*");
        server.start();
    }



    @Test
    void doGet_whenExist_thenReturnList() throws Exception {
        final String url = "http://localhost:8080/authors";
        final String expectedBody = objectMapper.writeValueAsString(List.of(AUTHOR_DTO_1, AUTHOR_DTO_2, AUTHOR_DTO_3));
        given()
                .when()
                .get(url)
                .then()
                .statusCode(200)
                .header("Content-Type", equalTo(APPLICATION_JSON))
                .body(equalTo(expectedBody));
    }

    @Test
    void doGet_whenExist2_thenReturnList() throws Exception {
        final String url = "http://localhost:8080/authors/";
        final String expectedBody = objectMapper.writeValueAsString(List.of(AUTHOR_DTO_1, AUTHOR_DTO_2, AUTHOR_DTO_3));
        given()
                .when()
                .get(url)
                .then()
                .statusCode(200)
                .header("Content-Type", equalTo(APPLICATION_JSON))
                .body(equalTo(expectedBody));
    }

    @Test
    void doGet_whenNotExist_thenReturnEmptyList() throws Exception {
        final String url = "http://localhost:8080/authors";
        final String expectedBody = objectMapper.writeValueAsString(Collections.emptyList());
        authorDao.delete(1L);
        authorDao.delete(2L);
        authorDao.delete(3L);
        given()
                .when()
                .get(url)
                .then()
                .statusCode(200)
                .header("Content-Type", equalTo(APPLICATION_JSON))
                .body(equalTo(expectedBody));
    }

    @Test
    void doGet_whenValidId_thenAuthorDto() throws Exception {
        final String url = "http://localhost:8080/authors/1";
        final String expectedBody = objectMapper.writeValueAsString(AUTHOR_DTO_1);
        given()
                .when()
                .get(url)
                .then()
                .statusCode(200)
                .header("Content-Type", equalTo(APPLICATION_JSON))
                .body(equalTo(expectedBody));
    }

    @Test
    void doGet_whenInvalidId_thenNotFoundString() throws Exception {
        final String url = "http://localhost:8080/authors/666";
        given()
                .when()
                .get(url)
                .then()
                .statusCode(400)
                .header("Content-Type", equalTo(TEXT_PLAIN))
                .body(equalTo(NOT_FOUNT_STRING));
    }

    @Test
    void doGet_ShouldInternalError() throws Exception {
        final String url = "http://localhost:8080/authors/wrong";
        given()
                .when()
                .get(url)
                .then()
                .statusCode(500)
                .header("Content-Type", equalTo(TEXT_PLAIN));
    }



    @Test
    void doPost_whenValidJson_thenAuthorDto() throws Exception {
        final String url = "http://localhost:8080/authors";
        final String body = objectMapper.writeValueAsString(AUTHOR_DTO_1);
        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(url)
                .then()
                .statusCode(200)
                .header("Content-Type", equalTo(APPLICATION_JSON))
                .body(equalTo(body));
    }

    @Test
    void doPost_whenValidJson2_thenAuthorDto() throws Exception {
        final String url = "http://localhost:8080/authors/";
        final String body = objectMapper.writeValueAsString(AUTHOR_DTO_1);
        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(url)
                .then()
                .statusCode(200)
                .header("Content-Type", equalTo(APPLICATION_JSON))
                .body(equalTo(body));
    }

    @Test
    void doPost_whenInvalidJson_thenJsonError() throws Exception {
        final String url = "http://localhost:8080/authors";
        final String body = objectMapper.writeValueAsString(READER_DTO_1);
        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(url)
                .then()
                .statusCode(400)
                .header("Content-Type", equalTo(TEXT_PLAIN));
    }

    @Test
    void doPost_whenConstraintViolation_thenDaoError() throws Exception {
        final String url = "http://localhost:8080/authors";
        AuthorDto authorDto = new AuthorDto();
        final String body = objectMapper.writeValueAsString(authorDto);
        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(url)
                .then()
                .statusCode(400)
                .header("Content-Type", equalTo(TEXT_PLAIN));
    }

    @Test
    void doPost_whenWrongUrl_thenUrlErrorString() throws Exception {
        final String url = "http://localhost:8080/authors/wrong";
        final String body = objectMapper.writeValueAsString(AUTHOR_DTO_1);
        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(url)
                .then()
                .statusCode(400)
                .header("Content-Type", equalTo(TEXT_PLAIN))
                .body(equalTo(URL_ERROR));
    }



    @Test
    void doPut_whenValidJson_thenSuccessString() throws Exception {
        final String url = "http://localhost:8080/authors/1";
        final String body = objectMapper.writeValueAsString(AUTHOR_DTO_1);
        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put(url)
                .then()
                .statusCode(200)
                .header("Content-Type", equalTo(TEXT_PLAIN))
                .body(equalTo(String.format(UPDATING_RESULT + "true")));
    }

    @Test
    void doPut_whenInvalidJson_thenJsonError() throws Exception {
        final String url = "http://localhost:8080/authors/1";
        final String body = objectMapper.writeValueAsString(READER_DTO_1);
        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put(url)
                .then()
                .statusCode(400)
                .header("Content-Type", equalTo(TEXT_PLAIN));
    }

    @Test
    void doPut_whenInvalidId_thenNotFoundString() throws Exception {
        final String url = "http://localhost:8080/authors/666";
        final String body = objectMapper.writeValueAsString(AUTHOR_DTO_1);
        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put(url)
                .then()
                .statusCode(400)
                .header("Content-Type", equalTo(TEXT_PLAIN))
                .body(equalTo(NOT_FOUNT_STRING));
    }

    @Test
    void doPut_whenEmptyId_thenIdErrorString() throws Exception {
        final String url = "http://localhost:8080/authors";
        final String body = objectMapper.writeValueAsString(AUTHOR_DTO_1);
        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put(url)
                .then()
                .statusCode(400)
                .header("Content-Type", equalTo(TEXT_PLAIN))
                .body(equalTo(ID_ERROR));
    }

    @Test
    void doPut_whenEmptyId2_thenIdErrorString() throws Exception {
        final String url = "http://localhost:8080/authors/";
        final String body = objectMapper.writeValueAsString(AUTHOR_DTO_1);
        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put(url)
                .then()
                .statusCode(400)
                .header("Content-Type", equalTo(TEXT_PLAIN))
                .body(equalTo(ID_ERROR));
    }

    @Test
    void doPut_ShouldInternalError() throws Exception {
        final String url = "http://localhost:8080/authors/wrong";
        final String body = objectMapper.writeValueAsString(AUTHOR_DTO_1);
        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put(url)
                .then()
                .statusCode(500)
                .header("Content-Type", equalTo(TEXT_PLAIN));
    }



    @Test
    void doDelete_whenValidId_thenSuccessString() throws Exception {
        final String url = "http://localhost:8080/authors/1";
        given()
                .when()
                .delete(url)
                .then()
                .statusCode(200)
                .header("Content-Type", equalTo(TEXT_PLAIN))
                .body(equalTo(DELETING_RESULT + "true"));
    }

    @Test
    void doDelete_whenInvalidId_thenNotFoundString() throws Exception {
        final String url = "http://localhost:8080/authors/666";
        given()
                .when()
                .delete(url)
                .then()
                .statusCode(400)
                .header("Content-Type", equalTo(TEXT_PLAIN))
                .body(equalTo(NOT_FOUNT_STRING));
    }

    @Test
    void doDelete_whenEmptyId_thenIdErrorString() throws Exception {
        final String url = "http://localhost:8080/authors";
        given()
                .when()
                .delete(url)
                .then()
                .statusCode(400)
                .header("Content-Type", equalTo(TEXT_PLAIN))
                .body(equalTo(ID_ERROR));
    }

    @Test
    void doDelete_whenEmptyId2_thenIdErrorString() throws Exception {
        final String url = "http://localhost:8080/authors/";
        given()
                .when()
                .delete(url)
                .then()
                .statusCode(400)
                .header("Content-Type", equalTo(TEXT_PLAIN))
                .body(equalTo(ID_ERROR));
    }

    @Test
    void doDelete_ShouldInternalError() throws Exception {
        final String url = "http://localhost:8080/authors/wrong";
        given()
                .when()
                .delete(url)
                .then()
                .statusCode(500)
                .header("Content-Type", equalTo(TEXT_PLAIN));
    }



    @AfterAll
    public static void stopServer() throws Exception {
        server.stop();
    }

}