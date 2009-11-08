package ScheduleProblem;

public class Main {

    /**
     * This is the main method. It is used for testing right now.
     * @param args
     */
    public static void main(String[] args) {

        boolean loadedOK = Schedule.initialize("Instance Input Files\\tickle.txt");

        if (loadedOK) {
            //Schedule.displayInfo();
        } else {
            throw new RuntimeException("Schedule failed to load.");
        }

        System.out.println("\nWe have now generated a random schedule.");

        Schedule ss = (Schedule) Schedule.random();
//        System.out.println(ss);
//        System.out.println(ss.studentSchedulesString());
//        System.out.println(ss.roomSchedulesString());


        /*
        // This is for checking a problem instance, generating a few solutions
        // and seeing a decent one
        float fitness = ss.fitness();
        System.out.println("The fitness function of this schedule gives: " + fitness);

        int trials = 20000;
        float tot = 0;
        Schedule best = null;
        float highestf = 0;
        for (int i = 0; i < trials; i++) {
        Schedule r = (Schedule) Schedule.random();
        float f = r.fitness();
        if (f > highestf) {
        highestf = f;
        best = r;
        }
        tot += f;
        }
        System.out.println("Average fitness: " + (tot / trials));

        System.out.println(best);
        System.out.println(best.studentSchedulesString());
        System.out.println(best.roomSchedulesString());
        System.out.println("The fitness function of this schedule gives: " + best.fitness());
         */

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
