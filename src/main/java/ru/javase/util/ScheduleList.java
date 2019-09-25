package ru.javase.util;

import java.sql.Time;
import java.util.*;

/**
 * Implements collection
 *
 * @author ulcigor
 * @version 1.0
 */
public class ScheduleList implements Schedule {
    private Integer duration;
    private Unit unit;
    Map<Date, List<Slot>> slots = new HashMap<>();

    private class Neighbors {
        private Slot left;
        private Slot right;
        private Slot slot;
        private int index = 0;

        public Neighbors(Slot slot) { this.slot = slot; }
        public Slot getLeft() { return left; }
        public void setLeft(Slot left) { this.left = left; }
        public Slot getRight() { return right; }
        public void setRight(Slot right) { this.right = right; }
        public int getIndex() { return index; }
        public void setIndex(int index) { this.index = index; }
        public Slot getSlot() { return slot; }
        public void setSlot(Slot slot) { this.slot = slot; }

        public Slot getCollision() {
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
     * Must be binary search
     *
     * @param date Date
     * @param slot Slot
     */
    @Override
    public void add(Date date, Slot slot) {
        this.add(date, slot, false);
    }

    @Override
    public void add(Date date, Slot slot, Boolean ignore_collision) {
        List<Slot> list = this._get(date);
        Neighbors neighbors = getNeighbors(list, slot);

        if(!ignore_collision) {
            Slot collision_item = neighbors.getCollision();
            if (collision_item != null) {
                throw new ElementCollisionException("Element %s cross with %s", slot, collision_item);
            }
        }

        list.add(neighbors.getIndex(), slot);
    }

    @Override
    public Slot get(Date date, Slot slot) {
        List<Slot> list = _get(date);

        int index = indexOf(date, slot);
        return list.get(index);
    }

    @Override
    public Slot get(Date date, Time time) {
        return get(date, new SlotImpl(time));
    }

    @Override
    public void remove(Date date, Slot slot) {
        List<Slot> list = _get(date);

        int index = indexOf(date, slot);
        list.remove(index);
    }

    @Override
    public void remove(Date date, Time time) {
        remove(date, new SlotImpl(time));
    }

    @Override
    public void set(Date date, Integer index, Slot slot) {
        List<Slot> list = _get(date);

        Slot item = list.get(index);
        list.remove(item);

        try {
            add(date, slot);
        } catch (ElementCollisionException e) {
            list.add(item);
            throw new ElementCollisionException(e.getMessage());
        }
    }

    @Override
    public boolean contains(Date date, Slot slot) {
        int index = indexOf(date, slot);
        return index != -1;
    }

    @Override
    public boolean contains(Date date, Time time) {
        return contains(date, new SlotImpl(time));
    }

    @Override
    public int indexOf(Date date, Slot slot) {
        List<Slot> list = _get(date);

        for(int i=0; i<list.size(); i++) {
            if(list.get(i).getTime().getTime() == slot.getTime().getTime()) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public int indexOf(Date date, Time time) {
        return indexOf(date, new SlotImpl(time));
    }

    @Override
    public void clear() {
        slots.clear();
    }

    private List<Slot> _get(Date date) {
        return this.slots.computeIfAbsent(date, k -> new ArrayList<>());
    }

    private Neighbors getNeighbors(List<Slot> list, Slot slot) {
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
