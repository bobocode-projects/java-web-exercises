package com.bobocode.mvc.data;

import com.bobocode.mvc.model.Note;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class Notes {
    private final Map<UUID, Note> notesMap = new HashMap<>();

    public List<Note> getAll(){
        var noteList = new ArrayList<>(notesMap.values());
        noteList.sort(Comparator.comparing(Note::getCreatedOn));
        return noteList;
    }

    public void add(Note note) {
        note.setId(UUID.randomUUID());
        note.setCreatedOn(LocalDateTime.now());
        notesMap.put(note.getId(), note);
    }
}
