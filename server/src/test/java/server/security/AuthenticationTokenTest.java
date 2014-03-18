package server.security;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import server.account.Account;
import server.config.AppConfigurationAware;

import javax.inject.Inject;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class AuthenticationTokenTest extends AppConfigurationAware {

    @Inject
    private PasswordEncoder passwordEncoder;

    Account admin;
    AuthenticationToken adminToken;
    long tokenDuration = 60;

    @Before
    public void prepare() {
        admin = new Account("admin", passwordEncoder.encode("admin"));
        adminToken = new AuthenticationToken(passwordEncoder, tokenDuration);
        adminToken.initFromAccount(admin);
        admin.setAuthToken(adminToken);
    }

    @Test
    public void testInvalidForOtherAccount() {
        Account otherAccount = new Account("guest", "guest");
        AuthenticationToken token = new AuthenticationToken(passwordEncoder, tokenDuration);
        token.initFromAccount(otherAccount);
        assertFalse(token.isValid(admin));
    }

    @Test
    public void testValidityFromSecurityHeader() {
        String tokenValue = adminToken.toString();
        AuthenticationToken token = createTokenFromValue(tokenValue);
        assertTrue(token.isValid(admin));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTokenFieldLength() {
        createTokenFromValue("a:b:c:d");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTokenUsername() {
        createTokenFromValue(":b");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTokenSecret() {
        createTokenFromValue("a:");
    }

    @Test
    public void testInvalidWhenExpires() {
        adminToken.setExpireTime(0);
        assertFalse(adminToken.isValid(admin));
    }


    private AuthenticationToken createTokenFromValue(String tokenValue) {
        AuthenticationToken token = new AuthenticationToken(passwordEncoder, tokenDuration);
        token.initFromTokenValue(tokenValue);
        return token;
    }

}
