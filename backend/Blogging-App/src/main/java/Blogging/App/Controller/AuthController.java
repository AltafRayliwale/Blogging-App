package Blogging.App.Controller;

import Blogging.App.exceptions.ApiException;
import Blogging.App.exceptions.ResourceNotFoundException;
import Blogging.App.payloads.JwtAuthRequest;
import Blogging.App.payloads.JwtAuthResponse;
import Blogging.App.payloads.UserDto;
import Blogging.App.security.JWTTokenHelper;
import Blogging.App.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private JWTTokenHelper jwtTokenHelper;
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest authRequest) {

        this.authenticate(authRequest.getUsername(), authRequest.getPassword());
      UserDetails userDetails =  this.userDetailsService.loadUserByUsername(authRequest.getUsername());
      String generateToken = this.jwtTokenHelper.generateToken(userDetails);
      JwtAuthResponse response = new JwtAuthResponse();
      response.setToken(generateToken);
      return new ResponseEntity<JwtAuthResponse>(response,HttpStatus.OK);

    }

    private void authenticate(String username, String password) {
      UsernamePasswordAuthenticationToken authenticationToken =new UsernamePasswordAuthenticationToken(username, password);
     try{
         this.authenticationManager.authenticate(authenticationToken);

     }
     catch (BadCredentialsException e) {
         System.out.println("Invalid Details !!");
         throw new ApiException("Invalid username or password");

     }


    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) throws ResourceNotFoundException {
      UserDto registeredUser =  this.userService.registerNewUser(userDto);
      return new ResponseEntity<UserDto>(registeredUser, HttpStatus.CREATED);
    }


}
