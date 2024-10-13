package controller;

import exceptions.InvalidFileFormatException;
import exceptions.InvalidLineException;
import exceptions.TileOutOfMapException;
import exceptions.TileUnavailableException;
import helpers.IOHelper;
import model.*;
import model.Map;
import view.TreasureHuntGUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Controller responsible for the good orchestration of the program
 */
public class TreasureHuntController {
    private final TreasureHuntGUI gui;
    protected Map map;

    /**
     * On creation, display the view and pass it launchAdventure as an ActionListener
     */
    public TreasureHuntController() {
        gui = new TreasureHuntGUI(_ -> launchAdventure());
    }

    /**
     * Principal method of the code
     * Will be called when clicking the launch adventure button
     * - Generate the map from input
     * - Execute the adventurers moves
     * - Write the results in the output
     * - All that while displaying dialogs in case of error or success
     */
    private void launchAdventure() {
        try {
            String inputPath = gui.getInputPath();
            String outputPath = gui.getOutputPath();
            BufferedReader reader = IOHelper.getInputReader(inputPath);
            generateMapFromReader(reader);
            executeAdventurersMoves();
            writeResults(outputPath);
            gui.displaySuccessDialog();
        } catch (IOException | InvalidLineException e) {
            gui.displayErrorDialog(e);
        }
    }

    /**
     * Generate the map and all its Tiles from the input file Reader
     */
    protected void generateMapFromReader(BufferedReader reader) throws IOException, InvalidLineException {
        //Read the first line and create the map
        String firstLine = reader.readLine();
        if (firstLine.charAt(0) != 'C') {
            //Display error message if the file doesn't start with C because we expect the map first
            //Could be a bad formatting or a bad encoding
            //This part could be improved for more flexibility in file reading
            throw new InvalidFileFormatException("Le fichier doit commencer par la carte. Veuillez vous assurez que la première ligne commence bien par C et que l'encodage est en UTF-8.");
        }

        //Split the line in parts
        //Throws InvalidLineException if line not valid
        //Generate Map from the parts
        String[] firstLineParts = firstLine.split(" - ");
        isLineValid(firstLineParts);
        map = new Map(Integer.parseInt(firstLineParts[1]), Integer.parseInt(firstLineParts[2]));

        //Continue by reading all the lines and creating the corresponding tiles
        String line;
        while ((line = reader.readLine()) != null) {
            try {
                Tile newTile = createTileFromLine(line);
                if (!map.isInsideBoundaries(newTile.getX(), newTile.getY())) {
                    throw new TileOutOfMapException("La ligne suivante tente de créer un objet en dehors des limites de la carte : " + newTile.getOutputLine() + ". Elle sera ignorée.");
                }
                map.addTile(newTile);
            } catch (TileUnavailableException | TileOutOfMapException e) {
                //If the tile is unavailable or out of the map boundaries, show a prompt to the user indicating the line
                //But continue with the other lines
                gui.displayErrorDialog(e);
            }
        }
    }

    /**
     * Execute all the moves in all the adventurers move list
     * The adventurers are stored in the same order as they are read from the input file
     * So conflict will be handled the right way
     */
    protected void executeAdventurersMoves() {
        List<Adventurer> adventurers = map.getAllAdventurers();

        while (!adventurers.isEmpty()) {
            //Use an Iterator to prevent ConcurrentModificationException
            Iterator<Adventurer> iterator = adventurers.iterator();
            while (iterator.hasNext()) {
                Adventurer a = iterator.next();

                //Execute the next move
                char nextMove = a.getNextMove();
                switch (nextMove) {
                    case 'A' -> {
                        // get the next tile
                        HashMap<String, Integer> coordinates = a.getNextTileCoordinates();
                        int x = coordinates.get("x");
                        int y = coordinates.get("y");
                        Optional<Tile> nextTile = map.getTile(x, y);
                        // we check the next tile before moving
                        if (map.isInsideBoundaries(x, y) && (nextTile.isEmpty() || nextTile.get().isCrossable())) {
                            a.moveForward();
                            //Get an eventual treasure on the tile
                            Optional<Treasure> treasure = map.getTreasure(x, y);
                            if (!treasure.isEmpty()) {
                                //Pick it up if any
                                map.pickupTreasure(treasure.get());
                                //Increase adventurer treasure count
                                a.increaseTreasureCount();

                            }
                        }
                    }
                    case 'G' -> a.turnLeft();
                    case 'D' -> a.turnRight();
                }
                //Remove the move that has been executed from the list
                a.removeNextMove();

                //If the move list is empty, we remove the adventurer from our iterator
                if (a.getMoveList().isEmpty()) {
                    iterator.remove();
                }
            }
        }
    }

    /**
     * Write the results in the output file
     */
    private void writeResults(String outputPath) {
        //Get writer with IOHelper in a try-with-ressources that will handle the writer close
        try (PrintWriter writer = IOHelper.getOutputWriter(outputPath)) {

            //Start by writing the map line
            writer.println(map.getOutputLine());

            //Then write all the others
            map.getAllTiles().forEach(t -> writer.println(t.getOutputLine()));
        } catch (IOException e) {
            gui.displayErrorDialog(e);
        }

    }

    /*-------------------------   UTILS   -------------------------*/

    /**
     * Create a Tile from a
     *
     * @param line of input
     * @return the created Tile and
     * @throws TileUnavailableException if the tile is already taken or
     * @throws InvalidLineException     if the string format is bad
     */
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
            case null, default -> {
                return null;
            }
        }
    }

    /**
     * Check if a tile is available
     *
     * @throws TileUnavailableException
     */
    private boolean isTileAvailable(int x, int y) throws TileUnavailableException {
        if (map.getTile(x, y).isEmpty()) {
            return true;
        }
        throw new TileUnavailableException("Position indisponible aux coordonnées x:" + x + ", y:" + y + ". Cette ligne sera ignorée.");
    }


    /**
     * Check if a line is valid
     *
     * @throws InvalidLineException
     */
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
            throw new InvalidLineException("Format invalide à la ligne :" + String.join(" - ", lineParts));
        }
        return true;
    }

    /**
     * Check if a string is present in a list of string
     */
    private boolean isStringInList(String testedString, String[] list) {
        List<String> possibleCharList = Arrays.asList(list);
        return possibleCharList.contains(testedString);
    }
}
