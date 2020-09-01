package std.security.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import std.dto.TokenPayload;
import std.security.core.TokenAuthentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String BEARER_ = "Bearer ";

    @Autowired
    private RestTemplate restTemplate;

    @Value("${auth.server.url.read}")
    private String tokenReadUrl;

    protected JwtAuthenticationFilter(String defaultFilterProcessesUrl) {
        super("/**");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_)) {
            throw new AuthenticationCredentialsNotFoundException("No JWT token found in request headers");
        }

        String authToken = authorizationHeader.replace(BEARER_, "");

        ResponseEntity<TokenPayload> responseEntity = restTemplate.postForEntity(
                tokenReadUrl,
                authToken,
                TokenPayload.class
        );

        var tokenAuthentication = new TokenAuthentication(responseEntity.getBody().getUsername());

        return getAuthenticationManager().authenticate(tokenAuthentication);
    }


}
