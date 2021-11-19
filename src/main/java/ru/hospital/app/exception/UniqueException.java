package ru.hospital.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class UniqueException extends RuntimeException{
    public UniqueException(String message) {
        super(message);
    }
}
