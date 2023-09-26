import java.io.IOException;

public class main {
    public static void main (String[] args) throws IOException
    {
        Index index = new Index();
        index.init();
        index.addBlob("testFile.txt");
        index.addBlob("testFile2.txt");
        Index.removeBlob("testFile.txt");
    }
    
}
