package chapter.one.LearnSpringBoot.services;

import chapter.one.LearnSpringBoot.dto.UserRoleSetup;
import chapter.one.LearnSpringBoot.entities.Role;
import chapter.one.LearnSpringBoot.entities.User;
import chapter.one.LearnSpringBoot.enums.RoleNameEnum;
import chapter.one.LearnSpringBoot.repositories.RoleRepository;
import chapter.one.LearnSpringBoot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class TestDataSetupService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    public UserRoleSetup init() throws Exception {
        UserRoleSetup setup = new UserRoleSetup();
        setup.setRoles(new LinkedList<>());
        setup.setUsers(new LinkedList<>());
        RoleNameEnum[] roleNames = RoleNameEnum.values();
        for (RoleNameEnum roleName : roleNames) {
            // Create a role
            Role role = saveRoleByRoleName(roleName);
            // Create a user for that role
            User user = saveUserByRole(role);
            setup.getRoles().add(role);
            setup.getUsers().add(user);
        }
        return setup;
    }

    public UserRoleSetup initRoles() throws Exception {
        UserRoleSetup setup = new UserRoleSetup();
        setup.setRoles(new LinkedList<>());
        RoleNameEnum[] roleNames = RoleNameEnum.values();
        for (RoleNameEnum roleName : roleNames) {
            // Create a role
            Role role = saveRoleByRoleName(roleName);
            setup.getRoles().add(role);
        }
        return setup;
    }

    public UserRoleSetup initUsers() throws Exception {
        UserRoleSetup setup = new UserRoleSetup();
        setup.setRoles(roleRepository.findAll());
        setup.setUsers(new LinkedList<>());
        for (Role role : setup.getRoles()) {
            // Create a user for that role
            User user = saveUserByRole(role);
            setup.getUsers().add(user);
        }
        return setup;
    }

    public String convertRoleToEmail(String roleNameString) {
        roleNameString = roleNameString.toLowerCase();
        return roleNameString+"@"+roleNameString+".com";
    }

    public Role saveRoleByRoleName(RoleNameEnum roleName) {
        Role role = new Role();
        role.setRoleName(roleName);
        return roleRepository.saveAndFlush(role);
    }

    public User saveUserByRole(Role role) throws Exception {
        if (role == null) {
            throw new Exception("Role not found!");
        }
        User user = new User();
        String roleNameStr = role.getRoleName().name();
        user.setFullName("I AM "+roleNameStr);
        user.setEmail(convertRoleToEmail(roleNameStr));
        user.setPassword(encoder.encode(user.getEmail()));
        user.setDateOfBirth(LocalDate.of(2000, 1, 1));
        user.setPhoneNumber("+918104524524");
        user.setAuthorities(Set.of(role));
        return userRepository.saveAndFlush(user);
    }


}
