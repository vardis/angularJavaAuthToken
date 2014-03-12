package server.account;

import org.junit.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import server.AppConstants;
import server.config.WebSecurityConfigurationAware;
import server.utils.AuthTokenUtils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserAuthenticationIntegrationTest extends WebSecurityConfigurationAware {

    @Test
    public void requiresAuthentication() throws Exception {
        mockMvc.perform(get("/account/current").secure(true))
                .andExpect(status().is(401));
    }

    @Test
    public void userAuthenticates() throws Exception {
        MockHttpServletRequestBuilder post = post("/signIn")
                .secure(true)
                .param("username", "admin")
                .param("password", "admin");

        mockMvc.perform(post)
                .andExpect(status().isOk())
                .andExpect(content().contentType(AppConstants.CONTENT_TYPE_JSON));
    }

    @Test
    public void userAuthenticationFails() throws Exception {
        MockHttpServletRequestBuilder post = post("/").secure(true);
        AuthTokenUtils.addSecurityHeader(post, "wrong", "credentials");

        mockMvc.perform(post)
                .andExpect(status().isUnauthorized());
    }
}
