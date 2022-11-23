package chapter.one.LearnSpringBoot.converter;


import chapter.one.LearnSpringBoot.dto.RegisterRequestDto;
import chapter.one.LearnSpringBoot.entities.Role;
import chapter.one.LearnSpringBoot.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Component
public final class UserDtoConverter {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private ModelMapper modelMapper;

    public User userRegisterDtoToUser(RegisterRequestDto userRegister, @NotNull Role userRole) {
        User user = modelMapper.map(userRegister, User.class); // Default mapping e.g. email to email
        user.setPassword(encoder.encode(user.getPassword())); // Need to encode before saving to db
        user.setAuthorities(Set.of(userRole));
        return user;
    }
}
