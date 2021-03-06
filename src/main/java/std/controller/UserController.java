package std.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import std.entitty.User;
import std.repository.UserRepository;
import std.security.auth.TokenAuthentication;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> users() {
        return userRepository.findAll();
    }

    @GetMapping(path = "/hello", headers = HttpHeaders.AUTHORIZATION)
    public String hello() {
        var authentication = (TokenAuthentication) SecurityContextHolder.getContext().getAuthentication();

        return String.format(
                "hello %s\nthere is your roles: %s",
                authentication.getPrincipal(),
                authentication.getAuthorities()
        );
    }

    @PostMapping
    public User saveUser(@RequestBody User user) {
        return userRepository.save(user);
    }
}
