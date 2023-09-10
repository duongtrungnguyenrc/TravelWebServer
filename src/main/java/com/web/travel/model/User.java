package com.web.travel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.boot.model.source.spi.FetchCharacteristics;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(length = 100)
    private String fullName;
    @NotBlank
    private String address;
    @NotBlank
    @Email
    @Column(unique = true)
    private String email;
    @NotBlank
    @Column
    private String password;
    @NotBlank
    @Column(length = 13)
    private String phone;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Collection<Order> orders;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Collection<Rate> rates;

    public User(String fullName, String address, String email, String password, String phone) {
        this.fullName = fullName;
        this.address = address;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }
}
