package Blogging.App.services.impl;

import Blogging.App.config.AppConstants;
import Blogging.App.entities.Role;
import Blogging.App.entities.User;
import Blogging.App.payloads.UserDto;
import Blogging.App.repositories.RoleRepo;
import Blogging.App.repositories.UserRepo;
import Blogging.App.services.UserService;
import Blogging.App.exceptions.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.stream.Collectors;
@Transactional
@Service
public class UserServiceimpl implements UserService {


    @Autowired
    private RoleRepo roleRepo;

    private final UserRepo userRepo;

    @Autowired
    private final ModelMapper modelMapper ;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceimpl(UserRepo userRepo, ModelMapper modelMapper) {
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
    }


    @Override
    public UserDto registerNewUser(UserDto userDto) throws ResourceNotFoundException {
        // Map userDto to User entity
        User user = this.modelMapper.map(userDto, User.class);

        // Encode the password before saving
        user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));

        // Fetch the role by ID, if not found, throw a custom ResourceNotFoundException
        Role role = this.roleRepo.findById(AppConstants.NORMAL_USER)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Role",                  // resourceName
                        "id",                     // fieldName (in this case, the ID field)
                        AppConstants.NORMAL_USER // fieldValue (ID value)
                ));

        // Check if the user already has the role to avoid duplication
        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);  // Add the role if not already assigned
        }

        // Save the user to the database
        User newUser = this.userRepo.save(user);

        // Map the saved user entity back to UserDto and return it
        return this.modelMapper.map(newUser, UserDto.class);
    }



    @Override
    public UserDto createUser(UserDto userDto) {
     User user=this.dtoToUser(userDto);
     User savedUser=this.userRepo.save(user);
     return this.userToDto(savedUser);

    }

    @Override
    public UserDto updateUser(UserDto user, Integer userId) throws ResourceNotFoundException {
       User user1 = this.userRepo.findById(userId)
               .orElseThrow(() -> new ResourceNotFoundException("User"," Id ", userId));

         user1.setUsername(user.getUsername());
         user1.setEmail(user.getEmail());
         user1.setPassword(this.passwordEncoder.encode(user.getPassword()));
         user1.setAbout(user.getAbout());

         User updateUser=this.userRepo.save(user1);
         UserDto userDto1=this.userToDto(updateUser);
       return userDto1;
    }
    
    
    @Override
    public UserDto updateProfileImage(Integer userId, String fileName)
            throws ResourceNotFoundException {

        User user = this.userRepo.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "Id", userId));

        user.setProfileImage(fileName);

        User savedUser = this.userRepo.save(user);

        return this.userToDto(savedUser);
    }
    

    @Override
    public UserDto getUserById(Integer userId) throws ResourceNotFoundException {

        User user=userRepo.findById(userId)
                .orElseThrow(() ->new ResourceNotFoundException("user"," Id ",userId));

        return this.userToDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users=this.userRepo.findAll();

   List<UserDto> userDtos = users.stream().map(user->this.userToDto(user)).collect(Collectors.toList());

    return userDtos;
    }

    @Override
    public void deleteUser(Integer userId) throws ResourceNotFoundException {

       User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","Id",userId));
             this.userRepo.delete(user);
    }

    private User dtoToUser(UserDto userDto){

        User user = this.modelMapper.map(userDto, User.class);
        user.setProfileImage(userDto.getProfileImage());
        return user;
//        User us= new User();
//        us.setId(users.getId());
//        us.setUsername(users.getUsername());
//        us.setPassword(users.getPassword());
//        us.setEmail(users.getEmail());
//        us.setAbout(users.getAbout());

    }

    public UserDto userToDto(User user) {

        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setUsername(user.getDisplayName());   // <-- Display name
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setAbout(user.getAbout());
        userDto.setRoles(user.getRoles());
        userDto.setProfileImage(user.getProfileImage());

        return userDto;
    }


    
}

