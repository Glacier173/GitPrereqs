import java.util.Scanner;
import java.util.*;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class tree {
    private StringBuilder sb;
    private String holdTreeForAddDirectory;

    public tree() {
        sb = new StringBuilder();
    }

    public void writeToFile() throws Exception {
        String file = Blob.encryptThisString(sb.toString());
        holdTreeForAddDirectory = Blob.encryptThisString(sb.toString());
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("./objects/" + file)));
        pw.print(file.toString());
        pw.close();
    }

    public String getSha() throws IOException {
        return Blob.encryptThisString(sb.toString());
    }

    public void add(String string) {
        String inside = sb.toString();
        if (inside.contains(string)) {
            return;
        } else if (sb.length() == 0)
            sb.append(string);
        else {
            sb.append("\n");
            sb.append(string);
        }
    }

    public void remove(String string) {
        String inside = sb.toString();
        if (!inside.contains(string)) {
            return;
        }

        StringBuilder sbtwo = new StringBuilder();
        Scanner scan = new Scanner(inside);
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            if (!line.contains(string))
                sbtwo.append(line);
        }

        scan.close();
        sb = sbtwo;

    }

    public String getTree() {
        return sb.toString();
    }

    public static String encryptThisString(String input) throws IOException {
        try {
            // getInstance() method is called with algorithm SHA-1
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return hashtext;
        }
         // For specifying wrong message digest algorithms
         catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String addDirectory(String directoryPath) throws IOException
    {
        File rootDir = new File(directoryPath);
        Index index = new Index();
        index.init();
        tree tree = new tree();
        for (String file : rootDir.list())
        {
            String pathToFile = directoryPath + "/" + file;
            File f = new File(pathToFile);
            if (f.isFile())
            {
                index.addBlob(pathToFile); //Here I'm not adding "f" because its a file, so I need to add the path which is a String
                tree.add("blob : " + index.fileToString(encryptThisString(pathToFile)) + " : " + file);
            }
            else if (f.isDirectory())
            {
                index.addBlob(pathToFile);
                tree.add("blob : " + tree.addDirectory(pathToFile) + " : " + file);
            }
        }
        String shaOfTreeDir = holdTreeForAddDirectory;
        return shaOfTreeDir;
    }

}
