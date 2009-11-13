package ScheduleProblem;

import java.util.Iterator;
import java.util.List;

class Student implements Evaluator, HasTimetable {

    /**
     * The name of this student
     */
    private String name;
    /**
     * The courses that the student is enrolled in
     */
    private List<Course> courses;

    /**
     * Constructor. Make a student with a given name and list of courses
     * @param name The student's name
     * @param courses The courses for which this student will have to attend
     * exams
     */
    public Student(String name, List<Course> courses) {
        this.name = name;
        this.courses = courses;
    }

//    /**
//     * Evaluate the fitness of a schedule from the perspective of this student
//     * @param sched The schedule to consider
//     * @return The "fitness" of the schedule from this student's perspective.
//     */
//    public float evaluate(Schedule sched) {
//        float ret = 0.0f;
//        int numTimes = Schedule.getNumTimes();
//
//        Iterator<Timing> it = getTimeTable(sched).iterator();
//        Timing last = it.next();
//        Timing next;
//        while (it.hasNext()) {
//            next = it.next();
//            if (next.equals(last)) {
//                // Penalty for two exams with same time is 10
//                ret -= 20.0f;
//            } else if (next.getDay() == last.getDay()) {
//                // Having 2 exams on the same day is bad. Max penalty 2
//                ret -= 2 + 2.0 * (numTimes - next.getTime() + last.getTime() + 1) / numTimes;
//            } else if (next.getDay() > last.getDay() + 1) {
//                // If exams are 2 days apart, the student likes it. Reward 5.
//                ret += 2.0f;
//            }
//
//            last = next;
//        }
//
//        return ret;
//    }

    /**
     * Evaluate the quality of a student's schedule
     * @param sched The schedule to evaluate
     * @return The "fitness" of this timetable. How much this room likes the
     * schedule.
     */
    public float evaluate(Schedule sched) {
        float penalty = 0;
        int numTimes = Schedule.getNumTimes();

        TimeTable t = getTimeTable(sched);
        if (t.numTimings() == 0) {
            /* It is good when rooms are not ever used. We can use them for
             * something else for the whole exam period.
             */
            return 1;
        }
        Iterator<Timing> it = t.iterator();
        Timing last = it.next();
        Timing next;
        while (it.hasNext()) {
            next = it.next();
            // If two consecutive timings are identical, very very bad
            if (next.equals(last)) {
                penalty += 50;
                // If two consecutive exams happen on the same day
            } else if (next.getDay() == last.getDay()) {
                // If the exams happen in consecutive time slots, very bad
                if (next.getTime() == last.getTime() + 1) {
                    penalty += 5;
                    // Not as bad if it happens later in the same day, but still bad
                } else {
                    penalty += 3;
//                    ret += 2.0f * (numTimes - next.getTime() + last.getTime()) / numTimes;
                }
                // If we have exams on two consecutive days, also slightly bad
            } else if (next.getDay() == last.getDay() + 1) {
                // If we have exams on consecutive days
                penalty += 0.5;
            }

            last = next;
        }


        return 1 / (penalty + 1);
    }

    /**
     * Get the timetable of this student for a particular schedule
     * @param sched The schedule to consider
     * @return The timetable of when this student has to attend exams.
     */
    public TimeTable getTimeTable(Schedule sched) {
        return sched.getTimes(this);
    }

    /**
     * Get the courses that this student is enrolled in
     * @return The list of courses that this student is enrolled in.
     */
    public List<Course> getCourses() {
        return courses;
    }

    /**
     * Get the name of this student
     * @return The name of this student
     */
    public String getName() {
        return name;
    }
}
