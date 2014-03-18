package server.security;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.security.crypto.password.PasswordEncoder;
import server.account.Account;

/**
 * username:secret
 */
public class AuthenticationToken {

    private String value;

    @JsonIgnore
    private long expireTime;

    @JsonIgnore
    private String userName;

    @JsonIgnore
    private String secret;

    @JsonIgnore
    private PasswordEncoder encoder;

    @JsonIgnore
    private long tokenDurationInMinutes;

    public AuthenticationToken(PasswordEncoder encoder, long tokenDurationInMinutes) {
        this.encoder = encoder;
        this.tokenDurationInMinutes = tokenDurationInMinutes;
    }

    public void initFromAccount(Account account) {
        userName = account.getUsername();
        secret = computeSecret(account);
        expireTime = System.currentTimeMillis() + tokenDurationInMinutes * 60 * 1000;
        value = account.getUsername() + ":" + secret;
    }

    public void initFromTokenValue(String authTokenValue) {
        if (authTokenValue == null) {
            throw new IllegalArgumentException("Token is empty");
        }

        String[] parts = authTokenValue.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid token format");
        }

        userName = parts[0];
        secret = parts[1];

        if (secret.length() == 0 || userName.length() == 0) {
            throw new IllegalArgumentException("Invalid token value");
        }

        value = userName + ":" + secret;
    }

    public boolean isValid(Account account) {
        if (account != null && account.getUsername().equalsIgnoreCase(userName)) {
            AuthenticationToken existingToken = account.getAuthToken();
            if (existingToken.getExpireTime() > System.currentTimeMillis()) {
                if (encoder.matches(createRawSecret(account), secret)) {
                    return true;
                }
            }
        }
        return false;
    }

    private String computeSecret(Account account) {
        return encoder.encode(createRawSecret(account));
    }

    private String createRawSecret(Account account) {
        return account.getUsername() + ":" + account.getPassword();
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return value;
    }

    public String getValue() {
        return value;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

}
