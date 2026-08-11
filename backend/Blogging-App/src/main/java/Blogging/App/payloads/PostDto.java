package Blogging.App.payloads;

import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import Blogging.App.entities.Category;
import Blogging.App.entities.Comment;
import Blogging.App.entities.User;

public class PostDto {
    private String title;

    private String content;

    private String imageName;

    private Date addedDate;

    private Integer postId;
    
  

    private Set<CommentDto> comments = new HashSet<>();


    public Set<CommentDto> getComments() {
        return comments;
    }

    public void setComments(Set<CommentDto> comments) {
        this.comments = comments;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public CategoryDto getCategory() {
        return category;
    }

    public void setCategory(CategoryDto category) {
        this.category = category;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    private UserDto user;
    private CategoryDto category;

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }



    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
