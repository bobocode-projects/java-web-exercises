import com.bobocode.mvc.HelloSpringMvcApp;
import com.bobocode.mvc.model.Note;
import com.bobocode.mvc.storage.Notes;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(classes = HelloSpringMvcApp.class)
public class RestControllerTest {

    @Autowired
    private Notes notes;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void statusIsOkWhenGetMethodCalled() throws Exception {
        mockMvc.perform(get("/api/notes"))
                .andExpect(status().isOk());
    }

    @Test
    void applicationTypeIsJsonWhenGetMethodCalled() throws Exception {
        mockMvc.perform(get("/api/notes"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void returnValidDataWhenGetMethodCalled() throws Exception {
        fillNotes(new Note("title 1", "text 1"), new Note("title 2", "text 2"));
        mockMvc.perform(get("/api/notes"))
                .andExpect(jsonPath("$[0].title", is("title 1")))
                .andExpect(jsonPath("$[0].text", is("text 1")))
                .andExpect(jsonPath("$[1].title", is("title 2")))
                .andExpect(jsonPath("$[1].text", is("text 2")));
    }

    @Test
    void statusIsOkWhenPostNonNullFields() throws Exception {
        mockMvc.perform(post("/api/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new Note("Title", "Text")))
        )
                .andExpect(status().isOk());
    }

    @Test
    void statusIs400WhenRequiredFieldsAreEmpty() throws Exception {
        mockMvc.perform(post("/api/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new Note()))
        )
                .andExpect(status().is4xxClientError());
    }

    @Test
    void addNoteWhenPostMethodCalled() throws Exception {
        assertTrue(notes.getAll().isEmpty());
        Note note = new Note("Title", "Text");

        mockMvc.perform(post("/api/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(note))
        );

        assertFalse(notes.getAll().isEmpty());
        assertEquals(notes.getAll().size(), 1);
    }

    @SneakyThrows
    private String asJsonString(Object object) {
        return new ObjectMapper().writeValueAsString(object);
    }

    private void fillNotes(Note... notes) {
        for (Note note : notes) {
            this.notes.add(note);
        }
    }
}

