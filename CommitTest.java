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

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

public class CommitTest {

    @AfterAll
    static void cleanup() throws IOException
    {
        File cleanObj = new File("./objects");
        cleanObj.mkdir();
        for (File f : cleanObj.listFiles())
        {
            f.delete();
        }
        File f1 = new File("file1");
        File f2 = new File("file2");
        File f3 = new File("file3");
        File f4 = new File("file4");
        File f5 = new File("file5");
        File f6 = new File("file6");
        File f7 = new File("file7");
        File f8 = new File("file8");
        f1.delete();
        f2.delete();
        f3.delete();
        f4.delete();
        f5.delete();
        f6.delete();
        f7.delete();
        f8.delete();
    }

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
        void testCreatingCommit1() throws Exception {
                File file1 = new File("file1");
                createFile("file1", "fileContents1");
                File file2 = new File("file2");
                createFile("file2", "fileContents2");
                Index index = new Index();
                index.init();
                index.addBlob("file1");
                index.addBlob("file2");
                Commit c0 = new Commit("Wyatt", "this is a test");

                String shaOfFile1 = c0.encryptPassword(index.fileToString("file1"));
                String shaOfFile2 = c0.encryptPassword(index.fileToString("file2"));
                String contentsOfTree = "blob : " + shaOfFile2 + " : file2" + "\n" + "blob : " + shaOfFile1 + " : file1";
                String shaOfTree = c0.encryptPassword(contentsOfTree);
                Date date = new Date(java.lang.System.currentTimeMillis());
                String commitContents = shaOfTree + "\n" + "\n" + "\n" + "Wyatt" + "\n" + date + "\n" + "this is a test";
                String shaOfCommit = c0.encryptPassword(commitContents);
                File fileForTree = new File("./objects/" + shaOfTree);
                File fileForCommit = new File("./objects/" + shaOfCommit);
                assertTrue(fileForTree.exists());
                assertEquals(index.fileToString("./objects/" + shaOfTree),contentsOfTree);
                assertTrue(fileForCommit.exists());
                assertEquals(index.fileToString("./objects/" + shaOfCommit),commitContents);
        }

        @Test
        void testCreatingCommitWithFilesAndFolder() throws Exception {
                File file1 = new File("file1");
                createFile("file1", "file1Contents");//got this from addDirectoryTest.java
                File file2 = new File("file2");
                createFile("file2", "file2Contents");
                Index ind = new Index();
                ind.init();
                Date date = new Date(java.lang.System.currentTimeMillis());
                ind.addBlob("file1");
                ind.addBlob("file2");
                Commit c0 = new Commit("Wyatt", "oh wow it works maybe????");
                File file3 = new File("file3");
                createFile("file3", "file3Contents");
                File file4 = new File("file4");
                createFile("file4", "file4Contents");
                File dir = new File("dir");
                dir.mkdir();
                Index ind2 = new Index();
                ind2.init();
                ind2.addBlob("file3");
                ind2.addBlob("file4");
                ind2.addBlob("dir");
                Commit c1 = new Commit(c0.getSha(), "Wonka", "Willy chocolate");
                String file1Sha = c0.encryptPassword(ind.fileToString("file1"));
                String file2Sha = c0.encryptPassword(ind.fileToString("file2"));
                String treeContents = "blob : " + file2Sha + " : file2" + "\n" + "blob : " + file1Sha + " : file1";
                String treeSha = c0.encryptPassword(treeContents);
                String commitContents = treeSha + "\n" + "\n" + "\n" + "Wyatt" + "\n" + date + "\n" + "oh wow it works maybe????";
                String newCommitContents = treeSha + "\n" + "\n" + c1.getSha() + "\n" + "Wyatt" + "\n" + date + "\n" + "oh wow it works maybe????";
                String commitSha = c0.encryptPassword(commitContents);
                File treeFile = new File("./objects/" + treeSha);
                File commitFile = new File("./objects/" + commitSha);
                String file3Sha = c1.encryptPassword(ind.fileToString("file3"));
                String file4Sha = c1.encryptPassword(ind.fileToString("file4"));
                String treeContents1 = "tree : " + treeSha + "\n" + "blob : " + file4Sha + " : file4" + "\n" + "blob : " + file3Sha + " : file3" + "\n" + "tree : da39a3ee5e6b4b0d3255bfef95601890afd80709 : dir";
                String treeSha1 = c1.encryptPassword(treeContents1);
                String newCommitContents1 = treeSha1 + "\n" + c0.getSha() + "\n" + "\n" + "Wonka" + "\n" + date+ "\n" + "Willy chocolate";
                String newCommitSha1 = c0.encryptPassword(newCommitContents1);
                File treeFile2 = new File("./objects/" + treeSha1);
                File commitFile2 = new File("./objects/" + newCommitSha1);
                assertTrue("tree doesn't exist", treeFile.exists());
                assertEquals("contents of tree wrong", ind.fileToString("./objects/" + treeSha), treeContents);
                assertTrue("commit1 doesn't exist", commitFile.exists());
                assertEquals("contents of this commit wrong", ind.fileToString("./objects/" + commitSha), newCommitContents);
                assertTrue("tree doesn't exist", treeFile2.exists());
                assertEquals("contents of tree wrong", ind.fileToString("./objects/" + treeSha1), treeContents1);
                assertTrue("commit2 doesn't exist", commitFile2.exists());
                assertEquals("contents of this commit wrong", ind.fileToString("./objects/" + newCommitSha1), newCommitContents1);
        }

        @Test
        void testCreatingFourCommits() throws Exception {
                Date date = new Date(java.lang.System.currentTimeMillis());
                File file1 = new File("file1");
                createFile("file1", "fileContents1");
                File file2 = new File("file2");
                createFile("file2", "fileContents2");
                Index ind = new Index();
                ind.init();
                ind.addBlob("file1");
                ind.addBlob("file2");
                Commit firstCommit = new Commit("Wyatt", "first commit");

                File file3 = new File("file3");
                createFile("file3", "fileContents3");
                File file4 = new File("file4");
                createFile("file4", "fileContents4");
                File dir = new File("dir");
                dir.mkdir();
                Index ind2 = new Index();
                ind2.init();
                ind2.addBlob("file3");
                ind2.addBlob("file4");
                ind2.addBlob("dir");
                Commit commit1 = new Commit(firstCommit.getSha(), "Wyatt", "second commit");
                File f1 =  new File("index");
                f1.delete();
                f1.createNewFile();
                //ind.removeBlob("dir");
                System.out.println(ind.fileToString("index"));
                File file5 = new File("file5");
                createFile("file5", "fileContents5");
                File file6 = new File("file6");
                createFile("file6", "fileContents6");
                new FileWriter("./index", false).close();
                Index ind3 = new Index();
                ind3.init();
                ind3.addBlob("file5");
                ind3.addBlob("file6");
                Commit commit2 = new Commit(commit1.getSha(), "Wyatt", "third commit");
                File f2 =  new File("index");
                f2.delete();
                f2.createNewFile();
                File file7 = new File("file7");
                createFile("file7", "fileContents7");
                File file8 = new File("file8");
                createFile("file8", "fileContents8");
                File dir2 = new File("dir2");
                dir2.mkdir();
                Index ind4 = new Index();
                ind4.init();
                ind4.addBlob("file7");
                ind4.addBlob("file8");
                ind4.addBlob("dir2");
                Commit commit3 = new Commit(commit2.getSha(), "Wyatt", "fourth commit");
                File f3 =  new File("index");
                f3.delete();
                f3.createNewFile();
                //
                /*String shaOfParent = parentCom.getSha();
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
                */
                String shaOfFirstFile = firstCommit.encryptPassword(ind.fileToString("file1"));
                String shaOfSecondFile = firstCommit.encryptPassword(ind2.fileToString("file2"));
                String contentsOfTreeForFirstCommit = "blob : " + shaOfSecondFile + " : file2" + "\n" + "blob : " + shaOfFirstFile + " : file1";
                String shaOfTreeForFirstCommit = firstCommit.encryptPassword(contentsOfTreeForFirstCommit);
                String contentsForFirstCommit = shaOfTreeForFirstCommit + "\n" + "\n" + "\n" + "Wyatt" + "\n" + date + "\n" + "first commit";
                String contentsOfFirstCommitAfterSecondCommit = shaOfTreeForFirstCommit + "\n" + "\n" + commit1.getSha() + "\n" + "Wyatt" + "\n" + date + "\n" + "first commit";
                String shaOfFirstCommit = firstCommit.encryptPassword(contentsForFirstCommit);
                File fileOfTreeForFirstCommit = new File("./objects/" + shaOfTreeForFirstCommit);
                File fileOfFirstCommit = new File("./objects/" + shaOfFirstCommit);
                String shaOfThirdFile = firstCommit.encryptPassword(ind.fileToString("file3"));
                String shaOfFourthFile = firstCommit.encryptPassword(ind.fileToString("file4"));
                String contentsOfTreeForSecondCommit = "tree : " + shaOfTreeForFirstCommit + "\n" + "blob : " + shaOfFourthFile + " : file4\nblob : " + shaOfThirdFile + " : file3\ntree : da39a3ee5e6b4b0d3255bfef95601890afd80709 : dir";
                String shaOfTreeForSecondCommit = firstCommit.encryptPassword(contentsOfTreeForSecondCommit);
                String contentsForSecondCommit = shaOfTreeForSecondCommit + "\n" + firstCommit.getSha() + "\n" + "\n" + "Wyatt" + "\n" + date + "\n" + "second commit";
                String contentsOfSecondCommitAfterThirdCommit = shaOfTreeForSecondCommit + "\n" + firstCommit.getSha() + "\n" + commit2.getSha() + "\n" + "Wyatt" + "\n" + date + "\n" + "second commit";
                String shaOfSecondCommit = firstCommit.encryptPassword(contentsForSecondCommit);
                File fileOfTreeForSecondCommit = new File("./objects/" + shaOfTreeForSecondCommit);
                File fileOfSecondCommit = new File("./objects/" + shaOfSecondCommit);
                String shaOfFifthFile = firstCommit.encryptPassword(ind.fileToString("file5"));
                String shaOfSixthFile = firstCommit.encryptPassword(ind.fileToString("file6"));
                String contentsOfTreeForThirdCommit = "tree : " + shaOfTreeForSecondCommit + "\n" + "blob : " + shaOfSixthFile + " : file6" + "\n" + "blob : " + shaOfFifthFile + " : file5";
                String shaOfTreeForThirdCommit = firstCommit.encryptPassword(contentsOfTreeForThirdCommit);
                //Tree.deleteFile(shaOfTreeForThirdCommit, "dir");
                String contentsOfThirdCommit = shaOfTreeForThirdCommit + "\n" + commit1.getSha() + "\n" + "\n" + "Wyatt" + "\n" + date + "\n" + "third commit";
                String contentsOfThirdCommitAfterFourthCommit = shaOfTreeForThirdCommit + "\n" + commit1.getSha() + "\n" + commit3.getSha() + "\n" + "Wyatt" + "\n" + date + "\n" + "third commit";
                String shaOfThirdCommit = firstCommit.encryptPassword(contentsOfThirdCommit);
                File fileOfTreeForThirdCommit = new File("./objects/" + shaOfTreeForThirdCommit);
                File fileOfThirdCommit = new File("./objects/" + shaOfThirdCommit);
                String shaOfSeventhFile = firstCommit.encryptPassword(ind.fileToString("file7"));
                String shaOfEighthFile = firstCommit.encryptPassword(ind.fileToString("file8"));
                String contentsOfTreeForFourthCommit = "tree : " + shaOfTreeForThirdCommit + "\n" + "blob : " + shaOfEighthFile + " : file8\nblob : " + shaOfSeventhFile + " : file7" + "\n" + "tree : da39a3ee5e6b4b0d3255bfef95601890afd80709 : dir2";
                String shaOfTreeForFourthCommit = firstCommit.encryptPassword(contentsOfTreeForFourthCommit);
                String contentsOfFourthCommit = shaOfTreeForFourthCommit + "\n" + commit2.getSha() + "\n" + "\n" + "Wyatt"  + "\n" + date + "\n" + "fourth commit";
                String shaOfFourthCommit = firstCommit.encryptPassword(contentsOfFourthCommit);
                File fileOfTreeForFourthCommit = new File("./objects/" + shaOfTreeForFourthCommit);
                File fileOfFourthCommit = new File("./objects/" + shaOfFourthCommit);
                assertEquals(ind.fileToString("./objects/" + shaOfTreeForFirstCommit), contentsOfTreeForFirstCommit);
                assertEquals(ind.fileToString("./objects/" + shaOfFirstCommit), contentsOfFirstCommitAfterSecondCommit);
                assertEquals(ind.fileToString("./objects/" + shaOfTreeForSecondCommit), contentsOfTreeForSecondCommit);
                assertEquals(ind.fileToString("./objects/" + shaOfSecondCommit), contentsOfSecondCommitAfterThirdCommit);
                assertEquals((ind.fileToString("./objects/" + shaOfTreeForThirdCommit)), contentsOfTreeForThirdCommit);
                assertEquals(ind.fileToString("./objects/" + shaOfThirdCommit), contentsOfThirdCommitAfterFourthCommit);
                assertEquals(ind.fileToString("./objects/" + shaOfTreeForFourthCommit), contentsOfTreeForFourthCommit);
                assertEquals(ind.fileToString("./objects/" + shaOfFourthCommit), contentsOfFourthCommit);
        }

        @Test
        void testWithFiveCommitsDelete() throws Exception {
                Date date = new Date(java.lang.System.currentTimeMillis());
                File file1 = new File("file1");
                createFile("file1", "fileContents1");
                File file2 = new File("file2");
                createFile("file2", "fileContents2");
                Index ind = new Index();
                ind.init();
                ind.addBlob("file1");
                ind.addBlob("file2");
                Commit firstCommit = new Commit("Wyatt", "first commit");

                File file3 = new File("file3");
                createFile("file3", "fileContents3");
                File file4 = new File("file4");
                createFile("file4", "fileContents4");
                File dir = new File("dir");
                dir.mkdir();
                Index ind2 = new Index();
                ind2.init();
                ind2.addBlob("file3");
                ind2.addBlob("file4");
                ind2.addBlob("dir");
                Commit commit1 = new Commit(firstCommit.getSha(), "Wyatt", "second commit");
                File f1 =  new File("index");
                f1.delete();
                f1.createNewFile();
                ind.removeBlob("dir");
                System.out.println(ind.fileToString("index"));
                File file5 = new File("file5");
                createFile("file5", "fileContents5");
                File file6 = new File("file6");
                createFile("file6", "fileContents6");
                new FileWriter("./index", false).close();
                Index ind3 = new Index();
                ind3.init();
                ind3.addBlob("file5");
                ind3.addBlob("file6");
                ind3.removeBlob("file2");
                Commit commit2 = new Commit(commit1.getSha(), "Wyatt", "third commit");
                File f2 =  new File("index");
                f2.delete();
                f2.createNewFile();
                File file7 = new File("file7");
                createFile("file7", "fileContents7");
                File file8 = new File("file8");
                createFile("file8", "fileContents8");
                File dir2 = new File("dir2");
                dir2.mkdir();
                Index ind4 = new Index();
                ind4.init();
                ind4.addBlob("file7");
                ind4.addBlob("file8");
                ind4.addBlob("dir2");
                Commit commit3 = new Commit(commit2.getSha(), "Wyatt", "fourth commit");
                File f3 =  new File("index");
                f3.delete();
                f3.createNewFile();
                //
                /*String shaOfParent = parentCom.getSha();
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
                */
                String shaOfFirstFile = firstCommit.encryptPassword(ind.fileToString("file1"));
                String shaOfSecondFile = firstCommit.encryptPassword(ind2.fileToString("file2"));
                String contentsOfTreeForFirstCommit = "blob : " + shaOfSecondFile + " : file2" + "\n" + "blob : " + shaOfFirstFile + " : file1";
                String shaOfTreeForFirstCommit = firstCommit.encryptPassword(contentsOfTreeForFirstCommit);
                String contentsForFirstCommit = shaOfTreeForFirstCommit + "\n" + "\n" + "\n" + "Wyatt" + "\n" + date + "\n" + "first commit";
                String contentsOfFirstCommitAfterSecondCommit = shaOfTreeForFirstCommit + "\n" + "\n" + commit1.getSha() + "\n" + "Wyatt" + "\n" + date + "\n" + "first commit";
                String shaOfFirstCommit = firstCommit.encryptPassword(contentsForFirstCommit);
                File fileOfTreeForFirstCommit = new File("./objects/" + shaOfTreeForFirstCommit);
                File fileOfFirstCommit = new File("./objects/" + shaOfFirstCommit);
                String shaOfThirdFile = firstCommit.encryptPassword(ind.fileToString("file3"));
                String shaOfFourthFile = firstCommit.encryptPassword(ind.fileToString("file4"));
                String contentsOfTreeForSecondCommit = "tree : " + shaOfTreeForFirstCommit + "\n" + "blob : " + shaOfFourthFile + " : file4\nblob : " + shaOfThirdFile + " : file3\ntree : da39a3ee5e6b4b0d3255bfef95601890afd80709 : dir";
                String shaOfTreeForSecondCommit = firstCommit.encryptPassword(contentsOfTreeForSecondCommit);
                String contentsForSecondCommit = shaOfTreeForSecondCommit + "\n" + firstCommit.getSha() + "\n" + "\n" + "Wyatt" + "\n" + date + "\n" + "second commit";
                String contentsOfSecondCommitAfterThirdCommit = shaOfTreeForSecondCommit + "\n" + firstCommit.getSha() + "\n" + commit2.getSha() + "\n" + "Wyatt" + "\n" + date + "\n" + "second commit";
                String shaOfSecondCommit = firstCommit.encryptPassword(contentsForSecondCommit);
                File fileOfTreeForSecondCommit = new File("./objects/" + shaOfTreeForSecondCommit);
                File fileOfSecondCommit = new File("./objects/" + shaOfSecondCommit);
                String shaOfFifthFile = firstCommit.encryptPassword(ind.fileToString("file5"));
                String shaOfSixthFile = firstCommit.encryptPassword(ind.fileToString("file6"));
                String contentsOfTreeForThirdCommit = "tree : " + shaOfTreeForSecondCommit + "\n" + "blob : " + shaOfSixthFile + " : file6" + "\n" + "blob : " + shaOfFifthFile + " : file5";
                String shaOfTreeForThirdCommit = firstCommit.encryptPassword(contentsOfTreeForThirdCommit);
                String contentsOfThirdCommit = shaOfTreeForThirdCommit + "\n" + commit1.getSha() + "\n" + "\n" + "Wyatt" + "\n" + date + "\n" + "third commit";
                String contentsOfThirdCommitAfterFourthCommit = shaOfTreeForThirdCommit + "\n" + commit1.getSha() + "\n" + commit3.getSha() + "\n" + "Wyatt" + "\n" + date + "\n" + "third commit";
                String shaOfThirdCommit = firstCommit.encryptPassword(contentsOfThirdCommit);
                File fileOfTreeForThirdCommit = new File("./objects/" + shaOfTreeForThirdCommit);
                File fileOfThirdCommit = new File("./objects/" + shaOfThirdCommit);
                String shaOfSeventhFile = firstCommit.encryptPassword(ind.fileToString("file7"));
                String shaOfEighthFile = firstCommit.encryptPassword(ind.fileToString("file8"));
                String contentsOfTreeForFourthCommit = "tree : " + shaOfTreeForThirdCommit + "\n" + "blob : " + shaOfEighthFile + " : file8\nblob : " + shaOfSeventhFile + " : file7" + "\n" + "tree : da39a3ee5e6b4b0d3255bfef95601890afd80709 : dir2";
                String shaOfTreeForFourthCommit = firstCommit.encryptPassword(contentsOfTreeForFourthCommit);
                String contentsOfFourthCommit = shaOfTreeForFourthCommit + "\n" + commit2.getSha() + "\n" + "\n" + "Wyatt"  + "\n" + date + "\n" + "fourth commit";
                String shaOfFourthCommit = firstCommit.encryptPassword(contentsOfFourthCommit);
                File fileOfTreeForFourthCommit = new File("./objects/" + shaOfTreeForFourthCommit);
                File fileOfFourthCommit = new File("./objects/" + shaOfFourthCommit);
                assertEquals(ind.fileToString("./objects/" + shaOfTreeForFirstCommit), contentsOfTreeForFirstCommit);
                assertEquals(ind.fileToString("./objects/" + shaOfFirstCommit), contentsOfFirstCommitAfterSecondCommit);
                assertEquals(ind.fileToString("./objects/" + shaOfTreeForSecondCommit), contentsOfTreeForSecondCommit);
                assertEquals(ind.fileToString("./objects/" + shaOfSecondCommit), contentsOfSecondCommitAfterThirdCommit);
                assertEquals((ind.fileToString("./objects/" + shaOfTreeForThirdCommit)), contentsOfTreeForThirdCommit);
        }

        public static int ordinalIndexOf(String str, String substr, int n) {
            int pos = str.indexOf(substr);
            while (--n > 0 && pos != -1)
                pos = str.indexOf(substr, pos + 1);
            return pos;
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
