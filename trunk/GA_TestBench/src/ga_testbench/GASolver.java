package ga_testbench;

import ScheduleProblem.Schedule;

/**
 *
 * @param <T> The class representing a Problem instance that can be solved.
 * @author dave
 */
public class GASolver<T extends Individual> implements Solver {

    private Population<T> population;
    private final GAParams params;

    public GASolver(GAParams params) {
        this.params = params;
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
        boolean loaded = Schedule.initialize(params.getInstanceDataFile()); // Load the desired problem instance
        if (!loaded) {
            throw new RuntimeException("Problem instance couldn't be loaded.");
        }

        population = new GAPopulation(params);
//
//        if (params.getTimeLimit() <= 0 && params.getMaxGens() <= 0) {
//            throw new RuntimeException("No stopping condition set. Cannot run the GA.");
//        }

        while (evolutionNotDone()) {
            population.evolve();
            if (params.isVerbose()) {
                addReport();
            }
        }

        if (params.isVerbose()) {
            verboseTidyUp();
        }
        return population.getBest();
    }

    /**
     * Check if we're done evolving the population yet.
     * @return Boolean representing whether we can stop evolving.
     */
    private boolean evolutionNotDone() {
        if (params.getMaxGenerations() != -1 &&
                population.currentGenerationNumber() >= params.getMaxGenerations()) {
            return false;
        }
        return true;

    }

    /**
     * Get the number of fitness evaluations that have been performed since
     * this Solver was created.
     * @return The number of evaluations that have been performed
     */
    public int numEvaluations() {
        return population.numEvaluations();
    }
}
