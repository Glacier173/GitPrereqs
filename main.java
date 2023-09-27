import java.io.IOException;

public class main {
    public static void main (String[] args) throws Exception
    {
        Index index = new Index();
        index.init();
        index.addBlob("testFile.txt");
        index.addBlob("testFile2.txt");
        Index.removeBlob("testFile.txt");
        tree mainTree = new tree();
        //mainTree.addDirectory("objects/");
    }
    
}
