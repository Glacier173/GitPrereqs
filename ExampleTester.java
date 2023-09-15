
import static org.junit.Assert.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ExampleTester {
    @TempDir
    static Path tempDir;

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        /*
         * Utils.writeStringToFile("junit_example_file_data.txt", "test file contents");
         * Utils.deleteFile("index");
         * Utils.deleteDirectory("objects");
         */
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        /*
         * Utils.deleteFile("junit_example_file_data.txt");
         * Utils.deleteFile("index");
         * Utils.deleteDirectory("objects");
         */
    }

    @Test
    @DisplayName("Test if index works")
    void testInitialize() throws Exception {

        File file = new File("index");

        Index.init();

        assertTrue(file.exists());

        File objects = new File("objetcs");

        assertTrue(objects.exists() && objects.isDirectory());
    }

    @Test
    @DisplayName("Testing adding blobs")
    void testAdd() throws Exception {

    }

    @Test
    @DisplayName("Test if adding a blob works. Check file contents")
    void testCreateBlob() throws Exception {
        File file = new File(tempDir.toFile(), "test.txt");
        String fill = "testing";
        Files.write(file.toPath(), fill.getBytes());

        Blob blob = new Blob(file.getAbsolutePath());

        File blobFile = new File("objects/" + blob.encryptThisString(fill));
        assertTrue(blobFile.exists());

        // Read the content of the Blob
        String blobbed = Blob.reader("objects/" + blob.encryptThisString(fill));

        // Verify that the file contents are the same between the original and Blob'd
        // file
        assertEquals(file, blobbed);

        // Clean up the temporary files (optional, depending on your needs)
        file.delete();
        blobFile.delete();
    }
}
