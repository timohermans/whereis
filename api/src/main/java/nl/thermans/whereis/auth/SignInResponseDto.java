package nl.thermans.whereis.auth;

import jakarta.validation.constraints.NotNull;

public record SignInResponseDto(
        @NotNull
        String token,
        @NotNull
        String email,
        @NotNull
        String[] roles,
        String tokenType
) {
};
