package exception;

public class DbExec extends RuntimeException {
    public DbExec(String message) {
        super(message);
    }
}
