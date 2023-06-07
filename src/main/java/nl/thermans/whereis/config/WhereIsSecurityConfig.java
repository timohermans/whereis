package nl.thermans.whereis.config;

import nl.thermans.whereis.user.UserDetailsServiceImpl;
import nl.thermans.whereis.user.AccountRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WhereIsSecurityConfig {
    private final AccountRepository accountRepository;

    public WhereIsSecurityConfig(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        var provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsServiceImpl userDetailsService() {
        return new UserDetailsServiceImpl(accountRepository);
    }
}
