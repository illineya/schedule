package ru.javase.util;

/**
 * Collection exception
 *
 * @author ulcigor
 * @version 1.0
 */
public class ElementConflictException extends RuntimeException {
    ElementConflictException(String message) {
        super(message);
    }

    ElementConflictException(String format, Slot ... slots) {
        super(String.format(format, slots));
    }
}
