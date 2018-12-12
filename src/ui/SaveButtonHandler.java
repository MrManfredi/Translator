package ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;

public class SaveButtonHandler implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        JFileChooser choosedFile = new JFileChooser();
        int ret = choosedFile.showDialog(null, "Select File");
        if (ret == JFileChooser.APPROVE_OPTION) {
            try
            {
                File file = choosedFile.getSelectedFile();
                FileWriter writer = new FileWriter(file);
                writer.write(MainWindow.getCode().getText());
                writer.close();
            }
            catch (Exception ex)
            {
                JOptionPane.showMessageDialog(null, "Error");
            }
        }
    }
}
