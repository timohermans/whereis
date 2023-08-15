package nl.thermans.whereis.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Optional;

import static nl.thermans.whereis.auth.AuthConstants.SECRET;

public class Token {
    private final DecodedJWT tokenDecoded;

    public Token(String token) {
        tokenDecoded = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token);

    }

    public String getEmail() {
        return tokenDecoded.getSubject();
    }
}
