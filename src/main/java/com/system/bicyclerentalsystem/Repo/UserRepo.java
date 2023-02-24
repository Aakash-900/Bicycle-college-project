package com.system.bicyclerentalsystem.Repo;

import com.system.bicyclerentalsystem.Entity.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {
    @Query(value = "SELECT * FROM users WHERE email = ?1", nativeQuery = true)
    Optional<User> findByEmail(String email);

    @Query(value = "UPDATE users SET password =?1 WHERE email = ?2", nativeQuery = true)
    void updatePassword(String updatePassword, String email);

}
