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

    public static void init() throws IOException {
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

    public static String fileToString(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        StringBuilder sb = new StringBuilder();
        while (br.ready()) {
            sb.append((char) br.read());
        }
        br.close();
        return sb.toString();
    }

    public static void addBlob(String fileName) throws IOException {
        if (!hMap.containsKey(fileName)) {
            String hash = Blob.encryptThisString(fileName);
            Path p = Paths.get(fileName);
            String contents = Blob.reader(p);
            writer(contents, "objects/" + hash);
            if (hash != null) {
                hMap.put(fileName, hash);
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("index", true))) {
                bw.write(fileName + " : " + hash);
                bw.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File" + fileName + " already exists in the index");
        }
    }

    public static void writer(String str, String file) throws IOException {
        PrintWriter pw = new PrintWriter(file);
        pw.print(str);
        pw.close();
    }

    public static void removeBlob(String fileName) throws IOException {
        if (hMap.containsKey(fileName)) {
            hMap.remove(fileName);
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("index"))) {
            for (Map.Entry<String, String> keys : hMap.entrySet()) {
                bw.write(keys.getKey() + " : " + keys.getValue());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
