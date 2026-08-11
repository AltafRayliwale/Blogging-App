package Blogging.App.tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class PostApiTest extends BaseTest {

    @Test
    void createAndGetPostTest() {

        String body = """
                {
                    "title": "RestAssured Test Post",
                    "content": "Testing API with JWT",
                    "imageName": "default.png"
                }
                """;

        int postId =
                getRequestSpec()
                    .body(body)
                .when()
                    .post("/posts/user/45/category/1/posts")
                .then()
                    .statusCode(201)
                    .body("title", equalTo("RestAssured Test Post"))
                    .extract()
                    .path("postId");

        getRequestSpec()
        .when()
            .get("/posts/" + postId)
        .then()
            .statusCode(200)
            .body("postId", equalTo(postId))
            .body("title", equalTo("RestAssured Test Post"))
            .body("content", equalTo("Testing API with JWT"));
    }

    @Test
    void updatePostTest() {

        String body = """
                {
                    "title": "Original Title",
                    "content": "Original Content",
                    "imageName": "default.png"
                }
                """;

        int postId =
                getRequestSpec()
                    .body(body)
                .when()
                    .post("/posts/user/45/category/1/posts")
                .then()
                    .statusCode(201)
                    .extract()
                    .path("postId");

        String updateBody = """
                {
                    "title": "Updated Title",
                    "content": "Updated Content",
                    "imageName": "default.png"
                }
                """;

        getRequestSpec()
            .body(updateBody)
        .when()
            .put("/posts/" + postId)
        .then()
            .statusCode(200)
            .body("postId", equalTo(postId))
            .body("title", equalTo("Updated Title"))
            .body("content", equalTo("Updated Content"));
    }

    @Test
    void deletePostTest() {

        String body = """
                {
                    "title": "Post To Delete",
                    "content": "This post will be deleted",
                    "imageName": "default.png"
                }
                """;

        int postId =
                getRequestSpec()
                    .body(body)
                .when()
                    .post("/posts/user/45/category/1/posts")
                .then()
                    .statusCode(201)
                    .extract()
                    .path("postId");

        getRequestSpec()
        .when()
            .delete("/posts/" + postId)
        .then()
            .statusCode(200);

        given()
            .baseUri(BASE_URI)
        .when()
            .get("/posts/" + postId)
        .then()
            .statusCode(404);
    }

    @Test
    void getNonExistingPostTest() {

        getRequestSpec()
        .when()
            .get("/posts/999999")
        .then()
            .statusCode(404);
    }

    @Test
    void updateNonExistingPostTest() {

        String body = """
                {
                    "title": "Invalid Update",
                    "content": "Testing invalid post",
                    "imageName": "default.png"
                }
                """;

        getRequestSpec()
            .body(body)
        .when()
            .put("/posts/999999")
        .then()
            .statusCode(404);
    }

    @Test
    void deleteNonExistingPostTest() {

        getRequestSpec()
        .when()
            .delete("/posts/999999")
        .then()
            .statusCode(404);
    }

    @Test
    void createPostWithoutTokenTest() {

        String body = """
                {
                    "title": "Unauthorized Test Post",
                    "content": "Testing API without JWT",
                    "imageName": "default.png"
                }
                """;

        given()
            .baseUri(BASE_URI)
            .contentType("application/json")
            .body(body)
        .when()
            .post("/posts/user/45/category/1/posts")
        .then()
            .statusCode(401);
    }
}