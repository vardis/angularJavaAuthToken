package server.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import server.account.Account;

import javax.inject.Inject;

/**
 * Constructs AuthenticationToken instances.
 *
 * Takes care of wiring dependencies from the application context into the tokens.
 */
@Service
public class AuthenticationTokenFactory {

    @Value("${auth.token.duration.minutes}")
    private long tokenDuration;

    @Inject
    private PasswordEncoder encoder;

    public AuthenticationToken fromAccount(Account account) {
        AuthenticationToken token = new AuthenticationToken(encoder, tokenDuration);
        token.initFromAccount(account);
        return token;
    }

    public AuthenticationToken fromAuthorizationHeader(String header) {
        AuthenticationToken token = new AuthenticationToken(encoder, tokenDuration);
        token.initFromTokenValue(header);
        return token;
    }
}
