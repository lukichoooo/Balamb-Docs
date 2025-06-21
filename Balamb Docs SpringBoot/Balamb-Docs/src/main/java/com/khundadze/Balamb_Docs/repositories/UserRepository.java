package com.khundadze.Balamb_Docs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khundadze.Balamb_Docs.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

    public User findByEmail(String email);

    public User findByName(String name);
}
