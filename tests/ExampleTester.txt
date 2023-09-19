package tests;

import static org.junit.Assert.*;
import org.junit.jupiter.api.io.TempDir;

import Index;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

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

}
