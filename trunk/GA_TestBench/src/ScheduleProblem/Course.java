package ScheduleProblem;

class Course implements CourseInterface {

    String name;

    public String getName() {
        return this.name;
    }

    public Timing getTiming(Schedule sched) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
