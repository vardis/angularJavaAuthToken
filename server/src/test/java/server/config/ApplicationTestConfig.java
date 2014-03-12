package server.config;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import server.Application;
import server.account.AccountRepository;

/**
 * Created by giorgos on 11/02/14.
 */
@Configuration
@ComponentScan(basePackageClasses = Application.class,
        excludeFilters = @ComponentScan.Filter({Controller.class, Configuration.class}))
public class ApplicationTestConfig {

    @Bean
    public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
        PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
        ppc.setLocations(new Resource[]{
                new ClassPathResource("/app.test.properties")
        });
        return ppc;
    }

    @Bean
    public AccountRepository accountRepository() {
        return new AccountRepository();
    }
}
