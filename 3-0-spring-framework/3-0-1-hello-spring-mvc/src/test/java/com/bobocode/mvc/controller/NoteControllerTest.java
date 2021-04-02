package com.bobocode.mvc.controller;

import com.bobocode.mvc.HelloSpringMvcApp;
import com.bobocode.mvc.controller.NoteController;
import com.bobocode.mvc.data.Notes;
import com.bobocode.mvc.model.Note;
import lombok.SneakyThrows;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(classes = HelloSpringMvcApp.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NoteControllerTest {
    @MockBean
    private Notes notes;

    @Autowired
    private NoteController controller;

    @Autowired
    private MockMvc mockMvc;

    @Order(1)
    @Test
    void classIsMarkedAsController() {
        var controllerAnnotation = NoteController.class.getAnnotation(Controller.class);

        assertNotNull(controllerAnnotation);
    }

    @Order(2)
    @Test
    void requestMappingIsSpecified() {
        var requestMappingAnnotation = NoteController.class.getAnnotation(RequestMapping.class);
        var urlMapping = extractUrlMapping(requestMappingAnnotation);

        assertThat(urlMapping).isEqualTo("/notes");
    }

    @Order(3)
    @Test
    void getMappingIsImplemented() {
        var foundMethodWithGetMapping = Arrays.stream(NoteController.class.getDeclaredMethods())
                .anyMatch(
                        method -> Arrays.stream(method.getDeclaredAnnotations())
                                .anyMatch(a -> a.annotationType().equals(GetMapping.class))
                );

        assertTrue(foundMethodWithGetMapping);
    }

    @Order(4)
    @Test
    void getNotesMethodAcceptsModelAsParameter() {
        var getNotesMethod = Arrays.stream(NoteController.class.getDeclaredMethods())
                .filter(
                        method -> Arrays.stream(method.getDeclaredAnnotations())
                                .anyMatch(a -> a.annotationType().equals(GetMapping.class))
                )
                .findAny()
                .orElseThrow();

        assertThat(getNotesMethod.getParameterCount()).isEqualTo(1);
        assertThat(getNotesMethod.getParameterTypes()[0]).isEqualTo(Model.class);
    }

    @Order(5)
    @Test
    @SneakyThrows
    void getNotesMethodReturnNotesViewName() {
        var getNotesMethod = Arrays.stream(controller.getClass().getDeclaredMethods())
                .filter(
                        method -> Arrays.stream(method.getDeclaredAnnotations())
                                .anyMatch(a -> a.annotationType().equals(GetMapping.class))
                )
                .findAny()
                .orElseThrow();

        var viewName = getNotesMethod.invoke(controller, new BindingAwareModelMap());

        assertThat(viewName).isEqualTo("notes");
    }

    @Order(6)
    @Test
    @SneakyThrows
    void getNotesMethodAddsNoteListToTheModel() {
        var noteList = givenNoteList();
        var model = new BindingAwareModelMap();
        var getNotesMethod = Arrays.stream(controller.getClass().getDeclaredMethods())
                .filter(
                        method -> Arrays.stream(method.getDeclaredAnnotations())
                                .anyMatch(a -> a.annotationType().equals(GetMapping.class))
                )
                .findAny()
                .orElseThrow();

        getNotesMethod.invoke(controller, model);

        assertThat(model.get("noteList")).isEqualTo(noteList);
    }

    @Order(7)
    @Test
    void getNotes() throws Exception {
        var noteList = givenNoteList();

        mockMvc.perform(get("/notes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("noteList"))
                .andExpect(model().attribute("noteList", noteList));
    }

    @Order(8)
    @Test
    void postMappingIsImplemented() {
        var foundMethodWithGetMapping = Arrays.stream(NoteController.class.getDeclaredMethods())
                .anyMatch(
                        method -> Arrays.stream(method.getDeclaredAnnotations())
                                .anyMatch(a -> a.annotationType().equals(PostMapping.class))
                );

        assertTrue(foundMethodWithGetMapping);
    }

    @Order(9)
    @Test
    void addNoteMethodAcceptsNewNoteAsParameter() {
        var addNoteMethod = Arrays.stream(NoteController.class.getDeclaredMethods())
                .filter(
                        method -> Arrays.stream(method.getDeclaredAnnotations())
                                .anyMatch(a -> a.annotationType().equals(PostMapping.class))
                )
                .findAny()
                .orElseThrow();

        assertThat(addNoteMethod.getParameterCount()).isEqualTo(1);
        assertThat(addNoteMethod.getParameterTypes()[0]).isEqualTo(Note.class);

    }

    @Order(10)
    @Test
    @SneakyThrows
    void addNoteMethodReturnsRedirectToNotes() {
        var addNoteMethod = Arrays.stream(NoteController.class.getDeclaredMethods())
                .filter(
                        method -> Arrays.stream(method.getDeclaredAnnotations())
                                .anyMatch(a -> a.annotationType().equals(PostMapping.class))
                )
                .findAny()
                .orElseThrow();


        var response = addNoteMethod.invoke(controller, new Note("Test", "Hello, World!"));

        assertThat(response).isEqualTo("redirect:/notes");
    }

    @Order(11)
    @Test
    @SneakyThrows
    void addNotePassPostedNote() {
        var note = new Note("Test", "Hello, World!");
        var addNoteMethod = Arrays.stream(NoteController.class.getDeclaredMethods())
                .filter(
                        method -> Arrays.stream(method.getDeclaredAnnotations())
                                .anyMatch(a -> a.annotationType().equals(PostMapping.class))
                )
                .findAny()
                .orElseThrow();


        addNoteMethod.invoke(controller, note);

        verify(notes).add(note);
    }

    @Order(12)
    @Test
    @SneakyThrows
    void addNote() {
        Note note = new Note("Test", "Hello, World!");

        mockMvc.perform(post("/notes")
                .param("title", note.getTitle())
                .param("text", note.getText())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/notes"));

        verify(notes).add(note);
    }

    private String extractUrlMapping(RequestMapping requestMapping) {
        if (requestMapping.value().length > 0) {
            return requestMapping.value()[0];
        } else {
            return requestMapping.path()[0];
        }
    }

    private List<Note> givenNoteList() {
        List<Note> noteList = List.of(
                new Note("Test", "Hello, World!"),
                new Note("Greeting", "Hey")
        );
        when(notes.getAll()).thenReturn(noteList);
        return noteList;
    }
}
