package com.khundadze.Balamb_Docs.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User extends BaseEntity {

    private String username;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private GlobalRole globalRole;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<DocumentPermission> permissions = new ArrayList<>();

    public User(String username, String email, String password, GlobalRole role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.globalRole = role;
    }
}