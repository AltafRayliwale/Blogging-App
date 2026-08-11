package Blogging.App.Controller;

import Blogging.App.exceptions.ResourceNotFoundException;
import Blogging.App.services.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "http://localhost:5173")
public class LikeController {

    @Autowired
    private LikeService likeService;

    // Like a Post
    @PostMapping("/{postId}/like/{userId}")
    public ResponseEntity<String> likePost(
            @PathVariable Integer postId,
            @PathVariable Integer userId)
            throws ResourceNotFoundException {

        likeService.likePost(postId, userId);

        return ResponseEntity.ok("Post liked successfully.");
    }

    // Unlike a Post
    @DeleteMapping("/{postId}/like/{userId}")
    public ResponseEntity<String> unlikePost(
            @PathVariable Integer postId,
            @PathVariable Integer userId)
            throws ResourceNotFoundException {

        likeService.unlikePost(postId, userId);

        return ResponseEntity.ok("Post unliked successfully.");
    }

    // Total Likes
    @GetMapping("/{postId}/likes")
    public ResponseEntity<Long> getLikeCount(
            @PathVariable Integer postId)
            throws ResourceNotFoundException {

        return ResponseEntity.ok(
                likeService.getLikeCount(postId)
        );
    }

    // Check if User Liked
    @GetMapping("/{postId}/liked/{userId}")
    public ResponseEntity<Boolean> hasUserLiked(
            @PathVariable Integer postId,
            @PathVariable Integer userId)
            throws ResourceNotFoundException {

        return ResponseEntity.ok(
                likeService.hasUserLikedPost(postId, userId)
        );
    }
    
    
    
    
    
}