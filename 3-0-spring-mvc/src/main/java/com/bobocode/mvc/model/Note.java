package com.bobocode.mvc.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class Note {
    private UUID id = UUID.randomUUID();
    private String title;
    private String text;
    private LocalDate creationDate = LocalDate.now();
}
