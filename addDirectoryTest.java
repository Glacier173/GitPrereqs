import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class addDirectoryTest {
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
    void testAddDirectory() throws Exception {
        File f = new File("objects");
        f.mkdir();
        File dir = new File("./objects/directoryTest");
        dir.mkdir();
        createFile("directoryTest/file1.txt", "abcdefg");
        createFile("directoryTest/file2.txt", "hijklmn");
        createFile("directoryTest/file3.txt", "opqrstu");
        tree mainTree = new tree();
        String result = mainTree.addDirectory("directoryTest");

        //System.out.println("Tree contents: ");
        //System.out.println(mainTree.getTree());
        String expected = "02ab9869557ee5b2e8068ac153ddf3bd12d10674";
        assertEquals(expected, result);
    }

    void createSubDirectoryWithFiles(File parentDir, String subDirName) throws IOException {
        File subDir = new File(parentDir, subDirName);
        subDir.mkdir();
        System.out.println("subdir: " + subDir.getPath());

        // Create files within the subdirectory (you can add more as needed)
        File file1 = new File(subDir, "file4.txt");
        //file1.createNewFile();
        File file2 = new File(subDir, "file5.txt");
        //file2.createNewFile();
        try {
            createFile(file1.getPath(), "asdfg");
            createFile(file2.getPath(), "qwerty");
        } catch (IOException e) {
            fail("Failed to create files: " + e.getMessage());
        }
    }

    @Test
    void testAddDirectoryWithSubFolders() throws Exception{
        File dir = new File("directoryTestFile");
       // Files.createDirectories(Paths.get(dir.getPath()));
        dir.mkdir();
        createFile("directoryTestFile/file1.txt", "abcdefg");
        createFile("directoryTestFile/file2.txt", "hijklmn");
        createFile("directoryTestFile/file3.txt", "opqrstu");
        createSubDirectoryWithFiles(dir, "subDirectory");
        tree mainTree = new tree();
        mainTree.addDirectory("directoryTestFile");
        String expected = "024d45c0b43e7b916223877ac2e411cbc3fb65d3";
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader("./objects/" + expected));
        while(br.ready())
        {
            sb.append((char)br.read());
        }
        br.close();
        assertTrue(sb.toString().contains("file1.txt"));
    }
}
