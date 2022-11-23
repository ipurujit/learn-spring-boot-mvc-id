package chapter.one.LearnSpringBoot.config;

import chapter.one.LearnSpringBoot.auth.AuthEntryPoint;
import chapter.one.LearnSpringBoot.auth.AuthFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // why do we need this?
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http, AuthFilter authFilter, AuthEntryPoint authEntryPoint
    ) throws Exception {
        http
            .csrf()
            .disable()
            .exceptionHandling().authenticationEntryPoint(authEntryPoint).and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests()
            .antMatchers(HttpMethod.POST,
                    "/user/login", "/user/register", "/user/test/setup/roles")
            .permitAll()
            .antMatchers(HttpMethod.OPTIONS,
                    "/user/login", "/user/register", "/user/test/setup/roles")
            .permitAll()
//            .antMatchers("/**")
//            .hasAuthority("USER")
            .anyRequest()
            .authenticated();

        http.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
