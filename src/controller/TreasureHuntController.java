package controller;

import exceptions.InvalidLineException;
import exceptions.TileUnavailableException;
import helpers.IOHelper;
import helpers.LaunchAdventureHelper;
import model.*;
import view.TreasureHuntGUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TreasureHuntController {
    private TreasureHuntGUI gui;
    private Map map;

    public TreasureHuntController() {
        gui = new TreasureHuntGUI(e -> launchAdventure());
    }

    private void launchAdventure() {
        String inputPath = gui.getInputPath();
        String outputPath = gui.getOutputPath();
        generateMapFromInput(inputPath);
        executeAdventurersMoves();
        writeResults(outputPath);
    }

    private void executeAdventurersMoves() {

    }

    private void writeResults(String outputPath) {

    }

    private void generateMapFromInput(String inputPath) {
        try {
            BufferedReader reader = IOHelper.getInputReader(inputPath);
            //Read the first line and create the map
            String firstLine = reader.readLine();
            if (firstLine.charAt(0) != 'C') {
                //Display error message if the file doesn't start with C because we expect the map first
                //Could be a bad formatting or a bad encoding
                //This part could be improved for more flexibility in file reading
                return;
            }

            String[] firstLineParts = firstLine.split(" - ");
            isLineValid(firstLineParts);
            map = new Map(Integer.parseInt(firstLineParts[1]), Integer.parseInt(firstLineParts[2]));

            //Continue by reading all the lines and creating the corresponding tiles
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    map.addTile(createTileFromLine(line));
                } catch (TileUnavailableException e) {
                    //if the tile is unavailable, show a prompt to the user indicating the line
                    //but continue with the other lines
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        } catch (InvalidLineException e) {
            //
        }
    }

    private Tile createTileFromLine(String line) throws TileUnavailableException, InvalidLineException {
        String[] parts = line.split(" - ");
        if (!isLineValid(parts)) {
            throw new InvalidLineException();
        }
        switch (parts[0]) {
            case "M" -> {
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);

                isTileAvailable(x, y);
                return new Mountain(x, y);
            }
            case "T" -> {
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                int treasureQuantity = Integer.parseInt(parts[3]);

                isTileAvailable(x, y);
                return new Treasure(x, y, treasureQuantity);
            }
            case "A" -> {
                int x = Integer.parseInt(parts[2]);
                int y = Integer.parseInt(parts[3]);

                isTileAvailable(x, y);
                return new Adventurer(
                        parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3]),
                        parts[4],
                        parts[5]
                );
            }
        }
    }

    private boolean isTileAvailable(int x, int y) throws TileUnavailableException {
        if (map.getTile(x, y).isEmpty()) {
            return true;
        }
        throw new TileUnavailableException();
    }

    private boolean isLineValid(String[] lineParts) throws InvalidLineException {
        //Check the first character which should be "C", "M", "T" or "A"
        if (!isStringInList(lineParts[0], new String[]{"C", "M", "T", "A"})) {
            throw new InvalidLineException(String.join(" - ", lineParts));
        }
        //Try to parse the parts as int and use the exception to return false if not possible
        try {
            switch (lineParts[0]) {
                case "C", "M" -> {
                    Integer.parseInt(lineParts[1]);
                    Integer.parseInt(lineParts[2]);
                }
                case "T" -> {
                    Integer.parseInt(lineParts[1]);
                    Integer.parseInt(lineParts[2]);
                    Integer.parseInt(lineParts[3]);
                }
                case "A" -> {
                    Integer.parseInt(lineParts[2]);
                    Integer.parseInt(lineParts[3]);
                    //Check that all the necessary strings are present
                    if (lineParts[1].isEmpty() || lineParts[4].isEmpty() || lineParts[5].isEmpty()) {
                        throw new InvalidLineException(String.join(" - ", lineParts));
                    }
                    //Check for orientation
                    if (!isStringInList(lineParts[4], new String[]{"N", "E", "S", "O"})) {
                        throw new InvalidLineException(String.join(" - ", lineParts));
                    }
                    //Check for move list which should be zero or more char between A, G or D
                    String regex = "^[AGD]*$";
                    if (!lineParts[5].matches(regex)) {
                        throw new InvalidLineException(String.join(" - ", lineParts));
                    }
                }
            }
        } catch (NumberFormatException e) {
            throw new InvalidLineException(String.join(" - ", lineParts));
        }
        return true;
    }

    private boolean isStringInList(String testedString, String[] list) {
        List<String> possibleCharList = Arrays.asList(list);
        return possibleCharList.contains(testedString);
    }
}
