package com.mikhail.tarasevich.eventmanager.repository;

import com.mikhail.tarasevich.eventmanager.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface EventRepository extends JpaRepository<Event, Integer> {

    List<Event> findEventsByUserIdOrderById(int id);

    void deleteEventsByUserId(int userId);

}
