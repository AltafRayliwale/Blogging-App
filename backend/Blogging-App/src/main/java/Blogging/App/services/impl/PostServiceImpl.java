package Blogging.App.services.impl;

import Blogging.App.entities.Category;
import Blogging.App.entities.Post;
import Blogging.App.entities.User;
import Blogging.App.exceptions.ResourceNotFoundException;
import Blogging.App.payloads.PostDto;
import Blogging.App.payloads.PostResponse;
import Blogging.App.repositories.CategoryRepo;
import Blogging.App.repositories.PostRepo;
import Blogging.App.repositories.UserRepo;
import Blogging.App.services.PostService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Override
    public PostDto createPost(PostDto postDto, int userId, int categoryId) throws ResourceNotFoundException {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "User id", userId));

        Category category = this.categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Category id", categoryId));

        Post post = this.modelMapper.map(postDto, Post.class);

        post.setImageName("default.png");
        post.setAddedDate(new Date());
        post.setUser(user);
        post.setCategory(category);

        Post newPost = this.postRepo.save(post);

        return this.postToDto(newPost);
    }

    @Override
    public PostDto updatePost(PostDto postDto, int postId) throws ResourceNotFoundException {

        Post post = this.postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "Post id", postId));

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setImageName(postDto.getImageName());

        Post updatedPost = this.postRepo.save(post);

        return this.postToDto(updatedPost);
    }

    @Override
    public void deletePost(int postId) throws ResourceNotFoundException {

        Post post = this.postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "Post id", postId));

        this.postRepo.delete(post);
    }

    @Override
    public PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Post> pagePost = this.postRepo.findAll(pageable);

        List<PostDto> postDtos = pagePost.getContent()
                .stream()
                .map(this::postToDto)
                .collect(Collectors.toList());

        PostResponse response = new PostResponse();
        response.setContent(postDtos);
        response.setPageNumber(pagePost.getNumber());
        response.setPageSize(pagePost.getSize());
        response.setTotalElements((int) pagePost.getTotalElements());
        response.setTotalPages(pagePost.getTotalPages());
        response.setLastPage(pagePost.isLast());

        return response;
    }

    @Override
    public PostDto getPostById(int postId) throws ResourceNotFoundException {

        Post post = this.postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "Post id", postId));

        return this.postToDto(post);
    }

    @Override
    public List<PostDto> getPostsByCategory(Integer categoryId) throws ResourceNotFoundException {

        Category category = this.categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Category id", categoryId));

        List<Post> posts = this.postRepo.findByCategory(category);

        return posts.stream()
                .map(this::postToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDto> getPostsByUser(Integer userId) throws ResourceNotFoundException {

        userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "User id", userId));

        List<Post> posts = this.postRepo.findByUser_Id(userId);

        return posts.stream()
                .map(this::postToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDto> searchPosts(String keywords) {

        List<Post> posts = this.postRepo.findByTitleContaining(keywords);

        return posts.stream()
                .map(this::postToDto)
                .collect(Collectors.toList());
    }

    // ===========================
    // Custom Post -> PostDto Mapper
    // ===========================
    private PostDto postToDto(Post post) {

        PostDto postDto = this.modelMapper.map(post, PostDto.class);

        // Post author display name
        if (postDto.getUser() != null && post.getUser() != null) {
            postDto.getUser().setUsername(
                    post.getUser().getDisplayName()
            );
        }

        // Add comment user information
        if (post.getComments() != null && postDto.getComments() != null) {

            post.getComments().forEach(comment -> {

                postDto.getComments().stream()
                        .filter(dto -> dto.getId() == comment.getId())
                        .findFirst()
                        .ifPresent(commentDto -> {

                            if (comment.getUser() != null) {

                                commentDto.setUserId(
                                        comment.getUser().getId()
                                );

                                commentDto.setUserEmail(
                                        comment.getUser().getEmail()
                                );

                                commentDto.setUsername(
                                        comment.getUser().getDisplayName()
                                );
                            }
                        });
            });
        }

        return postDto;
    }
}