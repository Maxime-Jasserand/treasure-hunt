package model;

import model.enums.Orientation;

import java.util.HashMap;

public class Adventurer extends Tile {
    private final String name;
    private Orientation orientation;
    private String moveList;
    private int treasureCount;

    public Adventurer(String name, int x, int y, String orientation, String moveList) {
        super(x, y);
        this.name = name;
        this.orientation = Orientation.valueOf(orientation);
        this.moveList = moveList;
        this.treasureCount = 0;
    }

    /*-------------------------   MOVEMENT METHODS  -------------------------*/
    public HashMap<String, Integer> getNextTileCoordinates(){
        int nextX = x;
        int nextY = y;
        switch (orientation) {
            case N -> nextY--;
            case E -> nextX++;
            case S -> nextY++;
            case W -> nextX--;
        }
        HashMap<String, Integer> coordinates = new HashMap<>();
        coordinates.put("x", nextX);
        coordinates.put("y", nextY);
        return coordinates;
    }

    public void moveForward() {
        switch (orientation) {
            case N -> y--;
            case E -> x++;
            case S -> y++;
            case W -> x--;
        }
    }

    public void turnLeft() {
        switch (orientation) {
            case N -> orientation = Orientation.W;
            case E -> orientation = Orientation.N;
            case S -> orientation = Orientation.E;
            case W -> orientation = Orientation.S;
        }
    }

    public void turnRight() {
        switch (orientation) {
            case N -> orientation = Orientation.E;
            case E -> orientation = Orientation.S;
            case S -> orientation = Orientation.W;
            case W -> orientation = Orientation.N;
        }
    }

    /*-------------------------   UTILS  -------------------------*/
    public String getName(){
        return name;
    }

    public Orientation getOrientation(){
        return orientation;
    }

    public String getMoveList() {
        return moveList;
    }

    public char getNextMove(){
        return moveList.charAt(0);
    }

    public void removeNextMove(){
        moveList = moveList.substring(1);
    }

    public int getTreasureCount(){
        return treasureCount;
    }

    public void increaseTreasureCount(){
        treasureCount++;
    }

    @Override
    public boolean isCrossable() {
        return false;
    }

    @Override
    public String getOutputLine() {
        return "A - " + name + " - " + x + " - " + y + " - " + orientation.toString() + " - " + treasureCount;
    }
}
