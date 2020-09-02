package std.security.auth;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.ForwardAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String BEARER_ = "Bearer ";

    public JwtAuthenticationFilter(AuthenticationManager jwtAuthenticationManager) {
        super(requestMatcher());
        setAuthenticationManager(jwtAuthenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_)) {
            throw new AuthenticationCredentialsNotFoundException("No JWT token found in request headers");
        }

        var authToken = authorizationHeader.replace(BEARER_, "");
        var tokenAuthentication = new TokenAuthentication(authToken);

        setAuthenticationSuccessHandler(new ForwardAuthenticationSuccessHandler(request.getRequestURI()));
        return getAuthenticationManager().authenticate(tokenAuthentication);
    }

    public static RequestMatcher requestMatcher() {
        return new AndRequestMatcher(
                new AntPathRequestMatcher("/api/users/**"),
                new NegatedRequestMatcher(
                        new AntPathRequestMatcher("/api/users", HttpMethod.GET.name())
                ),
                new NegatedRequestMatcher(
                        new AntPathRequestMatcher("/api/users", HttpMethod.POST.name())
                ),
                new NegatedRequestMatcher(
                        new AntPathRequestMatcher("/refresh", HttpMethod.POST.name())
                ),
                new NegatedRequestMatcher(
                        new AntPathRequestMatcher("/login", HttpMethod.POST.name())
                ),
                new NegatedRequestMatcher(
                        new AntPathRequestMatcher("/swagger-ui/**")
                ),
                new NegatedRequestMatcher(
                        new AntPathRequestMatcher("/swagger-resources/**")
                )
        );
    }
}
