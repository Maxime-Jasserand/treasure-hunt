package model;

public class Treasure extends Tile {
    private final int quantity;

    public Treasure(int x, int y, int quantity) {
        super(x, y);
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}
