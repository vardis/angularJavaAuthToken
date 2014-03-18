package server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;
import server.account.AccountService;
import server.security.AuthenticationTokenProcessingFilter;

import java.security.SecureRandom;

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
        BytesKeyGenerator keyGenerator = KeyGenerators.secureRandom();
        SecureRandom random = new SecureRandom(keyGenerator.generateKey());
        return new BCryptPasswordEncoder(10, random);
    }

}