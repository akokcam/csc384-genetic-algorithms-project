package ScheduleProblem;

import ga_testbench.GAParams;
import ga_testbench.GASolver;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

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


    private static String GABestStats = "";
    private static String GAMeanStats = "";
    private static String randomSearchStats = "";
    private static String hillClimbingStats = "";
    private static String mutateSearchStats = "";

    /**
     * This is the real main program. We can change it quickly to use any of 
     * the various data-gathering-type main methods that we devise.
     * @param args Useless ignored argument parameter
     */
    public static void main(String[] args) {
//        davesTestingMain();
        //getGAParameterStats();

        int maxpop = 40;
        int maxgen = 100;
        double copies = 2;
        double mutations = 20;
        double crossovers = 40;
        double randoms = 25;
        getAllStats(maxpop, maxgen, copies, mutations, crossovers, randoms, SMALLINSTANCEFILE);
    }

    /**
     * This should dump some stats to a file so we can evaluate the quality of
     * various GA parameter settings.
     */
    private static void getGAParameterStats() {
        GAParams params = new GAParams(SMALLINSTANCEFILE);
        int maxpop = 81;
        int maxgen = 100;
        double copies = 1;
        params.setPopulationSize(maxpop);
        params.setMaxGenerations(maxgen);
        params.setCopies(copies);
        final String outFileName = "Data Files\\GA Parameters Data with population " + maxpop + " and " + maxgen + " generations.txt";

        try {
            FileWriter fstream = new FileWriter(outFileName);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("GA Parameters in 3 Dimensions, testing the quality of different settings of the crossover and mutation proportions.\n");
            // Number of dimensions in data
            out.write("3\n");
            out.write("Proportion of Crossovers\n");
            out.write("Proportion of Mutations\n");
            out.write("Attained Fitness\n");
            // Number of series
            out.write("1\n");
            out.write("GA with parameters" + "\n");

            for (double crossovers = 0; crossovers < maxpop; crossovers += 1) {
                for (double mutations = 0; mutations + crossovers < maxpop; mutations += 1) {
                    double randoms = maxpop - mutations - crossovers - copies;

                    // Set Generation proportions
                    params.setMutations(mutations);
                    params.setCrossovers(crossovers);
                    params.setRandoms(randoms);

                    // Perform the test
                    GASolver<Schedule> worker = new GASolver<Schedule>(params);
                    Schedule best = (Schedule) worker.run();

                    String line = crossovers + ", " + mutations + ", " + best.fitness();

                    out.write(line);
                    out.write("\n");
                    System.out.println(line);
                }
            }

            out.write("END-SERIES\n");
            out.close();
        } catch (IOException ex) {
            System.err.println("Error opening file " + outFileName + " for write");
        }
    }

    public static void getAllStats(int maxpop, int maxgen, double copies, double mutations, double crossovers, double randoms, String instanceFile) {
        GAParams params = new GAParams(instanceFile, maxpop, maxgen, copies, mutations, crossovers, randoms, true);
        int samplePeriod = params.numEvalsPerGeneration();

        // Run the 4 different searches, using the number of evaluations made by
        // the GA search as the max number of evaluations the other algos are allowed to use
        int maxEvals = getGAStats(params);
        getRandomSearchStats(maxEvals, samplePeriod);
        getHillClimbingStats(maxEvals, samplePeriod);
        getMutateSearchStats(maxEvals, samplePeriod);

        printStats("Data Files\\FullStats_" + maxEvals + ".txt", maxEvals);
    }

    private static int getGAStats(GAParams params) {
        GASolver<Schedule> worker = new GASolver<Schedule>(params);
        worker.run();
        int evals = worker.numEvaluations();

        GABestStats = worker.getBestStatsString();
        GAMeanStats = worker.getMeanStatsString();
        return evals;
    }

    private static void getRandomSearchStats(int maxEvals, int samplePeriod) {
        randomSearch(maxEvals, samplePeriod);
        randomSearchStats = getRandomSearchString();
    }
    
    private static void getHillClimbingStats(int maxEvals, int samplePeriod) {
        ((Schedule) Schedule.random()).hillClimb(maxEvals, samplePeriod);
        hillClimbingStats = Schedule.getHillclimbSearchString();
    }

    private static void getMutateSearchStats(int maxEvals, int samplePeriod) {
        mutateSearch(maxEvals, samplePeriod);
        mutateSearchStats = getMutateSearchString();
    }

    private static void printStats(String outFileName, int maxEvals) {
        try {
            FileWriter fstream = new FileWriter(outFileName);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("Performance of the 4 searches using at most " + maxEvals + " evaluations.\n");
            // Number of dimensions in data
            out.write("2\n");
            out.write("Number of evaluations\n");
            out.write("Fitness\n");
            // Number of series
            out.write("5\n");

            out.write(GABestStats + "\n");
            out.write(GAMeanStats + "\n");
            out.write(randomSearchStats + "\n");
            out.write(hillClimbingStats + "\n");
            out.write(mutateSearchStats + "\n");

            out.close();
        } catch (IOException ex) {
            System.err.println("Error opening file " + outFileName + " for write");
        }
    }

    /**
     * This is the dave test main method. It is used for testing things to make sure
     * they work right.
     * @param args
     */
    public static void davesTestingMain() {
        int maxpop = 40;
        int maxgen = 100;
        double copies = 2;
        double mutations = 20;
        double crossovers = 40;
        double randoms = 25;

        GAParams params = new GAParams(SMALLINSTANCEFILE);
        params.setPopulationSize(maxpop);
        params.setMaxGenerations(maxgen);
        // Set Generation proportions
        params.setCopies(copies);
        params.setMutations(mutations);
        params.setCrossovers(crossovers);
        params.setRandoms(randoms);
        params.setVerbose(true);

        int samplePeriod = params.numEvalsPerGeneration();

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
        System.out.println(worker.getBestStatsString());
        System.out.println(worker.getMeanStatsString());

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

    /**
     * Get the data resulting from a run of the random search method
     * @return the data resulting from a run of the random search method
     */
    public static String getRandomSearchString() {
        return randomSearchString + "END-SERIES";
    }
}
