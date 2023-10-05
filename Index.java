import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Index {
    private static HashMap<String, String> hMap = new HashMap<>();
    private static HashMap<String, String> mapForDirs = new HashMap();

    public void init() throws IOException {
        hMap = new HashMap<>();
        File file = new File("index");
        File dir = new File("objects");
        if (file.exists()) {
            file.delete();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        if (!dir.isDirectory()) {
            dir.mkdir();
        }
        String str = fileToString("index");
        Scanner sc = new Scanner(str);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (!line.isEmpty()) {
                String fileName = line.split(" : ")[0];
                String hash = line.split(" : ")[1];
                hMap.put(fileName, hash);
            }
        }
        sc.close();

    }

    public String fileToString(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        StringBuilder sb = new StringBuilder();
        while (br.ready()) {
            sb.append((char) br.read());
        }
        br.close();
        return sb.toString();
    }

    public void addBlob(String fileName) throws Exception {
        File f = new File(fileName);
        if (f.isDirectory())
        {
            Tree tree = new Tree();
            mapForDirs.put(fileName, tree.addDirectory(fileName));
        }
        if(f.isFile())
        {
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(f));
            while (br.ready())
            {
                sb.append((char)br.read());
            }
            br.close();
            String hash = Blob.encryptThisString(sb.toString());
            hMap.put(fileName, hash);
        }
        writeInd();
    }

    public static void writeInd() throws IOException {
        FileWriter fw = new FileWriter("./index");
        for (String file : hMap.keySet())
        {
            fw.write("blob : " + hMap.get(file) + " : " + file + "\n");
        }
        for (String dir : mapForDirs.keySet())
        {
            fw.write("tree : " + mapForDirs.get(dir) + " : " + dir + "\n");
        }
        fw.close();
    }

    public void removeBlob(String fileName) throws IOException {
        hMap.remove(fileName);
        writeInd();
    }
}
