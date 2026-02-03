package DatabaseExceptions;

public class InvalidStatementException extends Exception {
    private String message;
    public InvalidStatementException(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
