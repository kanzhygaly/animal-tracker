package kz.yerakh.animaltrackerservice.exception;

public class EntryAlreadyExistException extends RuntimeException {

    public EntryAlreadyExistException(String message) {
        super(message);
    }
}
