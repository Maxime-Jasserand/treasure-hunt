package view;

import controller.TreasureHuntController;
import helpers.FileChooserHelper;
import model.Treasure;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

public class TreasureHuntGUI extends JFrame {
    private JTextField inputTextField;
    private JTextField outputTextField;

    public TreasureHuntGUI(ActionListener submitEvent) {
        super("Chasse au trésor");
        setSize(1200, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(0, 1));
        add(new JLabel("<html><div style='margin: 16px'>" +
                "<div style='margin-bottom: 8px; font-size: 16px;'>Salutations aventurier, j'imagine que tu as hâte de trouver le trésor !</div>" +
                "<div style='font-size: 12px; font-weight: 300;'>Alors indiques nous l'emplacement de la carte, éventuellement un dossier de destination pour visualiser le résultat et on s'occupe du reste !</div>" +
                "</div></html>"));

        JPanel inputFileChooser = new JPanel();
        inputFileChooser.add(new JLabel("<html><div>Fichier d'entrée</div></html>"));
        inputTextField = new JTextField(25);
        inputFileChooser.add(buildFileChooser(inputTextField));
        add(inputFileChooser);

        JPanel outputFileChooser = new JPanel();
        outputFileChooser.add(new JLabel("<html><div>Dossier de destination (facultatif)</div></html>"));
        outputTextField = new JTextField(25);
        outputFileChooser.add(buildFileChooser(outputTextField));
        add(outputFileChooser);

        JButton launchButton = new JButton("Partir à l'aventure");
        launchButton.addActionListener(submitEvent);
        add(launchButton);

        setVisible(true);
    }

    public String getInputPath(){
        return inputTextField.getText();
    }

    public String getOutputPath(){
        return outputTextField.getText();
    }

    private JPanel buildFileChooser(JTextField textField) {
        JPanel panel = new JPanel();
        JButton button = new JButton("...");
        panel.add(textField);
        panel.add(button);

        // BUTTON ACTION
        FileChooserHelper fileChooserHelper = new FileChooserHelper(textField);
        button.addActionListener(fileChooserHelper);

        // VISUAL CUSTOMIZATION
        panel.setBackground(textField.getBackground());
        panel.setBorder(textField.getBorder());
        textField.setBorder(null);

        return panel;
    }
}
