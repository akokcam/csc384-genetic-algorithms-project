package ga_testbench;

import ScheduleProblem.Schedule;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

/**
 * Some of this is still sloppy. I make too many trees. Stuff like that. But
 * it's quick enough that the evaluations are still by far the big time
 * requirement, and this works fine.
 *
 * @author dave
 */
public class GAPopulation implements Population {

    /**
     * The parameters for this population
     */
    GAParams params;
    /**
     * A list containing the members of the current generation.
     */
    private List<Schedule> pop;
    /**
     * The fitnesses of the current individuals.
     */
    private double[] fitnesses;
    /**
     * The total of the fitnesses of the current generation.
     */
    private double fitnessTotal = 0;
    /**
     * The minimum fitness in the population.
     */
    private double fitnessMin;
    /**
     * The maximum fitness in the population.
     */
    private double fitnessMax;
    /**
     * The number of individual evaluations performed.
     */
    private int evals;
    /**
     * The number of times evolve() has been called for this population.
     */
    private int currentGeneration;
    /**
     * This is the random number generator used by the class.
     */
    private static Random rand;
    /**
     * Should be true if the state of the population has all of the members
     * evaluated.
     */
    private boolean allEvaluated;
    /**
     * The Map used to perform uniform selection from the population
     */
    private TreeMap<Double, Schedule> selectionTree;
    private double treemax;

    /**
     * Make a new population.
     * @param size The number of individuals to keep at every generation.
     */
    GAPopulation(GAParams params) {
        this.params = params;
        this.pop = new ArrayList<Schedule>(params.getPopSize());
        fitnesses = new double[params.getPopSize()];
        this.evals = 0; // No evaluations have been performed yet.
        this.currentGeneration = 0;
        this.allEvaluated = false;
        GAPopulation.rand = new Random();

        // Any evaluation should set both of these
        this.fitnessMin = Double.POSITIVE_INFINITY;
        this.fitnessMax = Double.NEGATIVE_INFINITY;

        // Set all of the fitnesses to NaN
        for (int i = 0; i < params.getPopSize(); i++) {
            Schedule randomIndividual = (Schedule) Schedule.random();
            this.pop.add(randomIndividual);
            this.fitnesses[i] = Double.NaN; // All of the fitnesses are initialized to Nan
        }
    }

    /**
     * Get the best individual in the current generation.
     * @return The individual in the current generation with the highest
     * absolute fitness.
     */
    public Individual getBest() {
        if (!allEvaluated) {
            evaluateAll();
        }

        double val = Double.NEGATIVE_INFINITY;
        int index = 0;

        for (int i = 0; i < params.getPopSize(); i++) {
            if (fitnesses[i] > val) {
                index = i;
                val = fitnesses[i];
            }
        }

        return pop.get(index);
    }

    /**
     * Add some copies from the current population to the list newList.
     * Note: we mess with the current generation's fitnesses array, so it
     * cannot be trusted after calling this.
     * @param newList The list to add copies to
     * @param newFitnesses The new list of fitnesses
     */
    private void addCopies(List<Schedule> newList, double[] newFitnesses) {
        // Add the copies
        TreeMap<Double, Schedule> tree = new TreeMap<Double, Schedule>();
        for (int i = 0; i < params.getPopSize(); i++) {
            tree.put(fitnesses[i], pop.get(i));
        }

        for (int i = 0; i < params.getNumCopies(); i++) {
            Entry<Double, Schedule> highest = tree.pollLastEntry();
            int location = newList.size();
            newList.add(highest.getValue());
            // Update the total fitness and individual fitness so that we don't 
            // reevaluate copies
            fitnessTotal += highest.getKey();
            newFitnesses[location] = highest.getKey();
        }
    }

    /**
     * Add some crossed over children from pairs of current individuals to the
     * list newList.
     * @param newList The list to add copies to
     */
    private void addCrossovers(List<Schedule> newList) {
        // Add the crossovers
        for (int i = 0; i < params.getNumCrossovers(); i++) {
            Schedule first = getRandomWeightedIndividual();
            Schedule second = getRandomWeightedIndividual();
            newList.add((Schedule) first.crossWith(second));
        }
    }

    /**
     * Modify some of the current individuals wwith mutations and put them on
     * the new list.
     * @param newList The list to add copies to
     */
    private void addMutations(List<Schedule> newList) {
        // Add the mutated individiduals
        for (int i = 0; i < params.getNumMutations(); i++) {
            Schedule individual = getRandomWeightedIndividual();
            // Mutate it by a random amount, weighted a little bit down, so we
            // always mutate less than half. This is sane.
            double mutAmt = rand.nextDouble() / 2;
            newList.add((Schedule) individual.mutate(mutAmt));
        }
    }

    /**
     * Add some completely random individuals to the new list
     * @param newList The list to add copies to
     */
    private void addRandoms(List<Schedule> newList) {
        // Add the required number of randoms.
        for (int i = 0; i < params.getNumRandoms(); i++) {
            Schedule randomIndividual = (Schedule) Schedule.random();
            newList.add(randomIndividual);
        }
    }

    /**
     * Get an individual with probability weighted by its normalized fitness.
     * This should only ever be called after a call to evaluateAll()
     * @return A random individual in the current population.
     */
    private Schedule getRandomWeightedIndividual() {
        assert (allEvaluated);
        double r = rand.nextDouble() * treemax;
        Schedule ret = selectionTree.ceilingEntry(r).getValue();
        assert (ret != null);
        return ret;
    }

    /**
     * Evolve the population on generation, using the proportions set by
     * setNextGenerationProportions.
     */
    public void evolve() {
        int size = params.getNumCopies() + params.getNumCrossovers() +
                params.getNumMutations() + params.getNumRandoms();
        if (size != params.getPopSize()) {
            throw new RuntimeException("The size of the population doesn't match its components.");
        }

        // Make sure that all have been evaluated
        evaluateAll();

        List<Schedule> newList = new ArrayList<Schedule>(size);
        double[] newFitnesses = new double[size];
        for (int i = 0; i < size; i++) {
            newFitnesses[i] = Double.NaN;
        }

        makeSelectionTree();
        addRandoms(newList);
        addMutations(newList);
        addCrossovers(newList);
        fitnessTotal = 0;
        addCopies(newList, newFitnesses);

        this.pop = newList;
        this.fitnesses = newFitnesses;
        this.fitnessMax = Double.NEGATIVE_INFINITY;
        this.fitnessMin = Double.POSITIVE_INFINITY;

        this.currentGeneration++;
        this.allEvaluated = false;
        selectionTree = null;

        evaluateAll();
    }

    /**
     * Update the internal min and max values. This is needed whenever an
     * individual is evaluated to ensure that the weighting works correctly,
     * since we want to allow negative fitnesses, to be general. Selection
     * proportionate value is actually individual fitness - minFitness, so
     * that the weakest individual should never be selected.
     * @param val The new fitness value to consider
     */
    private void updateMinMax(double val) {
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
    public double fitnessAverage() {
        if (!allEvaluated) {
            evaluateAll();
        }
        return fitnessTotal / params.getPopSize();
    }

    /**
     * Get the variance of the fitness of individuals in this generation.
     * @return The variance of the fitness of individuals in this generation.
     */
    public double fitnessVariance() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Evaluate the fitness of all of the members of the current generation if
     * they're not known.
     */
    private void evaluateAll() {
        double currentFitness;
        for (int i = 0; i < params.getPopSize(); i++) {
//        for (Schedule current : pop) {
            // If the last generation set the fitness, then we don't need to evaluate it.
            if (Double.isNaN(fitnesses[i])) {
                Individual current = pop.get(i);
                currentFitness = current.fitness();

                fitnesses[i] = currentFitness;
                this.fitnessTotal += currentFitness;
                updateMinMax(currentFitness);
                evals++; // We've done another evaluation
            }
        }

        this.allEvaluated = true;
    }

    /**
     * Get the normed fitness (where no negative fitnesses are allowed).
     * @param index The individual to check the fitness of.
     * @return The normed fitness
     */
    private double getNormedFitness(int index) {
        assert (allEvaluated);
        return fitnesses[index] - fitnessMin;
    }

    /**
     * Get the minimum of 2 doubles.
     * @param a Some number
     * @param b Another number
     * @return The minimum of the two
     */
    private double min(double a, double b) {
        return (a < b) ? a : b;
    }

    /**
     * Make the selection tree so that you can select individuals weighted by
     * their fitness
     */
    private void makeSelectionTree() {
        assert (allEvaluated);
        selectionTree = new TreeMap<Double, Schedule>();

        double tot = 0;
        for (int i = 0; i < params.getPopSize(); i++) {
            tot += getNormedFitness(i);
            selectionTree.put(tot, this.pop.get(i));
        }
        treemax = tot;
    }

    /**
     * Find out how many fitness evaluations took place
     * @return The number of times the fitness function was called.
     */
    public int numEvaluations() {
        return evals;
    }
}
