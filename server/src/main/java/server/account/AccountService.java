package server.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import server.security.AuthenticationToken;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Set;

/**
 * Provides Account related services.
 */
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Inject
    private AuthenticationManager authenticationManager;

    /**
     * Implements the UserDetailsService.loadUserByUsername using the
     * AccountRepository to fetch the matching accounts.
     *
     * @param username the unique user callsign
     * @return the matched Account
     * @throws UsernameNotFoundException if no user was found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account acc = accountRepository.findByUsername(username);
        if (acc == null) {
            throw new UsernameNotFoundException("user not found");
        }
        return createUser(acc);
    }

    /**
     * Attempts to sign in with the given credentials.
     * It will also populate the Authentication property of the current
     * security context using a UsernamePasswordAuthenticationToken instance.
     *
     * @param username the username
     * @param password the password
     * @return Upon successful login, the Account that was matched by the given credentials
     */
    public Account signIn(String username, String password) {
        Account credentials = new Account(username, password);
        Authentication authentication = new UsernamePasswordAuthenticationToken(createUser(credentials), password);
        Authentication fullAuthentication = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(fullAuthentication);

        Account account = accountRepository.findByUsername(username);
        AuthenticationToken token = new AuthenticationToken(account);
        account.setAuthToken(token.toString());
        return account;
    }

    /**
     * Creates an instance of org.springframework.security.core.userdetails.User
     * from the given account.
     * @param account an Account instance
     * @return the compatible User instance
     */
    private User createUser(Account account) {
        return new User(account.getUsername(), account.getPassword(), createAuthorities(account));
    }

    /**
     * Utility method to create the set of SimpleGrantedAuthority instances
     * given a user account.
     * @param account the user account
     * @return a Set of SimpleGrantedAuthority
     */
    private Set<SimpleGrantedAuthority> createAuthorities(Account account) {
        if (StringUtils.hasText(account.getRole())) {
            return Collections.singleton(new SimpleGrantedAuthority(account.getRole()));
        }
        return Collections.emptySet();
    }

}
