package Blogging.App.tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CommentApiTest extends BaseTest {

    @Test
    void createCommentTest() {

        String postBody = """
                {
                    "title": "Comment Test Post",
                    "content": "Testing comments with RestAssured",
                    "imageName": "default.png"
                }
                """;

        int postId =
                getRequestSpec()
                    .body(postBody)
                .when()
                    .post("/posts/user/45/category/1/posts")
                .then()
                    .statusCode(201)
                    .extract()
                    .path("postId");

        String commentBody = """
                {
                    "content": "This is a RestAssured test comment"
                }
                """;

        int commentId =
                getRequestSpec()
                    .body(commentBody)
                .when()
                    .post("/comments/" + postId)
                .then()
                    .statusCode(201)
                    .body("content",
                            equalTo("This is a RestAssured test comment"))
                    .extract()
                    .path("id");

        given()
            .baseUri(BASE_URI)
        .when()
            .get("/posts/" + postId)
        .then()
            .statusCode(200)
            .body("comments[0].id", equalTo(commentId))
            .body("comments[0].content",
                    equalTo("This is a RestAssured test comment"));

        getRequestSpec()
        .when()
            .delete("/comments/" + commentId)
        .then()
            .statusCode(200)
            .body("message",
                    equalTo("Comment Deleted Successfully"))
            .body("success", equalTo(true));
    }

    @Test
    void deleteNonExistingCommentTest() {

        getRequestSpec()
        .when()
            .delete("/comments/999999")
        .then()
            .statusCode(404)
            .body("message",
                    equalTo("Comment not found in commentId : 999999"))
            .body("success", equalTo(false));
    }

    @Test
    void createCommentForNonExistingPostTest() {

        String body = """
                {
                    "content": "This comment should fail"
                }
                """;

        getRequestSpec()
            .body(body)
        .when()
            .post("/comments/999999")
        .then()
            .statusCode(404);
    }
}