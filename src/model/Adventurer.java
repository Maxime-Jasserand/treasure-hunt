package model;

import model.enums.Direction;
import model.enums.Orientation;

public class Adventurer extends Tile {
    private String name;
    private Orientation orientation;
    private String moveList;
    private int treasureCount;

    public Adventurer(String name, int x, int y, String orientation, String moveList) {
        super(x, y);
        this.orientation = Orientation.valueOf(orientation);
        this.treasureCount = 0;
    }

    public void pickupTreasure(int quantity) {
        treasureCount += quantity;
    }

    public void moveForward() {
        switch (orientation) {
            case NORTH -> y--;
            case EAST -> x++;
            case SOUTH -> y++;
            case WEST -> x--;
        }
    }

    public void turn(Direction direction) {
        switch (direction) {
            case LEFT -> turnLeft();
            case RIGHT -> turnRight();
        }
    }

    private void turnLeft() {
        switch (orientation) {
            case NORTH -> orientation = Orientation.WEST;
            case EAST -> orientation = Orientation.NORTH;
            case SOUTH -> orientation = Orientation.EAST;
            case WEST -> orientation = Orientation.SOUTH;
        }
    }

    private void turnRight() {
        switch (orientation) {
            case NORTH -> orientation = Orientation.EAST;
            case EAST -> orientation = Orientation.SOUTH;
            case SOUTH -> orientation = Orientation.WEST;
            case WEST -> orientation = Orientation.NORTH;
        }
    }


    @Override
    public boolean isCrossable() {
        return false;
    }
}
