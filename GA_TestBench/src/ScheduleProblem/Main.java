package ScheduleProblem;

import ga_testbench.GAParams;
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
        int maxpop = 80;
        int maxgen = 400;
        double copies = 1;
        double mutations = 20;
        double crossovers = 40;
        double randoms = 25;

        GAParams params = new GAParams(BIGINSTANCEFILE);
        params.setPopulationSize(maxpop);
        params.setMaxGenerations(maxgen);
        // Set Generation proportions
        params.setCopies(copies);
        params.setMutations(mutations);
        params.setCrossovers(crossovers);
        params.setRandoms(randoms);

        System.out.println("Running GA with " + maxgen + " generations and " + maxpop + " population...");
        System.out.println("Proportions are " + params.proportionString());

        // Attempt to use GASolver
        GASolver<Schedule> worker = new GASolver<Schedule>(params);
        Schedule best = (Schedule) worker.run();
        int evals = worker.numEvaluations();
//        best.showAllInfo();

        System.out.println("Evaluate function called " + evals + " times.");
        System.out.println("The fitness function of this schedule gives: " + best.fitness());


        // Do Random search
        Schedule randomBest = randomSearch(evals);
        System.out.println("Random search with same number of evaluations gives fitness: " + randomBest.fitness());


        // Do HillClimbing search
        Schedule hillBest = ((Schedule) Schedule.random()).hillClimb(evals);
        System.out.println("Random Hillclimbing gives fitness: " + hillBest.fitness() + " in " + Schedule.getHillEvals() + " fitness evaluations");
//        hillBest.showAllInfo();

        // Do mutatesearch
        Schedule mutateSearchBest = mutateSearch(evals);
        System.out.println("Mutation Search gives fitness: " + mutateSearchBest.fitness() + " in " + evals + " fitness evaluations");


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
     * Search by mutating an individual and testing if it's better than the best
     * seen. Make 1% changes to the current best and check the fitness. If it's
     * better, replace the best.
     * @param evals The number of times to perform evaluations
     * @return
     */
    private static Schedule mutateSearch(int evals) {
        Schedule best = (Schedule) Schedule.random();
        double bestfit = best.fitness();
        while (evals > 0) {
            Schedule tester = (Schedule) best.mutate(0.01);
            double tfit = tester.fitness();
            evals--;
            if (tfit > bestfit) {
                best = tester;
                bestfit = tfit;
            }
        }
        return best;
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
