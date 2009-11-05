package ga_testbench;

/**
 *
 * @author dave
 */
public class GASolver<T extends Individual> implements Solver {

    private int popSize;
    private float timeLimit;
    /**
     * The maximum number of generations that should be processed before
     * returning the best individual encountered.
     */
    private int maxGens;
    /**
     * The proportion of the next generations that should be copies of
     * individuals from the current generation.
     */
    private float copies;
    /**
     * The proportion of the next generation shat should be made by mutating
     * individuals when making a new generation.
     */
    private float mutations;
    /**
     * The proportion of the next generation that should be made by crossing
     * over pairs of individuals between generations.
     */
    private float crossovers;
    /**
     * The proportion of the next generation that should be made by generating
     * new random individuals.
     */
    private float randoms = -1;


    private Population<T> population;
    long startTime;
    long endTime;
    private boolean verboseOn;

    public GASolver() {
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
    }

    public void setPopulationSize(int size) {
        popSize = size;
    }

    public void setMaxGenerations(int maxGens) {
        this.maxGens = maxGens;
    }

    public void setTimeLimit(float timeLimit) {
        this.timeLimit = timeLimit;

        this.startTime = System.currentTimeMillis();
        this.endTime = this.startTime + (long) (timeLimit * 1000);
    }

    public void setNextGenerationProportions(float copies,
            float mutations, float crossovers, float randoms) {
        float tot = copies + mutations + crossovers + randoms;
        this.copies = copies / tot;
        this.mutations = mutations / tot;
        this.crossovers = crossovers / tot;
        this.randoms = randoms / tot;
    }

    public void setVerboseMode(String outputFile) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Run the genetic algorithm with the current settings.
     * @return
     */
    public Individual run() {
        population = new GAPopulation<T>(popSize);
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

    /**
     * Add a line of data to the verbose report.
     */
    private void addReport() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
