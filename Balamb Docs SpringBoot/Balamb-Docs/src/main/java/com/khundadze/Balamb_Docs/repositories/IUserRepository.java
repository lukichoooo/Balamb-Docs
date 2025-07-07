package com.khundadze.Balamb_Docs.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khundadze.Balamb_Docs.models.User;

public interface IUserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByUsername(String name);

    List<User> findTop5ByUsernameStartsWithIgnoreCase(String name);

}
