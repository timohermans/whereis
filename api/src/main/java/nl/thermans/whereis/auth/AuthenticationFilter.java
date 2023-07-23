package nl.thermans.whereis.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nl.thermans.whereis.user.Account;
import nl.thermans.whereis.user.AccountRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static nl.thermans.whereis.auth.AuthConstants.*;

// based on https://www.freecodecamp.org/news/how-to-setup-jwt-authorization-and-authentication-in-spring/
public class AuthenticationFilter extends BasicAuthenticationFilter {

    private final AccountRepository accountRepository;

    public AuthenticationFilter(AuthenticationManager authenticationManager, AccountRepository accountRepository) {
        super(authenticationManager);
        this.accountRepository = accountRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(header);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }


    private UsernamePasswordAuthenticationToken getAuthentication(String tokenWithPrefix) {
        // TODO: nadenken over token expiry enzo. Dus exceptions afvangen
        String token = tokenWithPrefix.replace(TOKEN_PREFIX, "");
        String userJson = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token)
                .getSubject();

        if (userJson == null) {
            return null;
        }

        Optional<Account> account = accountRepository.findByEmail(userJson);
        return account.map(a -> new UsernamePasswordAuthenticationToken(a, null, a.getAuthorities()))
                .orElseThrow();
    }

}
