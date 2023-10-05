
import static org.junit.Assert.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BlobTest {
    @TempDir
    static Path tempDir;

    @Test
    @DisplayName("Test if adding a blob works.")
    void testCreateBlob() throws Exception {
        File file = new File("test.txt");
        file.createNewFile();
        assertTrue("File doesn't exist", file.exists());
        String fill = "testing";
        Files.write(file.toPath(), fill.getBytes());

        Blob blob = new Blob(file);

        File blobFile = blob.getPath().toFile();
        assertTrue(blobFile.exists());

        String inside = blob.getFileData().toString();

        assertTrue(inside, true);

        file.delete();
        blobFile.delete();
    }

    @Test
    @DisplayName("If file contents are the same")
    void testBlobContents() throws Exception {
        File file = new File("test.txt");
        file.createNewFile();
        //assertTrue("File doesn't exist", file.exists());
        String fill = "testing";
        Files.write(file.toPath(), fill.getBytes());

        Blob blob = new Blob(file);

        File blobFile = blob.getPath().toFile();

        BufferedReader br = new BufferedReader(new FileReader(blobFile));
        StringBuilder sb = new StringBuilder();
        while (br.ready())
        {
            sb.append((char)br.read());
        }
        br.close();
        assertTrue("Contents do not match", sb.toString().equals(fill));
        file.delete();
        blobFile.delete();
    }

}
