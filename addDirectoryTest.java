import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class addDirectoryTest {
    private static void createFile(String directoryPath, String fileContents) throws IOException {
        File file = new File(directoryPath);
        FileWriter fw = new FileWriter(directoryPath);
        PrintWriter pw = new PrintWriter(fw);
        pw.print(fileContents);
        fw.close();
        pw.close();
    }
    @Test
    void testAddDirectory() throws Exception {
        File dir = new File("directoryTest");
        dir.mkdir();
        createFile("directoryTest/file1.txt", "abcdefg");
        createFile("directoryTest/file2.txt", "hijklmn");
        createFile("directoryTest/file3.txt", "opqrstu");
        tree mainTree = new tree();
        String result = mainTree.addDirectory("directoryTest");

        //System.out.println("Tree contents: ");
        //System.out.println(mainTree.getTree());
        String expected = "3c760db4248d9dd38c13c9eea0f1c0bddea2fc68";
        assertEquals(expected, result);
    }
}
