package Blogging.App.services;

import Blogging.App.exceptions.ResourceNotFoundException;

public interface LikeService {

    void likePost(Integer postId, Integer userId) throws ResourceNotFoundException;

    void unlikePost(Integer postId, Integer userId) throws ResourceNotFoundException;

    long getLikeCount(Integer postId) throws ResourceNotFoundException;

    boolean hasUserLikedPost(Integer postId, Integer userId) throws ResourceNotFoundException;
}