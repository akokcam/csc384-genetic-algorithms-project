package ScheduleProblem;

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
     * @param s The schedule to evaluate
     * @return The "fitness" of this timetable. How much this room likes the
     * schedule.
     */
    public float evaluate(Schedule s) {
        /* This doesn't need to particularly efficient, it just needs to work
         */
        throw new UnsupportedOperationException("Not supported yet.");
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
