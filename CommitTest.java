//import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
//import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.jupiter.api.Test;

public class CommitTest {

    @Test
    void testConstructor() throws Exception {
        Commit testCom = new Commit("", "Bob", "this is a test");
        String sha = testCom.getSha();

        String dirName = "./objects/";
        File dir = new File (dirName);
        File check = new File(dir,sha);

        //how do i check if time keeps changing?
        assertTrue(check.exists());
    }

    @Test
    void testConstructor2() throws Exception {
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

    private static void createFile(String directoryPath, String fileContents) throws IOException {
        File file = new File(directoryPath);
        file.createNewFile();
        FileWriter fw = new FileWriter(directoryPath);
        PrintWriter pw = new PrintWriter(fw);
        pw.print(fileContents);
        fw.close();
        pw.close();
    }

    @Test
        void testCReatingCommit1() throws Exception {
                File test1 = new File("test1");
                createFile("test1", "testing1");
                File test2 = new File("test2");
                createFile("test2", "testing2");
                Index index = new Index();
                index.init();
                index.addBlob("test1");
                index.addBlob("test2");
                Commit c0 = new Commit("Bob", "this is a test");

                String test1Sha = c0.encryptPassword(index.fileToString("test1"));
                String test2Sha = c0.encryptPassword(index.fileToString("test2"));
                String treeContents = "blob : " + test2Sha + " : test2\nblob : " + test1Sha + " : test1";
                String treeSha = c0.encryptPassword(treeContents);
                Date date = new Date(java.lang.System.currentTimeMillis());
                String commitContents = treeSha + "\n\n\nBob\n" + date+ "\n" + "this is a test";
                String commitSha = c0.encryptPassword(commitContents);
                File treeFile = new File("./objects/" + treeSha);
                File commitFile = new File("./objects/" + commitSha);
                assertTrue("tree does not exist", treeFile.exists());
                assertEquals("tree has wrong contents", index.fileToString("./objects/" + treeSha),treeContents);

                assertTrue("commit does not exist", commitFile.exists());
                assertEquals("commit has wrong contents", index.fileToString("./objects/" + commitSha),commitContents);
        }
    /*
    @Test
    public void testConstructorThreeCommits() throws Exception
    {
        Index ind = new Index();
        Commit parentCom = new Commit("Bob", "this is a test");
        String shaOfParent = parentCom.getSha();
        String parentCommitContents = ind.fileToString("./objects/" + shaOfParent);
        int i = parentCommitContents.lastIndexOf("\n");
        int j = Commit.ordinalIndexOf(parentCommitContents, "\n", 4);
        Commit commitWithPrevCommit = new Commit(shaOfParent, "Wyatt", "this is not a test");
        String shaOfChild = commitWithPrevCommit.getSha();
        Commit thirdCommit = new Commit(shaOfChild, "Willy", "explosions");

        // Testing if the childSha is updated for the Parent
        // Read thu parent commit
        // Get third line
        // Verify theres actually a SHA there
        parentCommitContents = ind.fileToString("./objects/" + shaOfParent);

        
        //System.out.println(sha);
        String dirName = "./objects/";
        File dir = new File (dirName);
        File check = new File(dir,shaOfChild);

        //how do i check if time keeps changing?
        //File f = new File(testCom.getSha());
        assertTrue(check.exists());
        //assertTrue("d5af8be8c2a1c1163e51f44e8631247217b0e4ca".equals(parentCom.encryptPassword(parentCom.getContents((f)))));
        
    }
*/
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
