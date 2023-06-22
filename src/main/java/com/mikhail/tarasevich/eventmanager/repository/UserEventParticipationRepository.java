package com.mikhail.tarasevich.eventmanager.repository;

import com.mikhail.tarasevich.eventmanager.entity.UserEventParticipation;
import com.mikhail.tarasevich.eventmanager.util.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface UserEventParticipationRepository extends JpaRepository<UserEventParticipation, Integer> {

    List<UserEventParticipation> findUserEventParticipationsByEventIdAndStatusOrderById(int eventId, Status status);

    List<UserEventParticipation> findUserEventParticipationsByUserIdOrderById(int userId);

    void deleteUserEventParticipationsByUserId (int userId);

    void deleteUserEventParticipationsByEventId (int eventId);

    @Modifying
    @Query(value = "DELETE FROM user_event_participations WHERE event_id IN (SELECT id FROM events WHERE events.user_id = :userId)", nativeQuery = true)
    void deleteUserEventParticipationsByEventUserId(@Param("userId") int userId);

}
