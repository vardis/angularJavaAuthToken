package server.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import server.account.AccountService;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Intercepts incoming requests and attempts to authenticate the current user
 * based on the presence of an authentication token stored under the HTTP headers.
 *
 * @see https://github.com/philipsorst/angular-rest-springsecurity
 */
public class AuthenticationTokenProcessingFilter extends GenericFilterBean {

    @Inject
    private AccountService accountService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (!(request instanceof HttpServletRequest)) {
            throw new RuntimeException("Expecting a HTTP request");
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String tokenHeader = httpRequest.getHeader(SecurityHeaders.AUTH_TOKEN);

        if (StringUtils.hasLength(tokenHeader)) {
            AuthenticationToken token = new AuthenticationToken(tokenHeader);

            if (token.getUserName() != null) {
                UserDetails userDetails = accountService.loadUserByUsername(token.getUserName());
                if (token.isValid(userDetails)) {
                    setAuthenticationInSecurityContext((HttpServletRequest) request, userDetails);
                }
            }
        }
        chain.doFilter(request, response);
    }

    private void setAuthenticationInSecurityContext(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
