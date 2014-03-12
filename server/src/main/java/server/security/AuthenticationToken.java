package server.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * username:expirationTime:secret
 */
public class AuthenticationToken {

    public static final String SALT = "xG47JD!·";

    @Value("${auth.token.duration.minutes}")
    private long tokenDuration;

    private String token;

    private long expireTime;

    private String userName;

    private String secret;

    public AuthenticationToken(UserDetails account) {
        userName = account.getUsername();
        secret = computeSignature(account);
        expireTime = System.currentTimeMillis() + tokenDuration * 1000;

        StringBuilder signatureBuilder = new StringBuilder();
        signatureBuilder.append(account.getUsername());
        signatureBuilder.append(":");
        signatureBuilder.append(expireTime);
        signatureBuilder.append(":");
        signatureBuilder.append(secret);

        token = signatureBuilder.toString();
    }

    public AuthenticationToken(String authTokenValue) {
        if (authTokenValue == null) {
            throw new IllegalArgumentException("Token is empty");
        }

        String[] parts = authTokenValue.split(":");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid token format");
        }

        userName = parts[0];
        expireTime = Long.valueOf(parts[1]);
        secret = parts[2];
        token = authTokenValue;

        if (secret.length() == 0 || userName.length() == 0) {
            throw new IllegalArgumentException("Invalid token value");
        }
    }

    public boolean isValid(UserDetails account) {
        if (account.getUsername().equalsIgnoreCase(userName)) {
            if (expireTime < System.currentTimeMillis()) {
                if (computeSignature(account).equals(secret)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String computeSignature(UserDetails account) {
        StringBuilder signatureBuilder = new StringBuilder();
        signatureBuilder.append(account.getUsername());
        signatureBuilder.append(":");
        signatureBuilder.append(account.getPassword());
        signatureBuilder.append(":");
        signatureBuilder.append(SALT);

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No MD5 algorithm available!");
        }

        return new String(Hex.encode(digest.digest(signatureBuilder.toString().getBytes())));
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return token;
    }
}
