package nl.thermans.whereis.auth;

import jakarta.validation.Valid;
import nl.thermans.whereis.user.Account;
import nl.thermans.whereis.user.AccountRepository;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

//@Validated
@RestController
public class SignupController {

    private final AuthenticationManager authenticationManager;
    private final AccountRepository accountRepository;

    public SignupController(AuthenticationManager authenticationManager, AccountRepository accountRepository) {
        this.authenticationManager = authenticationManager;
        this.accountRepository = accountRepository;
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<?> handle(@Valid @RequestBody SignupDto form) {
        if (accountRepository.existsByEmail(form.email())) {
            return new ResponseEntity<>("User already exists!", HttpStatus.BAD_REQUEST);
        }

        Account account = new Account(
                form.email(),
                form.password(),
                form.username(),
                form.firstname(),
                form.lastname()
        );

        accountRepository.save(account);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
