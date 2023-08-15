package nl.thermans.whereis.auth;

import jakarta.validation.constraints.NotNull;

public record SignInResponse(
        @NotNull
        String token,
        @NotNull
        String refreshToken,
        @NotNull
        String email,
        @NotNull
        String[] roles,
        String tokenType
) {
};
