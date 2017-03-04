package fmp.ee;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * @author pedrozaf
 * @since 3/4/17
 */
public class WeekendListChooser {

    public static void main(String[] args) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("Selected iput file: " + selectedFile.getAbsolutePath());

            try {
                File outputFile = new WeekendList(new File(selectedFile.getAbsolutePath())).create();
                JOptionPane.showMessageDialog(null, "Weekend list saved as: " + outputFile.getAbsolutePath());
            }
            catch (IOException e) {
                System.err.println("An error occurred processing file: " + selectedFile.getAbsolutePath());
                e.printStackTrace();
            }
        }
    }
}
