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
    private String date;
    private String summary;
    private Tree mainTree;
    private String contentsOfFile;
    private String sha ="";
    private Date date1;

    
    public Commit(String prevCommitSha, String author, String summary) throws IOException {
        File objects = new File("./objects");
        if (!objects.exists())
            objects.mkdirs();
        
        String sha = createTree();

        commit = new File("commit");

        //print sha
        PrintWriter pw = new PrintWriter(new FileWriter("commit"));
        pw.println(sha);

        //print location
        pw.println(prevCommitSha);

        //print empty line
        pw.println();

        //print date
        pw.println(getDate());

        //print name
        pw.println(author);

        //print summary
        pw.print(summary);

        pw.close();

        rename(commit);
    }

    public Commit(String author, String summary) throws IOException
    {
        Index ind = new Index();
        ind.init();
        File f = new File("first");
        if (f.exists())
        {
            String str = ind.fileToString("first");
            prevSha = Blob.encryptThisString(str);
        }
        this.mainTree = new Tree(getLineOne());
        this.author = author;
        this.summary = summary;
        this.date1 = new Date(java.lang.System.currentTimeMillis());
        contentsOfFile = mainTree.getSha() + "\n" + prevSha + "\n" + nextSha + "\n" + author + "\n" + date1 + "\n" + summary;
        sha = encryptPassword(contentsOfFile);
        File commitFile = new File("./objects/" + sha);
        if (!commitFile.exists())
        {
            commitFile.createNewFile();
        }
        PrintWriter writer = new PrintWriter(new FileWriter(commitFile));
        writer.print(contentsOfFile);
        writer.close();
        linkToNextCom();
        FileWriter fw = new FileWriter("first");
        fw.write(sha);
        fw.close();
        new FileWriter("./index", false).close();
    }

    public void linkToNextCom() throws IOException {
        File firstFile = new File("first");
        if (!firstFile.exists())
        {
            return;
        }
        File currentFile = new File("./objects/" + prevSha);
        File holderFile = new File("./objects/holder");
        try (BufferedReader br = new BufferedReader(new FileReader(currentFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(holderFile))) {
            String line = "";
            for (int i = 0; i < 2; i++)
            {
                line = br.readLine();
                bw.write(line + "\n");
            }
            bw.write(sha + "\n");
            for (int i = 0; i < 2; i++)
            {
                line = br.readLine();
            }
            while ((line = br.readLine()) != null)
            {
                bw.write(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        holderFile.renameTo(currentFile);
    }
    

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
        String path = "./objects/" + getSha();
        File file = new File (path);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String ret = br.readLine();
        br.close();
        return ret;
    }

    private String getContents(File fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String str = "";

        /*while(br.ready()) {
            str += br.readLine()+"\n";
        }*/

        for(int i=1; i<=6; i++) {
            if(i!=3) {
                str += br.readLine()+"\n";
            }
        }

        str = str.trim();//get rid of extra line

        br.close();
        return str;
    }

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

        br.close();*/

        String str = getContents(fileName);

        //converting to sha1

        String sha1 = encryptPassword(str);

        //printing to objects folder
        String dirName = "./objects/";
        File dir = new File (dirName);
        File newFile = new File (dir, sha1);

        PrintWriter pw = new PrintWriter(newFile);

        pw.print(str);

        pw.close();
    }

    private String encryptPassword(String password)
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

    private String byteToHex(final byte[] hash)
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
