package com.example.tests.books;

import com.example.config.BookTestConfig;
import com.example.dao.MySqlDaoSql;
import com.example.model.Book;
import com.example.model.CustomError;
import com.example.utils.Properties;
import com.google.gson.Gson;
import io.restassured.http.ContentType;
import org.junit.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.apache.http.protocol.HTTP.CONTENT_TYPE;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {BookTestConfig.class})
public class BooksEndToEndTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(BooksEndToEndTests.class);
    private static String apiEndpoint = Properties.getApiEndpoint();

    @BeforeClass
    public static void setUp(){
        MySqlDaoSql mySqlDaoSql = new MySqlDaoSql(BookTestConfig.jdbcTemplate());
        mySqlDaoSql.truncateTable();
        mySqlDaoSql.batchInsert(getBooks());

        LOGGER.info("Test API Endpoint: \n" + apiEndpoint);
    }

    @AfterClass
    public static void tearDown(){
        MySqlDaoSql mySqlDaoSql = new MySqlDaoSql(BookTestConfig.jdbcTemplate());
        mySqlDaoSql.truncateTable();
    }

    @Test
    public void givenGetBooksUrl_WhenSuccessReturns200StatusAndNonZeroBooks() {
        List books = new ArrayList<>();
        books = given()
                .contentType(CONTENT_TYPE)
                .when()
                .get(apiEndpoint)
                .then()
                .statusCode(200)
                .extract().body().as(books.getClass());

        Assert.assertTrue("failure, size did not match ", books.size() > 0);
    }

    @Test
    public void givenGetBookUrl_WhenSuccessReturns200StatusAnd1Books() {
        Book book = given()
                .contentType(CONTENT_TYPE)
                .when()
                .get(apiEndpoint+1)
                .then()
                .statusCode(200)
                .extract().body().as(Book.class);

        Assert.assertEquals("failure, Author did not match ", "Author1", book.getAuthor());
        Assert.assertEquals("failure, Title did not match ", "Title1", book.getTitle());
    }

    @Test
    public void givenGetBookUrl_WhenNotFoundReturns404AndAnErrorMessage() {
        CustomError error = given()
                .contentType(CONTENT_TYPE)
                .when()
                .get(apiEndpoint+100)
                .then()
                .statusCode(404)
                .extract().body().as(CustomError.class);

        Assert.assertEquals("failure, Error message did not match ", "'100' not found!", error.getError());
    }

    @Test
    public void givenPostBookUrl_WhenSuccessReturns201AndCreatedBook() {
        Book book = new Book();
        book.setAuthor("TestAuthor");
        book.setTitle("TestTitle");

        Gson gson = new Gson();
        String requestBody = gson.toJson(book);

        Book createdBook = given()
                .contentType(CONTENT_TYPE)
                .headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON)
                .body(requestBody)
                .when()
                .post(apiEndpoint)
                .then()
                .statusCode(201)
                .extract().body().as(Book.class);

        Assert.assertEquals("failure, Author did not match ", "TestAuthor", createdBook.getAuthor());
        Assert.assertEquals("failure, Title did not match ", "TestTitle", createdBook.getTitle());
    }

//    @Test
//    public void givenPostBookUrl_WhenConflictOfBooksReturns409AndAnErrorMessage() {
//        Book book = new Book();
//        book.setAuthor("Author1");
//        book.setTitle("Title1");
//
//        Gson gson = new Gson();
//        String requestBody = gson.toJson(book);
//
//        CustomError error = given()
//                .contentType(CONTENT_TYPE)
//                .headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON)
//                .body(requestBody)
//                .when()
//                .post(apiEndpoint)
//                .then()
//                .statusCode(409)
//                .extract().body().as(CustomError.class);
//
//        Assert.assertEquals("failure, Error message did not match ", "'AUTHOR' cannot be duplicate!", error.getError());
//    }

    @Test
    public void givenPutBookUrl_WhenSuccessReturns200AndUpdatedBook() {
        Book book = new Book();
        book.setId(5l);
        book.setAuthor("Author5Updated");
        book.setTitle("Title5Updated");

        Gson gson = new Gson();
        String requestBody = gson.toJson(book);

        Book updatedBook = given()
                .contentType(CONTENT_TYPE)
                .headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON)
                .body(requestBody)
                .when()
                .put(apiEndpoint)
                .then()
                .statusCode(200)
                .extract().body().as(Book.class);

        Assert.assertEquals("failure, Author did not match ", "Author5Updated", updatedBook.getAuthor());
        Assert.assertEquals("failure, Title did not match ", "Title5Updated", updatedBook.getTitle());
    }

//    @Test
//    public void givenPutBookUrl_WhenConflictReturns409AndErrorMessage() {
//        Book book = new Book();
//        book.setId(2l);
//        book.setAuthor("Author3");
//        book.setTitle("Title3");
//
//        Gson gson = new Gson();
//        String requestBody = gson.toJson(book);
//
//        CustomError error = given()
//                .contentType(CONTENT_TYPE)
//                .headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON)
//                .body(requestBody)
//                .when()
//                .post(apiEndpoint)
//                .then()
//                .statusCode(409)
//                .extract().body().as(CustomError.class);
//
//        Assert.assertEquals("failure, Error message did not match ", "'AUTHOR' cannot be duplicate!", error.getError());
//    }

    @Test
    public void givenPutBookUrl_WhenNotFoundReturns404AndErrorMessage() {
        Book book = new Book();
        book.setId(100l);
        book.setAuthor("Author");
        book.setTitle("Title");

        Gson gson = new Gson();
        String requestBody = gson.toJson(book);

        CustomError error = given()
                .contentType(CONTENT_TYPE)
                .headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON)
                .body(requestBody)
                .when()
                .post(apiEndpoint)
                .then()
                .statusCode(404)
                .extract().body().as(CustomError.class);

        Assert.assertEquals("failure, Error message did not match ", "'100' not found!", error.getError());
    }

    @Test
    public void givenDeleteBookUrl_WhenSuccessReturns204() {
                given()
                .contentType(CONTENT_TYPE)
                .headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON)
                .when()
                .delete(apiEndpoint+4)
                .then()
                .statusCode(204);
    }

    @Test
    public void givenDeleteBookUrl_WhenNotFoundReturns404AndErrorMessage() {
        CustomError error = given()
                .contentType(CONTENT_TYPE)
                .headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON)
                .when()
                .delete(apiEndpoint+100)
                .then()
                .statusCode(404)
                .extract().body().as(CustomError.class);

        Assert.assertEquals("failure, Error message did not match ", "'100' not found!", error.getError());
    }

    private static List<Book> getBooks() {
        List<Book> books = new ArrayList<Book>();
        for(int i=1; i < 6; i++){
            Book book = new Book();
            long id = 0l;
            book.setId(id+i);
            book.setAuthor("Author" + i);
            book.setTitle("Title" + i);
            books.add(book);
        }
        return books;
    }
}
