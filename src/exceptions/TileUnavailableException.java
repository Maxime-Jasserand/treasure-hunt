package exceptions;

public class TileUnavailableException extends Exception{
    public TileUnavailableException(){
        super("Tile unavailable");
    }
}
