package chapter.one.LearnSpringBoot.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Stream;

@Component
public class AuthFilter extends OncePerRequestFilter {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserDetailsService userDetailsService;

    private static final String LOGIN = "/user/login";
    private static final String REGISTER = "/user/register";
    private static final String ROLES_SETUP = "/user/test/setup/roles";

    // https://stackoverflow.com/questions/58938733/unable-to-skip-the-onceperrequestfilter-filter-for-a-login-url-basically-to-get
    // https://stackoverflow.com/questions/52370411/springboot-bypass-onceperrequestfilter-filters/52370780#52370780
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return Stream.of(LOGIN, REGISTER, ROLES_SETUP)
                .anyMatch(val -> new AntPathMatcher().match(val,request.getRequestURI()));
    }

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
            logger.error("Cannot authenticate user: ", e);
        }
        filterChain.doFilter(request, response);
    }

    public String getLogin() {
        return LOGIN;
    }

    public String getRegister() {
        return REGISTER;
    }

    public String getRolesSetup() {
        return ROLES_SETUP;
    }
}
