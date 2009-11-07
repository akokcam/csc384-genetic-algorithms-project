package ScheduleProblem;

class Course implements CourseInterface {

    String name;

    public Course(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public Timing getTiming(Schedule sched) {
        return sched.getTime(this);
    }
}
