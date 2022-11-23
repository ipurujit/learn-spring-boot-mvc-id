package chapter.one.LearnSpringBoot.auth;

import chapter.one.LearnSpringBoot.config.JwtSetupConfig;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class AuthService {

    @Autowired
    private JwtSetupConfig jwtSetupConfig;

    private SecretKeySpec key;

    public SecretKeySpec getKey() {
        if (key == null) {
            key = new SecretKeySpec(jwtSetupConfig.getSecret().getBytes(), jwtSetupConfig.getAlgorithm());
        }
        return key;
    }

    private static final String AUTH_HEADER = "Authorization";
    private static final String AUTH_EXPOSE_HEADER = "Access-Control-Expose-Headers";

    // This will set the token in login response?
    public String generateToken(String username) {
        return Jwts
                .builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + jwtSetupConfig.getExpiration()))
                .signWith(getKey())
                .compact();
    }

    public void addToken(HttpServletResponse response, String username) {
        // Add to response header
        response.addHeader(AUTH_HEADER, jwtSetupConfig.getPrefix()+" "+generateToken(username));
        response.addHeader(AUTH_EXPOSE_HEADER, AUTH_HEADER);
    }

    public void addTokenToHeaders(HttpHeaders responseHeaders, String username) {
        // Add to response header
        responseHeaders.add(AUTH_HEADER, jwtSetupConfig.getPrefix()+" "+generateToken(username));
        responseHeaders.add(AUTH_EXPOSE_HEADER, AUTH_HEADER);
    }

    public String getUsernameFromToken(HttpServletRequest request) {
        String token = request.getHeader(AUTH_HEADER);
        if (token != null && token.startsWith(jwtSetupConfig.getPrefix())) {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token.replace(jwtSetupConfig.getPrefix(), ""))
                    .getBody()
                    .getSubject();
        }
        return null;
    }

}
