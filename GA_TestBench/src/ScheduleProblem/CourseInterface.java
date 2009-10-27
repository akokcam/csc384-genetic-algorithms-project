package ScheduleProblem;

/**
 *
 * @author Dave
 */
interface CourseInterface {

    public String getName();

    public Timing getTiming(Schedule sched);
}
