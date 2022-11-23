package chapter.one.LearnSpringBoot.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthFilter extends OncePerRequestFilter {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try {
            String userEmail = authService.getUsernameFromToken(request);
            // If email
            UserDetails user = userDetailsService.loadUserByUsername(userEmail);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            // authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            logger.error("Cannot authenticate user: {}", e);
        }
        filterChain.doFilter(request, response);
    }


}
