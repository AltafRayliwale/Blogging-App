package Blogging.App.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import Blogging.App.entities.Like;
import Blogging.App.entities.Post;
import Blogging.App.entities.User;

import java.util.Optional;

public interface LikeRepo extends JpaRepository<Like, Integer> {

    // Check if a user has already liked a post
    Optional<Like> findByUserAndPost(User user, Post post);

    // Count total likes on a post
    long countByPost(Post post);

    // Delete a like (Unlike)
    void deleteByUserAndPost(User user, Post post);
}