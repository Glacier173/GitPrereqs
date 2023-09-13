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
    public Blob(String path) throws IOException {
        File file = new File(path);
        StringBuilder str = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(file));
        while (br.ready()) {
            str.append((char) br.read());
        }
        br.close();
        String hash = encryptThisString(str.toString());
        String directPath = "objects/" + File.separator + File.separator + hash;
        Path directoryPathing = Paths.get(directPath);
        Files.createDirectories(directoryPathing);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(directPath))) {
            bw.write(hash);
        }
    }

    public static String reader(String inputFile) throws IOException {
        File file = new File(inputFile);
        StringBuilder str = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(file));
        while (br.ready()) {
            str.append((char) br.read());
        }
        br.close();
        return str.toString();
    }

    public static String encryptThisString(String input) throws IOException {
        String inputHash = reader(input);
        try {
            // getInstance() method is called with algorithm SHA-1
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(inputHash.getBytes());

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