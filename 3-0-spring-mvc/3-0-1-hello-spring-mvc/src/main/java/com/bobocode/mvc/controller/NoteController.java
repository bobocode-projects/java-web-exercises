package com.bobocode.mvc.controller;

import com.bobocode.mvc.model.Note;
import com.bobocode.mvc.storage.Notes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notes")
public class NoteController {
    private final Notes notes;

    @GetMapping
    public String getNotes(Model model) {
        model.addAttribute("noteList", notes.getAll());
        return "notes";
    }

    @PostMapping
    public String addNote(Note note) {
        notes.add(note);
        return "redirect:/notes";
    }
}