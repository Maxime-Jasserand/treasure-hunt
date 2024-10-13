package view;

import exceptions.EmptyInputException;
import exceptions.InvalidFileFormatException;
import helpers.FileChooserHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Our view is made of
 * - a styled introduction welcoming the user
 * - an input file chooser for .txt files only
 * - an output file chooser for .txt files or directories
 * - a button to launch the process
 */
public class TreasureHuntGUI extends JFrame {
    protected JTextField inputTextField;
    protected JTextField outputTextField;

    public TreasureHuntGUI(ActionListener launchAdventureEvent) {
        //JFrame initialization
        super("Chasse au trésor");
        setSize(1200, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(0, 1));

        //Styled introduction
        add(new JLabel("<html><div style='margin: 16px'>" +
                "<div style='margin-bottom: 8px; font-size: 16px;'>Salutations aventurier, j'imagine que tu as hâte de trouver le trésor !</div>" +
                "<div style='font-size: 12px; font-weight: 300;'>Alors indiques nous l'emplacement de la carte, éventuellement une destination (un dossier ou un fichier .txt qui sera écrasé) on s'occupe du reste !</div>" +
                "</div></html>"));

        //Input file chooser
        JPanel inputFileChooser = new JPanel();
        inputFileChooser.add(new JLabel("<html><div>Fichier d'entrée (.txt)</div></html>"));
        inputTextField = new JTextField(25);
        FileChooserHelper inputChooserHelper = new FileChooserHelper(inputTextField, false);
        inputFileChooser.add(buildFileChooser(inputTextField, inputChooserHelper));
        add(inputFileChooser);

        //Input file/directory chooser
        JPanel outputFileChooser = new JPanel();
        outputFileChooser.add(new JLabel("<html><div>Destination (facultatif)</div></html>"));
        outputTextField = new JTextField(25);
        FileChooserHelper outputChooserHelper = new FileChooserHelper(outputTextField, true);
        outputFileChooser.add(buildFileChooser(outputTextField, outputChooserHelper));
        add(outputFileChooser);

        //Launch button
        JButton launchButton = new JButton("Partir à l'aventure");
        launchButton.addActionListener(launchAdventureEvent);
        add(launchButton);

        setVisible(true);
    }

    /*-------------------------   DIALOGS METHODS  -------------------------*/

    /**
     * Display a non-blocking error message
     */
    public void displayErrorDialog(Exception error) {
        new Thread(() -> JOptionPane.showMessageDialog(null, error.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE)).start();
    }

    public void displaySuccessDialog() {
        JOptionPane.showMessageDialog(null, "L'expédition est un succès !", "Félicitations", JOptionPane.INFORMATION_MESSAGE);
    }

    /*-------------------------   PATH EXTRACTION METHODS   -------------------------*/

    /**
     * Get input path from inputTextField
     *
     * @return a String representing the input file path
     * @throws EmptyInputException        if the input is empty
     * @throws InvalidFileFormatException if the file format is bad
     */
    public String getInputPath() throws EmptyInputException, InvalidFileFormatException {
        if (inputTextField.getText().isEmpty()) {
            throw new EmptyInputException();
        }
        if (!inputTextField.getText().endsWith(".txt")) {
            throw new InvalidFileFormatException();
        }
        return inputTextField.getText();
    }

    /**
     * Get output path from outputTextField
     *
     * @return a String representing the output file path
     * @throws InvalidFileFormatException if the file format is bad
     */
    public String getOutputPath() throws InvalidFileFormatException {
        //If no output is specified, use the input to create a new file
        if (outputTextField.getText().isEmpty()) {
            return inputTextField.getText().substring(0, inputTextField.getText().length() - 4) + " - results.txt";     //remove .txt then add - results.txt
        }
        //If an output is specified, we can either add a new result.txt file if the user select a directory
        File file = new File(outputTextField.getText());
        if (file.isDirectory()) {
            return new File(file, "results.txt").getAbsolutePath();
        }
        //Or simply return the selected file while checking for the type
        if (!outputTextField.getText().endsWith(".txt")) {
            throw new InvalidFileFormatException();
        }
        return outputTextField.getText();
    }

    /*-------------------------   UTILS   -------------------------*/

    /**
     * Build a file chooser JPanel from
     *
     * @param textField      hosting the result
     * @param actionListener event opening a fileChooser
     * @return a JPanel made of a text field containing a button
     */
    private JPanel buildFileChooser(JTextField textField, ActionListener actionListener) {
        JPanel panel = new JPanel();
        JButton button = new JButton("...");
        panel.add(textField);
        panel.add(button);

        //Add button action
        button.addActionListener(actionListener);

        //Visual customization
        panel.setBackground(textField.getBackground());
        panel.setBorder(textField.getBorder());
        textField.setBorder(null);

        return panel;
    }
}
