package nl.thermans.whereis.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import nl.thermans.whereis.user.Account;

import java.time.Instant;
import java.util.UUID;

@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(unique = true)
    private String token;

    @OneToOne
    private Account user;

    @NotNull
    private Instant expiryDate;

    public RefreshToken(Account account, Long refreshTokenDurationMs) {
        user = account;
        expiryDate = Instant.now().plusMillis(refreshTokenDurationMs);
        token = UUID.randomUUID().toString();
    }

    protected RefreshToken() {
    }

    public Boolean isExpired() {
        return expiryDate.compareTo(Instant.now()) < 0;
    }

    public Account getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }
}
