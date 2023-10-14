import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Formatter;

import javax.lang.model.util.ElementScanner14;

public class Commit {
    private File commit;
    private String prevSha = "";
    private String nextSha = "";
    private String author;
    private String shaOfThisCommit;
    private String date;
    private String summary;
    private Tree mainTree;
    private String contentsOfFile;
    private String sha1 ="";
    private Date date1;
    private String shaOfPrevTree;

    
    public Commit(String prevCommitSha, String author, String summary) throws Exception {
        prevSha = prevCommitSha;
        File objects = new File("./objects");
        if (!objects.exists())
            objects.mkdirs();
        Tree tree = new Tree();
        if (prevCommitSha != "")
        {
            addPreviousTreeToCurrentTree(prevCommitSha, tree);
        }
        String sha = createTree(tree);
        date1 = new Date(java.lang.System.currentTimeMillis());
        commit = new File("commit");

        
        PrintWriter pw = new PrintWriter(new FileWriter("commit"));
        pw.println(sha);

        //print location
        pw.println(prevCommitSha);

        //print empty line
        pw.println();

        //print name
        pw.println(author);

        //print date
        pw.println(date1);


        //print summary
        pw.print(summary);

        pw.close();
        //shaOfThisCommit = encryptPassword(getContents(commit));
        rename(commit);
        updateChildShaOfParent(shaOfThisCommit);
    }

    public String getShaOfPrevTree() throws IOException
    {
        shaOfPrevTree = getLineOne(prevSha);
        return shaOfPrevTree;
    }

    //public void setFirstToNext
    
    public void updateChildShaOfParent(String newestShaToAddToParent) throws IOException
    {
        String tempprevsha = prevSha;
        if (prevSha == "")
        {
            return;
        }
        File f = new File("./objects/" + prevSha);
        if (f.exists())
        {

            Index ind = new Index();
            String parentCommitContents = ind.fileToString("./objects/" + prevSha);
            System.out.println(parentCommitContents);
            int secondNewLine = Commit.ordinalIndexOf(parentCommitContents, "\n", 2);
            int thirdNewLine = Commit.ordinalIndexOf(parentCommitContents, "\n", 3);
            //String toWrite = Commit.insertString("", prevSha, secondNewLine);
            //System.out.println(toWrite);
            String parentCommitContentsFirstTwoLines = parentCommitContents.substring(0,secondNewLine+1);
            String parentCommitContentsLastThreeLines = parentCommitContents.substring(thirdNewLine, parentCommitContents.length());
            parentCommitContents = parentCommitContentsFirstTwoLines + newestShaToAddToParent + parentCommitContentsLastThreeLines;
            PrintWriter pw = new PrintWriter("./objects/" + prevSha);
            pw.write(parentCommitContents);
            pw.close();
            /*
            BufferedReader br = new BufferedReader(new FileReader(f));
            StringBuilder sb = new StringBuilder();
            PrintWriter writer = new PrintWriter(f);
            sb.append(br.readLine() + "\n");
            sb.append(br.readLine() + "\n");
            sb.append(toWrite +"\n");
            sb.append(br.readLine() + "\n");
            sb.append(br.readLine() + "\n");
            sb.append(br.readLine() + "\n");
            String temp = sb.toString();
            writer.write(temp);
            br.close();
            */
            //writer.close();
        }
    }

    public static String insertString( String originalString, String stringToBeInserted, int index) 
    { 
  
        // Create a new string 
        String newString = new String(); 
  
        for (int i = 0; i < originalString.length(); i++) { 
  
            // Insert the original string character 
            // into the new string 
            newString += originalString.charAt(i); 
  
            if (i == index) { 
  
                // Insert the string to be inserted 
                // into the new string 
                newString += stringToBeInserted; 
            } 
        } 
  
        // return the modified String 
        return newString; 
    } 

    public static int ordinalIndexOf(String str, String substr, int n) {
        int pos = str.indexOf(substr);
        while (--n > 0 && pos != -1)
            pos = str.indexOf(substr, pos + 1);
        return pos;
    }

    public void addPreviousTreeToCurrentTree(String parentCommit, Tree tree) throws Exception {
            tree.add("tree : " + getLineOne(parentCommit));
    }

    public Commit(String author, String summary) throws Exception
    {
        this("", author, summary);
        /*File f = new File("./objects/" + currSha);
        BufferedReader br = new BufferedReader(new FileReader(f));
            StringBuilder sb = new StringBuilder();
            PrintWriter writer = new PrintWriter(f);
            sb.append(br.readLine() + "\n");
            sb.append(br.readLine() + "\n");
            sb.append(prevSha);
            sb.append(br.readLine() + "\n");
            sb.append(br.readLine() + "\n");
            sb.append(br.readLine() + "\n");
            writer.write(sb.toString());
            br.close();
            writer.close();
            */
        //sha1 = createTree();
        /*
        Index ind = new Index();
        ind.init();
        File f = new File("first");
        if (f.exists())
        {
            String str = ind.fileToString("first");
            prevSha = Blob.encryptThisString(str);
        }
        this.mainTree = new Tree();
        this.author = author;
        this.summary = summary;
        this.date1 = new Date(java.lang.System.currentTimeMillis());
        contentsOfFile = mainTree.getSha() + "\n" + prevSha + "\n" + nextSha + "\n" + author + "\n" + date1 + "\n" + summary;
        sha1 = encryptPassword(contentsOfFile);
        File commitFile = new File("./objects/" + sha1);
        if (!commitFile.exists())
        {
            commitFile.createNewFile();
        }
        PrintWriter writer = new PrintWriter(new FileWriter(commitFile));
        writer.print(contentsOfFile);
        writer.close();
        //linkToNextCom();
        FileWriter fw = new FileWriter("first");
        fw.write(sha1);
        fw.close();
        new FileWriter("./index", false).close();
        */
        
    }

    /*public void linkToNextCom() throws IOException {
        File firstFile = new File("first");
        if (!firstFile.exists())
        {
            return;
        }
        File file = new File("./objects/" + prevSha);
        File fileToReset = new File("./objects/" + "holder");
        try (BufferedReader br = new BufferedReader(new FileReader(file));
             BufferedWriter bw = new BufferedWriter(new FileWriter(fileToReset))) {
            String line = "";
        for (int i = 0; i < 5; i++)
        {
            line = br.readLine();
            if (i == 2)
            {
                bw.write(sha1 + "\n");
            }
            else
            {
                bw.write(line + "\n");
            }
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileToReset.renameTo(file);
    }
    */
    

    public String createTree(Tree tree) throws Exception {
        writeIndexFileToTreeFile(tree);
        File indexClear = new File("index");
        indexClear.delete();
        indexClear.createNewFile();
        String sha = tree.getSha();
        return sha;
    }

    public String getDate() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        return timeStamp;
    }

    public void writeIndexFileToTreeFile(Tree tree) throws Exception {
        File indexFile = new File("index");
        if (!indexFile.exists()) {
            throw new Exception("file doesn't exist");
        }
        BufferedReader br = new BufferedReader(new FileReader(indexFile));
        String read;
        while (br.ready()) {
            read = br.readLine();
            if (!read.contains("*deleted*"))
            {
                tree.add(read);
            }
        }
        br.close();
        tree.writeToFile();
    }

    public String getLineOne(String prevCommitSha) throws IOException
    {
        if (prevCommitSha == "")
        {
            return "";
        }
        String path = "./objects/" + prevCommitSha;
        File file = new File (path);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String ret = br.readLine();
        br.close();
        return ret;
    }

    String getContents(File fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        StringBuilder sb = new StringBuilder();
        while (br.ready())
        {
            sb.append(br.readLine() + "\n");
        }
        br.close();
        return sb.toString();
        /*BufferedReader br = new BufferedReader(new FileReader(fileName));
        String str = "";
        */
        /*while(br.ready()) {
            str += br.readLine()+"\n";
        }*/
        /*
        for(int i=1; i<=6; i++) {
            if(i!=3) {
                str += br.readLine()+"\n";
            }
        }

        str = str.trim();//get rid of extra line

        br.close();
        return str;
        */
    }
    /*public static String getSHA(String contents) {
        String sha1 = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(contents.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sha1;
    }
    */
    public String getSha() {
        return shaOfThisCommit;
    }
    private void rename(File fileName) throws IOException {
        /*BufferedReader br = new BufferedReader(new FileReader(fileName));
        String str = "";

        while(br.ready()) {
            str += br.readLine()+"\n";
        }

        for(int i=1; i<=6; i++) {
            if(i!=3) {
                str += br.readLine()+"\n";
            }
        }

        str = str.trim();//get rid of extra line

        br.close();
        */
        String str = getContents(fileName);
        str = str.stripTrailing();

        //converting to sha1
        // TODO - INCLUDES 3rd LINE IN THE COMMIT
        shaOfThisCommit = encryptPassword(str);

        //printing to objects folder
        String dirName = "./objects/";
        File dir = new File (dirName);
        File newFile = new File (dir, shaOfThisCommit);

        PrintWriter pw = new PrintWriter(newFile);
        /*StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        sb.append(br.readLine() + "\n");
        sb.append(br.readLine() + "\n");
        sb.append("\n");
        sb.append(br.readLine() + "\n");
        sb.append(br.readLine() + "\n");
        sb.append(br.readLine());
        br.close();
        pw.print(sb.toString());
        pw.close();
        */
        //str = str.stripTrailing();
        pw.write(str);
        pw.close();

        File tempCommitFile = new File("commit");
        tempCommitFile.delete();
        tempCommitFile.createNewFile();
    }

    String encryptPassword(String password)
    {
        String sha1 = "";
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return sha1;
    }

    private static String byteToHex(final byte[] hash)
    {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

}
