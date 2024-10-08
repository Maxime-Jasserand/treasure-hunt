package model;

public class Mountain extends Tile{
    public Mountain(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean isCrossable(){
        return false;
    }
}
