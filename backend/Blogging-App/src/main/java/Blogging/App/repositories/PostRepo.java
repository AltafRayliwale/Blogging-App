package Blogging.App.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Blogging.App.entities.Category;
import Blogging.App.entities.Post;
import Blogging.App.entities.User;

import java.util.List;

public interface PostRepo extends JpaRepository<Post, Integer> {

    List<Post> findByUser(User user);

    // Query by userId (integer, assuming user_id is the foreign key)
    List<Post> findByUser_Id(Integer userId);

    // Query by Category entity
    List<Post> findByCategory(Category category);
//    @Query("select p from Post p where p.title like :key")
//    List<Post> findByTitle(@Param("key") String title);
    List<Post> findByTitleContaining(String title);

}
