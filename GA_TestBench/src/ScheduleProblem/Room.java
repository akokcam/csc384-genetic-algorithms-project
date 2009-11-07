package ScheduleProblem;

class Room implements Evaluator, HasTimetable {

    String name;

    public Room(String name) {
        this.name = name;
    }

    public float evaluate(Schedule s) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public TimeTable getTimeTable(Schedule sched) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
