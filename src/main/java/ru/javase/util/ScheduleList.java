package ru.javase.util;

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
    private Map<Date, Set<T>> slots = new TreeMap<>();

    public class Neighbors {
        private T left;
        private T right;
        private T slot;

        public Neighbors(T slot) { this.slot = slot; }
        public T getLeft() { return left; }
        public void setLeft(T left) { this.left = left; }
        public T getRight() { return right; }
        public void setRight(T right) { this.right = right; }
        public T getSlot() { return slot; }
        public void setSlot(T slot) { this.slot = slot; }

        public Boolean isConflict() {
            return left != null || right != null;
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
        Set<T> list = this._get(date);
        Neighbors neighbors = getNeighbors(list, slot);

        if(!ignore_collision) {
            if (neighbors.isConflict()) {
                if(neighbors.getLeft() != null && neighbors.getRight() != null) {
                    throw new ElementConflictException("Element %s conflict with %s and %s", slot, neighbors.getLeft(), neighbors.getRight());
                }
                if(neighbors.getLeft() != null) {
                    throw new ElementConflictException("Element %s conflict with %s", slot, neighbors.getLeft());
                }
            }
        }

        list.add(slot);
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
        Set<T> list = _get(date);

        Optional<T> element = list.stream().filter(line -> line.getTime().getTime() == slot.getTime().getTime()).findFirst();
        if(element.isPresent()) {
            return element.get();
        }

        return null;
    }

    /**
     * Remove element from collection
     *
     * @param date Date
     * @param slot T
     */
    @Override
    public void remove(Date date, T slot) {
        Set<T> list = _get(date);

        T item = get(date, slot);
        list.remove(item);
    }

    /**
     * If contains element
     *
     * @param date Date
     * @param slot T
     * @return Boolean
     */
    @Override
    public boolean contains(Date date, T slot) {
        T item = get(date, slot);
        return item != null;
    }

    /**
     * Empty collection
     */
    @Override
    public void clear() {
        slots.clear();
    }

    /**
     * Get slots
     *
     * @return Map&lt;Date, List&lt;T&gt;&gt;
     */
    @Override
    public Map<Date, Set<T>> getSlots() {
        return slots;
    }

    /**
     * Get Neighbors for element
     *
     * @param date Date
     * @param slot T
     * @return Neighbors
     */
    @Override
    public Neighbors getNeighbors(Date date, T slot) {
        return getNeighbors(_get(date), slot);
    }

    @Override
    public Set<Date> getDates() {
        return slots.keySet();
    }

    /**
     * Get Neighbors for element
     *
     * @param list List&lt;T&gt;
     * @param slot T
     * @return Neighbors
     */
    private Neighbors getNeighbors(Set<T> list, T slot) {
        Neighbors neighbors = new Neighbors(slot);

        for(T item : list) {
            if(item.getTime().getTime() < slot.getTime().getTime() && item.getTime().getTime() + duration * unit.getDuration() * 1000 > slot.getTime().getTime()) {
                neighbors.setLeft(item);
            }
            if(item.getTime().getTime() > slot.getTime().getTime() && item.getTime().getTime() - duration * unit.getDuration() * 1000 < slot.getTime().getTime()) {
                neighbors.setRight(item);
            }
        }

        return neighbors;
    }

    /**
     * Find element. If not exists must be return empty list
     *
     * @param date Date
     * @return List&lt;T&gt;
     */
    private Set<T> _get(Date date) {
        return this.slots.computeIfAbsent(date, k -> new TreeSet<>());
    }
}