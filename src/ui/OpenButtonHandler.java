package ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;


public class OpenButtonHandler implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        JFileChooser choosedFile = new JFileChooser();
        int ret = choosedFile.showDialog(null, "Select File");
        if (ret == JFileChooser.APPROVE_OPTION) {
            try
            {
                File file = choosedFile.getSelectedFile();
                FileReader reader = new FileReader(file);
                Scanner scanner = new Scanner(reader);
                StringBuilder stringBuilder = new StringBuilder();
                while (scanner.hasNextLine())
                {

                    stringBuilder.append(scanner.nextLine()).append("\n");
                }
                MainWindow.setCode(stringBuilder.toString());
                reader.close();
            }
            catch (Exception ex)
            {
                JOptionPane.showMessageDialog(null, "Error");
            }
        }
    }
}
