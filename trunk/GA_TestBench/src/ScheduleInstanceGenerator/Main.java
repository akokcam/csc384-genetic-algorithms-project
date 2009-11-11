package ScheduleInstanceGenerator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 * This is used to geenrate Scheduling problem instances with desired
 * properties. Just change the static final ints at the top.
 * @author dave
 */
public class Main {

    public static final String FILENAME = "Instance Input Files/biggerinstance.txt";
    public static final int NUMDAYS = 10;
    public static final int NUMTIMES = 8;
    public static final int NUMCOURSES = 200;
    public static final int NUMSTUDENTS = 300;
    public static final int NUMROOMS = 10;
    public static final int MAXCOURSESPERSTUDENT = 8;

    public static void main(String[] args) {
        Random rand = new Random();
        try {

            FileWriter fstream = new FileWriter(FILENAME);
            BufferedWriter out = new BufferedWriter(fstream);
//            writeStream out = System.out;
            out.write(NUMDAYS + " " + NUMTIMES + "\n");

            out.write("\n" + NUMCOURSES + "\n");
            for (int c = 1; c <= NUMCOURSES; c++) {
                out.write("Course " + c + "\n");
            }

            out.write("\n" + NUMROOMS + "\n");
            for (int r = 1; r <= NUMROOMS; r++) {
                out.write("Room " + r + "\n");
            }

            out.write("\n" + NUMSTUDENTS + "\n");
            for (int i = 1; i <= NUMSTUDENTS; i++) {
                out.write("Student " + i + "\n");
                // Tend toward more courses with a sqrt
                int numCourses = (int) (Math.sqrt(rand.nextFloat()) * MAXCOURSESPERSTUDENT);
                if (numCourses == 0) {
                    numCourses = 1;
                }
                out.write(numCourses + "\n");
                Set cs = new TreeSet();
                for (int k = 0; k < numCourses; k++) {
                    int coursenum = (int) (1 + rand.nextFloat() * NUMCOURSES);
                    if (cs.contains(coursenum)) {
                        k--;
                    } else {
                        out.write("Course " + coursenum + "\n");
                        cs.add(coursenum);
                    }
                }
            }


            out.close();
        } catch (IOException e) {
        }
    }
}
