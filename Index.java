import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class Index {
    private static HashMap<String, String> hMap = new HashMap<>();
    public static void init() throws IOException
    {
        File file = new File("index");
        File dir = new File("objects");

        if (!file.isFile())
        {
            file.createNewFile();
        }
        if (!dir.isDirectory())
        {
            dir.mkdir();
        }
    }
    public static void addBlob(String fileName) throws IOException
    {
        init();
        String hash = Blob.encryptThisString(fileName);
        if (hash != null)
        {
            hMap.put(fileName, hash);
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter("index", true));
        bw.write(fileName + " : " + hash);
        bw.write("\n");
        bw.close();
    }
}
