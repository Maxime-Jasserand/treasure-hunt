package controller;

import exceptions.InvalidFileFormatException;
import exceptions.InvalidLineException;
import model.*;
import model.enums.Orientation;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class TreasureHuntControllerTest {
    TreasureHuntController controller;

    @Before
    public void setup() {
        controller = new TreasureHuntController();
    }

    /*-------------------------   MAP GENERATION   -------------------------*/
    @Test
    public void shouldGenerateMapFromReader() throws IOException, InvalidLineException {
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        when(mockReader.readLine())
                .thenReturn("C - 3 - 4")
                .thenReturn("M - 1 - 0")
                .thenReturn("M - 2 - 1")
                .thenReturn("T - 0 - 3 - 2")
                .thenReturn("T - 1 - 3 - 3")
                .thenReturn("A - Lara - 1 - 1 - S - AADADAGGA")
                .thenReturn(null);

        controller.generateMapFromReader(mockReader);

        //Test the map itself
        Map testedMap = controller.map;
        assertEquals(testedMap.getX(), 3);
        assertEquals(testedMap.getY(), 4);

        //Test its content
        Object[] testedTiles = testedMap.getAllTiles().toArray();
        assertEquals(testedTiles.length, 5);

        assertTrue(((Mountain) testedTiles[0]).hasCoordinates(1, 0));
        assertTrue(((Mountain) testedTiles[1]).hasCoordinates(2, 1));

        assertTrue(((Treasure) testedTiles[2]).hasCoordinates(0, 3));
        assertEquals(((Treasure) testedTiles[2]).getQuantity(), 2);

        assertTrue(((Treasure) testedTiles[3]).hasCoordinates(1, 3));
        assertEquals(((Treasure) testedTiles[3]).getQuantity(), 3);

        assertEquals(((Adventurer) testedTiles[4]).getName(), "Lara");
        assertTrue(((Adventurer) testedTiles[4]).hasCoordinates(1, 1));
        assertEquals(((Adventurer) testedTiles[4]).getOrientation(), Orientation.S);
        assertEquals(((Adventurer) testedTiles[4]).getMoveList(), "AADADAGGA");
    }

    @Test
    public void shouldNotCreateTileOutOfMap() throws InvalidLineException, IOException {
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        when(mockReader.readLine())
                .thenReturn("C - 3 - 4")
                .thenReturn("M - 10 - 0")
                .thenReturn(null);

        controller.generateMapFromReader(mockReader);

        //The map should contain zero tile
        List<Tile> createdTiles = controller.map.getAllTiles();
        assertEquals(createdTiles.size(), 0);

    }
    /*-------------------------   ADVENTURERS MOVEMENTS   -------------------------*/
    @Test
    public void shouldExecuteAdventurersMoves() throws IOException, InvalidLineException {
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        when(mockReader.readLine())
                .thenReturn("C - 3 - 4")
                .thenReturn("M - 1 - 0")
                .thenReturn("M - 2 - 1")
                .thenReturn("T - 0 - 3 - 2")
                .thenReturn("T - 1 - 3 - 3")
                .thenReturn("A - Lara - 1 - 1 - S - AADADAGGA")
                .thenReturn(null);

        controller.generateMapFromReader(mockReader);
        controller.executeAdventurersMoves();

        Adventurer adventurer = controller.map.getAllAdventurers().getFirst();
        //Expect the adventurer to have an empty move list
        assertTrue(adventurer.getMoveList().isEmpty());
        //End on position 0,3
        assertTrue(adventurer.hasCoordinates(0, 3));
        //Oriented to the South
        assertEquals(adventurer.getOrientation(), Orientation.S);
        //With 3 treasures
        assertEquals(adventurer.getTreasureCount(), 3);
    }

    @Test
    public void adventurersShouldNotCrossMountains() throws IOException, InvalidLineException {
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        when(mockReader.readLine())
                .thenReturn("C - 3 - 4")
                .thenReturn("M - 1 - 1")
                .thenReturn("A - Lara - 1 - 0 - S - A")
                .thenReturn(null);

        controller.generateMapFromReader(mockReader);
        controller.executeAdventurersMoves();

        Adventurer adventurer = controller.map.getAllAdventurers().getFirst();
        //Expect the adventurer to have an empty move list
        assertTrue(adventurer.getMoveList().isEmpty());
        //End on the same position as it began, since it can't cross mountains
        assertTrue(adventurer.hasCoordinates(1, 0));
    }

    @Test
    public void adventurersShouldNotCrossEachOther() throws IOException, InvalidLineException {
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        when(mockReader.readLine())
                .thenReturn("C - 3 - 4")
                .thenReturn("A - Lara - 1 - 0 - S - A")
                .thenReturn("A - John - 1 - 1 - E - A")
                .thenReturn(null);

        controller.generateMapFromReader(mockReader);
        controller.executeAdventurersMoves();

        Adventurer lara = controller.map.getAllAdventurers().getFirst();
        //Expect the adventurer to have an empty move list
        assertTrue(lara.getMoveList().isEmpty());
        //End on the same position as it began, since it can't cross other adventurers
        assertTrue(lara.hasCoordinates(1, 0));

        Adventurer john = controller.map.getAllAdventurers().get(1);
        //Expect the adventurer to have an empty move list
        assertTrue(john.getMoveList().isEmpty());
        //Is allowed to move on the next tile
        assertTrue(john.hasCoordinates(2, 1));
    }

    @Test
    public void shouldPickupATreasure() throws IOException, InvalidLineException {
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        when(mockReader.readLine())
                .thenReturn("C - 3 - 4")
                .thenReturn("A - Lara - 1 - 0 - S - AA")
                .thenReturn("T - 1 - 1 - 2")
                .thenReturn(null);

        controller.generateMapFromReader(mockReader);
        controller.executeAdventurersMoves();

        Adventurer adventurer = controller.map.getAllAdventurers().getFirst();
        //Expect the adventurer to have an empty move list
        assertTrue(adventurer.getMoveList().isEmpty());
        //It should be able to cross the treasure
        assertTrue(adventurer.hasCoordinates(1, 2));
        //And have one treasure
        assertEquals(adventurer.getTreasureCount(), 1);

        //Also the treasure should still exist but with a reduced quantity
        Treasure treasure = controller.map.getTreasure(1, 1).get();
        assertEquals(treasure.getQuantity(), 1);
    }

    @Test
    public void shouldNotMoveOutsideMap() throws IOException, InvalidLineException {
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        when(mockReader.readLine())
                .thenReturn("C - 3 - 4")
                .thenReturn("A - Lara - 1 - 0 - N - AA")
                .thenReturn("T - 1 - 1 - 2")
                .thenReturn(null);

        controller.generateMapFromReader(mockReader);
        controller.executeAdventurersMoves();

        Adventurer adventurer = controller.map.getAllAdventurers().getFirst();
        //Expect the adventurer to have an empty move list
        assertTrue(adventurer.getMoveList().isEmpty());
        //It should stay on the same position
        assertTrue(adventurer.hasCoordinates(1, 0));
    }

    /*-------------------------   EXCEPTION TESTING   -------------------------*/
    @Test
    public void shouldThrowInvalidLineException() throws IOException {
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        when(mockReader.readLine())
                .thenReturn("Clearly not a valid line");

        try {
            controller.generateMapFromReader(mockReader);
            fail("Expected InvalidLineException");
        } catch (InvalidLineException e) {
            //test passes
        }
    }

    @Test
    public void shouldThrowInvalidFileFormatException() throws IOException, InvalidLineException {
        BufferedReader mockReader = Mockito.mock(BufferedReader.class);
        when(mockReader.readLine())
                .thenReturn("M - 2 - 2");

        try {
            controller.generateMapFromReader(mockReader);
            fail("Expected InvalidFileFormatException");
        } catch (InvalidFileFormatException e) {
            //test passes
        }
    }
}