package helpers;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Open a dialog with a file chooser
 * The file chooser will allow only .txt files to be selected
 * It can also allow directory selection if specified (for output)
 * Once a file or directory is selected, it's path will be written to the textField set in constructor
 */
public class FileChooserHelper implements ActionListener {
    JTextField textField;
    private final boolean allowDirectories;

    public FileChooserHelper(JTextField textField, boolean allowDirectories) {
        this.textField = textField;
        this.allowDirectories = allowDirectories;
    }

    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();

        //Allow only txt files
        FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt", "txt");
        fileChooser.setFileFilter(filter);

        //Allow directories if specified
        if(allowDirectories){
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        }

        //If user confirm selection, write the path in textField
        int confirmSelection = fileChooser.showOpenDialog(null);
        if (confirmSelection == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String path = file.getAbsolutePath();
            textField.setText(path);
        }
    }
}
