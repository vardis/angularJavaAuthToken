package server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import server.account.AccountService;
import server.security.AuthenticationTokenProcessingFilter;

/**
 * Provides security related beans.
 */
@Configuration
@ImportResource(value = "classpath:spring-security-context.xml")
class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public AuthenticationTokenProcessingFilter authenticationTokenProcessingFilter() {
        return new AuthenticationTokenProcessingFilter();
    }

    @Bean
    public AccountService userService() {
        return new AccountService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new StandardPasswordEncoder();
    }
}