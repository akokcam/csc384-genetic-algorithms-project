package ScheduleProblem;

/**
 *
 * @author Dave
 */
public class Timing implements Comparable {

    private int day;
    private int time;

    public Timing(int day, int time) {
        this.day = day;
        this.time = time;
    }

    public int getDay() {
        return day;
    }

    public int getTime() {
        return time;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Timing other = (Timing) obj;
        if (this.day != other.day) {
            return false;
        }
        if (this.time != other.time) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + this.day;
        hash = 41 * hash + this.time;
        return hash;
    }

    /**
     * Get a unique integer representing this timing. The first possible slot
     * is 0, and the value of a timing on day x is at lease x * number of times
     * on a day. The timing are ranked in chronological order here.
     * @return The overall location of thie timing.
     */
    public int toInt() {
        return day * Schedule.getNumTimes() + time;
    }

    /**
     * Compare this Timing to another timing
     * @param obj Another Timing
     * @return 1, 0, or -1 if this schedule is, respectively, after, at the
     * same time, or later than obj
     */
    public int compareTo(Object obj) {
        if (obj == null) {
            throw new ClassCastException("Cannot compare to null object.");
        }
        if (getClass() != obj.getClass()) {
            throw new ClassCastException("Compared object must be a Timing.");
        }
        final Timing other = (Timing) obj;

        if (this.day != other.day) {
            return (this.day > other.day) ? 1 : -1;
        }
        if (this.time != other.time) {
            return (this.time > other.time) ? 1 : -1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Day " + day + ", Time: " + time;
    }
}
