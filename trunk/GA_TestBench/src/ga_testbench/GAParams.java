/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ga_testbench;

/**
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

    public int getMaxGenerations() {
        return maxGens;
    }

    public int getNumCopies() {
        return copies;
    }

    public int getNumCrossovers() {
        return crossovers;
    }

    public int getNumRandoms() {
        return randoms;
    }

    public int getNumMutations() {
        return mutations;
    }

    public void setCopies(double copies) {
        this.copiesP = copies;
        fixProportions();
    }

    public void setCrossovers(double crossovers) {
        this.crossoversP = crossovers;
        fixProportions();
    }

    public void setMutations(double mutations) {
        this.mutationsP = mutations;
        fixProportions();
    }

    public void setRandoms(double randoms) {
        this.randomsP = randoms;
        fixProportions();
    }

    public String getInstanceDataFile() {
        return instanceDataFile;
    }

    public void setInstanceDataFile(String instanceDataFile) {
        this.instanceDataFile = instanceDataFile;
    }

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

    public boolean isVerbose() {
        return verboseOn;
    }

    public void setVerbose(boolean verboseOn) {
        this.verboseOn = verboseOn;
    }

    private double propTot() {
        double ret = 0;
        ret += copiesP + crossoversP + randomsP + mutationsP;
        return ret;
    }

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

    public String proportionString() {
        String ret = "";
        ret += "Copies: " + getNumCopies() + ", ";
        ret += "Mutations: " + getNumMutations() + ", ";
        ret += "Crossovers: " + getNumCrossovers() + ", ";
        ret += "Randoms: " + getNumRandoms();

        return ret;
    }
}
