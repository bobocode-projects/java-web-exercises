package com.bobocode.mvc.controller;

import com.bobocode.mvc.model.Note;
import com.bobocode.mvc.storage.Notes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notes")
public class NoteController {

    private final Notes notes;

    @GetMapping
    public String getNotes() {
        return "notes";
    }

    @PostMapping
    public String addNote(Note note) {
        notes.add(note);
        return "redirect:/notes";
    }

    @ModelAttribute("note")
    private Note getNoteAttr() {
        return new Note();
    }

    @ModelAttribute("notes")
    private List<Note> getNotesAttr() {
        return notes.getAll();
    }
}
