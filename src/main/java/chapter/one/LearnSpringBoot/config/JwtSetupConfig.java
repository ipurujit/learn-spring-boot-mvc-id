package chapter.one.LearnSpringBoot.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
@NoArgsConstructor
public class JwtSetupConfig {
    private int expiration;
    private String secret;
    private String prefix;
    private String algorithm;
}
