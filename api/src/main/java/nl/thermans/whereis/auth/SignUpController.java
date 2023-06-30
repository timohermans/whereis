package nl.thermans.whereis.auth;

import jakarta.validation.Valid;
import nl.thermans.whereis.user.Account;
import nl.thermans.whereis.user.AccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignUpController {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public SignUpController(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/api/auth/sign-up")
    public ResponseEntity<?> handle(@Valid @RequestBody SignUpDto form) {
        String encodedPassword = passwordEncoder.encode(form.password());
        Account account = new Account(
                form.email(),
                encodedPassword,
                form.username(),
                form.firstname(),
                form.lastname()
        );

        accountRepository.save(account);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
