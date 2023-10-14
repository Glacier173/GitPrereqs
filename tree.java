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
        File file = new File("./objects/" + shaOfTree);
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
                sb.append(read + "\n");
            }
        }
    }
        br.close();
        /*for(int i = 0; i < arr.size(); i++)
        {
            add(arr.get(i));
        }
        */
        String sbString = sb.toString();
        String line = sbString.substring(0, ordinalIndexOf(sbString, "\n", 1));
        ArrayList<String> toDelete = new ArrayList<String>();
        toDelete.add(line);
        for (int i = 0; i < toDelete.size(); i ++)
        {
            if (toDelete.get(i).contains("blob : "))
            {
                avoidDeletedAndEdited(shaOfTree, toDelete.get(i));
            }
        }
        if (isHere == true && morePrevShaTree != "")
        {
            sb.append("tree : " + morePrevShaTree);
        }
        if (isHere == false){
            deleteFile(morePrevShaTree, deleteLine);
        }
        //add(sb.toString());
    }

    public void avoidDeletedAndEdited(String prevTreeSha, String line) throws Exception
    {
        String blobOrTree = line.substring(0,4);//so lucky that this works holy ("blob" count = "tree" count)
        if(blobOrTree.equals("tree"))
        {
            sb.append(line);
            prevTreeSha = line.substring(7,47);
        }
        else if (blobOrTree.equals("blob"))
        {
            sb.append(line);
        }
        else if (line.contains("*edited*"))
        {
            editExisting(prevTreeSha, line.substring(9));//9 bc *edited* is 8 and then space!!
        }
        else if (line.contains("*deleted*"))
        {
            deleteFile(prevTreeSha, line.substring(10));//10 bc *deleted* is 9 then space!
        }
    }

    public static int ordinalIndexOf(String str, String substr, int n) {
        int pos = str.indexOf(substr);
        while (--n > 0 && pos != -1)
            pos = str.indexOf(substr, pos + 1);
        return pos;
    }

    public void editExisting(String shaOfTree, String line) throws Exception
    {
        //File edited = new File(line);
        String shaOfFile = "";
        //ArrayList<String> arr = new ArrayList<String>();
        String morePrevTreeSha ="";
        boolean isHere = false;
        StringBuilder sb = new StringBuilder();
        shaOfFile = Blob.encryptThisString(line);
        File treeFile = new File("./objects/" + shaOfTree);
        BufferedReader br = new BufferedReader(new FileReader(treeFile));
        while (br.ready())
        {
            String read = br.readLine();
            if(read.contains("tree : "))
            {
                morePrevTreeSha = read.substring(7,47);//copy paste from the delete method b/c sha are still 40 chars and tree : is 7
            }
            else{
                if (read.contains(line))
                {
                    isHere = true;
                }
                else{
                    sb.append(read + "\n");
                }
            }
        }
        br.close();

        if (isHere == true)
        {
            if (morePrevTreeSha != "")
            {
                sb.append("tree : " + morePrevTreeSha);//we can do this b/c we know that this tree sha is not null
            }
        }
        ArrayList<String> arrToEdit = new ArrayList<String>();
        String lineToEdit = line.substring(0, ordinalIndexOf(line, "\n", 1));
        arrToEdit.add(lineToEdit);
        for (int i = 0; i < arrToEdit.size(); i++)
        {
            avoidDeletedAndEdited(shaOfTree, arrToEdit.get(i));
        }
        if(isHere == false)
        {
            editExisting(morePrevTreeSha, line);
        }
        //add(sb.toString());
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