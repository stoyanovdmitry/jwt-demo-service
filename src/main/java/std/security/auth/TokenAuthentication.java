package std.security.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@Setter
@Getter
public class TokenAuthentication extends AbstractAuthenticationToken {

    private String principal;
    private String credentials;
    private Collection<GrantedAuthority> authorities;

    public TokenAuthentication(String token) {
        super(Collections.emptyList());
        this.credentials = token;
    }
}
