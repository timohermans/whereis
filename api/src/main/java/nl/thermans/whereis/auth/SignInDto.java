package nl.thermans.whereis.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record SignInDto(
        @Email
        @NotNull
        String email,
        @NotNull
        String password
) {
}
