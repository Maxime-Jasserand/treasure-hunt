package model;

import model.enums.Direction;
import model.enums.Orientation;

public class Adventurer extends Tile {
    private Orientation orientation;
    private int treasureCount;

    public Adventurer(int x, int y, Orientation orientation) {
        super(x, y);
        this.orientation = orientation;
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
