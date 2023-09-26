import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class tester {
    public static void main (String[] args) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        System.out.println(timeStamp);





        //test
        tree tree = new tree();
        String sha = tree.getSha();
        System.out.println("SHA: " + sha);




        //test
        /*File test = new File("test.txt");
        PrintWriter pw = new PrintWriter("test.txt");
        pw.println("hi");
        pw.println("hi");
        pw.println("hi");
        pw.println("hi");
        pw.println("hi");
        pw.println("hi");
        pw.close();*/

        Commit testCom = new Commit("732d12f7e4f2e629e2954acbb720c32c0be985d1", null, "Bob", "this is a test");

        String dirName = "./objects/";
        File dir = new File (dirName);
        //File check = new File(dir,"76a77319fcb1f9662189048e62b50012e1aee06b");
        //File check = new File(dir,"76a77319fcb1f9662189048e62b50012e1aee06b");
    }
}
