package ScheduleProblem;

import java.util.Iterator;

class Room implements Evaluator, HasTimetable {

    /**
     * The name of the room
     */
    private String name;

    /**
     * Constructor. Make a room with a name
     * @param name
     */
    public Room(String name) {
        this.name = name;
    }

    /**
     * Get the name of a course
     * @return The name of this course
     */
    public String getName() {
        return name;
    }

    /**
     * Evaluate the quality of a course's timetable
     * @param sched The schedule to evaluate
     * @return The "fitness" of this timetable. How much this room likes the
     * schedule.
     */
    public float evaluate(Schedule sched) {
        float ret = 0.0f;
        int numTimes = Schedule.getNumTimes();

        TimeTable t = getTimeTable(sched);
        if (t.numTimings() == 0) {
            /* It is good when rooms are not ever used. We can use them for
             * something else for the whole exam period.
             */
            return -2;
        }
        Iterator<Timing> it = t.iterator();
        Timing last = it.next();
        Timing next;
        while (it.hasNext()) {
            next = it.next();
            if (next.equals(last)) {
                // If two times the room is being used are identical, we hate it
                ret -= 20;
            } else if (next.getDay() == last.getDay()) {
                /* If the room is used more than once on the same day, we like
                 * it. We like it when the next time is as close as possible to
                 * the last time, so the envigelators don't have to find some
                 * way to kill the afternoon.
                 */
                if (next.getTime() == last.getTime() + 1) {
                    /* We don't like it to be too close to the last exam. We
                     * need some time to handle the crows after one ends and
                     * before one starts.
                     */
                    ret -= 1;
                } else {
                    ret += 2;
                    ret += 2.0f * (numTimes - next.getTime() + last.getTime()) / numTimes;
                }
            }
            last = next;
        }


        return ret;
    }

    /**
     * Get the timetable that this room has in a certain schedule.
     * @param sched The schedule to check.
     * @return The timetable of this room's uses
     */
    public TimeTable getTimeTable(Schedule sched) {
        return sched.getTimes(this);
    }
}
