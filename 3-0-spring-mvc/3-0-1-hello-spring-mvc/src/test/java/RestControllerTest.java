import com.bobocode.mvc.HelloSpringMvcApp;
import com.bobocode.mvc.data.Notes;
import com.bobocode.mvc.model.Note;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = HelloSpringMvcApp.class)
public class RestControllerTest {

    @Autowired
    private Notes notes;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllNotes() throws Exception {
        notes.add(new Note("Title 1", "Text 1"));

        mockMvc.perform(get("/api/notes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        int lastElementIndex = notes.getAll().size() - 1;

        assertEquals("Title 1", notes.getAll().get(lastElementIndex).getTitle());
        assertEquals("Text 1", notes.getAll().get(lastElementIndex).getText());
    }

    @Test
    void addNote() throws Exception {
        Note note = new Note("Title 2", "Title 2");
        mockMvc.perform(post("/api/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(note))
        )
                .andExpect(status().isOk());

        int lastElementIndex = notes.getAll().size() - 1;

        assertEquals("Title 2", notes.getAll().get(lastElementIndex).getTitle());
        assertEquals("Title 2", notes.getAll().get(lastElementIndex).getText());
    }

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
}

