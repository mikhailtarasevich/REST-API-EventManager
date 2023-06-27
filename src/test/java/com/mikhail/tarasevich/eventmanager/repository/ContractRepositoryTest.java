package com.mikhail.tarasevich.eventmanager.repository;

import com.mikhail.tarasevich.eventmanager.config.SpringTestConfig;
import com.mikhail.tarasevich.eventmanager.entity.Contract;
import com.mikhail.tarasevich.eventmanager.util.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = SpringTestConfig.class)
class ContractRepositoryTest {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void findByIdOrderById_inputUserId_expectedEntity() {

        int id = 2;

        Optional<Contract> foundEntity =
                contractRepository.findById(id);

        assertTrue(foundEntity.isPresent());
        assertEquals(id, foundEntity.get().getId());
    }

    @Test
    void findContractByUserIdAndStatus_inputUserIdAndStatus_expectedEntityList() {

        int userId = 2;

        List<Contract> foundEntities = contractRepository.findContractByUserIdAndStatusOrderById(userId, Status.PENDING);

        assertEquals(1, foundEntities.size());
        assertEquals(1, foundEntities.get(0).getId());
    }

    @Test
    void findContractByStatusOrderById_inputStatus_expectedEntityList() {

        List<Contract> foundEntities = contractRepository.findContractByStatusOrderById(Status.PENDING);

        assertEquals(1, foundEntities.size());
        assertEquals(1, foundEntities.get(0).getId());
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteContractsByUserId_inputUserId_expectedEntitiesDeleted() {

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "contracts",
                "user_id = 2"));

        contractRepository.deleteContractsByUserId(2);

        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "contracts",
                "user_id = 2"));
    }

}
