package ScheduleProblem;

import java.util.List;

class Student implements Evaluator, HasTimetable {

    private String name;
    private List<Course> courses;

    public Student(String name, List<Course> courses) {
        this.name = name;
        this.courses = courses;
    }

    public float evaluate(Schedule sched) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public TimeTable getTimeTable(Schedule sched) {
        return sched.getTimes(this);
    }

    public List<Course> getCourses() {
        return courses;
    }
}
