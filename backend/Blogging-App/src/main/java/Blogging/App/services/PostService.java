package Blogging.App.services;

import Blogging.App.entities.Post;
import Blogging.App.exceptions.ResourceNotFoundException;
import Blogging.App.payloads.PostDto;
import Blogging.App.payloads.PostResponse;


import java.util.List;

public interface PostService {

    PostDto createPost(PostDto post, int userId , int categoryId ) throws ResourceNotFoundException;

    PostDto updatePost(PostDto postDto  , int postId) throws ResourceNotFoundException;

    void deletePost(int postId) throws ResourceNotFoundException;

    PostResponse getAllPost(Integer pageNumber, Integer pageSize,String sortBy,String sortDir);

    PostDto getPostById(int postId) throws ResourceNotFoundException;

    List<PostDto> getPostsByCategory(Integer categoryId) throws ResourceNotFoundException;

    List<PostDto> getPostsByUser(Integer userId) throws ResourceNotFoundException;

    List<PostDto> searchPosts(String keyword);


}
