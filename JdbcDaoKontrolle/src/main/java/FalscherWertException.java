/**
 * Unchecked exception for invalid user inputs.
 * Could be used by the ui to inform the user.
 */
public class FalscherWertException extends RuntimeException {
    public FalscherWertException(String message) {
        super(message);
    }
}
