package org.informatics.winter_olympics.exception;

public class OlympicsNotFoundException extends ObjectNotFoundException {
    public OlympicsNotFoundException(long id) {
        super("Olympics with id " + id + " was not found!", id);
    }
}
