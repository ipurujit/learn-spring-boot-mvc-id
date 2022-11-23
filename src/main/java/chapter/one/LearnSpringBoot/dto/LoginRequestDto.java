package chapter.one.LearnSpringBoot.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String email;
    private String password;

    public String getEmail() {
        return this.email == null ? null : this.email.toLowerCase();
    }
}
