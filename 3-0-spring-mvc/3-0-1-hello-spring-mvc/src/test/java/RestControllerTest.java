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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
    void getAll() throws Exception {
        fillNotes(new Note("title 1", "text 1"), new Note("title 2", "text 2"));

        mockMvc.perform(get("/api/notes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title", is("title 1")))
                .andExpect(jsonPath("$[0].text", is("text 1")))
                .andExpect(jsonPath("$[1].title", is("title 2")))
                .andExpect(jsonPath("$[1].text", is("text 2")));
    }

    @Test
    void addNote() throws Exception {
        Note note = new Note("Note", "Note text");

        assertTrue(notes.getAll().isEmpty());
        mockMvc.perform(post("/api/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(note))
        )
                .andExpect(status().isOk());

        assertEquals(note.getTitle(), notes.getAll().get(0).getTitle());
        assertEquals(note.getText(), notes.getAll().get(0).getText());
    }

    @Test
    void statusIs4xxWhenRequiredFieldsAreEmpty() throws Exception {
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

    private void fillNotes(Note... notes) {
        for (Note note : notes) {
            this.notes.add(note);
        }
    }

}

