package org.informatics.winter_olympics.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ToString
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ObjectNotFoundException extends RuntimeException {
    private final long id;

    public ObjectNotFoundException(String message, long id) {
        super(message);
        this.id = id;
    }
}
