package server.account;

import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * An in-memory repository for accounts.
 * Accounts are stored in a hash map, keyed by their username.
 */
public class AccountRepository {

    Map<String, Account> accounts = new HashMap<>();

    @Inject
    private PasswordEncoder passwordEncoder;

    /**
     * Creates a test account with knonw credentials.
     */
    @PostConstruct
    public void onCreate() {
        Account testAccount = new Account("admin", "admin", "ADMIN");
        saveEncrypted(testAccount);
    }

    public Account findByUsername(String username) {
        return accounts.get(username);
    }


    public Account save(Account account) {
        accounts.put(account.getUsername(), account);
        return account;
    }

    /**
     * Encrypts the account password before saving it in the repository.
     */
    public Account saveEncrypted(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        accounts.put(account.getUsername(), account);
        return account;
    }
}
