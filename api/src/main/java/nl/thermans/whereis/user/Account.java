package nl.thermans.whereis.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Entity
public class Account {
    @Id
    @GeneratedValue
    private Long id;
    @Email
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String username;
    @NotNull
    private String firstname;
    @NotNull
    private String lastname;
    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;

    public Account(@NotNull String email, @NotNull String password, @NotNull String username, @NotNull String firstname, @NotNull String lastname) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
//        this.role = Role.Student;
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
}
