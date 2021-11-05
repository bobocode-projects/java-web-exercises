package com.bobocode.mvc.controller;

import com.bobocode.mvc.data.Notes;
import lombok.RequiredArgsConstructor;

/**
 * {@link NoteController} is a typical controller that powers Spring MVC Notes application. This application provides
 * an ability to show the list of notes, and a new note using a form. The UI of the app is implemented using Thymeleaf
 * (YOU DON'T NEED TO KNOW IT). And the purpose of the controller is to power that UI and wire it to the storage that
 * is already provided as a field of class {@link Notes}. The base URL is `/notes`.
 * <p>
 * This controller handles the HTTP GET request that should list all notes. In order to do that, it fetches the list
 * of notes, adds that list as an attribute to the {@link org.springframework.ui.Model}, and returns a corresponding view.
 * The view (which is Thymeleaf HTML template) expects to receive an attribute called `noteList`.
 * In classical Spring MVC, controller methods return a string value which stores a view name.
 * A {@link org.springframework.web.servlet.ViewResolver} is already configured to look for an HTML template in
 * `/resources/templates`. So the method should just return an HTML file name without `.html`.
 * <p>
 * In order to add a new note, this controller handles HTTP POST request that is sent by HTML form. After submitting,
 * the request will have to POST parameters called `title` and `text` accordingly. So this controller retrieves those
 * params from the request and use them in order to create and add a new note. Once a new note is added, it redirects the
 * request to the notes page.
 * <p>
 * Such controllers are used when both back-end and the UI are provided by the same Spring MVC application. In reality,
 * most application that have a rich and powerful UI, are not built like that. They have separate front-end and
 * back-end applications. The back-end app, will only need to provide data and don't care about view. In that case
 * the same controller will look like {@link com.bobocode.mvc.api.NoteRestController}
 */
@RequiredArgsConstructor
public class NoteController {
    private final Notes notes;

    // TODO: implement controller methods according to the javadoc and verify your impl using NoteControllerTest

}
