import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Blob {
    private Path filePath;
    private String name;
    private byte[] fileData;
    private String sha;

    public Blob(File file) throws IOException {
        System.out.println("File exists: " + file.exists());
        StringBuilder str = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(file));
        while (br.ready()) {
            str.append((char) br.read());
        }
        br.close();
        String hash = encryptThisString(str.toString());
        String folder = "objects/";
        this.filePath = Paths.get(folder, hash);
        // Files.createDirectories(directoryPathing);
        // File blob = new File(filePath, hash);
        Files.write(this.filePath, (str.toString()).getBytes());
        this.fileData = (str.toString()).getBytes();
        // System.out.println("Blob exists: " + blob.exists());
        // System.out.println("Blob exists: " + blob.getAbsolutePath());
        // try (BufferedWriter bw = new BufferedWriter(new FileWriter(blob))) {
        // bw.write(str.toString());
        // }
    }

    public Path getPath() {
        return filePath;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public static String reader(Path p) throws IOException {
        StringBuilder str = new StringBuilder();
        // BufferedReader br = new BufferedReader(file);
        BufferedReader br = Files.newBufferedReader(p);
        while (br.ready()) {
            str.append((char) br.read());
        }
        br.close();
        return str.toString();
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
            while (hashtext.length() < 40) {
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

    public String fileToString(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        StringBuilder sb = new StringBuilder();
        while (br.ready()) {
            sb.append(br.read());
        }
        br.close();
        return sb.toString();
    }
}