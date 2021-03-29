import com.bobocode.mvc.HelloSpringMvcApp;
import com.bobocode.mvc.controller.NoteController;
import com.bobocode.mvc.data.Notes;
import com.bobocode.mvc.model.Note;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(classes = HelloSpringMvcApp.class)
public class NoteControllerTest {
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

        mockMvc.perform(get("/notes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("noteList"))
                .andExpect(model().attribute("noteList", noteList));
    }

    @Test
    void addNote() throws Exception {
        Note note = new Note("Test", "Hello, World!");

        mockMvc.perform(post("/notes")
                .param("title", note.getTitle())
                .param("text", note.getText())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/notes"));

        verify(notes).add(note);
    }
}
