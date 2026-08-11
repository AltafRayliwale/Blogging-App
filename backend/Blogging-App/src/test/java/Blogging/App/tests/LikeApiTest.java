package Blogging.App.tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LikeApiTest extends BaseTest {

    @Test
    void likePostTest() {

        String body = """
                {
                    "title": "Like Test Post",
                    "content": "Testing like functionality",
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
            .post("/api/posts/" + postId + "/like/45")
        .then()
            .statusCode(200)
            .body(equalTo("Post liked successfully."));

        long count =
                given()
                    .baseUri(BASE_URI)
                .when()
                    .get("/api/posts/" + postId + "/likes")
                .then()
                    .statusCode(200)
                    .extract()
                    .as(Long.class);

        assertEquals(1L, count);

        boolean liked =
                given()
                    .baseUri(BASE_URI)
                .when()
                    .get("/api/posts/" + postId + "/liked/45")
                .then()
                    .statusCode(200)
                    .extract()
                    .as(Boolean.class);

        assertTrue(liked);

        getRequestSpec()
        .when()
            .delete("/api/posts/" + postId + "/like/45")
        .then()
            .statusCode(200)
            .body(equalTo("Post unliked successfully."));

        long countAfterUnlike =
                given()
                    .baseUri(BASE_URI)
                .when()
                    .get("/api/posts/" + postId + "/likes")
                .then()
                    .statusCode(200)
                    .extract()
                    .as(Long.class);

        assertEquals(0L, countAfterUnlike);

        boolean likedAfterUnlike =
                given()
                    .baseUri(BASE_URI)
                .when()
                    .get("/api/posts/" + postId + "/liked/45")
                .then()
                    .statusCode(200)
                    .extract()
                    .as(Boolean.class);

        assertFalse(likedAfterUnlike);
    }

    @Test
    void duplicateLikeTest() {

        String body = """
                {
                    "title": "Duplicate Like Test",
                    "content": "Testing duplicate likes",
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
            .post("/api/posts/" + postId + "/like/45")
        .then()
            .statusCode(200);

        getRequestSpec()
        .when()
            .post("/api/posts/" + postId + "/like/45")
        .then()
            .statusCode(200);

        long count =
                given()
                    .baseUri(BASE_URI)
                .when()
                    .get("/api/posts/" + postId + "/likes")
                .then()
                    .statusCode(200)
                    .extract()
                    .as(Long.class);

        assertEquals(1L, count);
    }

    @Test
    void likeNonExistingPostTest() {

        getRequestSpec()
        .when()
            .post("/api/posts/999999/like/45")
        .then()
            .statusCode(404);
    }

    @Test
    void unlikeNonExistingPostTest() {

        getRequestSpec()
        .when()
            .delete("/api/posts/999999/like/45")
        .then()
            .statusCode(404);
    }
}