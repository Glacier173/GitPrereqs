
import static org.junit.Assert.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class IndexTest {

    @Test
    @DisplayName("Test if adding a blob works.")
    void testAdd() throws Exception {
        File file = new File("test.txt");
        //Blob b = new Blob(file);
        String contentsOfFile = "testing";
        Files.write(file.toPath(), contentsOfFile.getBytes());
        Index ind = new Index();
        //File object = new File("index");
        ind.addBlob("test.txt");
        String ret = ind.fileToString("index");
        assertTrue(ret.contains(Blob.encryptThisString(contentsOfFile)));
        file.delete();
    }


    @Test
    void testInit() throws Exception {
        Index index = new Index();
        index.init();
        assertTrue(new File("index").exists());
    }

    @Test
    void testRemoveBlob() throws Exception {
        File file = new File("test.txt");
        //Blob b = new Blob(file);
        String contentsOfFile = "testing";
        Files.write(file.toPath(), contentsOfFile.getBytes());
        Index ind = new Index();
        //File object = new File("index");
        ind.addBlob("test.txt");
        String ret = ind.fileToString("index");
        assertTrue(ret.contains(Blob.encryptThisString(contentsOfFile)));
        //file.delete();
        ind.removeBlob("test.txt");
        ret = ind.fileToString("index");
        assertTrue(!ret.contains(Blob.encryptThisString(contentsOfFile)));
    }

    @Test
    void testWriter() throws Exception {
        Index ind = new Index();
        File f = new File("test.txt");
        f.createNewFile();
        String contents = "testing";
        FileWriter fw = new FileWriter(f);
        fw.write(contents);
        fw.close();
        ind.addBlob("test.txt");
        Index.writeInd();
        String ret = ind.fileToString("index");
        assertTrue(ret.contains(Blob.encryptThisString(contents)));
    }
}
