package com.bobocode.mvc.controller;

import com.bobocode.mvc.model.Note;
import com.bobocode.mvc.storage.Notes;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notes")
public class NoteController {

    private final Notes notes;

    @GetMapping
    public String getNotes(Model model) {
        model.addAttribute("notes", notes.getAll());
        model.addAttribute("newNote", new Note());
        return "notes";
    }

    @PostMapping
    public String addNote(@ModelAttribute Note note) {
        notes.add(note);
        return "redirect:/notes";
    }
}
