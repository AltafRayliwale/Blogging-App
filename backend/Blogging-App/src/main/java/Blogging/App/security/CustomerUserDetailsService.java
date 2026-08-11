package Blogging.App.security;

import Blogging.App.entities.User;
import Blogging.App.exceptions.ResourceNotFoundException;
import Blogging.App.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

   @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user;

        try {
            user = this.userRepo.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User", "email : " + username, 0));
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }

        return user;
    }
}
