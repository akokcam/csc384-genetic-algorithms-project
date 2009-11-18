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
     * These store the data points for the search methods
     * Do not use directly. Instead, use the get... method to get the end bits
     * added for you.
     */
    private static String randomSearchString = "";
    private static String mutateSearchString = "";

    /**
     * This is the real main program. We can change it quickly to use any of 
     * the various data-gathering-type main methods that we devise.
     * @param args Useless ignored argument parameter
     */
    public static void main(String[] args) {
        testingMain();
    }

    /**
     * This is the main method. It is used for testing right now.
     * @param args
     */
    public static void testingMain() {
        int maxpop = 40;
        int maxgen = 100;
        double copies = 2;
        double mutations = 20;
        double crossovers = 40;
        double randoms = 25;
        int samplePeriod = 100;

        GAParams params = new GAParams(TESTINSTANCE);
        params.setPopulationSize(maxpop);
        params.setMaxGenerations(maxgen);
        // Set Generation proportions
        params.setCopies(copies);
        params.setMutations(mutations);
        params.setCrossovers(crossovers);
        params.setRandoms(randoms);

        System.out.println("Running GA with " + maxgen + " generations and " + maxpop + " population...");
        System.out.println("Proportions are " + params.proportionString());

        /****************************/
        /* Attempt to use GASolver  */
        /****************************/
        GASolver<Schedule> worker = new GASolver<Schedule>(params);
        Schedule best = (Schedule) worker.run();
        int evals = worker.numEvaluations();
//        best.showAllInfo();

        System.out.println("Evaluate function called " + evals + " times.");
        System.out.println("The fitness function of this schedule gives: " + best.fitness());


        /****************************/
        /* Do Random search         */
        /****************************/
        Schedule randomBest = randomSearch(evals, samplePeriod);
        System.out.println("Random search with same number of evaluations gives fitness: " + randomBest.fitness());
//        randomBest.showAllInfo();
//        System.out.println(getRandomSearchString());

        /****************************/
        /* Do HillClimbing search   */
        /****************************/
        Schedule hillBest = ((Schedule) Schedule.random()).hillClimb(evals, samplePeriod);
        System.out.println("Random Hillclimbing gives fitness: " + hillBest.fitness() + " in " + Schedule.getHillEvals() + " fitness evaluations");
//        hillBest.showAllInfo();
//        System.out.println(Schedule.getHillclimbSearchString());

        /****************************/
        /* Do mutatesearch          */
        /****************************/
        Schedule mutateSearchBest = mutateSearch(evals, samplePeriod);
        System.out.println("Mutation Search gives fitness: " + mutateSearchBest.fitness() + " in " + evals + " fitness evaluations");
//        mutateSearchBest.showAllInfo();
//        System.out.println(getMutateSearchString());

    }

    /**
     * Search by mutating an individual and testing if it's better than the best
     * seen. Make 1% changes to the current best and check the fitness. If it's
     * better, replace the best.
     * @param evals The number of times to perform evaluations
     * @return
     */
    private static Schedule mutateSearch(int maxEvals, int samplePeriod) {
        Schedule best = (Schedule) Schedule.random();
        mutateSearchString = "Mutate Search with Evaluation limit " + maxEvals + "\n";
        int evals = 0;
        double bestfit = best.fitness();
        evals++;
        mutateSearchString += evals + ", " + bestfit + "\n";
        while (evals < maxEvals) {
            // Gradually shrink the degree of difference from the last one
            Schedule tester = (Schedule) best.mutate(0.001 + Math.pow(1.0 - (double) evals / maxEvals, 2));
            double tfit = tester.fitness();
            evals++;
            if (tfit > bestfit) {
                best = tester;
                bestfit = tfit;
            }
            if (evals % samplePeriod == 0) {
                mutateSearchString += evals + ", " + bestfit + "\n";
            }
        }
        mutateSearchString += evals + ", " + bestfit + "\n";
        return best;
    }

    /**
     * Get the mutate search string for use in output data files.
     * @return the string, safe to just drop into generated data files
     */
    public static String getMutateSearchString() {
        return mutateSearchString + "END-SERIES";
    }

    /**
     * Search randomly for a timetable with high fitness.
     * @param evals The number of schedule evaluations to perform.
     * @return The best found schedule.
     */
    private static Schedule randomSearch(int maxEvals, int samplePeriod) {
        Schedule best = null;
        randomSearchString = "Random Search with Evaluation limit " + maxEvals + "\n";
        double fitness = Double.NEGATIVE_INFINITY;
        for (int i = 1; i <= maxEvals; i++) {
            Schedule t = (Schedule) Schedule.random();
            double f = t.fitness();
            if (f > fitness) {
                best = t;
                fitness = f;
            }
            if (i % samplePeriod == 0) {
                randomSearchString += i + ", " + fitness + "\n";
            }
        }

        randomSearchString += maxEvals + ", " + fitness + "\n";
        return best;
    }

    public static String getRandomSearchString() {
        return randomSearchString + "END-SERIES";
    }
}
