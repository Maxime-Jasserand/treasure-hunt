package helpers;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class FileChooserHelper implements ActionListener {
    JTextField textField;

    public FileChooserHelper(JTextField textField){
        this.textField = textField;
    }

    public void actionPerformed(ActionEvent e){
        JFileChooser fileChooser = new JFileChooser();

        int shouldOpen = fileChooser.showOpenDialog(null);

        if(shouldOpen == JFileChooser.APPROVE_OPTION){
            File file = fileChooser.getSelectedFile();
            String path = file.getAbsolutePath();
            textField.setText(path);
        }
    }
}
