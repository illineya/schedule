package ru.javase.util;

/**
 * Collection exception
 *
 * @author ulcigor
 * @version 1.0
 */
public class ElementCollisionException extends RuntimeException {
    private SlotImpl slot;

    ElementCollisionException(String message) {
        super(message);
    }

    ElementCollisionException(String format, Slot slot) {
        super(String.format(format, slot.getTime()));
    }

    ElementCollisionException(String format, Slot slot1, Slot slot2) {
        super(String.format(format, slot1.getTime(), slot2.getTime()));
    }
}
