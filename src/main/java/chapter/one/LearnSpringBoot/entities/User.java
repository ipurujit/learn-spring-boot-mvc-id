package chapter.one.LearnSpringBoot.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(schema = "auth", name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull // This checks before db save
    private Long id;

    @Column(unique = true, nullable = false, length = 254)
    @Size(min = 5, max = 254, message = "")
    @ColumnTransformer(write = "LOWER(?)") // This means SQL query to write will wrap email in LOWER()
    // But a setEmail operation will still get the one passed until reloaded from backend
    // So we need to override getter and setter
    // E.g. setEmail("ABC") will still show "ABC" as email on getEmail even after db write
    @Email(message = "Please provide a valid email")
    @NotNull(message = "") // This checks before db save
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    @Size(min = 8, max = 192, message = "Password must contain at least 8 characters")
    @NotNull(message = "Please provide a password") // This checks before db save
    private String password;

    @Column(nullable = false)
    @NotNull(message = "Please provide a valid date of birth in format:" +
            " YYYY-MM-DD e.g., 2000-12-25") // This checks before db save
    private LocalDate dateOfBirth;

    @Column(nullable = false, length = 300)
    @Size(min = 1, max = 300, message = "Full name must contain " +
            "one or more characters not exceeding length of 300")
    @NotNull(message = "Please provide your full name") // This checks before db save
    private String fullName;

    @Column(nullable = false, length = 15)
    // @Size(min = 11, max = 20, message = "") // Regex will take care of length
    @Pattern(regexp = "^\\+[1-9]\\d{0,2}[1-9]\\d{9,11}$",
            message = "Please provide a phone number in standard format: " +
                    "'+<country code><phone number>' with no extra 0, space or dash," +
                    " e.g., +19876543210")
    @NotNull // This checks before db save
    private String phoneNumber; // E.164 format like [+][country code][subscriber no.]

    // @ManyToMany(targetEntity = Role.class, cascade = {CascadeType.PERSIST, CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH})
    @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER) // Keep it lazy?
    @JoinTable(
            schema = "auth",
            name = "user_roles", // Keep table names plural
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<GrantedAuthority> authorities;

    @Column(nullable = false)
    private Boolean accountNonExpired = true; // Non Expired

    @Column(nullable = false)
    private Boolean accountNonLocked = true; // Non Locked

    @Column(nullable = false)
    private Boolean enabled = true; // is Enabled

    @Column(nullable = false)
    private Boolean credentialsNonExpired = true; // is credentials valid

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @CreationTimestamp
    private ZonedDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @UpdateTimestamp
    private ZonedDateTime updatedAt;

    public String getEmail() {
        return this.email.toLowerCase();
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return this.getId().equals(user.getId()) || this.getEmail().equals(user.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String toStringCustom() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", fullName='" + fullName + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                "}\n";
    }


    /*
    THERE ARE LIFECYCLE EVENTS FOR JPA ENTITY
    https://docs.oracle.com/cd/E13189_01/kodo/docs41/full/html/ejb3_overview_pc_callbacks.html
     */
}
