package org.informatics.winter_olympics.exception;

public class CompetitionNotFoundException extends ObjectNotFoundException {
    public CompetitionNotFoundException(long id) {
        super("Competition with id " + id + " was not found!", id);
    }
}
