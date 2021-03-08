package com.bobocode.mvc.storage;

import com.bobocode.mvc.model.Note;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class NoteStorage {
    private final Map<UUID, Note> notes = new HashMap<>();

    public List<Note> getAllNotes(){
        return new ArrayList<>(notes.values());
    }

    public void addNote(Note note) {
        notes.put(note.getId(), note);
    }
}
