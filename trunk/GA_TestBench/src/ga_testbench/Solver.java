package ga_testbench;

interface Solver<T extends Individual> {

    public void setPopulationSize(int size);

    public void setMaxGenerations(int max);

    public void setTimeLimit(double seconds);

    public void setNextGenerationProportions(double copies, double mutations,
            double crossovers, double randoms);

    /* This might provide some statistics to the screen at every nth generation
     * or something like that
     */
    public void setVerboseMode(String outputFile);

    /* Actually do all of the serious work.
     */
    public T run();
}
