package chapter.one.LearnSpringBoot.repositories;

import chapter.one.LearnSpringBoot.entities.Role;
import chapter.one.LearnSpringBoot.entities.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String EMAIL = "abc@abc.abc";

    private User user;

    @BeforeEach
    void setUpUser() {
        user = new User();
    }

    @Test
    void test001CreateUser() {
        user.setEmail(EMAIL.toUpperCase());
        user.setPassword(passwordEncoder.encode(EMAIL));
        user.setPhoneNumber("+919876543210");
        user.setDateOfBirth(LocalDate.of(2000, 10, 10));
        user.setFullName("ANIKA MORARBAJI");
        user = userRepository.saveAndFlush(user);
        System.out.println(user.toStringCustom());
        assertNotNull(user.getId());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
    }

    @Test
    void test002FindByEmail() {
        assertNotNull(userRepository.findByEmail(EMAIL).orElseThrow());
    }

    @Test
    void test003DeleteUser() {
        user = userRepository.findByEmail(EMAIL).orElseThrow();
        System.out.println(user.toStringCustom());
        userRepository.delete(user);
        assertThrows(Exception.class, () -> userRepository.findByEmail(EMAIL).orElseThrow());
    }
}