package ScheduleProblem;

import java.util.List;

/**
 *
 * @author Dave
 */
public class TimeTable implements TimeTableInterface {

    private List<Timing> times;

    public void addTime(Timing time) {
        times.add(time);
    }

    public int countDuplicates() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
