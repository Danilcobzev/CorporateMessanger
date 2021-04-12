package com.example.firstSpringProgect.repos;

import com.example.firstSpringProgect.domen.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Long> {
    User findByUsername(String username);

    User findByActivationCode(String code);
}
