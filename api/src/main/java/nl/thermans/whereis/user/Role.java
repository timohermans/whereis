package nl.thermans.whereis.user;

import jakarta.persistence.*;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, unique = true)
    private ERole name;

    protected Role() {}

    public Role(ERole name) {
        this.name = name;
    }

    public ERole getName() {
        return name;
    }
}
