package nl.thermans.whereis.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;

import static nl.thermans.whereis.auth.AuthConstants.EXPIRATION_TIME;
import static nl.thermans.whereis.auth.AuthConstants.SECRET;

@RestController
public class SignInController {
    private final AuthenticationManager authenticationManager;

    public SignInController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/api/auth/sign-in")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInDto dto) {
        // TODO: Nadenken over refreshtokens

        // TODO: BadCredentialsException
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                dto.email(),
                dto.password(),
                new ArrayList<>()
        ));

        // TODO: Role claim vullen
        String token = JWT.create()
                .withIssuer("Timo")
                .withSubject(dto.email())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));

        // TODO: fatsoenlijk response object maken
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
