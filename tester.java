import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class tester {
    public static void main (String[] args) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        System.out.println(timeStamp);

        Tree tree = new Tree();
        String sha = tree.getSha();
        System.out.println("SHA: " + sha);
    }
}
