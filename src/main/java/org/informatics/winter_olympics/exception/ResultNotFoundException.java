package org.informatics.winter_olympics.exception;

public class ResultNotFoundException extends ObjectNotFoundException {
    public ResultNotFoundException(long id) {
        super("Result with id " + id + " was not found!", id);
    }
}
