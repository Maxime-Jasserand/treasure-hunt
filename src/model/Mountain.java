package model;

public class Mountain extends Tile{
    public Mountain(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean isCrossable(){
        return false;
    }

    @Override
    public String getOutputLine(){
        return "M - " + x + " - " + y;
    }
}
