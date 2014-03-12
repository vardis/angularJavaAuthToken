package server.signup;

import com.google.gson.Gson;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import server.account.Account;
import server.account.AccountRepository;
import server.account.AccountService;
import server.error.JsonErrors;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
public class SignupController {

    @Inject
	private AccountRepository accountRepository;

    @Inject
	private AccountService accountService;

    @Inject
    private Gson gson;

	@RequestMapping(value = "signup", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
	public String signup(@Valid @ModelAttribute SignupForm signupForm, Errors errors, HttpServletResponse response) {
		if (errors.hasErrors()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return new JsonErrors(errors).toString();
		}
        Account account = accountRepository.findByUsername(signupForm.getEmail());
        if (account == null) {
            account = accountRepository.saveEncrypted(signupForm.createAccount());
        }
//		accountService.signIn(account);

        return gson.toJson(account);
	}
}
