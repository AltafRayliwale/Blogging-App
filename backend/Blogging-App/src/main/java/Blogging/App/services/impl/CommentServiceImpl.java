package Blogging.App.services.impl;

import Blogging.App.entities.Comment;
import Blogging.App.entities.Post;
import Blogging.App.entities.User;
import Blogging.App.exceptions.ResourceNotFoundException;
import Blogging.App.payloads.CommentDto;
import Blogging.App.repositories.CommentRepo;
import Blogging.App.repositories.PostRepo;
import Blogging.App.repositories.UserRepo;
import Blogging.App.services.CommentService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public CommentDto createComment(
            CommentDto commentDto,
            Integer postId)
            throws ResourceNotFoundException {

        Post post = this.postRepo.findById(postId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Post",
                                "post Id",
                                postId
                        ));

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = this.userRepo.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User",
                                "email",
                                0
                        ));

        Comment comment = this.modelMapper
                .map(commentDto, Comment.class);

        comment.setPost(post);
        comment.setUser(user);

        Comment savedComment = this.commentRepo.save(comment);

        CommentDto response = this.modelMapper
                .map(savedComment, CommentDto.class);

        // Important: use the actual database-generated ID
        response.setId(savedComment.getId());

        response.setUserId(user.getId());
        response.setUserEmail(user.getEmail());
        response.setUsername(user.getDisplayName());

        return response;
    }
    @Override
    public void deleteComment(Integer commentId)
            throws ResourceNotFoundException {

        // Find comment
        Comment comment = this.commentRepo
                .findById(commentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Comment",
                                "commentId",
                                commentId
                        )
                );


        // Get currently logged-in user's email
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();


        // Find logged-in user
        User user = this.userRepo
                .findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User",
                                "email",
                                0
                        )
                );


        // Check ownership
        if (comment.getUser() == null ||
                comment.getUser().getId() != user.getId()) {

            throw new RuntimeException(
                    "You are not allowed to delete this comment."
            );
        }


        // Delete only if current user owns it
        this.commentRepo.delete(comment);
    }
}