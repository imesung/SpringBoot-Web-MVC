package com.mesung.demospringmvc;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {

    public List<Event> getEvent() {
        Event event1 = Event.builder()
                .name("Spring Web MVC study 1")
                .limitOfEnrollment(5)
                .startDateTime(LocalDateTime.of(2020, 2, 5, 10, 0))
                .startDateTime(LocalDateTime.of(2020, 2, 5, 11, 0))
                .build();

        Event event2 = Event.builder()
                .name("Spring Web MVC study 2")
                .limitOfEnrollment(5)
                .startDateTime(LocalDateTime.of(2020, 2, 5, 10, 0))
                .startDateTime(LocalDateTime.of(2020, 2, 5, 11, 0))
                .build();

        return List.of(event1, event2);
    }
}
