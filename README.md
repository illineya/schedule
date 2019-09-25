# schedule
Library to support schedules on java

* **Using**
```
Schedule schedule = new ScheduleList(1, Schedule.Unit.HOUR);
schedule.add(date1, slot1);
schedule.add(date2, slot2);

schedule.get(date1, slot1);
schedule.indexOf(date1, slot1);
schedule.remove(date1, slot1);
schedule.set(date1, index, slot1);
```
