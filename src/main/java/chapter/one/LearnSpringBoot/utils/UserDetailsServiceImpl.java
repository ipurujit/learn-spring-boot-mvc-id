package chapter.one.LearnSpringBoot.utils;

import chapter.one.LearnSpringBoot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// This is a utility service because it is autowired in Security configuration
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (email == null) {
            throw new UsernameNotFoundException("Invalid email!");
        }
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user associated with given email!"));
    }
}
