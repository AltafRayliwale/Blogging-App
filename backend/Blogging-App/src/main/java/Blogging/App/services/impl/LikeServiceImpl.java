package Blogging.App.services.impl;


import Blogging.App.entities.Like;
import Blogging.App.entities.Post;
import Blogging.App.entities.User;
import Blogging.App.exceptions.ResourceNotFoundException;
import Blogging.App.repositories.LikeRepo;
import Blogging.App.repositories.PostRepo;
import Blogging.App.repositories.UserRepo;
import Blogging.App.services.LikeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeRepo likeRepo;

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private UserRepo userRepo;

    @Override
    public void likePost(Integer postId, Integer userId) throws ResourceNotFoundException {

        Post post = postRepo.findById(postId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post", "Post Id", postId));

        User user = userRepo.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "User Id", userId));

        // Prevent duplicate likes
        if (likeRepo.findByUserAndPost(user, post).isPresent()) {
            return;
        }

        Like like = new Like();

        like.setUser(user);
        like.setPost(post);

        likeRepo.save(like);
    }

    @Override
    public void unlikePost(Integer postId, Integer userId) throws ResourceNotFoundException {

        Post post = postRepo.findById(postId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post", "Post Id", postId));

        User user = userRepo.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "User Id", userId));

        likeRepo.deleteByUserAndPost(user, post);
    }

    @Override
    public long getLikeCount(Integer postId) throws ResourceNotFoundException {

        Post post = postRepo.findById(postId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post", "Post Id", postId));

        return likeRepo.countByPost(post);
    }

    @Override
    public boolean hasUserLikedPost(Integer postId, Integer userId) throws ResourceNotFoundException {

        Post post = postRepo.findById(postId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post", "Post Id", postId));

        User user = userRepo.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "User Id", userId));

        return likeRepo.findByUserAndPost(user, post).isPresent();
    }
}