import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
        if (hMap.containsKey(fileName))
        {
            return;
        }
        String hash = Blob.encryptThisString(fileName);
        if (hash != null)
        {
            hMap.put(fileName, hash);
        }
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("index", true))){
        bw.write(fileName + " : " + hash);
        bw.newLine();
    } catch (IOException e){
        e.printStackTrace();
    }
    }
    public static void removeBlob(String fileName) throws IOException
    {
        if (hMap.containsKey(fileName))
        {
            hMap.remove(fileName);
        }
        try( BufferedWriter bw = new BufferedWriter(new FileWriter("index"))){
        for (Map.Entry<String, String> keys : hMap.entrySet())
        {
            bw.write(keys.getKey() + " : " + keys.getValue());
            bw.newLine();
        }
    } catch(IOException e){
        e.printStackTrace();
    }
    }
}
