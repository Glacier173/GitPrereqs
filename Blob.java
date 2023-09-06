import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Blob
{
    public static String encryptThisString(String input)
    {
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
    public void writeToObjects(String in) throws IOException
    {
        String hash = encryptThisString(in);
        String path = "Users/wyatt/Documents/GitHub/Objects" + File.separator + hash + File.separator + "hash.txt";
        // Use relative path for Unix systems
        File f = new File(path);
        f.getParentFile().mkdirs(); 
        f.createNewFile();
    }
    public String fileToString() throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader("hash.txt"));
        StringBuilder sb = new StringBuilder();
        while (br.ready())
        {
            sb.append(br.read());
        }
        br.close();
        return sb.toString();
    }
}