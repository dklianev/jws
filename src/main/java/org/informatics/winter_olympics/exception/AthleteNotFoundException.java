package org.informatics.winter_olympics.exception;

public class AthleteNotFoundException extends ObjectNotFoundException {
    public AthleteNotFoundException(long id) {
        super("Athlete with id " + id + " was not found!", id);
    }
}
