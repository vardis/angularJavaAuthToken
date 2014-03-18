package server.signin;

import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.AppConstants;
import server.account.Account;
import server.account.AccountRepository;
import server.account.AccountService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

/**
 * Handles sign-in and sign-out operations for the users.
 */
@RestController
public class SignInController {

    @Inject
    private AccountRepository accountRepository;

    @Inject
    private AccountService accountService;

    /**
     * Handles POST requests for signing-in a user with the given credentials.
     * @param username the username to login with
     * @param password the password
     * @param response the HTTP response
     * @return the JSON representation of the Account in case of successful login or the 401 status code
     */
    @RequestMapping(value = "signIn", method = RequestMethod.POST, produces = AppConstants.CONTENT_TYPE_JSON)
    public Account signIn(@RequestParam("username") String username,
                          @RequestParam("password") String password,
                          HttpServletResponse response) {
        Account account = null;
        try {
            account = accountService.signIn(username, password);
        } catch (AuthenticationException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
        return account;
    }

    /**
     * Signs-out the given user.
     *
     * It takes care of removing the authentication token from the
     * persisted storage in order to invalidate any future requests
     * that try to re-use it.
     *
     * @param principal the principal of the user to sign out
     * @param response the HTTP response
     */
    @RequestMapping(value = "signOut", method = RequestMethod.POST)
    public void signOut(Principal principal, HttpServletResponse response) throws IOException {
        Account account = accountRepository.findByUsername(principal.getName());
        if (account != null) {
            account.setAuthToken(null);
            accountRepository.save(account);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
