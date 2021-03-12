import com.bobocode.mvc.HelloSpringMvcApp;
import com.bobocode.mvc.model.Note;
import com.bobocode.mvc.storage.Notes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = HelloSpringMvcApp.class)
@AutoConfigureMockMvc
public class ControllerTest {

    @Autowired
    private Notes notes;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void returnNoteListModelWhenGetRequestPerformed() throws Exception {
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
    void requestContainsEmptyNoteModelWhenPerformGet() throws Exception {
        mockMvc.perform(get("/notes"))
                .andExpect(model().attributeExists("newNote"));
    }

    @Test
    void addNoteToNotesListWhenPerformPostRequest() throws Exception {
        assertTrue(notes.getAll().isEmpty());

        mockMvc.perform(post("/notes")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .content(String.valueOf(model().attribute("note", new Note("title", "text"))))
        );

        assertFalse(notes.getAll().isEmpty());
    }

    @Test
    void noteHasIdWhenSaveItToStorageAfterPost() throws Exception {
        mockMvc.perform(post("/notes")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .content(
                        String.valueOf(model().attribute("newNote", new Note("title", "text")))
                )
        );

        assertNotNull(notes.getAll().get(0).getId());
    }

    @Test
    void noteHasDateWhenSaveItToStorageAfterPost() throws Exception {
        mockMvc.perform(post("/notes")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .content(
                        String.valueOf(model().attribute("newNote", new Note("title", "text")))
                )
        );

        assertNotNull(notes.getAll().get(0).getCreationDate());
    }

    private void fillNotes(Note... notes) {
        for (Note note : notes) {
            this.notes.add(note);
        }
    }
}
