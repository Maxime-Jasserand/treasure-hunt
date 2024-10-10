package model;

import model.enums.Orientation;

import java.util.Optional;

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

    /**
     * A move will either be :
     * - Turn, which is simple
     * - Move forward which require to check if it's possible before moving and eventually picking up a treasure
     *
     * @param map is used to check where the adventurer will land
     */
    public void executeNextMove(Map map) {
        char nextMove = moveList.charAt(0);
        switch (nextMove) {
            case 'A' -> {
                // we check the next tile before moving
                if (isNextTileCrossable(map)) {
                    moveForward();
                    pickupTreasureIfAny(map);
                }
            }
            case 'G' -> turnLeft();
            case 'D' -> turnRight();
        }

        //Remove the move that has been executed from the list
        moveList = moveList.substring(1);
    }

    public void pickupTreasureIfAny(Map map) {
        //Get an eventual treasure on the tile
        Optional<Treasure> treasure = map.getTreasure(x, y);
        if (treasure.isEmpty()) {
            return;
        }
        //Pick it up if any
        map.pickupTreasure(treasure.get());
        //Increase adventurer treasure count
        treasureCount++;
    }

    //The next tile is crossable if it's empty or crossable
    public boolean isNextTileCrossable(Map map) {
        int nextX = x;
        int nextY = y;
        switch (orientation) {
            case N -> nextY--;
            case E -> nextX++;
            case S -> nextY++;
            case W -> nextX--;
        }
        Optional<Tile> nextTile = map.getTile(nextX, nextY);
        return nextTile.isEmpty() || nextTile.get().isCrossable();
    }

    public void moveForward() {
        switch (orientation) {
            case N -> y--;
            case E -> x++;
            case S -> y++;
            case W -> x--;
        }
    }

    private void turnLeft() {
        switch (orientation) {
            case N -> orientation = Orientation.W;
            case E -> orientation = Orientation.N;
            case S -> orientation = Orientation.E;
            case W -> orientation = Orientation.S;
        }
    }

    private void turnRight() {
        switch (orientation) {
            case N -> orientation = Orientation.E;
            case E -> orientation = Orientation.S;
            case S -> orientation = Orientation.W;
            case W -> orientation = Orientation.N;
        }
    }

    /*-------------------------   UTILS  -------------------------*/
    @Override
    public boolean isCrossable() {
        return false;
    }

    @Override
    public String getOutputLine() {
        return "A - " + name + " - " + x + " - " + y + " - " + orientation.toString() + " - " + treasureCount;
    }

    public String getMoveList() {
        return moveList;
    }
}
