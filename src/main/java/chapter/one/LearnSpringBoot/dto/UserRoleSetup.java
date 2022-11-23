package chapter.one.LearnSpringBoot.dto;

import chapter.one.LearnSpringBoot.entities.Role;
import chapter.one.LearnSpringBoot.entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class UserRoleSetup {
    List<User> users;
    List<Role> roles;
    String msg;
}
