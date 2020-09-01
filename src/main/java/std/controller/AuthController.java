package std.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import std.dto.AuthRequest;
import std.dto.AuthResponse;
import std.entitty.User;
import std.repository.UserRepository;

import java.util.Objects;
import java.util.Optional;

@RestController
public class AuthController {

    @Value("${auth.server.url.generate}")
    private String generateUrl;

    @Value("${auth.server.url.refresh}")
    private String refreshUrl;

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;

    public AuthController(RestTemplate restTemplate, UserRepository userRepository) {
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody User loginForm) {
        User user = userRepository.findUserByUsername(loginForm.getUsername())
                                  .filter(foundUser -> Objects.equals(foundUser.getPassword(), loginForm.getPassword()))
                                  .orElseThrow(
                                          () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials")
                                  );

        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                generateUrl, AuthRequest.of(user), AuthResponse.class
        );

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new ResponseStatusException(response.getStatusCode());
        }

        return Optional.ofNullable(response.getBody())
                       .orElseThrow(() -> new ResponseStatusException(
                               HttpStatus.INTERNAL_SERVER_ERROR,
                               "Auth server respond with empty body"
                       ));

    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody String refreshToken) {
        return restTemplate.postForEntity(refreshUrl, refreshToken, AuthResponse.class);
    }
}
