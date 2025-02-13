package com.aashish.hospital_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    public Set<String> getRoleNames() {
        if (this.roles == null) {
            return Set.of(); // Return an empty set instead of throwing NullPointerException
        }
        return this.roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }

}
