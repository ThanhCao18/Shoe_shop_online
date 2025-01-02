package com.example.ananas.repository;

import com.example.ananas.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface User_Repository  extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameOrEmail(String username, String email);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT COUNT(*) AS total_users " +
            "FROM ananas.user u " +
            "WHERE DATE(u.create_at) = :date",
            nativeQuery = true)
    BigDecimal getNumberOfUsersCreatedOn(@Param("date") String date);


}
