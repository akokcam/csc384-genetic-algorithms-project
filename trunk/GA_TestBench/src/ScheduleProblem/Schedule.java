package ScheduleProblem;

import ga_testbench.Individual;
import java.util.List;

/**
 * This is the big daddy class. It keeps static lists of students and rooms
 * which it asks to evaluate itself when it needs to be evaluated.
 */
public class Schedule extends ga_testbench.Individual implements ScheduleInterface {

    private static List<Student> students;
    private static List<Room> rooms;
    private List<Course> courses;

    @Override
    public Individual random() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float fitness() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mutate(float difference) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Individual crossWith(Individual other) {
        // Have to cast other to type Schedule after verifying that it is indeed
        // that type
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * This will be called once by the solver. It has to load the instance info
     * and make the static members.
     * @param filename File from which to load instance data
     */
    public static void initialize(String filename) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public TimeTable getTimes(Course course) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public TimeTable getTimes(Room room) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
