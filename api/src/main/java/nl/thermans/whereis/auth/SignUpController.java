package nl.thermans.whereis.auth;

import jakarta.validation.Valid;
import nl.thermans.whereis.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class SignUpController {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public SignUpController(AccountRepository accountRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/api/auth/sign-up")
    public ResponseEntity<?> handle(@Valid @RequestBody SignUpDto form) {
        String encodedPassword = passwordEncoder.encode(form.password());

        Optional<Role> studentRoleFromDb = roleRepository.findByName(ERole.Student);
        Role studentRole = studentRoleFromDb.orElseGet(() -> new Role(ERole.Student));

        if (studentRoleFromDb.isEmpty()) {
            roleRepository.save(studentRole);
        }

        Account account = new Account(
                form.email(),
                encodedPassword,
                form.username(),
                form.firstname(),
                form.lastname(),
                studentRole
        );

        accountRepository.save(account);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
