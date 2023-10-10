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

public class Commit {
    private File commit;
    private String prevSha = "";
    private String nextSha = "";
    private String author;
    private String currSha;
    private String date;
    private String summary;
    private Tree mainTree;
    private String contentsOfFile;
    private String sha1 ="";
    private Date date1;

    
    public Commit(String prevCommitSha, String author, String summary) throws IOException {
        prevSha = prevCommitSha;
        File objects = new File("./objects");
        if (!objects.exists())
            objects.mkdirs();
        
        String sha = createTree();
        date1 = new Date(java.lang.System.currentTimeMillis());
        commit = new File("commit");
        /*
        StringBuilder sb = new StringBuilder();
        sb.append(sha);
        sb.append("\n" + prevCommitSha);
        sb.append("\n" + author);
        sb.append("\n");
        sb.append("\n" + date1);
        sb.append("\n" + summary);
        currSha = encryptPassword(sb.toString());
        nextSha = currSha;
        File com = new File("./objects/" + currSha);
        PrintWriter writer = new PrintWriter(com);
        writer.println(sha);
        writer.println(prevCommitSha);
        writer.println("");
        writer.println(author);
        writer.println(date1);
        writer.print(summary);

        writer.close();
        */
        //print sha
        //File f = new File("commit");
        
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

        rename(commit);
        updateChildShaOfParent();
        if (prevCommitSha != "")
        {
            File f = new File("./objects/" + prevCommitSha);
        //BufferedReader br = new BufferedReader(new FileReader(commit));
        BufferedReader br2 = new BufferedReader(new FileReader(commit));
        StringBuilder sb = new StringBuilder();
        PrintWriter writer = new PrintWriter(encryptPassword(getContents(f)));
        sb.append(br2.readLine() + "\n");
        sb.append(br2.readLine() + "\n");
        sb.append(encryptPassword(getContents(commit)));
        sb.append(br2.readLine() + "\n");
        sb.append(br2.readLine() + "\n");
        sb.append(br2.readLine() + "\n");
        br2.close();
        writer.write(sb.toString());
        writer.close();
        }
    }

    //public void setFirstToNext
    
    public void updateChildShaOfParent() throws IOException
    {
        if (prevSha == "")
        {
            return;
        }
        File f = new File("./objects/" + prevSha);
        if (f.exists())
        {
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
        }
    }

    public Commit(String author, String summary) throws IOException
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
    

    public String createTree() throws IOException {
        Tree tree = new Tree();
        String sha = tree.getSha();
        return sha;
    }

    public String getDate() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        return timeStamp;
    }

    public String getLineOne() throws IOException
    {
        if (prevSha == "")
        {
            return "";
        }
        String path = "./objects/" + prevSha;
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
    public String getSha() throws IOException {
        String str = getContents(commit);
        return encryptPassword(str);
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

        String sha1 = encryptPassword(str);

        //printing to objects folder
        String dirName = "./objects/";
        File dir = new File (dirName);
        //File f = new File("./objects/" + sha1);
        File newFile = new File (dir, sha1);

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
