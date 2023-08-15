package nl.thermans.whereis.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.validation.Valid;
import nl.thermans.whereis.user.Account;
import nl.thermans.whereis.user.RefreshToken;
import nl.thermans.whereis.user.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static nl.thermans.whereis.auth.AuthConstants.*;

@RestController
public class SignInController {
    @Value("${nl.hermans.whereis.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;

    public SignInController(AuthenticationManager authenticationManager, RefreshTokenRepository refreshTokenRepository) {
        this.authenticationManager = authenticationManager;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @PostMapping("/api/auth/sign-in")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInRequest dto) {
        Authentication user = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                dto.email(),
                dto.password(),
                new ArrayList<>()
        ));
        Account account = (Account) user.getPrincipal();

        String[] roles = account.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new);
        String token = account.toToken();

        RefreshToken refreshToken = refreshTokenRepository.findByUserId(account.getId())
                .map(t -> {
                    if (t.isExpired()) {
                        refreshTokenRepository.delete(t);
                        return null;
                    }
                    return t;
                })
                .orElseGet(() -> {
                    var refreshNew = new RefreshToken(account, refreshTokenDurationMs);
                    refreshNew = refreshTokenRepository.save(refreshNew);
                    return refreshNew;
                });

        return new ResponseEntity<>(new SignInResponse(
                token,
                refreshToken.getToken(),
                account.getEmail(),
                roles,
                TOKEN_PREFIX
        ), HttpStatus.OK);
    }
}
