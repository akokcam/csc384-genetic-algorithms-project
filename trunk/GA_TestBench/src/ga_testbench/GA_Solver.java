package ga_testbench;

interface GA_Solver<T extends GA_Individual> {

    public void setPopulationSize(int size);

    public void setMaxGenerations(int max);

    public void setTimeLimit(float seconds);

    /* This might provide some statistics to the screen at every nth generation
     * or something like that
     * */
    public void setVerboseMode(boolean verbose);

    /* Actually do all of the serious work.
     * */
    public T run();
}
