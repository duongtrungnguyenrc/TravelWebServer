package com.web.travel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.web.travel.model.enumeration.EUserStatus;
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
    @Column(length = 100)
    private String fullName;
    private String address;
    @Email
    @Column(unique = true)
    private String email;
    @Column
    private String password;
    @Column(length = 13)
    private String phone;
    @Enumerated(EnumType.STRING)
    private EUserStatus active;
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Collection<DestinationBlog> destinationBlogs;
    public User(String fullName, String address, String email, String password, String phone) {
        this.fullName = fullName;
        this.address = address;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }
}
