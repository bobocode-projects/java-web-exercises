package com.bobocode.mvc.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Note {
    private UUID id;
    @NonNull
    private String title;
    @NonNull
    private String text;
    private LocalDateTime createdOn;

    public Note(String title, String text) {
        this.title = title;
        this.text = text;
    }
}
