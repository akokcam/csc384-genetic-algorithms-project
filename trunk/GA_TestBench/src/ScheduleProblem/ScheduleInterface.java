package ScheduleProblem;

/**
 *
 * @author Dave
 */
interface ScheduleInterface {

    public TimeTable getTimes(Course course);

    public TimeTable getTimes(Room room);
}
