package nl.thermans.whereis.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record SignupDto(
        @Email
        @NotNull
        String email,
        @NotNull
        String password,
        @NotNull
        String username,
        @NotNull
        String firstname,
        @NotNull
        String lastname) {
}
