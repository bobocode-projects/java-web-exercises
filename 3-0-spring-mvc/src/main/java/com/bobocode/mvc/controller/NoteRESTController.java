package com.bobocode.mvc.controller;

import com.bobocode.mvc.model.Note;
import com.bobocode.mvc.service.NoteService;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
@RestController
@RequestMapping("api/v1/notes")
public class NoteRESTController {

    private final NoteService service;

    @GetMapping
    public List<Note> getNotes() {
        return service.getAllNotes();
    }

    @PostMapping
    public void addNote(@RequestBody Note note) {
        service.addNote(note);
    }
}
