package model;

public abstract class Tile {
    protected int x;
    protected int y;

    public Tile(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public boolean hasCoordinates(int x, int y){
        return this.x == x && this.y == y;
    }

    public boolean isCrossable(){
        return true;
    }

    public abstract String getOutputLine();
}
