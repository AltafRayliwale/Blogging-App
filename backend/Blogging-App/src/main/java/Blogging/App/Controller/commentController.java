package Blogging.App.Controller;

import Blogging.App.entities.Comment;
import Blogging.App.exceptions.ResourceNotFoundException;
import Blogging.App.payloads.ApiResponse;
import Blogging.App.payloads.CommentDto;
import Blogging.App.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class commentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/{postId}")
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto comment , @PathVariable int postId) throws ResourceNotFoundException {

        CommentDto createComment = this.commentService.createComment(comment,postId);
        return new ResponseEntity<CommentDto>(createComment, HttpStatus.CREATED);
    }
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse> deleteComment(
            @PathVariable int commentId) throws ResourceNotFoundException {

        System.out.println("DELETE COMMENT HIT: " + commentId);

        this.commentService.deleteComment(commentId);

        ApiResponse response =
                new ApiResponse("Comment Deleted Successfully", true);

        System.out.println("MESSAGE = " + response.getMessage());
        System.out.println("SUCCESS = " + response.isSuccess());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
