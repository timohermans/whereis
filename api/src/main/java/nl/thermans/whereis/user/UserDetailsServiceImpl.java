package nl.thermans.whereis.user;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountRepository _repository;

    public UserDetailsServiceImpl(AccountRepository repository) {
        _repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Account> account = _repository.findByEmail(email);

        return account.map(a -> new User(a.getEmail(), a.getPassword(), a.getAuthorities()))
                .orElseThrow(() -> new UsernameNotFoundException("Tried username: " + email));
    }
}
