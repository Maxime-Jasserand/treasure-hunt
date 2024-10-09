package helpers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LaunchAdventureHelper implements ActionListener {
    private String inputPath;
    private String outputPath;

    LaunchAdventureHelper(String inputPath, String outputPath){
        this.inputPath = inputPath;
        this.outputPath = outputPath;
    }

    public void actionPerformed(ActionEvent e){

    }
}
