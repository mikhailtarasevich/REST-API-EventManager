package com.mikhail.tarasevich.eventmanager.repository;

import com.mikhail.tarasevich.eventmanager.config.SpringTestConfig;
import com.mikhail.tarasevich.eventmanager.entity.Event;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = SpringTestConfig.class)
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Test
    void findEventsByUserIdOrderById_inputUserId_expectedEventList() {

        List<Event> foundEntities =
                eventRepository.findEventsByUserIdOrderById(2);

        assertEquals(3, foundEntities.size());
        assertEquals(List.of("Event 1", "Event 4", "Event 5"), foundEntities.stream().map(Event::getName).collect(Collectors.toList()));
    }

}
