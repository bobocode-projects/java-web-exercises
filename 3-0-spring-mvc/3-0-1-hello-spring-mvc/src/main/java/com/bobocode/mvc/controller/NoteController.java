package com.bobocode.mvc.controller;

import com.bobocode.mvc.storage.Notes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/notes")
public class NoteController {
    private final Notes notes;


}
