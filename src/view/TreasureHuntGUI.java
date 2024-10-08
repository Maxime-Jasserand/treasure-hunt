package view;

import helpers.FileChooserHelper;
import model.Treasure;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class TreasureHuntGUI extends JFrame {
    public TreasureHuntGUI() {
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
        inputFileChooser.add(buildFileChooser());
        add(inputFileChooser);

        JPanel outputFileChooser = new JPanel();
        outputFileChooser.add(new JLabel("<html><div>Dossier de destination (facultatif)</div></html>"));
        outputFileChooser.add(buildFileChooser());
        add(outputFileChooser);

        add(new JButton("Partir à l'aventure"));

        setVisible(true);
    }

    private JPanel buildFileChooser() {
        JPanel panel = new JPanel();
        JTextField textField = new JTextField(25);
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
