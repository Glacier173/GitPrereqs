import java.io.File;
import java.io.IOException;

public class Index {
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
    public static void addBlob(String path) throws IOException
    {
        init();

        
    }
}
