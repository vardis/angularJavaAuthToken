package server.utils;

import org.springframework.security.crypto.codec.Base64;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.UnsupportedEncodingException;

/**
 * Created by giorgos on 31/01/14.
 */
public class AuthTokenUtils {

    public static String getBasicAuthenticationHeader(String user, String password) throws UnsupportedEncodingException {
        String input = user + ":" + password;
        String baHeader = new String(Base64.encode(input.getBytes()), "UTF-8");
        return "Basic " + baHeader;
    }

    public static RequestBuilder addSecurityHeader(MockHttpServletRequestBuilder builder, String u, String p) throws UnsupportedEncodingException {
        return builder.header("Authorization", getBasicAuthenticationHeader(u, p));
    }
}
