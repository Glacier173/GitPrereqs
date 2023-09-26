import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Formatter;

public class Commit {
    private File commit;
    
    public Commit(String prevCommit, String nextCommit, String author, String summary) throws IOException {
        File objects = new File("./objects");
        if (!objects.exists())
            objects.mkdirs();
        
        String sha = createTree();

        commit = new File("commit");

        //print sha
        PrintWriter pw = new PrintWriter(new FileWriter("commit"));
        pw.println(sha);

        //print location
        String prevSha = encryptPassword("prevCommit");
        pw.println(prevSha);

        //print empty line
        String nextSha = encryptPassword("nextCommit");
        pw.println(nextSha);

        //print date
        pw.println(getDate());

        //print name
        pw.println(author);

        //print summary
        pw.print(summary);

        pw.close();

        rename(commit);
    }

    public String createTree() throws IOException {
        tree tree = new tree();
        String sha = tree.getSha();
        return sha;
    }

    public String getDate() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        return timeStamp;
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
