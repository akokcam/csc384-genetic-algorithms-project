package ScheduleProblem;

import ga_testbench.Individual;

public class Main {

    /**
     * This is the main method. It is used for testing right now.
     * @param args
     */
    public static void main(String[] args) {

        boolean loadedOK = Schedule.initialize("Instance Input Files\\testinitfile.txt");

        if (loadedOK) {
            Schedule.displayInfo();
        }

        Schedule ss = (Schedule) Schedule.random();
        System.out.println(ss);

        System.out.println(ss.studentSchedulesString());

    }
}
