
import static org.junit.Assert.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class IndexTest {
    @TempDir
    static Path tempDir;

    @Test
    @DisplayName("Test if adding a blob works.")
    void testAdd() throws Exception {
        File file = new File(tempDir.toFile(), "test.txt");
        String fill = "testing";
        Files.write(file.toPath(), fill.getBytes());

        File object = new File("objects/" + Blob.encryptThisString(file.getAbsolutePath()));
        assertTrue(object.exists());
        file.delete();
    }

    @Test
    void testFileToString() {

    }

    @Test
    void testInit() throws Exception {
        Index index = new Index();
        index.init();
        assertTrue(new File("index").exists());
    }

    @Test
    void testRemoveBlob() throws Exception {
        File file = new File(tempDir.toFile(), "test.txt");
        String fill = "testing";
        Files.write(file.toPath(), fill.getBytes());

        File object = new File("objects/" + Blob.encryptThisString(file.getAbsolutePath()));
        assertTrue(object.exists());
        file.delete();
        assertFalse(object.exists());
    }

    @Test
    void testWriter() throws Exception {
        File file = new File(tempDir.toFile(), "test.txt");
        String fill = "testing";
        Index.writer(fill, file.getAbsolutePath());
        assertTrue("File doesn't contain written string", file.toString().contains(fill));
    }
}
