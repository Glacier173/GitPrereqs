import java.io.IOException;

public class main {
    public static void main (String[] args) throws Exception
    {
        Index index = new Index();
        index.init();
        index.addBlob("testFile.txt");
        index.addBlob("testFile2.txt");
        //Index.removeBlob("testFile.txt");
        Tree mainTree = new Tree();
        //mainTree.addDirectory("objects/");
    }
    
}
