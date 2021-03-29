package com.bobocode.mvc.data;


import com.bobocode.mvc.model.Note;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
public class Notes {
    private final Map<UUID, Note> notes = new LinkedHashMap<>();

    public List<Note> getAll(){
        return new ArrayList<>(notes.values());
    }

    public void add(Note note) {
        note.setId(UUID.randomUUID());
        note.setCreationDate(LocalDate.now());
        notes.put(note.getId(), note);
    }
}
