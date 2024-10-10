package model;

public class Treasure extends Tile {
    private int quantity;

    public Treasure(int x, int y, int quantity) {
        super(x, y);
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void pickupTreasure() {
        quantity--;
    }

    @Override
    public String getOutputLine(){
        return "T - " + x + " - " + y + " - " + quantity;
    }
}
