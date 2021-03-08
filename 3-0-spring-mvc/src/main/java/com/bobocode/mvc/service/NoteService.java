package com.bobocode.mvc.service;

import com.bobocode.mvc.model.Note;
import com.bobocode.mvc.storage.NoteStorage;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
public class NoteService {
    private final NoteStorage storage;

    public List<Note> getAllNotes() {
        return storage.getAllNotes();
    }

    public void addNote(Note note) {
        storage.addNote(note);
    }
}
