package Blogging.App.services;

import Blogging.App.exceptions.ResourceNotFoundException;
import Blogging.App.payloads.CommentDto;

public interface CommentService {

    CommentDto createComment(CommentDto comment, Integer postId)
            throws ResourceNotFoundException;

    void deleteComment(Integer commentId)
            throws ResourceNotFoundException;
}