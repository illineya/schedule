package ru.javase.util;

import java.sql.Time;
import java.util.*;

/**
 * Implements collection
 *
 * @author ulcigor
 * @version 1.0
 */
public class ScheduleList<T extends Slot> implements Schedule<T> {
    private Integer duration;
    private Unit unit;
    Map<Date, List<T>> slots = new HashMap<>();

    private class Neighbors {
        private T left;
        private T right;
        private T slot;
        private int index = 0;

        public Neighbors(T slot) { this.slot = slot; }
        public T getLeft() { return left; }
        public void setLeft(T left) { this.left = left; }
        public T getRight() { return right; }
        public void setRight(T right) { this.right = right; }
        public int getIndex() { return index; }
        public void setIndex(int index) { this.index = index; }
        public T getSlot() { return slot; }
        public void setSlot(T slot) { this.slot = slot; }

        public T getCollision() {
            if(left != null && left.getTime().getTime() + (duration * unit.getDuration() * 1000) > slot.getTime().getTime()) {
                return left;
            }

            if(right != null && right.getTime().getTime() - (duration * unit.getDuration() * 1000) < slot.getTime().getTime()) {
                return right;
            }

            return null;
        }
    }

    /**
     * Create new instance
     *
     * @param duration Float
     * @param unit Unit
     * @throws NullPointerException if the specified duration is null
     * @throws NumberFormatException if the specified unit is null
     */
    public ScheduleList(Integer duration, Unit unit) {
        if(duration == null) throw new NullPointerException("Duration must not be null");
        if(unit == null) throw  new NullPointerException("Unit must not be null");
        if(duration <= 0) throw new NumberFormatException("duration must be greater than 0");

        this.duration = duration;
        this.unit = unit;
    }

    /**
     * Add new element in collection
     *
     * @param date Date
     * @param slot T
     */
    @Override
    public void add(Date date, T slot) {
        this.add(date, slot, false);
    }

    /**
     * Add new element in collection.
     * May ignore collision
     *
     * @param date Date
     * @param slot T
     * @param ignore_collision Boolean
     */
    @Override
    public void add(Date date, T slot, Boolean ignore_collision) {
        List<T> list = this._get(date);
        Neighbors neighbors = getNeighbors(list, slot);

        if(!ignore_collision) {
            T collision_item = neighbors.getCollision();
            if (collision_item != null) {
                throw new ElementCollisionException("Element %s cross with %s", slot, collision_item);
            }
        }

        list.add(neighbors.getIndex(), slot);
    }

    /**
     * Get element by date and time slot
     *
     * @param date Date
     * @param slot T
     * @return T
     */
    @Override
    public T get(Date date, T slot) {
        List<T> list = _get(date);

        int index = indexOf(date, slot);
        return list.get(index);
    }

    @Override
    public void remove(Date date, T slot) {
        List<T> list = _get(date);

        int index = indexOf(date, slot);
        list.remove(index);
    }

    @Override
    public void set(Date date, Integer index, T slot) {
        List<T> list = _get(date);

        T item = list.get(index);
        list.remove(item);

        try {
            add(date, slot);
        } catch (ElementCollisionException e) {
            list.add(item);
            throw new ElementCollisionException(e.getMessage());
        }
    }

    @Override
    public boolean contains(Date date, T slot) {
        int index = indexOf(date, slot);
        return index != -1;
    }

    @Override
    public int indexOf(Date date, T slot) {
        List<T> list = _get(date);

        for(int i=0; i<list.size(); i++) {
            if(list.get(i).getTime().getTime() == slot.getTime().getTime()) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public void clear() {
        slots.clear();
    }

    private List<T> _get(Date date) {
        return this.slots.computeIfAbsent(date, k -> new ArrayList<>());
    }

    private Neighbors getNeighbors(List<T> list, T slot) {
        Neighbors neighbors = new Neighbors(slot);

        if(list.size() > 0) {
            for(int i=0; i<list.size(); i++) {
                if(list.get(i).getTime().getTime() >= slot.getTime().getTime()) {
                    neighbors.setRight(list.get(i));
                    neighbors.setIndex(i);
                    if(i > 0) { neighbors.setLeft(list.get(i - 1)); }
                    break;
                } else {
                    neighbors.setLeft(list.get(i));
                    neighbors.setIndex(i + 1);
                }
            }
        }

        return neighbors;
    }
}
