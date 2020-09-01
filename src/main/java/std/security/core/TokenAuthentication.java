package std.security.core;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

public class TokenAuthentication extends AbstractAuthenticationToken {

    private String principal;

    public TokenAuthentication(String username) {
        super(Collections.emptyList());
        principal = username;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public String getPrincipal() {
        return principal;
    }
}
