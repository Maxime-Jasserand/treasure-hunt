package view;

import exceptions.EmptyInputException;
import exceptions.InvalidFileFormatException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TreasureHuntGUITest {
    private TreasureHuntGUI gui;

    @Before
    public void setup() {
        gui = new TreasureHuntGUI(e -> {
        });
    }

    /*-------------------------   INPUT TESTS   -------------------------*/
    @Test
    public void shouldReturnInputText() throws InvalidFileFormatException, EmptyInputException {
        String expectedText = "documents/map.txt";
        gui.inputTextField.setText(expectedText);
        String testedText = gui.getInputPath();
        assertEquals(expectedText,testedText);
    }

    @Test public void shouldThrowEmptyInputException() throws InvalidFileFormatException {
        try{
            String testedText = gui.getInputPath();
            fail("Expected EmptyInputException");
        } catch (EmptyInputException e) {
            //test passes
        }
    }

    @Test public void shouldThrowInvalidInputFileFormatException() throws EmptyInputException {
        try{
            gui.inputTextField.setText("documents/map.docx");
            String testedText = gui.getInputPath();
            fail("Expected InvalidFileFormatException");
        } catch (InvalidFileFormatException e) {
            //test passes
        }
    }

    /*-------------------------   OUTPUT TESTS   -------------------------*/
    @Test
    public void shouldReturnOutputTextFromDirectory() throws InvalidFileFormatException {
        String expectedText = "/Users/maximejasserand/Desktop/results.txt";
        gui.outputTextField.setText("/Users/maximejasserand/Desktop");
        String testedText = gui.getOutputPath();
        assertEquals(expectedText,testedText);
    }

    @Test
    public void shouldReturnOutputTextFromFile() throws InvalidFileFormatException {
        String expectedText = "/Users/maximejasserand/Desktop/already existing file.txt";
        gui.outputTextField.setText(expectedText);
        String testedText = gui.getOutputPath();
        assertEquals(expectedText,testedText);
    }

    @Test
    public void shouldOutputTextFieldBeFacultative()throws InvalidFileFormatException {
        gui.inputTextField.setText("documents/map.txt");
        String expectedText = "documents/map - results.txt";
        String testedText = gui.getOutputPath();
        assertEquals(expectedText,testedText);
    }

    @Test public void shouldThrowInvalidOutputFileFormatException() {
        try{
            gui.outputTextField.setText("documents/map.docx");
            String testedText = gui.getOutputPath();
            fail("Expected InvalidFileFormatException");
        } catch (InvalidFileFormatException e) {
            //test passes
        }
    }
}