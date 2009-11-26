package ScheduleProblem;

import ga_testbench.GAParams;
import ga_testbench.GASolver;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    // Sadly, we need backslashes for windows machines
    public static final String TESTINSTANCE = "Instance Input Files\\tickle.txt";
    public static final String SMALLINSTANCEFILE = "Instance Input Files\\smallish instance.txt";
    public static final String MEDIUMINSTANCEFILE = "Instance Input Files\\biggerinstance.txt";
    public static final String HUGEINSTANCE = "Instance Input Files\\super_huge_instance.txt";
    /**
     * These store the data points for the search methods
     * Do not use directly. Instead, use the get... method to get the end bits
     * added for you.
     */
    private static String randomSearchString = "";
    private static String mutateSearchString = "";
    private static String GABestStats = "";
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

//        getGAParameterStats(51, 150, 1);
//        getGASizeStats();

//        getAllStats(50, 600, 1, 9, 110, 0, HUGEINSTANCE);

        int maxpop = 70;
        int maxgen = 400;
        double copies = 1;
        double mutations = 6;
        double crossovers = 63;
        double randoms = 0;
        getAllAverageStats(maxpop, maxgen, copies, mutations, crossovers, randoms, SMALLINSTANCEFILE, 10);
    }

    /**
     * This should dump some stats to a file so we can evaluate the quality of
     * various GA parameter settings.
     */
    private static void getGAParameterStats(int popSize, int numGens, int numTrials) {
        GAParams params = new GAParams(TESTINSTANCE);
        int diff = 1;
        double copies = 1;
        params.setPopulationSize(popSize);
        params.setMaxGenerations(numGens);
        params.setCopies(copies);
        final String outFileName = "Data Files\\Parameters Data with population " +
                popSize + " and " + numGens + " generations.txt";

        System.out.println(outFileName);

        try {
            FileWriter fstream = new FileWriter(outFileName);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("GA Parameters in 3 Dimensions, testing the quality of different settings of the crossover and mutation proportions.\n");
            // Number of dimensions in data
            out.write("3\n");
            out.write("Proportion of Randoms\n");
            out.write("Proportion of Mutations\n");
            out.write("Attained Fitness\n");
            // Number of series
            out.write("1\n");
            out.write("GA with parameters" + "\n");
            for (double rands = 0; rands < popSize; rands += diff) {
                for (double mutations = 0; mutations + rands < popSize; mutations += diff) {
                    double crossovers = popSize - mutations - rands - copies;
                    double fitness = 0;

                    // Set Generation proportions
                    params.setMutations(mutations);
                    params.setCrossovers(crossovers);
                    params.setRandoms(rands);

                    for (int i = 0; i < numTrials; i++) {
                        // Perform the test
                        GASolver<Schedule> worker = new GASolver<Schedule>(params);
                        Schedule best = (Schedule) worker.run();
                        fitness += best.fitness();
                    }

                    String line = (int) rands + ", " + (int) mutations + ", " + fitness / numTrials;

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

    /**
     * This should dump some stats to a file so we can evaluate the quality of
     * various GA Size parameter settings.
     */
    private static void getGASizeStats() {
        String inputFile = MEDIUMINSTANCEFILE;
        int numTrials = 5;
        int evalsCutoff = 11000;

        int poplow = 5;
        int pophigh = 150;
        int popdiff = 2;

        int genlow = 5;
        int genhigh = 250;
        int gendiff = 5;

        final String outFileName = "Data Files\\GA Size Data - Pop from " +
                poplow + "-" + pophigh + " Gen from " + genlow + "-" +
                genhigh + " with " + numTrials + " Trials.txt";

        System.out.println(outFileName);

        try {
            FileWriter fstream = new FileWriter(outFileName);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("GA Size Parameters in 3 Dimensions, testing the quality of " +
                    "different settings of the population and number of generations.\n");
            // Number of dimensions in data
            out.write("3\n");
            out.write("Population Size\n");
            out.write("Number of Generations\n");
            out.write("Attained Fitness\n");
            // Number of series
            out.write("1\n");
            out.write("GA with parameters" + "\n");

            for (int population = poplow; population <= pophigh; population += popdiff) {
                for (int numgens = genlow; numgens <= genhigh; numgens += gendiff) {
                    GAParams params = new GAParams(inputFile);
                    double crossovers = 0.91 * population;
                    double randoms = 0.0;
                    double mutations = 0.09 * population;
                    double copies = 1;
                    // Set Generation proportions
                    params.setMutations(mutations);
                    params.setCrossovers(crossovers);
                    params.setRandoms(randoms);
                    params.setCopies(copies);
                    params.setPopulationSize(population);
                    params.setMaxGenerations(numgens);

                    if (params.numEvalsPerGeneration() * numgens > evalsCutoff) {
                        continue;
                    }

                    double fitness = 0;
                    for (int i = 0; i < numTrials; i++) {
                        // Perform the test
                        GASolver<Schedule> worker = new GASolver<Schedule>(params);
                        Schedule best = (Schedule) worker.run();
                        fitness += best.fitness();
                    }

                    String line = (int) population + ", " + (int) numgens + ", " + fitness / numTrials;

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

    public static void getAllAverageStats(int popsize, int numgens, double numCopies,
            double numMutations, double numCrossovers, double numRandoms, String instanceFile, int numIterations) {

        GAParams params = new GAParams(instanceFile, popsize, numgens, numCopies, numMutations, numCrossovers, numRandoms, true);

        // We don't mind having more points for the non-GA methods
        int samplePeriod = params.numEvalsPerGeneration();

        System.out.println("The sample period is " + samplePeriod);
        int maxEvals = 0;
        for (int i = 1; i <= numIterations; i++) {
            System.out.println("\n--- Running iteration number " + i + " of " + numIterations + " ---");
            // Run the 4 different searches, using the number of evaluations made by
            // the GA search as the max number of evaluations the other algos are allowed to use
            System.out.println("We expect " + (params.numEvalsPerGeneration() * params.getMaxGenerations() + params.getPopSize()) + " evaluations to be performed.");
            maxEvals = getGAStats(params);
            System.out.println("GA Done! (" + maxEvals + " evals performed)");
            getRandomSearchStats(maxEvals, samplePeriod);
            System.out.println("Random Done!");
            getHillClimbingStats(maxEvals, samplePeriod);
            System.out.println("HillClimbing Done!");
            getMutateSearchStats(maxEvals, samplePeriod);
            System.out.println("MutateSearch Done!");

            // Generate temporary files holding data of each iteration
            String outfile = "Data Files\\tempAverageStatsOutput\\" + i + ".txt";
            printStats(outfile, maxEvals);
        }

        // We generate uniform filenames so that it's easy to know what data is in a file
        String outfile = "Data Files\\FullAverageStats over " + numIterations + " iterations with " + maxEvals + " evals " +
                params.getPopSize() + " pop " + params.getMaxGenerations() + " gens " +
                params.getNumCopies() + " copies " + params.getNumMutations() + " mutations " +
                params.getNumCrossovers() + " crossovers " + params.getNumRandoms() + " randoms" + ".txt";

        createAverageStatsFile(outfile, numIterations, maxEvals, samplePeriod);
        //cleanUpTempStats(numIterations);
    }

    private static void createAverageStatsFile(String outFileName, int numIterations, int maxEvals, int samplePeriod) {
        String header = "";
        String GATitle = "";
        String RandomTitle = "";
        String HillTitle = "";
        String MutateTitle = "";

        int[] fitnessIntervals = new int[maxEvals / samplePeriod + 1];
        double[] GAStatsSum = new double[maxEvals / samplePeriod + 1];
        double[] RandomStatsSum = new double[maxEvals / samplePeriod + 1];
        double[] HillStatsSum = new double[maxEvals / samplePeriod + 1];
        double[] MutateStatsSum = new double[maxEvals / samplePeriod + 1];

        fitnessIntervals[0] = 1;

        for (int i = 1; i <= numIterations; i++) {
            File infile = new File("Data Files\\tempAverageStatsOutput\\" + i + ".txt");
            Scanner scanner = null;
            try {
                scanner = new Scanner(infile);
            } catch (FileNotFoundException ex) {
                System.out.println(ex.getMessage());
                System.exit(0); //UGLY, but just want to get it done, am supposing this is working anyway.
            }

            // Get header or skip it if we already have it.
            for (int j = 0; j < 5; j++) {
                if (i == 1) {
                    header = header + scanner.nextLine() + "\n";
                } else {
                    scanner.nextLine();
                }
            }

            GATitle = scanner.nextLine() + "\n";
            // Get GA data and the intervals if this is first pass through
            for (int j = 1; j <= maxEvals / samplePeriod; j++) {
                String temp = scanner.nextLine();
                String[] vals = temp.split(", ");

                if (i == 1) {
                    fitnessIntervals[j] = Integer.parseInt(vals[0]);
                }
                GAStatsSum[j] += Double.parseDouble(vals[1]);
            }

            scanner.nextLine(); // Skip "END-SERIES"


            RandomTitle = scanner.nextLine() + "\n";
            // Get Random data
            for (int j = 0; j <= maxEvals / samplePeriod; j++) {
                String temp = scanner.nextLine();
                String[] vals = temp.split(", ");
                RandomStatsSum[j] += Double.parseDouble(vals[1]);
            }

            scanner.nextLine(); // Skip "END-SERIES"

            HillTitle = scanner.nextLine() + "\n";
            // Get Hill Climbing data
            for (int j = 0; j <= maxEvals / samplePeriod; j++) {
                String temp = scanner.nextLine();
                String[] vals = temp.split(", ");
                HillStatsSum[j] += Double.parseDouble(vals[1]);
            }

            scanner.nextLine(); // Skip "END-SERIES"

            MutateTitle = scanner.nextLine() + "\n";
            // Get Mutate data
            for (int j = 0; j <= maxEvals / samplePeriod; j++) {
                String temp = scanner.nextLine();
                String[] vals = temp.split(", ");
                MutateStatsSum[j] += Double.parseDouble(vals[1]);
            }

            scanner.nextLine(); // Skip "END-SERIES"
            scanner.close();
        }

        // Calcultate average
        RandomStatsSum[0] = RandomStatsSum[0] / numIterations;
        HillStatsSum[0] = HillStatsSum[0] / numIterations;
        MutateStatsSum[0] = MutateStatsSum[0] / numIterations;
        for (int i = 1; i <= maxEvals / samplePeriod; i++) {
            GAStatsSum[i] = GAStatsSum[i] / numIterations;
            RandomStatsSum[i] = RandomStatsSum[i] / numIterations;
            HillStatsSum[i] = HillStatsSum[i] / numIterations;
            MutateStatsSum[i] = MutateStatsSum[i] / numIterations;
        }

        // Print this shit out
        try {
            FileWriter fstream = new FileWriter(outFileName);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(header);

            out.write(GATitle);
            for (int i = 1; i <= maxEvals / samplePeriod; i++) {
                out.write(fitnessIntervals[i] + ", " + GAStatsSum[i] + "\n");
            }
            out.write("END-SERIES\n");

            out.write(RandomTitle);
            for (int i = 0; i <= maxEvals / samplePeriod; i++) {
                out.write(fitnessIntervals[i] + ", " + RandomStatsSum[i] + "\n");
            }
            out.write("END-SERIES\n");

            out.write(HillTitle);
            for (int i = 0; i <= maxEvals / samplePeriod; i++) {
                out.write(fitnessIntervals[i] + ", " + HillStatsSum[i] + "\n");
            }
            out.write("END-SERIES\n");

            out.write(MutateTitle);
            for (int i = 0; i <= maxEvals / samplePeriod; i++) {
                out.write(fitnessIntervals[i] + ", " + MutateStatsSum[i] + "\n");
            }
            out.write("END-SERIES\n");

            out.close();

            System.out.println("Output written in " + outFileName);
        } catch (IOException ex) {
            System.err.println("Error opening file " + outFileName + " for write");
        }
    }

    private static void cleanUpTempStats(int numIterations) {
        for (int i = 1; i <= numIterations; ++i) {
            String fileName = "Data Files\\tempAverageStatsOutput\\" + i + ".txt";
            File f = new File(fileName);
            f.delete();
        }
    }

    public static void getAllStats(int popsize, int numgens, double numCopies,
            double numMutations, double numCrossovers, double numRandoms, String instanceFile) {

        GAParams params = new GAParams(instanceFile, popsize, numgens, numCopies, numMutations, numCrossovers, numRandoms, true);

        // We don't mind having more points for the non-GA methods
        int samplePeriod = params.numEvalsPerGeneration() / 5;

        // Run the 4 different searches, using the number of evaluations made by
        // the GA search as the max number of evaluations the other algos are allowed to use
        int nevals = (params.numEvalsPerGeneration() * params.getMaxGenerations());
        System.out.println("We expect " + nevals + " evaluations to be performed per phase.");
        System.out.println("If we're doing huge, that's " + (4 * nevals * 0.2 / 60) + " minutes total.");
        int maxEvals = getGAStats(params);
        System.out.println("GA Done! (" + maxEvals + " evals performed)");
        getRandomSearchStats(maxEvals, samplePeriod);
        System.out.println("Random Done!");
        getHillClimbingStats(maxEvals, samplePeriod);
        System.out.println("HillClimbing Done!");
        getMutateSearchStats(maxEvals, samplePeriod);
        System.out.println("MutateSearch Done!");

        // We generate uniform filenames so that it's easy to know what data is in a file
        String outfile = "Data Files\\FullStats" + " with " + maxEvals + " evals " +
                params.getPopSize() + " pop " + params.getMaxGenerations() + " gens " +
                params.getNumCopies() + " copies " + params.getNumMutations() + " mutations " +
                params.getNumCrossovers() + " crossovers " + params.getNumRandoms() + " randoms" + ".txt";

        printStats(outfile, maxEvals);

    }

    private static int getGAStats(GAParams params) {
        GASolver<Schedule> worker = new GASolver<Schedule>(params);
        worker.run();
        int evals = worker.numEvaluations();

        GABestStats = worker.getBestStatsString();
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
            out.write("4\n");

            out.write(GABestStats + "\n");
            out.write(randomSearchStats + "\n");
            out.write(hillClimbingStats + "\n");
            out.write(mutateSearchStats + "\n");

            out.close();

            System.out.println("Output written in " + outFileName);
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
            Schedule tester = (Schedule) best.mutate(0.01);
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
        //mutateSearchString += evals + ", " + bestfit + "\n";
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
            if (i % samplePeriod == 0 || i == 1) {
                randomSearchString += i + ", " + fitness + "\n";
            }
        }

        //randomSearchString += maxEvals + ", " + fitness + "\n";
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
