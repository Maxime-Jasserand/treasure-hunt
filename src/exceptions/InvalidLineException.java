package exceptions;

public class InvalidLineException extends Exception{
    public InvalidLineException(){
        super("Format de ligne invalide");
    }

    public InvalidLineException(String line){
        super("Format invalide à la ligne :" + line);
    }
}
