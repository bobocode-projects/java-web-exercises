import com.bobocode.mvc.HelloSpringMvcApp;
import com.bobocode.mvc.data.Notes;
import com.bobocode.mvc.model.Note;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = HelloSpringMvcApp.class)
public class RestControllerTest {

    @MockBean
    private Notes notes;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllNotes() throws Exception {
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

