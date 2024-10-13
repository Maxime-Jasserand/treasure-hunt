package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Map {
    private final int x;
    private final int y;
    private final List<Tile> tiles;

    public Map(int x, int y) {
        this.x = x;
        this.y = y;
        this.tiles = new ArrayList<>();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public List<Tile> getAllTiles() {
        return tiles;
    }

    public List<Adventurer> getAllAdventurers() {
        return tiles.stream().filter(t -> t instanceof Adventurer)
                .map(t -> (Adventurer) t)
                .collect(Collectors.toList());
    }

    public void addTile(Tile tile) {
        tiles.add(tile);
    }

    public Optional<Tile> getTile(int x, int y) {
        return tiles.stream().filter(t -> t.hasCoordinates(x, y)).findFirst();
    }

    public Optional<Treasure> getTreasure(int x, int y) {
        return tiles.stream().filter(t -> t.hasCoordinates(x, y) && t instanceof Treasure)
                .map(t -> (Treasure) t).findFirst();
    }

    public void pickupTreasure(Treasure treasure) {
        treasure.pickupTreasure();
        if (treasure.getQuantity() == 0) {
            tiles.remove(treasure);
        }
    }

    public String getOutputLine() {
        return "M - " + x + " - " + y;
    }

    public boolean isInsideBoundaries(int x, int y) {
        return x <= this.x && y <= this.y && x >= 0 && y >= 0;
    }
}