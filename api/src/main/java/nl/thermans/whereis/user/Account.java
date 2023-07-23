package nl.thermans.whereis.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Account {
    @Id
    @GeneratedValue
    private Long id;
    @Email
    @NotNull
    @Column(unique = true)
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String username;
    @NotNull
    @Size(min = 1, max = 20)
    @Pattern(regexp = "^[a-zA-Z .'-]+$")
    private String firstname;
    @NotNull
    @Size(min = 1, max = 20)
    @Pattern(regexp = "^[a-zA-Z .'-]+$")
    private String lastname;

    @ManyToMany
    private Set<Role> roles = new HashSet<>();

    public Account(@NotNull String email, @NotNull String password, @NotNull String username, @NotNull String firstname, @NotNull String lastname, @NotNull Role role) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;

        if (role.getName() != ERole.Student) {
            throw new IllegalArgumentException("An account must be of student role initially, because admins need to approve other roles first");
        }
        this.roles = Set.of(role);
    }

    protected Account() {
    }

    public @NotNull String getEmail() {
        return email;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public List<GrantedAuthority> getAuthorities() {
        return getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
    }
}
