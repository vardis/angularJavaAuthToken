package server.account;

import org.junit.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import server.AppConstants;
import server.config.WebSecurityConfigurationAware;
import server.security.SecurityHeaders;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserAuthenticationIntegrationTest extends WebSecurityConfigurationAware {

    @Inject
    private AccountRepository accountRepository;

    @Test
    public void successfulAuthentication() throws Exception {
        Account admin = accountRepository.findByUsername("admin");
        mockMvc.perform(get("/account/current")
                .secure(true)
                .header(SecurityHeaders.AUTH_TOKEN, admin.getAuthToken().getValue()))
               .andExpect(status().is(HttpServletResponse.SC_OK));
    }

    @Test
    public void requiresAuthentication() throws Exception {
        mockMvc.perform(get("/account/current").secure(true))
                .andExpect(status().is(HttpServletResponse.SC_UNAUTHORIZED));
    }

    @Test
    public void userAuthenticates() throws Exception {
        MockHttpServletRequestBuilder post = post("/signIn")
                .secure(true)
                .param("username", "admin")
                .param("password", "admin");

        Account admin = accountRepository.findByUsername("admin");
        mockMvc.perform(post)
                .andExpect(status().isOk())
                .andExpect(content().contentType(AppConstants.CONTENT_TYPE_JSON))
                .andExpect(jsonPath("$.authToken.value", is(admin.getAuthToken().getValue())));
    }

    @Test
    public void userAuthenticationFails() throws Exception {
        MockHttpServletRequestBuilder post = post("/").secure(true);

        mockMvc.perform(post)
                .andExpect(status().isUnauthorized());
    }
}
