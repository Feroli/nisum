package com.evaluacion.nisum.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity(name = "app_user")
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Builder.Default
    private LocalDateTime created = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime modified = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime lastLogin = LocalDateTime.now();

    private String token;

    @Builder.Default
    private Boolean isActive = true;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Phone> phones;
}