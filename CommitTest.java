//import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.jupiter.api.Test;

public class CommitTest {

    @Test
    void testConstructor() throws IOException {
        Commit testCom = new Commit("", "Bob", "this is a test");
        String sha = testCom.getSha();

        String dirName = "./objects/";
        File dir = new File (dirName);
        File check = new File(dir,sha);

        //how do i check if time keeps changing?
        assertTrue(check.exists());
    }

    @Test
    void testConstructor2() throws IOException {
        Index ind = new Index();
        Commit parentCom = new Commit("Bob", "this is a test");
        String shaOfParent = parentCom.getSha();
        String parentCommitContents = ind.fileToString("./objects/" + shaOfParent);
        int i = parentCommitContents.lastIndexOf("\n");
        int j = Commit.ordinalIndexOf(parentCommitContents, "\n", 4);
        Commit commitWithPrevCommit = new Commit(shaOfParent, "Wyatt", "this is not a test");

        // Testing if the childSha is updated for the Parent
        // Read thu parent commit
        // Get third line
        // Verify theres actually a SHA there
        parentCommitContents = ind.fileToString("./objects/" + shaOfParent);

        
        //System.out.println(sha);
        String dirName = "./objects/";
        File dir = new File (dirName);
        File check = new File(dir,shaOfParent);

        //how do i check if time keeps changing?
        //File f = new File(testCom.getSha());
        assertTrue(check.exists());
        //assertTrue("d5af8be8c2a1c1163e51f44e8631247217b0e4ca".equals(parentCom.encryptPassword(parentCom.getContents((f)))));
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
