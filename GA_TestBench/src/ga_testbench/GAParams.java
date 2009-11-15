package ga_testbench;

/**
 * This class keeps track of the various parameters that will be used by the
 * GAPopulation and GASolver classes.
 *
 * @author dave
 */
public class GAParams {

    /**
     * The number of individuals that a population should have.
     */
    private int popSize;
    /**
     * The maximum number of generations that should be processed before
     * returning the best individual encountered.
     */
    private int maxGens;
    /**
     * The proportion of the next generations that should be copies of
     * individuals from the current generation.
     */
    private double copiesP;
    private int copies;
    /**
     * The proportion of the next generation shat should be made by mutating
     * individuals when making a new generation.
     */
    private double mutationsP;
    private int mutations;
    /**
     * The proportion of the next generation that should be made by crossing
     * over pairs of individuals between generations.
     */
    private double crossoversP;
    private int crossovers;
    /**
     * The proportion of the next generation that should be made by generating
     * new random individuals.
     */
    private double randomsP;
    private int randoms;
    /**
     * Whether or not we should be outputting statistics when we evolve the population.
     */
    private boolean verboseOn;
    /**
     * This is where the problem instance data is located. The individual knows
     * how to decode this file and keep static variables for itself to remember
     * how to perform fitness evaluations.
     */
    private String instanceDataFile;

    /**
     * Default constructor. Default values are given, but can be set with the
     * set... methods.
     * @param instanceDataFile The location of the file containing problem
     * instance data
     */
    public GAParams(String instanceDataFile) {
        // Initial default population
        popSize = 100;

        // How to know if we're done
        maxGens = 300;

        // Default next-generation stuff
        copiesP = 0.01;
        mutationsP = 0.20;
        crossoversP = 0.69;
        randomsP = 0.10;
        fixProportions();

        // verbose off by default. Must be set on explicitly.
        verboseOn = false;

        // Set the instanceData field, so where we know where to get the
        // instance from, when we need it.
        this.instanceDataFile = instanceDataFile;
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
     * Get the number of generations that we're set to run
     * @return The number of generations
     */
    public int getMaxGenerations() {
        return maxGens;
    }

    /**
     * Get the number of copies that will be in the next generation
     * @return  The number of copies that will be in the next generation
     */
    public int getNumCopies() {
        return copies;
    }

    /**
     * Get the number of crossovers that will be in the next generation
     * @return The number of crossovers that will be in the next generation
     */
    public int getNumCrossovers() {
        return crossovers;
    }

    /**
     * Get the number of randoms that will be in the next generation
     * @return The number of randoms that will be in the next generation
     */
    public int getNumRandoms() {
        return randoms;
    }

    /**
     * Get the number of mutated individuals that will be in the next generation
     * @return The number of mutated individuals that will be in the next generation
     */
    public int getNumMutations() {
        return mutations;
    }

    /**
     * Set the proportion of copies that will be in the next generation
     * @param copies The proportion of copies
     */
    public void setCopies(double copies) {
        this.copiesP = copies;
        fixProportions();
    }

    /**
     * Set the proportion of crossovers that will be in the next generation
     * @param crossovers The proportion of crossovers that will be in the next generation
     */
    public void setCrossovers(double crossovers) {
        this.crossoversP = crossovers;
        fixProportions();
    }

    /**
     * Set the proportion of mutations that will be in the next generation
     * @param mutations the proportion of mutations that will be in the next generation
     */
    public void setMutations(double mutations) {
        this.mutationsP = mutations;
        fixProportions();
    }

    /**
     * Set the proportion of randoms that will be in the next generation
     * @param mutations the proportion of randoms that will be in the next generation
     */
    public void setRandoms(double randoms) {
        this.randomsP = randoms;
        fixProportions();
    }

    /**
     * Get the file to be used by a run of the GA
     * @return The filename
     */
    public String getInstanceDataFile() {
        return instanceDataFile;
    }

    /**
     * Set the file to be loaded as the problem instance for GA
     * @param instanceDataFile The filename
     */
    public void setInstanceDataFile(String instanceDataFile) {
        this.instanceDataFile = instanceDataFile;
    }

    /**
     * Get the population size that will be kept for each generation
     * @return The population size
     */
    public int getPopSize() {
        return popSize;
    }

    /**
     * Set the size of population that this run will use.
     * @param popSize The number of individuals to track in one generation.
     */
    public void setPopulationSize(int popSize) {
        this.popSize = popSize;
        fixProportions();
    }

    /**
     * Determine whether verbose mode is set on or off
     * @return whether verbose mode is set on or off
     */
    public boolean isVerbose() {
        return verboseOn;
    }

    /**
     * Choose whether verbose mode is set on or off
     * @param verboseOn whether verbose mode is set on or off
     */
    public void setVerbose(boolean verboseOn) {
        this.verboseOn = verboseOn;
    }

    /**
     * Get the total of the proportions set by the user
     * @return The total of the proportion variables
     */
    private double propTot() {
        double ret = 0;
        ret += copiesP + crossoversP + randomsP + mutationsP;
        return ret;
    }

    /**
     * Make the proportions set by the user into consistent integral quantities
     * for the next generations totaling the size.
     * NOTE: This must be called before any time the GAparams needs to report
     * consistent subpopulation sizes.
     */
    private void fixProportions() {
        if (copiesP < 0 || mutationsP < 0 || crossoversP < 0 || randomsP < 0) {
            throw new RuntimeException("Invalid generation proportions.");
        }

        double tot = propTot();
        this.copies = (int) Math.round(copiesP * popSize / tot);
        this.mutations = (int) Math.round(mutationsP * popSize / tot);
        this.crossovers = (int) Math.round(crossoversP * popSize / tot);
        this.randoms = (int) Math.round(randomsP * popSize / tot);

        // verify that everything sums to the popsize
        int dif = copies + mutations + crossovers + randoms - popSize;
        if (dif < -1 || dif > 1) {
            throw new RuntimeException("Proportions all messed up for some reason");
        }

        // Move the weirdness to the randoms
        randoms -= dif;
    }

    /**
     * Get a nicely formatted string indicating the quantities of the different
     * types of individuals in the population that will be used.
     * @return Nice string
     */
    public String proportionString() {
        String ret = "";
        ret += "Copies: " + getNumCopies() + ", ";
        ret += "Mutations: " + getNumMutations() + ", ";
        ret += "Crossovers: " + getNumCrossovers() + ", ";
        ret += "Randoms: " + getNumRandoms();

        return ret;
    }
}
