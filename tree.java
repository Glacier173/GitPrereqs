import java.util.Scanner;
import java.util.*;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.FileReader;

public class Tree {
    private StringBuilder sb;
    private String currSha;
    private String fileName;
    private String holdTreeForAddDirectory;

    public Tree() {
        sb = new StringBuilder();
    } 

    public void writeToFile() throws Exception {
        /*String file = Blob.encryptThisString(sb.toString());
        holdTreeForAddDirectory = Blob.encryptThisString(sb.toString());
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("./objects/" + file)));
        pw.print(file.toString());
        pw.close();*/
        String fileSHA1 = Blob.encryptThisString(sb.toString());
        String filePath = "./objects/" + fileSHA1;
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(filePath)))) {
            pw.print(sb.toString());
        }
    }

    public String getSha() throws IOException {
        return Blob.encryptThisString(sb.toString());
    }

    public void add(String string) throws Exception
    {
        //Tree tree = new Tree();
        String inside = sb.toString();
        if (inside.contains(string)) {
            return;
        } else if (sb.length() == 0)
            sb.append(string);
        else {
            sb.append("\n");
            sb.append(string);
        }
        //tree.writeToFile();
    }

    public void deleteFile(String shaOfTree, String deleteLine) throws Exception
    {
        deleteLine = deleteLine.substring(9,deleteLine.length());
        ArrayList<String> arr = new ArrayList<String>();
        Boolean isHere = false;
        StringBuilder sb = new StringBuilder();
        File file = new File(shaOfTree);
        String morePrevShaTree = "";
        BufferedReader br = new BufferedReader(new FileReader(file));
        while(br.ready())
        {
            String read = br.readLine();
            if (read.contains("tree : "))
            {
                morePrevShaTree = read.substring(7,47);//"tree : " is 7 characters and a sha is 40 (hence 7+40 = 47)
            }
            else{
                if(read.contains(deleteLine))
            {
                isHere = true;
            }
            else
            {
                sb.append(deleteLine + "\n");
            }
        }
    }
        br.close();
        for(int i = 0; i < arr.size(); i++)
        {
            add(arr.get(i));
        }
        if (isHere == true && morePrevShaTree != "")
        {
            sb.append("tree : " + morePrevShaTree);
        }
        if (isHere == false){
            deleteFile(morePrevShaTree, deleteLine);
        }
    }

    public void remove(String string) throws Exception
    {
        String inside = sb.toString();
        //Tree tree = new Tree();
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
        //tree.writeToFile();
    }

    public String getTree() {
        return sb.toString();
    }

    public static String encryptThisString(String input) throws IOException {
        try {
            // getInstance() method is called with algorithm SHA-1
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 40) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return hashtext;
        }
         // For specifying wrong message digest algorithms
         catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String addDirectory(String directoryPath) throws Exception
    {
        File rootDir = new File(directoryPath);
        if (!rootDir.exists())
        {
            throw new IOException ("This Directory pathing doesn't exist");
        }
        if (!rootDir.canRead())
        {
            throw new IOException ("Invalid Directory pathing");
        }
        Tree mainTree = new Tree();
        for (String fileDir : rootDir.list())
        {
            System.out.println(fileDir);
            //String pathToFile = directoryPath + "/" + file;
            File f = new File(rootDir, fileDir);
            if (f.isFile())
            {
                String filePath = f.getPath();
                String fileName = f.getName();
                String shaOfFile = Blob.encryptThisString(Blob.reader(Paths.get(filePath)));
                mainTree.add("blob : " + shaOfFile + " : " + fileName);
            }
            else if (f.isDirectory())
            {
                String dirPath = f.getPath();
                String dirName = f.getName();
                Tree childTree = new Tree();
                String shaOfSubDir = childTree.addDirectory(dirPath);

                mainTree.add("tree : " + shaOfSubDir + " : " + dirName);
            }
        }
        mainTree.writeToFile();
        return mainTree.getSha();
    }
}