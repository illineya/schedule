package ru.javase.util;

/**
 * Collection exception
 *
 * @author ulcigor
 * @version 1.0
 */
public class ElementCollisionException extends RuntimeException {
    ElementCollisionException(String message) {
        super(message);
    }

    ElementCollisionException(String format, Slot ... slots) {
        super(String.format(format, slots));
    }
}
