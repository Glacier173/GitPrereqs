import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Commit {
    
    public Commit() {
        File objects = new File("./objects");
        if (!objects.exists())
            objects.mkdirs();
    }

    //creates Tree and writes its sha, prev location sha, next location sha, name, date, and summary to a file in objects
    public void writeFile(String fileName) {
        Tree tree = new Tree();
        String sha = tree.getSha();

        String dirName = "./objects/";
        File dir = new File (dirName);
        File commit = new File(dir,"commit");

        PrintWriter pw = new PrintWriter(new FileWriter("commit"));
        pw.println(sha);
    }

}
