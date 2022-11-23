package chapter.one.LearnSpringBoot.controllers;

import chapter.one.LearnSpringBoot.auth.AuthService;
import chapter.one.LearnSpringBoot.dto.LoginRequestDto;
import chapter.one.LearnSpringBoot.dto.RegisterRequestDto;
import chapter.one.LearnSpringBoot.dto.UserResponseDto;
import chapter.one.LearnSpringBoot.dto.UserRoleSetup;
import chapter.one.LearnSpringBoot.entities.User;
import chapter.one.LearnSpringBoot.exceptions.BaseCustomException;
import chapter.one.LearnSpringBoot.services.TestDataSetupService;
import chapter.one.LearnSpringBoot.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private TestDataSetupService setupService;

    @Autowired
    private AuthService authService;

    @GetMapping
    public ResponseEntity<List<User>> getUsers(
            @RequestParam(name="limit", required=false, defaultValue = "100") int limit,
            @RequestParam(name="skip", required=false, defaultValue = "0") int skip
    ) {
        try {
            return new ResponseEntity<>(userService.getUsers(limit, skip), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(List.of(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/test/setup/roles")
    public ResponseEntity<UserRoleSetup> setupRoleData() {
        try {
            return new ResponseEntity<>(setupService.initRoles(), HttpStatus.OK);
        } catch (Exception e) {
            UserRoleSetup setup = new UserRoleSetup();
            setup.setMsg(e.getMessage());
            return new ResponseEntity<>(setup, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/test/setup/all")
    public ResponseEntity<UserRoleSetup> setupUserRoleData() {
        try {
            return new ResponseEntity<>(setupService.init(), HttpStatus.OK);
        } catch (Exception e) {
            UserRoleSetup setup = new UserRoleSetup();
            setup.setMsg(e.getMessage());
            return new ResponseEntity<>(setup, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody LoginRequestDto userLogin) {
        try {
            User user = userService.login(userLogin);
            HttpHeaders responseHeaders = new HttpHeaders();
            authService.addTokenToHeaders(responseHeaders, user.getUsername());
            return ResponseEntity.ok().headers(responseHeaders).body(new UserResponseDto(user, true));
        } catch (BadCredentialsException e) {
            logger.error("Invalid credentials");
            return new ResponseEntity<>(new UserResponseDto(new BaseCustomException("Invalid credentials", 40001)), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            logger.error("Failed to login", e.getCause());
            return new ResponseEntity<>(new UserResponseDto(new BaseCustomException("Failed to login", 50000)), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody RegisterRequestDto userRegister) {
        try {
            User user = userService.register(userRegister);
            return new ResponseEntity<>(new UserResponseDto(user, true), HttpStatus.OK);
        } catch (BaseCustomException e) {
            logger.error("Failed to process {}", e.getMessage());
            return new ResponseEntity<>(new UserResponseDto(e), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ConstraintViolationException e) {
            logger.error("Invalid input");
            String errorMsg = e.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .filter(msg -> !msg.isEmpty())
                    // .sorted()
                    .collect(Collectors.joining(". "));
            return new ResponseEntity<>(new UserResponseDto(new BaseCustomException(errorMsg, 400232)), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Failed to register", e.getCause());
            return new ResponseEntity<>(new UserResponseDto(new BaseCustomException("Failed to register", 50000)), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
