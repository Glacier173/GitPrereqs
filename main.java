import java.io.IOException;

public class main {
    public static void main (String[] args) throws IOException
    {
        Index.init();
        Blob b = new Blob();
        b.writeToObjects("testFile.txt");
    }
   
    
}
