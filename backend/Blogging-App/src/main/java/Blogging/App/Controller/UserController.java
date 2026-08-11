package Blogging.App.Controller;

import Blogging.App.services.FileService;
import org.springframework.beans.factory.annotation.Value;
import Blogging.App.exceptions.ResourceNotFoundException;
import Blogging.App.payloads.ApiResponse;
import Blogging.App.payloads.UserDto;
import Blogging.App.services.UserService;
import jakarta.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api")
public class UserController {


    @Autowired
    private final UserService userService;
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/")
    public ResponseEntity<UserDto> createUser(@Validated @RequestBody UserDto userDto) {
        UserDto createUserDto = this.userService.createUser(userDto); // Pass the userDto instance
        return new ResponseEntity<>(createUserDto, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@Validated @RequestBody UserDto userDto, @PathVariable("userId") Integer uid) throws ResourceNotFoundException {
       UserDto updatedUser =  this.userService.updateUser(userDto,uid);
        return ok(updatedUser);
    }

    @PreAuthorize("hasRole(ADMIN)")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId") Integer uid) throws ResourceNotFoundException {
       this.userService.deleteUser(uid);
       return new ResponseEntity<ApiResponse>(new ApiResponse("User Deleted Succcessfully",true),HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers()
    {
        return ResponseEntity.ok(this.userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getById(@PathVariable Integer userId) throws ResourceNotFoundException {
        return ResponseEntity.ok(this.userService.getUserById(userId));
    }
    
    @PostMapping("/image/{userId}")
    public ResponseEntity<UserDto> uploadUserImage(
            @RequestParam("image") MultipartFile image,
            @PathVariable Integer userId
    ) throws IOException, ResourceNotFoundException {

    	String fileName = this.fileService.uploadImage(path, image);

    	UserDto updatedUser =
    	        this.userService.updateProfileImage(userId, fileName);

    	return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
    
    @GetMapping(value = "/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadUserImage(
            @PathVariable String imageName,
            HttpServletResponse response
    ) throws IOException {

        InputStream resource = this.fileService.getResource(path, imageName);

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);

        StreamUtils.copy(resource, response.getOutputStream());
    }
    
    

}
