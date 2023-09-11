import java.io.IOException;

public class main {
    public static void main (String[] args) throws IOException
    {
        Index.init();
        Index.addBlob("testFile.txt");
        Index.addBlob("testFile2.txt");
        Index.removeBlob("testFile.txt");
    }
    
}
