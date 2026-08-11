package Blogging.App.tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class AuthApiTest extends BaseTest {

    @Test
    void invalidLoginTest() {

        String loginBody = """
                {
                    "username": "jack@gmail.com",
                    "password": "wrongPassword"
                }
                """;

        given()
            .baseUri(BASE_URI)
            .contentType("application/json")
            .body(loginBody)

        .when()
            .post("/api/v1/auth/login")

        .then()
            .statusCode(400)
            .body("message",
                    equalTo("Invalid username or password"));
    }
}