package nl.thermans.whereis.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;

import static nl.thermans.whereis.auth.AuthConstants.*;

@RestController
public class SignInController {
    private final AuthenticationManager authenticationManager;

    public SignInController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/api/auth/sign-in")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInDto dto) {
        // TODO: Nadenken over refreshtokens


        var user = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                dto.email(),
                dto.password(),
                new ArrayList<>()
        ));

        String[] roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new);
        String token = JWT.create()
                .withIssuer("Timo")
                .withSubject(dto.email())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .withArrayClaim("roles", roles)
                .sign(Algorithm.HMAC512(SECRET.getBytes()));

        User principal = (User) user.getPrincipal();
        return new ResponseEntity<>(new SignInResponseDto(
                token,
                principal.getUsername(),
                roles,
                TOKEN_PREFIX
        ), HttpStatus.OK);
    }
}
