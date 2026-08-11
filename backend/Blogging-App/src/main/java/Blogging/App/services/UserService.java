package Blogging.App.services;
import Blogging.App.exceptions.ResourceNotFoundException;
import Blogging.App.payloads.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    UserDto registerNewUser(UserDto userDto) throws ResourceNotFoundException;
    UserDto createUser(UserDto userDto);
    UserDto updateUser(UserDto user , Integer userId) throws ResourceNotFoundException;
    UserDto getUserById(Integer userId) throws ResourceNotFoundException;
    List<UserDto> getAllUsers();
    void deleteUser(Integer userId) throws ResourceNotFoundException;
    UserDto updateProfileImage(Integer userId, String fileName)
            throws ResourceNotFoundException;

}
