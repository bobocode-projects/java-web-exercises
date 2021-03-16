import com.bobocode.mvc.HelloSpringMvcApp;
import com.bobocode.mvc.model.Note;
import com.bobocode.mvc.storage.Notes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = HelloSpringMvcApp.class)
@AutoConfigureMockMvc
public class NoteControllerTest {

    @Autowired
    private Notes notes;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllNotes() throws Exception {
        fillNotes(
                new Note("title1", "text1"),
                new Note("title2", "text2")
        );

        mockMvc.perform(get("/notes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().size(2))
                .andExpect(model().attributeExists("notes"))
                .andExpect(model().attribute("notes", notes.getAll()));
    }

    @Test
    void addNote() throws Exception {
        Note note = new Note("note", "text");
        assertTrue(notes.getAll().isEmpty());

        mockMvc.perform(post("/notes")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("title", note.getTitle())
                .param("text", note.getText())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/notes"));

        assertEquals(1, notes.getAll().size());
        assertEquals("note", notes.getAll().get(0).getTitle());
        assertEquals("text", notes.getAll().get(0).getText());
    }

    private void fillNotes(Note... notes) {
        for (Note note : notes) {
            this.notes.add(note);
        }
    }
}
