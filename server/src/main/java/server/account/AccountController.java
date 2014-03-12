package server.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import server.AppConstants;

import java.security.Principal;

/**
 * Allows authenticated users to retrieve details of their current account.
 */
@RestController
@Secured("ROLE_USER")
class AccountController {

    private AccountRepository accountRepository;

    @Autowired
    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @RequestMapping(value = "account/current", method = RequestMethod.GET, produces = AppConstants.CONTENT_TYPE_JSON)
    @ResponseStatus(value = HttpStatus.OK)
    public Account accounts(Principal principal) {
        Assert.notNull(principal);
        return accountRepository.findByUsername(principal.getName());
    }
}
