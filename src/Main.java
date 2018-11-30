import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) throws Exception {

        String code = "";
        try {
            FileReader fr = new FileReader("code.txt");
            Scanner scan = new Scanner(fr);

            StringBuilder tmp = new StringBuilder();
            while (scan.hasNextLine()) {
                tmp.append(scan.nextLine()).append("\n");
            }
            if (tmp.length() > 0) {
                tmp.deleteCharAt(tmp.length() - 1);
            }
            code = tmp.toString();
            //System.out.println(code);
            fr.close();
        }
        catch (FileNotFoundException e)
        {
            JOptionPane.showMessageDialog(null, "File not found!");
        }
        Parser myFuckingAwesomeObject = new Parser();
        myFuckingAwesomeObject.parse(code);
        myFuckingAwesomeObject.show();
    }
}

