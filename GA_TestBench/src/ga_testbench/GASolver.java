package ga_testbench;

import ScheduleProblem.Schedule;

/**
 *
 * @author dave
 */
public class GASolver<T extends Individual> implements Solver {

    /**
     * The number of individuals that a population should have.
     */
    private int popSize;
    /**
     * The maximum amount of time, in seconds, that should be spent trying to
     * solve this instance.
     */
    private double timeLimit;
    /**
     * The maximum number of generations that should be processed before
     * returning the best individual encountered.
     */
    private int maxGens;
    /**
     * The proportion of the next generations that should be copies of
     * individuals from the current generation.
     */
    private double copies;
    /**
     * The proportion of the next generation shat should be made by mutating
     * individuals when making a new generation.
     */
    private double mutations;
    /**
     * The proportion of the next generation that should be made by crossing
     * over pairs of individuals between generations.
     */
    private double crossovers;
    /**
     * The proportion of the next generation that should be made by generating
     * new random individuals.
     */
    private double randoms = -1;
    /**
     * The population at a certain time.
     */
    private Population<T> population;
    /**
     * The time that the timing should count from, in milliseconds, from System.
     */
    private long startTime;
    /**
     * The time that we should stop evolving.
     */
    private long endTime;
    /**
     * Whether or not we should be outputting statistics when we evolve the population.
     */
    private boolean verboseOn;
    /**
     * This is where the problem instance data is located. The individual knows
     * how to decode this file and keep static variables for itself to remember
     * how to perform fitness evaluations.
     */
    private final String instanceDataFile;

    /**
     * Default constructor. Default values are given, but can be set with the
     * set... methods.
     */
    public GASolver(String instanceDataFile) {
        // Initial default population
        popSize = 1000;

        // How to know if we're done
        timeLimit = -1;
        maxGens = 100;

        // Default next-generation stuff
        copies = 0.01f;
        mutations = 0.20f;
        crossovers = 0.69f;
        randoms = 0.10f;

        // verbose off by default. Must be set on explicitly.
        verboseOn = false;

        // Set these explicitly
        startTime = -1;
        endTime = -1;

        // Set the instanceData field, so where we know where to get the
        // instance from, when we need it.
        this.instanceDataFile = instanceDataFile;
    }

    /**
     * Set the size of population that this run will use.
     * @param size The number of individuals to track in one generation.
     */
    public void setPopulationSize(int size) {
        popSize = size;
    }

    /**
     * Set the maximum number of generations that should be processed before
     * returning the best found individual.
     * @param maxGens An upper limit on the number of generations to make.
     */
    public void setMaxGenerations(int maxGens) {
        this.maxGens = maxGens;
    }

    /**
     * Set a time limit on the computation.
     * @param timeLimit Number of seconds to work for, at max.
     */
    public void setTimeLimit(double timeLimit) {
        this.timeLimit = timeLimit;

        this.startTime = System.currentTimeMillis();
        this.endTime = this.startTime + (long) (timeLimit * 1000);
    }

    /**
     * Set the proportions that should determine the composition of the
     * population from one generation to the next. The sum of these 4 fields can
     * be anything, because we normalize them, but they should all be positive.
     * @param copies The portion of the next generation that should be copies
     *  of the finest members of this generation.
     * @param mutations The approximate proportion of the next generation that
     *  should be made by randomly mutating individuals in the current
     *  generation.
     * @param crossovers The approximate proportion of the next generation that
     *  should be made by crossing over pairs of individuals from the current
     *  generation.
     * @param randoms The approximate proportion of the next generation that
     *  should be made by creating new random individuals.
     */
    public void setNextGenerationProportions(double copies,
            double mutations, double crossovers, double randoms) {
        if (copies < 0 || mutations < 0 || crossovers < 0 || randoms < 0) {
            throw new RuntimeException("Invalid generation proportions.");
        }
        double tot = copies + mutations + crossovers + randoms;
        this.copies = copies / tot;
        this.mutations = mutations / tot;
        this.crossovers = crossovers / tot;
        this.randoms = randoms / tot;
    }

    /**
     * Generate statistics when every generation is created.
     * @param outputFile File to put statistics in.
     */
    public void setVerboseMode(String outputFile) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Add a line of data to the verbose report.
     */
    private void addReport() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Close files and do whatever is necessary to complete the stats gathering.
     */
    private void verboseTidyUp() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Run the genetic algorithm with the current settings.
     * @return
     */
    public T run() {
        boolean loaded = Schedule.initialize(instanceDataFile); // Load the desired problem instance
        if (!loaded) {
            throw new RuntimeException("Problem instance couldn't be loaded.");
        }

        population = new GAPopulation(popSize);
        population.setNextGenerationProportions(copies, mutations, crossovers, randoms);

        if (timeLimit <= 0 && maxGens <= 0) {
            throw new RuntimeException("No stopping condition set. Cannot run the GA.");
        }

        while (evolutionNotDone()) {
            population.evolve();
            if (verboseOn) {
                addReport();
            }
        }

        if (verboseOn) {
            verboseTidyUp();
        }
        return population.getBest();
    }

    /**
     * Check if we're done evolving the population yet.
     * @return Boolean representing whether we can stop evolving.
     */
    private boolean evolutionNotDone() {
        if (startTime != -1 &&
                System.currentTimeMillis() > endTime) {
            return false;
        }
        if (maxGens != -1 &&
                population.currentGenerationNumber() >= maxGens) {
            return false;
        }
        return true;

    }

    public int numEvaluations() {
        return population.numEvaluations();
    }
}
