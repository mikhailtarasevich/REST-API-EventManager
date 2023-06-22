package com.mikhail.tarasevich.eventmanager.repository;

import com.mikhail.tarasevich.eventmanager.config.SpringTestConfig;
import com.mikhail.tarasevich.eventmanager.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = SpringTestConfig.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository ur;

    @Test
    void findUserByEmail_inputEmail_expectedUserInDB() {

        String email = "admin@example.com";

        Optional<User> user = ur.findUserByEmail(email);

        assertTrue(user.isPresent());
        assertEquals(email, user.get().getEmail());
    }

    @Test
    void findUsersByRoleOrderById_inputRoleName_expectedUserList() {

        String role = "ROLE_MANAGER";

        List<User> managers = ur.findUsersByRoleOrderById(role);

        assertEquals(3, managers.size());
    }

}
