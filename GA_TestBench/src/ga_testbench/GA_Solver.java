package ga_testbench;



interface GA_Solver<T extends GA_Individual> {

    public void setPopulationSize(int size);

    public void setMaxGenerations(int max);

    public void setTimeLimit(float seconds);

    public void setVerboseMode(boolean verbose);

    public T run();
}
