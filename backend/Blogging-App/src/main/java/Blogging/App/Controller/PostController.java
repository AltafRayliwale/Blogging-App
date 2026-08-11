package Blogging.App.Controller;

import Blogging.App.config.AppConstants;
import Blogging.App.exceptions.ResourceNotFoundException;
import Blogging.App.payloads.ApiResponse;
import Blogging.App.payloads.PostDto;
import Blogging.App.payloads.PostResponse;
import Blogging.App.services.FileService;
import Blogging.App.services.PostService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

   @Autowired
    private FileService fileService;

   @Value("${project.image}")
   private String path;



    @PostMapping("/user/{userId}/category/{categoryId}/posts")
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto,
                                              @PathVariable Integer userId,
                                              @PathVariable Integer categoryId) throws ResourceNotFoundException {


        PostDto createPost = this.postService.createPost(postDto, userId, categoryId);
        return new ResponseEntity<PostDto>(createPost, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}/posts")
    public ResponseEntity<List<PostDto>> getPostsByUser(@PathVariable Integer userId)
            throws ResourceNotFoundException {
        List<PostDto> posts = this.postService.getPostsByUser(userId);
        return new ResponseEntity<List<PostDto>>(posts, HttpStatus.OK);

    }

    @GetMapping("/category/{categoryId}/posts")
    public ResponseEntity<List<PostDto>> getPostsByCategory(@PathVariable Integer categoryId)
            throws ResourceNotFoundException {
        List<PostDto> posts = this.postService.getPostsByCategory(categoryId);
        return new ResponseEntity<List<PostDto>>(posts, HttpStatus.OK);

    }

    @GetMapping("/")
    public ResponseEntity<PostResponse> getAllPosts(
        @RequestParam(value = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
        @RequestParam(value ="pageSize",defaultValue =AppConstants.PAGE_SIZE ,required=false) Integer pageSize,
        @RequestParam(value="sortBy" ,defaultValue=AppConstants.SORT_BY,required=false)String sortBy,
        @RequestParam(value="sortDir",defaultValue = AppConstants.SORT_DIR,required = false)String sortDir

        ){
        PostResponse postResponse= (PostResponse) this.postService.getAllPost(pageNumber, pageSize,sortBy,sortDir);
        return new ResponseEntity<PostResponse>(postResponse, HttpStatus.OK);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Integer postId) throws ResourceNotFoundException {
        PostDto postDto = this.postService.getPostById(postId);
        return new ResponseEntity<PostDto>(postDto, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public ApiResponse deletePost(@PathVariable Integer postId) throws ResourceNotFoundException {
        this.postService.deletePost(postId);
        return new ApiResponse("Post is Successfully deleted ", true);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable Integer postId) throws ResourceNotFoundException {
        PostDto updatePost = this.postService.updatePost(postDto, postId);
        return new ResponseEntity<PostDto>(updatePost, HttpStatus.OK);
    }
    @GetMapping("/search/{keywords}")
    public ResponseEntity<List<PostDto>> searchPostByTitle(@PathVariable("keywords") String keywords){
               List<PostDto> result= this.postService.searchPosts(keywords);
               return new ResponseEntity<List<PostDto>>(result, HttpStatus.OK);
        }

    @PostMapping("/image/{postId}")
    public ResponseEntity<PostDto> uploadPostImage(
            @RequestParam("image") MultipartFile image,
            @PathVariable Integer postId
    ) throws IOException, ResourceNotFoundException {
        PostDto postDto = this.postService.getPostById(postId);


        String fileName = this.fileService.uploadImage(path,image);
      postDto.setImageName(fileName);
      PostDto updatePost = this.postService.updatePost(postDto,postId);
      return new ResponseEntity<PostDto>(updatePost, HttpStatus.OK);
    }

    @GetMapping("/image/{imageName}")
    public void downloadImage(
            @PathVariable String imageName,
            HttpServletResponse response) throws IOException {

        InputStream resource = fileService.getResource(path, imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }

//    @GetMapping(value = "/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
//    public void downloadImage(@PathVariable("imageName") String imageName, HttpServletResponse response) throws IOException {
//        // Get the image resource
//        try (InputStream resource = this.fileService.getResource(path, imageName)) {
//            // Set the content type for the response
//            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
//
//            // Copy the image data to the response output stream
//            StreamUtils.copy(resource, response.getOutputStream());
//        } catch (IOException ex) {
//            // Handle the case where the image cannot be found or there is an error
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            response.getWriter().write("Error loading image: " + ex.getMessage());
//        }
    }




