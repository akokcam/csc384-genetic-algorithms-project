package ScheduleProblem;

class Course implements CourseInterface {

    /**
     * The name of this course
     */
    private String name;

    /**
     * Constructor. Make a course with a name
     * @param name The name of the course
     */
    public Course(String name) {
        this.name = name;
    }

    /**
     * Get the name of this course
     * @return The name of this course
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the time of this course's exam in some schedule
     * @param sched The schedule to consider
     * @return The time that this course's exam occurs at
     */
    public Timing getTiming(Schedule sched) {
        return sched.getTime(this);
    }
}
