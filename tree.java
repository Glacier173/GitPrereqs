import java.util.Scanner;
import java.util.*;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class Tree {
    private StringBuilder sb;

    public Tree() {
        sb = new StringBuilder();
    }

    public void writeToFile() throws Exception {
        String file = Blob.encryptThisString(sb.toString());
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("./objects/" + file)));
        pw.print(file.toString());
        pw.close();
    }

    public void add(String string) {
        String inside = sb.toString();
        if (inside.contains(string)) {
            return;
        } else if (sb.length() == 0)
            sb.append(string);
        else {
            sb.append("\n");
            sb.append(string);
        }
    }

    public void remove(String string) {
        String inside = sb.toString();
        if (!inside.contains(string)) {
            return;
        }

        StringBuilder sbtwo = new StringBuilder();
        Scanner scan = new Scanner(inside);
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            if (!line.contains(string))
                sbtwo.append(line);
        }

        scan.close();
        sb = sbtwo;

    }

    public String getTree() {
        return sb.toString();
    }

}
