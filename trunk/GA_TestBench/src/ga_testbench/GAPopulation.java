package ga_testbench;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author dave
 */
public class GAPopulation<T extends Individual> implements Population {

    /**
     * The number of individuals to keep for a ganeration.
     */
    private int size;
    /**
     * A list containing the members of the current generation.
     */
    private List<Individual> pop;
    /**
     * The fitnesses of the current individuals.
     */
    private float[] fitnesses;
    /**
     * The total of the fitnesses of the current generation.
     */
    private float fitnessTotal = 0;
    /**
     * The minimum fitness in the population.
     */
    private float fitnessMin;
    /**
     * The maximum fitness in the population.
     */
    private float fitnessMax;
    /**
     * The number of individual evaluations performed.
     */
    private int evals;
    /**
     * The quantity of the next generations that should be copies of
     * individuals from the current generation.
     */
    private int numCopies = -1;
    /**
     * The quantity of the next generation that should be made by mutating
     * individuals in this generation.
     */
    private int numMutations = -1;
    /**
     * The quantity of the next generation that should be made by crossing
     * over pairs of individuals in this generation.
     */
    private int numCrossovers = -1;
    /**
     * The quantity of the next generation that should be made by generating
     * new random individuals.
     */
    private int numRandoms = -1;
    /**
     * The number of times evolve() has been called for this population.
     */
    private int currentGeneration;
    /**
     * This is the random number generator used by the class.
     */
    private static Random rand;

    /**
     * Make a new population.
     * @param size The number of individuals to keep at every generation.
     */
    public GAPopulation(int size) {
        this.size = size;
        this.pop = new ArrayList<Individual>(size);
        fitnesses = new float[size];
        this.evals = 0; // No evaluations have been performed yet.
        this.currentGeneration = 0;
        GAPopulation.rand = new Random();
        for (int i = 0; i < size; i++) {
            Individual randomIndividual = T.random();
            this.pop.add(randomIndividual);
            this.fitnesses[i] = Float.NaN; // All of the fitnesses are initialized to Nan
        }

    }

    /**
     * Get the best individual in the current generation.
     * @return The individual in the current generation with the highest
     * absolute fitness.
     */
    public Individual getBest() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Set the proportions for individuals in the next generation. The sum of
     *  these 4 fields should be equal to 1.
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
    public void setNextGenerationProportions(
            float copies, float mutations, float crossovers, float randoms) {

        /* Much of this error checking is unnecessary, really, but I like to be 
         * careful. -DK
         */

        if (copies < 0 || mutations < 0 || crossovers < 0 || randoms < 0) {
            throw new RuntimeException("Negative generation proportions " +
                    "not allowed.");
        }

        /* Calculate the precise number of each type that should be in the next
         * generation.
         */
        this.numCopies = (int) (copies * this.size);
        this.numMutations = (int) (mutations * this.size);
        this.numCrossovers = (int) (crossovers * this.size);
        this.numRandoms = (int) (randoms * this.size);
        int tot = this.numCopies + this.numMutations +
                this.numCrossovers + this.numRandoms;

        // The rounding error should never be more than one.
        int err = size - tot;
        if (err < -1 || err > 1) {
            // On rounding error, we fix mutations
            this.numMutations += err;
            assert (this.numMutations >= 0);
        }

        // Test it again.
        tot = this.numCopies + this.numMutations +
                this.numCrossovers + this.numRandoms;
        assert (tot == size);
    }

    /**
     * Get an individual with probability weighted by its normalized fitness.
     * This should only ever be called after a call to evaluateAll()
     * @return A random individual in the current population.
     */
    private Individual getRandomWeightedIndividual() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Evolve the population on generation, using the proportions set by
     * setNextGenerationProportions.
     */
    public void evolve() {
        if (numCopies < 0 || numMutations < 0 ||
                numCrossovers < 0 || numRandoms < 0) {
            throw new RuntimeException("You must initialize the " +
                    "nextGenerationProportions before evolving.");
        }

        evaluateAll();

        List<Individual> newList = new ArrayList<Individual>(size);
        float[] newFitnesses = new float[size];
        for (int i = 0; i < size; i++) {
            newFitnesses[i] = Float.NaN;
        }

        /* We need to put them all in a nice structure here, so we can select
         * from them based on a random number < fitness total.
         * This gets a little bit complicated, what with the selection
         * process and all.
         */

        // This should construct a nice structure that can be used to


        // Add the required number of randoms.
        for (int i = 0; i < numRandoms; i++) {
            Individual randomIndividual = T.random();
            newList.add(randomIndividual);
        }

        // Add the mutated individiduals
        for (int i = 0; i < numMutations; i++) {
            Individual individual = getRandomWeightedIndividual();
            // Mutate it by a random amount, weighted a little bit down, so we 
            // always mutate less than half. This is sane.
            float mutAmt = rand.nextFloat();
            mutAmt = min(mutAmt, 1 - mutAmt);

            newList.add(individual.mutate(mutAmt));
        }

        // Add the crossovers
        for (int i = 0; i < numCrossovers; i++) {
            Individual first = getRandomWeightedIndividual();
            Individual second = getRandomWeightedIndividual();
            newList.add(first.crossWith(second));
        }

        // Add the copies
        for (int i = 0; i < numCrossovers; i++) {
            // NOT DONE YET
            // Remember to update totalfitness and make min/maxFitness correct
            // for the new population
        }

        this.currentGeneration++;
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Update the internal min and max values. This is needed whenever an
     * individual is evaluated to ensure that the weighting works correctly,
     * since we want to allow negative fitnesses, to be general. Selection
     * proportionate value is actually individual fitness - minFitness, so
     * that the weakest individual should never be selected.
     * @param val The new fitness value to consider
     */
    private void updateMinMax(float val) {
        if (val > fitnessMax) {
            fitnessMax = val;
        }
        if (val < fitnessMin) {
            fitnessMin = val;
        }
    }

    /**
     * Get the number of evolutions that have taken place. If 0 is returned,
     * this must be the initial random population.
     * @return The generation number.
     */
    public int currentGenerationNumber() {
        return currentGeneration;
    }

    /**
     * Get the average fitness of individuals in this generation.
     * @return The average fitness of individuals in this generation.
     */
    public float fitnessAverage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Get the variance of the fitness of individuals in this generation.
     * @return The variance of the fitness of individuals in this generation.
     */
    public float fitnessVariance() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Evaluate the fitness of all of the members of the current generation if
     * they're not known.
     */
    private void evaluateAll() {
        int i = 0;
        float currentFitness;
        for (Individual current : pop) {
            // If the last generation set the fitness, then we don't need to evaluate it.
            if (fitnesses[i] == Float.NaN) {
                currentFitness = current.fitness();

                fitnesses[i] = currentFitness;
                this.fitnessTotal += currentFitness;
                updateMinMax(currentFitness);
                evals++; // We've done another evaluation
            }
            i++; // Do the next one next
        }

        /* Change all of the fitnesses depending on the value of the least fit.
         * This makes everything eeasier to weight.
         * It is OK to do this now because we will never need to evaluate any
         * of these individuals again.
         * NOTE: Perhaps statistics should be gathered before this normalizing,
         *  or at least we would need to save the value of fitnessMin.
         */
        for (int j = 0; j < size; j++) {
            fitnesses[j] -= fitnessMin;
        }
        fitnessMax -= fitnessMin;
        fitnessMin = 0;
    }

    /**
     * Get the minimum of 2 floats.
     * @param a Some number
     * @param b Another number
     * @return The minimum of the two
     */
    private float min(float a, float b) {
        return (a < b) ? a : b;
    }
}
