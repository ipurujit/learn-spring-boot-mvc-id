package chapter.one.LearnSpringBoot.entities;

import chapter.one.LearnSpringBoot.enums.RoleNameEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Entity
@Table(schema = "auth", name = "roles")
@NoArgsConstructor
@Getter
@Setter
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Please provide a valid role name")
    private RoleNameEnum roleName;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @CreationTimestamp
    private ZonedDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @UpdateTimestamp
    private ZonedDateTime updatedAt;

    @Override
    public String getAuthority() {
        return this.roleName.name();
    }
}
