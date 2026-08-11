package Blogging.App.tests;

import org.junit.jupiter.api.BeforeAll;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class BaseTest {

    protected static String token;

    protected static final String BASE_URI =
            "http://localhost:9292";

    @BeforeAll
    static void setup() {

        String loginBody = """
                {
                    "username": "jack@gmail.com",
                    "password": "123456"
                }
                """;

        token =
                given()
                    .baseUri(BASE_URI)
                    .contentType("application/json")
                    .body(loginBody)

                .when()
                    .post("/api/v1/auth/login")

                .then()
                    .statusCode(200)
                    .extract()
                    .path("token");
    }

    protected RequestSpecification getRequestSpec() {

        return given()
                .baseUri(BASE_URI)
                .header("Authorization", "Bearer " + token)
                .contentType("application/json");
    }
}