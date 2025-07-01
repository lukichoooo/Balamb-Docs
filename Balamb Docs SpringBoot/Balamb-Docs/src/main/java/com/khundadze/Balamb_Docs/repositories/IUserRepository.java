package com.khundadze.Balamb_Docs.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khundadze.Balamb_Docs.models.User;

public interface IUserRepository extends JpaRepository<User, Long> {

    public User findByEmail(String email);

    public User findByUsername(String name);

    List<User> findTop5ByUsernameStartsWithIgnoreCase(String name);

}
