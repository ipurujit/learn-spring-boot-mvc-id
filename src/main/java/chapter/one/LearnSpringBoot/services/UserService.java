package chapter.one.LearnSpringBoot.services;

import chapter.one.LearnSpringBoot.converter.UserDtoConverter;
import chapter.one.LearnSpringBoot.dto.LoginRequestDto;
import chapter.one.LearnSpringBoot.dto.RegisterRequestDto;
import chapter.one.LearnSpringBoot.entities.Role;
import chapter.one.LearnSpringBoot.entities.User;
import chapter.one.LearnSpringBoot.enums.RoleNameEnum;
import chapter.one.LearnSpringBoot.exceptions.RoleNotFoundException;
import chapter.one.LearnSpringBoot.exceptions.UserAlreadyExistsException;
import chapter.one.LearnSpringBoot.repositories.RoleRepository;
import chapter.one.LearnSpringBoot.repositories.UserRepository;
import chapter.one.LearnSpringBoot.utils.PagingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private PagingUtil pagingUtil;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager auth;

    @Autowired
    private UserDtoConverter userDtoConverter;

    public List<User> getUsers(int limit, int skip) {
        limit = pagingUtil.formatLimit(limit);
        skip = pagingUtil.formatSkip(skip);
        return userRepository.findAll(Pageable.ofSize(limit).withPage(skip / limit)).toList();
    }

    public User login(LoginRequestDto userLogin) {
        return (User) auth.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLogin.getEmail(), userLogin.getPassword()
                )
        ).getPrincipal();
    }

    public User register(RegisterRequestDto userRegister) throws UserAlreadyExistsException, RoleNotFoundException {
        // Check if user already exists (email)
        if (userRepository.countByEmail(userRegister.getEmail()) != 0) {
            throw new UserAlreadyExistsException("This email is already registered." +
                    " Please proceed to login if it's your email, otherwise try another one.");
        }
        // Create
        Role userRole = roleRepository.findByRoleName(RoleNameEnum.USER)
                .orElseThrow(() -> new RoleNotFoundException("User default permission failed to initialize", 50003));
        return userRepository.saveAndFlush(userDtoConverter.userRegisterDtoToUser(userRegister, userRole));
    }

}
