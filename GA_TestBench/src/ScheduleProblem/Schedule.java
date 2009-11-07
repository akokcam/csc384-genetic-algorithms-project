package ScheduleProblem;

import ga_testbench.Individual;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the big daddy class. It keeps static lists of students and rooms
 * which it asks to evaluate itself when it needs to be evaluated.
 */
public class Schedule extends ga_testbench.Individual {

    private static List<Student> students;
    private static List<Room> rooms;
    private static List<Course> courses;
    private static int numDays;
    private static int numTimes;
    private static int numCourses;
    private static int numRooms;
    private static int numStudents;
    private static boolean initialized;
    private static Random rand;

    /**
     * This will be called once by the solver. It has to load the instance info
     * and make the static members.
     * @param filename File from which to load instance data
     * @return True if initialization went smoothly, false on error
     */
    public static boolean initialize(String filename) {
        File infile = new File(filename);
        Scanner scanner;
        try {
            scanner = new Scanner(infile);
        } catch (FileNotFoundException ex) {
            return false;
        }

        try {
            numDays = scanner.nextInt();
            numTimes = scanner.nextInt();

            // Collect the courses from the file
            numCourses = scanner.nextInt();
            courses = new ArrayList<Course>(numCourses);
            scanner.nextLine(); // Skip line after getting an int
            for (int i = 0; i < numCourses; i++) {
                String name = scanner.nextLine();
                Course c = new Course(name);
                courses.add(c);
            }

            // Collect the rooms from the file
            numRooms = scanner.nextInt();
            rooms = new ArrayList<Room>(numRooms);
            scanner.nextLine(); // Skip line after getting an int
            for (int i = 0; i < numRooms; i++) {
                String name = scanner.nextLine();
                Room c = new Room(name);
                rooms.add(c);
            }

            // Collect the students from the file
            numStudents = scanner.nextInt();
            students = new ArrayList<Student>(numStudents);
            scanner.nextLine(); // Skip line after getting an int
            for (int i = 0; i < numStudents; i++) {
                String name = scanner.nextLine();

                int cnum = scanner.nextInt();
                scanner.nextLine(); // Skip line after getting an int
                List<Course> studentCourses = new ArrayList<Course>(cnum);
                for (int j = 0; j < cnum; j++) {
                    String courseName = scanner.nextLine();
                    studentCourses.add(getCourse(courseName));
                }
                Student stud = new Student(name, studentCourses);
                students.add(stud);
            }
        } catch (NoSuchElementException e) {
            return false;
        }

        rand = new Random();
        initialized = true;
        scanner.close();

        return true;
    }

    public static int getNumCourses() {
        verifyInitialized();
        return numCourses;
    }

    public static int getNumDays() {
        verifyInitialized();
        return numDays;
    }

    public static int getNumTimes() {
        return numTimes;
    }

    public static int getNumRooms() {
        verifyInitialized();
        return numRooms;
    }

    public static int getNumStudents() {
        verifyInitialized();
        return numStudents;
    }

    /**
     * Dump the instance info to the screen in a nice way
     */
    static void displayInfo() {
        System.out.println("The " + numCourses + " courses in this problem instance are:");
        for (Course course : courses) {
            System.out.println(course.getName());
        }
        System.out.println("The " + numRooms + " rooms in this problem instance are:");
        for (Room room : rooms) {
            System.out.println(room.getName());
        }
        System.out.println("The " + numStudents + " students in this problem instance are:");
        for (Student student : students) {
            List<Course> studCourses = student.getCourses();
            System.out.println(student.getName() + ", who takes these " + studCourses.size() + " classes:");
            for (Course c : studCourses) {
                System.out.println(" " + c.getName());
            }
        }
    }

    /**
     * Generate a random schedule
     * @return A random Schedule
     */
    public static Individual random() {
        if (!initialized) {
            throw new RuntimeException("Schedule not initialized.");
        }

        Timing[] times = new Timing[numCourses];
        int[] timingRooms = new int[numCourses];
        for (int i = 0; i < numCourses; i++) {
            int day = rand.nextInt(numDays);
            int time = rand.nextInt(numTimes);
            int room = rand.nextInt(numRooms);
            Timing t = new Timing(day, time);
            times[i] = t;
            timingRooms[i] = room;
        }

        return new Schedule(times, timingRooms);
    }

    /**
     * Get a particular course from the scheduling problem instance
     * @param courseName The name of the course to get
     * @return The course whose name is courseName
     */
    static Course getCourse(String courseName) {
        /* This is a lazy way of finding the course. We could put them in a
         *  hash table or something instead.
         * It's not so bad because we only do this on loading a file
         *  numStudents * avg(numClasses) times.
         */

        for (Course course : courses) {
            if (course.getName().equals(courseName)) {
                return course;
            }
        }
        throw new RuntimeException(courseName + "is an unrecognized course name.");
    }

    private static void verifyInitialized() {
        if (!initialized) {
            throw new RuntimeException("Schedule not initialized.");
        }
    }
    /****************************
     *                          *
     * Start non-static portion *
     *                          *
     ****************************/
    /**
     * The times that the different courses occur at, indexed by course number.
     */
    private Timing[] times;
    /**
     * The rooms assigned to the different courses, indexed by course number.
     */
    private int[] timingRooms;

    /**
     * Constructor for Schedule
     * @param times The Timings that the courses occur at
     */
    public Schedule(Timing[] times, int[] timingRooms) {
        verifyInitialized();

        if (times.length != numCourses || timingRooms.length != numCourses) {
            throw new RuntimeException("Given arrays have wrong size.");
        }
        this.times = times;
        this.timingRooms = timingRooms;
    }

    // THIS MUST BE DONE
    @Override
    protected Object clone() throws CloneNotSupportedException {
        Timing[] newTimes = times.clone();
        return super.clone();

        /* This is totally incomplete
         */



    }

    /**
     * Get the overall fitness of this schedule
     * @return The fitness of this schedule
     */
    @Override
    public float fitness() {
        float ret = 0;
        for (Student student : students) {
            ret += student.evaluate(this);
        }
        for (Room room : rooms) {
            ret += room.evaluate(this);
        }
        return ret;
    }

    // THIS IS NOT DONE
    @Override
    public Individual mutate(float difference) {
        Schedule ret;
        try {
            int numchanges = (int) difference * numCourses;
            ret = (Schedule) this.clone();
            for (int i = 0; i < numchanges; i++) {
                // DO SOME STUFF
            }
            // WHATEVER

        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Schedule.class.getName()).log(Level.SEVERE, null, ex);
            throw new UnsupportedOperationException("Not supported yet.");
        }
        return ret;
    }

    @Override
    public Individual crossWith(Individual other) {
        if (other == null || getClass() != other.getClass()) {
            throw new RuntimeException("Must cross with a Schedule");
        }
        final Schedule otherSched = (Schedule) other;

        // Have to cast other to type Schedule after verifying that it is indeed
        // that type
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Get the exam time of some course
     * @param course The course to find the exam time for
     * @return The Timing that the course's exam occurs at
     */
    Timing getTime(Course course) {
        int index;
        index = courses.indexOf(course);
        if (index == -1) {
            throw new RuntimeException("Unknown Course found.");
        }
        return this.times[index];
    }

    /**
     * Get the times that some room has an exam in it
     * @param room The room to find the TimeTable for
     * @return The TimeTable for the Timings where the room has an exam
     * scheduled in it
     */
    TimeTable getTimes(Room room) {
        TimeTable ret = new TimeTable();
        int index = rooms.indexOf(room);
        for (int i = 0; i < numCourses; i++) {
            if (timingRooms[i] == index) {
                ret.addTime(this.times[i]);
            }
        }
        return ret;
    }

    /**
     * Get the timetable that a student has
     * @param stud A student
     * @return The timetable for the courses that the student is enrolled in
     */
    TimeTable getTimes(Student stud) {
        // Get each of the timing for the student's courses
        TimeTable ret = new TimeTable();
        for (Course c : stud.getCourses()) {
            ret.addTime(c.getTiming(this));
        }
        return ret;
    }
}
