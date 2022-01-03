package scope;

public class AlreadyDeclaredException extends RuntimeException{
    public AlreadyDeclaredException(String msg){
        super(msg);
    }
}
