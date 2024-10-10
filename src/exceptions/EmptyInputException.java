package exceptions;

import java.io.IOException;

public class EmptyInputException extends IOException {
    public EmptyInputException(){
        super("Veuillez choisir un fichier d'entrée.");
    }
}
