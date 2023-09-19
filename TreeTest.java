import static org.junit.Assert.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TreeTest {

    private static String test = "test.txt";

    @BeforeAll
    static void runBeforeAll() throws Exception {
        File file = new File(test);

        String content = "this is a test";

        PrintWriter write = new PrintWriter(file);
        write.print(content);
        write.close();

        File objects = new File("./objects");
        objects.mkdirs();

    }

    @Test
    @DisplayName("Test if adding works")
    void addTest() throws Exception {
        Tree todd = new Tree();
        todd.add("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");
        todd.add("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt");
        assertTrue("Incorrectly added", todd.getTree().contains("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b")
                && todd.getTree().contains("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt"));
    }

    @Test
    @DisplayName("Test if writing works")
    void writeTest() throws Exception {
        Tree todd = new Tree();
        todd.add("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");
        todd.add("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt");
        String sha = Blob.encryptThisString(todd.getTree());
        File file = new File("./objects/" + sha);

        assertTrue("File doesn't exist?", file.exists());
        StringBuilder fileContent = new StringBuilder();
        try (BufferedReader read = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = read.readLine()) != null) {
                fileContent.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue("File contents do not match", fileContent.toString().equals(todd.getTree()));
    }

    @Test
    @DisplayName("Test if remove works")
    void removeTest() throws Exception {
        Tree todd = new Tree();
        todd.add("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");
        todd.add("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt");

        todd.remove("bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");
        assertTrue("Contains tree that should have been removed!",
                !todd.getTree().contains("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b"));

        todd.remove("file1.txt");
        assertTrue("Contains file that should have been removed!",
                !todd.getTree().contains("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f : file1.txt"));
    }
}