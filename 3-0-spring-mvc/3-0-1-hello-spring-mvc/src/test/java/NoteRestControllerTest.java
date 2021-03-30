import com.bobocode.mvc.HelloSpringMvcApp;
import com.bobocode.mvc.api.NoteRestController;
import com.bobocode.mvc.controller.NoteController;
import com.bobocode.mvc.data.Notes;
import com.bobocode.mvc.model.Note;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = HelloSpringMvcApp.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NoteRestControllerTest {

    @MockBean
    private Notes notes;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NoteRestController controller;

    @Order(1)
    @Test
    void classIsMarkedAsRestController() {
        var restControllerAnnotation = NoteRestController.class.getAnnotation(RestController.class);

        assertNotNull(restControllerAnnotation);
    }

    @Order(2)
    @Test
    void requestMappingIsSpecified() {
        var requestMappingAnnotation = NoteRestController.class.getAnnotation(RequestMapping.class);
        var urlMapping = extractUrlMapping(requestMappingAnnotation);

        assertThat(urlMapping).isEqualTo("/api/notes");
    }

    @Order(3)
    @Test
    void getMappingIsImplemented() {
        var foundMethodWithGetMapping = Arrays.stream(NoteRestController.class.getDeclaredMethods())
                .anyMatch(
                        method -> Arrays.stream(method.getDeclaredAnnotations())
                                .anyMatch(a -> a.annotationType().equals(GetMapping.class))
                );

        assertTrue(foundMethodWithGetMapping);
    }

    @Order(4)
    @Test
    void getNotesMethodHasNoParameters() {
        var getNotesMethod = Arrays.stream(NoteRestController.class.getDeclaredMethods())
                .filter(
                        method -> Arrays.stream(method.getDeclaredAnnotations())
                                .anyMatch(a -> a.annotationType().equals(GetMapping.class))
                )
                .findAny()
                .orElseThrow();

        assertThat(getNotesMethod.getParameterCount()).isZero();
    }

    @Order(5)
    @Test
    @SneakyThrows
    void getNotesMethodReturnsNoteList() {
        var noteList = givenNoteList();
        var getNotesMethod = Arrays.stream(NoteRestController.class.getDeclaredMethods())
                .filter(
                        method -> Arrays.stream(method.getDeclaredAnnotations())
                                .anyMatch(a -> a.annotationType().equals(GetMapping.class))
                )
                .findAny()
                .orElseThrow();

        var response = getNotesMethod.invoke(controller);

        assertThat(response).isEqualTo(noteList);
    }

    @Order(6)
    @Test
    void getNotes() throws Exception {
        List<Note> noteList = List.of(
                new Note("Test", "Hello, World!"),
                new Note("Greeting", "Hey")
        );
        when(notes.getAll()).thenReturn(noteList);

        mockMvc.perform(get("/api/notes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[\n" +
                        "  {\n" +
                        "    \"title\": \"Test\",\n" +
                        "    \"text\": \"Hello, World!\"\n" +
                        "  },\n" +
                        "  {\n" +
                        "    \"title\": \"Greeting\",\n" +
                        "    \"text\": \"Hey\"\n" +
                        "  }\n" +
                        "]"));
    }

    @Order(7)
    @Test
    void postMappingIsImplemented() {
        var foundMethodWithGetMapping = Arrays.stream(NoteRestController.class.getDeclaredMethods())
                .anyMatch(
                        method -> Arrays.stream(method.getDeclaredAnnotations())
                                .anyMatch(a -> a.annotationType().equals(PostMapping.class))
                );

        assertTrue(foundMethodWithGetMapping);
    }

    @Order(8)
    @Test
    void addNoteMethodAcceptsNewNoteAsParameter() {
        var addNoteMethod = Arrays.stream(NoteRestController.class.getDeclaredMethods())
                .filter(
                        method -> Arrays.stream(method.getDeclaredAnnotations())
                                .anyMatch(a -> a.annotationType().equals(PostMapping.class))
                )
                .findAny()
                .orElseThrow();

        assertThat(addNoteMethod.getParameterCount()).isEqualTo(1);
        assertThat(addNoteMethod.getParameterTypes()[0]).isEqualTo(Note.class);

    }

    @Order(9)
    @Test
    void addNoteMethodParameterIsMarkedAsRequestBody() {
        var addNoteMethod = Arrays.stream(NoteRestController.class.getDeclaredMethods())
                .filter(
                        method -> Arrays.stream(method.getDeclaredAnnotations())
                                .anyMatch(a -> a.annotationType().equals(PostMapping.class))
                )
                .findAny()
                .orElseThrow();

        var noteParam = addNoteMethod.getParameters()[0];
        var requestBodyAnnotation = noteParam.getDeclaredAnnotation(RequestBody.class);

        assertNotNull(requestBodyAnnotation);
    }

    @Order(10)
    @Test
    @SneakyThrows
    void addNoteMethodReturnsVoid() {
        var addNoteMethod = Arrays.stream(NoteRestController.class.getDeclaredMethods())
                .filter(
                        method -> Arrays.stream(method.getDeclaredAnnotations())
                                .anyMatch(a -> a.annotationType().equals(PostMapping.class))
                )
                .findAny()
                .orElseThrow();


        var returnType = addNoteMethod.getReturnType();

        assertThat(returnType).isEqualTo(Void.TYPE);
    }

    @Order(11)
    @Test
    @SneakyThrows
    void addNotePassPostedNote() {
        var note = new Note("Test", "Hello, World!");
        var addNoteMethod = Arrays.stream(NoteRestController.class.getDeclaredMethods())
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
    void addNote() throws Exception {
        Note note = new Note("Test", "Hello, World!");

        mockMvc.perform(post("/api/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(note))
        )
                .andExpect(status().isOk());

        verify(notes).add(note);
    }

    @Order(13)
    @Test
    void addNoteRespondWithClientErrorWhenFieldsAreEmpty() throws Exception {
        mockMvc.perform(post("/api/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new Note()))
        )
                .andExpect(status().is4xxClientError());
    }

    @SneakyThrows
    private String asJsonString(Object object) {
        return new ObjectMapper().writeValueAsString(object);
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

