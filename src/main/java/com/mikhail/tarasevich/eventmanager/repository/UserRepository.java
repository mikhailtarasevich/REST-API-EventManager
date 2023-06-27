package com.mikhail.tarasevich.eventmanager.repository;

import com.mikhail.tarasevich.eventmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository <User, Integer> {

    Optional<User> findUserByEmail (String email);

    @Query(value = "SELECT * FROM users u JOIN roles r ON u.role_id = r.id WHERE r.name = :role", nativeQuery = true)
    List<User> findUsersByRoleOrderById(@Param("role")String role);

}
