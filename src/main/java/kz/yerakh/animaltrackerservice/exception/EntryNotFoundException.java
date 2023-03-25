package kz.yerakh.animaltrackerservice.exception;

public class EntryNotFoundException extends RuntimeException {

    public EntryNotFoundException() {
        super("Entry not found");
    }
}
