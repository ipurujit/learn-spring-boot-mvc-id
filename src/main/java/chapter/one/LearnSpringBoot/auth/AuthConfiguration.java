package chapter.one.LearnSpringBoot.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthConfiguration {

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration auth
    ) throws Exception {
        return auth.getAuthenticationManager();
    }

    @Autowired
    public void configureGlobal(
            AuthenticationManagerBuilder authenticationManagerBuilder,
            UserDetailsService userService, // This is implemented in util class
            PasswordEncoder passwordEncoder
    ) throws Exception {
        authenticationManagerBuilder.userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
    }
}
