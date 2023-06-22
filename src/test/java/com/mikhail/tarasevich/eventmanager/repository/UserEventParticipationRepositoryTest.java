package com.mikhail.tarasevich.eventmanager.repository;

import com.mikhail.tarasevich.eventmanager.config.SpringTestConfig;
import com.mikhail.tarasevich.eventmanager.entity.UserEventParticipation;
import com.mikhail.tarasevich.eventmanager.util.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = SpringTestConfig.class)
class UserEventParticipationRepositoryTest {

    @Autowired
    private UserEventParticipationRepository uepRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Test
    void findUserEventParticipationsByEventIdAndStatusOrderById_inputEventIdAndStatus_expectedEntityList() {

        List<UserEventParticipation> foundEntities =
                uepRepository.findUserEventParticipationsByEventIdAndStatusOrderById(2, Status.PENDING);

        assertEquals(3, foundEntities.size());
        assertEquals(3, foundEntities.stream()
                .filter(e -> e.getStatus().equals(Status.PENDING)).collect(Collectors.toList()).size());
    }

    @Test
    void findUserEventParticipationsByUserIdOrderById_inputUserId_expectedEntityList() {

        int id = 5;

        List<UserEventParticipation> foundEntities =
                uepRepository.findUserEventParticipationsByUserIdOrderById(id);

        assertEquals(3, foundEntities.size());
        assertEquals(3, foundEntities.stream()
                .filter(e -> e.getUser().getId() == id).collect(Collectors.toList()).size());
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteUserEventParticipationsByUserId_inputUserId_expectedEntitiesDeleted() {

        assertEquals(3, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user_event_participations",
                "user_id = 5"));

        uepRepository.deleteUserEventParticipationsByUserId(5);

        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user_event_participations",
                "user_id = 5"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteUserEventParticipationsByEventId_inputEventId_expectedEntitiesDeleted() {

        assertEquals(3, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user_event_participations",
                "event_id = 2"));

        uepRepository.deleteUserEventParticipationsByEventId(2);

        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user_event_participations",
                "event_id = 2"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/schema.sql", "classpath:sql/data.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteUserEventParticipationsByEventUserId_inputManagerId_expectedEntitiesDeleted() {

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user_event_participations",
                "id = 2"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user_event_participations",
                "id = 3"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user_event_participations",
                "id = 4"));

        uepRepository.deleteUserEventParticipationsByEventUserId(4);

        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user_event_participations",
                "id = 2"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user_event_participations",
                "id = 3"));
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user_event_participations",
                "id = 4"));
    }

}
