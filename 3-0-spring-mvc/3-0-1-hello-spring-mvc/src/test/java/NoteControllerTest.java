import com.bobocode.mvc.HelloSpringMvcApp;
import com.bobocode.mvc.model.Note;
import com.bobocode.mvc.data.Notes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
        notes.add(new Note("Title 1", "Text 1"));

        mockMvc.perform(get("/notes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("noteList"))
                .andExpect(model().attribute("noteList", notes.getAll()));
    }

    @Test
    void addNote() throws Exception {
        Note note = new Note("Title 2", "Text 2");
        assertTrue(notes.getAll().isEmpty());

        mockMvc.perform(post("/notes")
                .param("title", note.getTitle())
                .param("text", note.getText())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/notes"));

        int lastElementIndex = notes.getAll().size() - 1;

        assertEquals("Title 2", notes.getAll().get(lastElementIndex).getTitle());
        assertEquals("Text 2", notes.getAll().get(lastElementIndex).getText());
    }
}
