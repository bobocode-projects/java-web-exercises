package com.bobocode.mvc.controller;

import com.bobocode.mvc.model.Note;
import com.bobocode.mvc.service.NoteService;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Data
@Controller
@RequestMapping("/notes")
public class NoteController {

    private final NoteService service;

    @GetMapping
    public String getNotes(Model model) {
        model.addAttribute("notes", service.getAllNotes());
        model.addAttribute("newNote", new Note());
        return "notes";
    }

    @PostMapping
    public String addNote(@ModelAttribute Note note) {
        service.addNote(note);
        return "redirect:/notes";
    }
}
