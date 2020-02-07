package com.mesung.demospringmvc;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
public class Event {

    private String name;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private int limitOfEnrollment;
}
