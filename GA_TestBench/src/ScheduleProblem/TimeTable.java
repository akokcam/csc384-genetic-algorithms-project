package ScheduleProblem;

import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

/**
 *
 * @author Dave
 */
public class TimeTable implements TimeTableInterface, Iterable<Timing> {

    /**
     * This is the Red-Black tree supporting the operations we can perform on
     * timetables.
     */
    private TreeMap<Timing, Integer> times;

    /**
     * This class allows us to iterate over the timings in the timetable in 
     * chronological order.
     */
    class TTIterator implements Iterator<Timing> {

        private Timing current;
        private Integer quantity;

        /**
         * Constructor for the TTIterator
         */
        private TTIterator() {
            current = getEarliest();
            quantity = times.get(current);
        }

        /**
         * Check iof there are any more timings to iterate over.
         * @return Whether any timings are left.
         */
        public boolean hasNext() {
            return current != null;
        }

        /**
         * Get the next chronological timing.
         * @return The next timing.
         */
        public Timing next() {
            if (quantity <= 0) {
                throw new RuntimeException("TTIterator screwed up some.");
            }
            Timing ret = current;
            quantity--;
            if (quantity == 0) {
                current = times.higherKey(current);
                if (current != null) {
                    quantity = times.get(current);
                }
            }
            return ret;
        }

        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    /**
     * Get the number of different times indicated by this timetable.
     * (NOTE: duplicated Timings count as only one timing here.)
     * @return The number of distinct times in this timetable
     */
    public int numTimings() {
        return times.size();
    }

    /**
     * Constructor to make an empty timetable.
     */
    public TimeTable() {
        this.times = new TreeMap<Timing, Integer>();
    }

    /**
     * Constructor to make a timetable based on a list of timings.
     * @param times
     */
    public TimeTable(List<Timing> times) {
        this.times = new TreeMap<Timing, Integer>();
        for (Timing timing : times) {
            addTime(timing);
        }
    }

    /**
     * Get the number of instances of a timing in this timetable
     * @param timing The timing to check for
     * @return The number of times that the timing is in this timetable
     */
    public int numHits(Timing timing) {
        Integer ret = times.get(timing);
        if (ret == null) {
            return 0;
        }
        return ret;
    }

    /**
     * Add another timing to this timetable
     * @param time The timing to add
     */
    public void addTime(Timing time) {
        times.put(time, numHits(time) + 1);
    }

    /**
     * Find the first timing in this timetable
     * @return The first chronological timing
     */
    public Timing getEarliest() {
        return times.firstKey();
    }

    /**
     * Get an iterator to return the timings in this schedule in chronological
     * order.
     * @return An iterator over the timings in this timetable
     */
    public Iterator<Timing> iterator() {
        return new TTIterator();
    }

    /**
     * Get a nice String representation of this timetable, indicating the number
     * of events at each time on every day and time.
     * @return String representation of a timetable.
     */
    @Override
    public String toString() {
        String ret = "";
        for (int time = 0; time < Schedule.getNumTimes(); time++) {
            ret += "Time " + (time + 1) + " :";
            for (int day = 0; day < Schedule.getNumDays(); day++) {
                Timing t = new Timing(day, time);
                int hits = numHits(t);
                ret += (hits == 0) ? "_" : hits;
            }
            ret += "\n";
        }
        return ret;
    }
}
