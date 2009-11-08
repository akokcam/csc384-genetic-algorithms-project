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

        System.out.println("\nWe have now generated a random schedule.");

        Schedule ss = (Schedule) Schedule.random();
        System.out.println(ss);

        System.out.println(ss.studentSchedulesString());
        System.out.println(ss.roomSchedulesString());

        float fitness = ss.fitness();
        System.out.println("The fitness function of this schedule gives: " + fitness);

/* the code below is to test schedule's clone method.
        Schedule c;
        try{
            c = ss.clone();
            System.out.println("\n****************testing c************\n");
            System.out.println(c);

            System.out.println(c.studentSchedulesString());
            System.out.println(c.roomSchedulesString());

            fitness = c.fitness();
            System.out.println("The fitness function of this schedule gives: " + fitness);
        }
        catch(Exception e){}
      */
    }
}
