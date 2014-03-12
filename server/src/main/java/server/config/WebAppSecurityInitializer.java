package server.config;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * Registers the springSecurityFilterChain filter for every URL in the application.
 *
 * This must be ordered before WebAppInitializer so that the security filters are
 * applied before anything else. Otherwise for e.g. someone could perform privileged
 * actions without having himself authenticated first.
 *
 */
@Order(1)
public class WebAppSecurityInitializer extends AbstractSecurityWebApplicationInitializer {

}