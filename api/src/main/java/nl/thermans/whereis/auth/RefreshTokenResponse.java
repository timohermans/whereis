package nl.thermans.whereis.auth;

public class RefreshTokenResponse {
    private String refreshToken;
    private String token;

    public RefreshTokenResponse(String refreshToken, String token) {
        this.refreshToken = refreshToken;
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getToken() {
        return token;
    }
}
