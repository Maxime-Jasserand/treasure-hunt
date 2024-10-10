package exceptions;

import java.io.IOException;

public class InvalidFileFormatException extends IOException {
    public InvalidFileFormatException(){
        super("Veuillez utiliser un fichier .txt");
    }

    public InvalidFileFormatException(String message){
        super(message);
    }
}
