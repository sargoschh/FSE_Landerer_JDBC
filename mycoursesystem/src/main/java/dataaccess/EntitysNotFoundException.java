package dataaccess;

public class EntitysNotFoundException extends RuntimeException{

    public EntitysNotFoundException(String message) {
        super(message);
    }
}
