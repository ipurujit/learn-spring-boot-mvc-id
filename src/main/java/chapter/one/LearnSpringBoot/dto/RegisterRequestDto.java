package chapter.one.LearnSpringBoot.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterRequestDto {
    private String email;
    private String password;
    private LocalDate dateOfBirth;
    private String fullName;
    private String phoneNumber;

    public String getEmail() {
        return this.email == null ? null : this.email.toLowerCase();
    }
}
