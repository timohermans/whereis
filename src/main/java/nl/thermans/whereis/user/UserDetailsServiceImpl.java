package nl.thermans.whereis.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountRepository _repository;

    public UserDetailsServiceImpl(AccountRepository repository) {
        _repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = _repository.findByUsername(username);
        if(account == null) {
            throw new UsernameNotFoundException("Tried username: " + username);
        }

        return new org.springframework.security.core.userdetails.User(account.getUsername(), account.getPassword(), Collections.emptyList());
    }
}
