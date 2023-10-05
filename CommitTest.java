//import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.jupiter.api.Test;

public class CommitTest {

    @Test
    void testConstructor() throws IOException {
        Commit testCom = new Commit("732d12f7e4f2e629e2954acbb720c32c0be985d1", null, "Bob", "this is a test");
        String sha = testCom.getSha();

        String dirName = "./objects/";
        File dir = new File (dirName);
        File check = new File(dir,sha);

        //how do i check if time keeps changing?
        assertTrue(check.exists());
    }

    @Test
    void testCreateTree() throws IOException {
        Tree tree = new Tree();
        String sha = tree.getSha();
        String expectedSha = "da39a3ee5e6b4b0d3255bfef95601890afd80709";

        assertTrue(sha.equals(expectedSha));
    }

    /*@Test
    void testGetDate() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        //um how do i test if the time keeps changing
        assertTrue(timeStamp.equals("20230921_195130"));
    }*/

}
