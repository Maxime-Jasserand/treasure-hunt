package model;

import java.util.List;
import java.util.Optional;

public class Map {
    private final int x;
    private final int y;
    private List<Tile> tiles;

    public Map(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void addTile(Tile tile){
        tiles.add(tile);
    }

    public Optional<Tile> getTile(int x, int y){
        return tiles.stream().filter(t -> t.hasCoordinates(x ,y)).findFirst();
    }

    public void removeTreasureTile(Treasure treasure){
        tiles.remove(treasure);
    }
}
