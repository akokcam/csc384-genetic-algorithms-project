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
    private List<T> pop;
    /**
     * The fitnesses of the current individuals.
     */
    private float[] fitnesses;
    /**
     * The total of the fitnesses of the current generation.
     */
    private float fitnessTotal = 0;
    /**
     * The number of individual evaluations performed.
     */
    private int evals;
    /**
     * The proportion of the next generations that should be copies of
     * individuals from the current generation.
     */
    private float copies = -1;
    /**
     * The proportion of the next generation that should be made by mutating
     * individuals in this generation.
     */
    private float mutations = -1;
    /**
     * The proportion of the next generation that should be made by crossing
     * over pairs of individuals in this generation.
     */
    private float crossovers = -1;
    /**
     * The proportion of the next generation that should be made by generating
     * new random individuals.
     */
    private float randoms = -1;
    /**
     * The number of times evolve() has been called for this population.
     */
    private int currentGeneration;

    /**
     * This is the random number generator used by the class.
     */
    private static Random rand = new Random();

    /**
     * Make a new population.
     * @param size The number of individuals to keep at every generation.
     */
    public GAPopulation(int size) {
        this.size = size;
        this.pop = new ArrayList<T>(size);
        fitnesses = new float[size];
        this.evals = 0; // No evaluations have been performed yet.
        this.currentGeneration = 0;
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

        if (copies < 0 || mutations < 0 || crossovers < 0 || randoms < 0) {
            throw new RuntimeException("Negative generation proportions " +
                    "not allowed.");
        }
        this.copies = copies;
        this.mutations = mutations;
        this.crossovers = crossovers;
        this.randoms = randoms;
    }

    /**
     * Evolve the population on generation, using the proportions set by
     * setNextGenerationProportions.
     */
    public void evolve() {
        if (copies < 0 || mutations < 0 || crossovers < 0 || randoms < 0) {
            throw new RuntimeException("You must initialize the " +
                    "nextGenerationProportions before evolving.");
        }

        // This gets a little bit complicated, what with the selection
        //  process and all.

        this.currentGeneration++;
        throw new UnsupportedOperationException("Not supported yet.");
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
        for (T current : pop) {
            // If the last generation set the fitness, then we don't need to evaluate it.
            if (fitnesses[i] == -1) {
                currentFitness = current.fitness();

                fitnesses[i] = currentFitness;
                this.fitnessTotal += currentFitness;
                evals++; // We've done another evaluation
            }
            i++; // Do the next one next
        }
    }
}
