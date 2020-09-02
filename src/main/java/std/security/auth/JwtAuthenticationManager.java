package std.security.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import std.dto.TokenPayload;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationManager implements AuthenticationManager {

    @Value("${auth.server.url.read}")
    private String tokenReadUrl;

    private final RestTemplate restTemplate;

    public JwtAuthenticationManager(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof TokenAuthentication) {
            try {
                proceedToken((TokenAuthentication) authentication);
            } catch (ResponseStatusException e) {
                throw new BadCredentialsException(e.getMessage(), e);
            }
        }

        return authentication;
    }

    private void proceedToken(TokenAuthentication authentication) throws ResponseStatusException {
        ResponseEntity<TokenPayload> responseEntity;

        responseEntity = restTemplate.postForEntity(
                tokenReadUrl,
                authentication.getCredentials(),
                TokenPayload.class
        );

        var payload = Optional.ofNullable(responseEntity.getBody())
                              .orElseThrow(() -> new AuthenticationServiceException("Can't read token"));

        authentication.setPrincipal(payload.getUsername());
        authentication.setAuthorities(defineAuthorities(payload));
        authentication.setAuthenticated(true);
    }

    private Collection<GrantedAuthority> defineAuthorities(TokenPayload payload) {
        return payload.getRoles()
                      .stream()
                      .map(SimpleGrantedAuthority::new)
                      .collect(Collectors.toList());
    }
}
