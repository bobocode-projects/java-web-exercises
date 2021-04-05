package com.bobocode.model.jpa;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "email")
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Role> roles = new HashSet<>();

    public void addRole(Role role) {
        roles.add(role);
        role.setUser(this);
    }

    public void addRoles(Set<Role> roles) {
        this.roles.addAll(roles);
        roles.forEach(role -> role.setUser(this));
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
        role.setUser(null);
    }
}
