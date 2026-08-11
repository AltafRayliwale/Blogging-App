package Blogging.App.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import Blogging.App.entities.Comment;

public interface CommentRepo extends JpaRepository<Comment, Integer> {
}
