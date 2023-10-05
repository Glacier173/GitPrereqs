
import static org.junit.Assert.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
    void testAddDirectory() throws Exception
    {
        File file = new File("Folder");
        file.mkdir();
        //File f1 = new File("./Folder/" + "file1");
        //f1.createNewFile();
        //FileWriter fw = new FileWriter(f1);
        //fw.write("abcdef");
        //fw.close();
        //File f2 = new File("./Folder/" + "file2");
        //f2.createNewFile();
        //FileWriter fw2 = new FileWriter(f2);
        //fw2.write("ghijkl");
        //fw2.close();
        Index ind = new Index();
        ind.addBlob("Folder");
        String ret = ind.fileToString("index");
        System.out.println(ret);
        assertTrue(ret.equals("tree : da39a3ee5e6b4b0d3255bfef95601890afd80709 : Folder"));
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
