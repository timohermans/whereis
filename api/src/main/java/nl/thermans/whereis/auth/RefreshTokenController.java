package nl.thermans.whereis.auth;

import jakarta.validation.Valid;
import nl.thermans.whereis.user.RefreshToken;
import nl.thermans.whereis.user.RefreshTokenRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RefreshTokenController {
    private final RefreshTokenRepository repo;

    public RefreshTokenController(RefreshTokenRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/api/auth/refreshtoken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) throws TokenRefreshException {
        String requestRefreshToken = request.getRefreshToken();

        // TODO: create a token rotation for extra safety
        return repo.findByToken(requestRefreshToken)
                .map(refreshToken -> {
                    if (refreshToken.isExpired()) {
                        repo.delete(refreshToken);
                        return null;
                    }
                    return refreshToken;
                })
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = user.toToken();
                    return ResponseEntity.ok(new RefreshTokenResponse(requestRefreshToken, token));
                })
                .orElse(ResponseEntity.badRequest().build());
    }
}
