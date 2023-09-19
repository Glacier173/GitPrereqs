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

import Index.java;

public class ExampleTester {
    @TempDir
    static Path tempDir;

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
