/**
 * Unchecked exception thrown in case of database / sqlexceptions.
 * Could be used by cli to inform the user.
 */
public class DatenbankException extends RuntimeException {
    public DatenbankException(String message) {
        super(message);
    }
}
