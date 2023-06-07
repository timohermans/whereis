package nl.thermans.whereis.user;

import jakarta.persistence.*;

@Entity
public class Account {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
