package com.bobocode.mvc.api;

import com.bobocode.mvc.data.Notes;
import lombok.RequiredArgsConstructor;

/**
 * This controller provides a very simple REST API for Notes. It implements two endpoints that allow you to add
 * a new note and get all notes.
 * <p>
 * The base URL is `/api/notes`. It accepts HTTP GET request to get all notes, and POST request to add a new note.
 * <p>
 * Both methods work with content type JSON. The endpoint that returns all notes does not require any input parameters,
 * while the one that adds a new note accepts a JSON with a new note fields in the request body. In order to get or
 * add a note, just use a provided {@link Notes} field as a storage.
 * <p>
 * This controller can only be used by a separate front-end application, since it provides only data and no UI. It shows
 * how Spring MVC is used nowadays to build enterprise web application that have separate front-end. But initially
 * Spring MVC was used to build the whole application including front-end. So the controllers were connected to the views
 * via models, like in {@link com.bobocode.mvc.controller.NoteController}
 */
@RequiredArgsConstructor
public class NoteRestController {
    private final Notes notes;

    // TODO: implement controller methods according to the javadoc verify your impl using NoteRestControllerTest
}
