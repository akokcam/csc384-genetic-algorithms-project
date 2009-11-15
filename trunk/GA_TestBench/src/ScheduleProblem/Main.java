package ScheduleProblem;

import ga_testbench.GASolver;

public class Main {

    // Sadly, we need backslashes for windows machines
    public static final String TESTINSTANCE = "Instance Input Files\\tickle.txt";
    public static final String SMALLINSTANCEFILE = "Instance Input Files\\smallish instance.txt";
    public static final String BIGINSTANCEFILE = "Instance Input Files\\biggerinstance.txt";
    public static final String HUGEINSTANCE = "Instance Input Files\\super_huge_instance.txt";

    /**
     * This is the main method. It is used for testing right now.
     * @param args
     */
    public static void main(String[] args) {
        // Attempt to use GASolver
        GASolver<Schedule> worker = new GASolver<Schedule>(SMALLINSTANCEFILE);
        int maxpop = 80;
        int maxgen = 300;
        worker.setMaxGenerations(maxgen);
        worker.setPopulationSize(maxpop);
        worker.setNextGenerationProportions(2, 10, 23, 15);
        System.out.println("Running GA with " + maxgen + " generations and " + maxpop + " population...");
        Schedule best = (Schedule) worker.run();
        int evals = worker.numEvaluations();
//        best.showAllInfo();

        System.out.println("Evaluate function called " + evals + " times.");

        System.out.println("The fitness function of this schedule gives: " + best.fitness());

        Schedule randomBest = randomSearch(evals);
        System.out.println("Random search with same number of evaluations gives fitness: " + randomBest.fitness());

        Schedule hillBest = ((Schedule) Schedule.random()).hillClimb(99999999);
        System.out.println("Random Hillclimbing gives fitness: " + hillBest.fitness() + " in " + Schedule.getHillEvals() + " fitness evaluations");
//        hillBest.showAllInfo();

        /*
        // This is for checking a problem instance, generating a few solutions
        // and seeing a decent one
        double fitness = ss.fitness();
        System.out.println("The fitness function of this schedule gives: " + fitness);

        int trials = 20000;
        double tot = 0;
        Schedule best = null;
        double highestf = 0;
        for (int i = 0; i < trials; i++) {
        Schedule r = (Schedule) Schedule.random();
        double f = r.fitness();
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

        /* the code below is to test schedule's mutation method.

        Schedule c = (Schedule) ss.mutate(0.5f);
        System.out.println("\n****************testing c************\n");
        System.out.println(c);

        System.out.println(c.studentSchedulesString());
        System.out.println(c.roomSchedulesString());

        fitness = c.fitness();
        System.out.println("The fitness function of this schedule gives: " + fitness);
         */
    }

    /**
     * Search randomly for a timetable with high fitness.
     * @param evals The number of schedule evaluations to perform.
     * @return The best found schedule.
     */
    private static Schedule randomSearch(int evals) {
        Schedule best = null;
        double fitness = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < evals; i++) {
            Schedule t = (Schedule) Schedule.random();
            double f = t.fitness();
            if (f > fitness) {
                best = t;
                fitness = f;
            }
        }
        return best;
    }
}
