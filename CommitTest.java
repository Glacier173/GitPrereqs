import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.jupiter.api.Test;

public class CommitTest {

    @Test
    void testConstructor() {

    }

    @Test
    void testCreateTree() throws IOException {
        Tree tree = new Tree();
        String sha = tree.getSha();
        String expectedSha = "da39a3ee5e6b4b0d3255bfef95601890afd80709";

        assertTrue(sha.equals(expectedSha));
    }

    @Test
    void testGetDate() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        //um how do i test if the time keeps changing
        assertTrue(timeStamp.equals("20230921_195130"));
    }

    @Test
    void testRename() {
        
    }
}
