package ga_testbench;

interface GA_Population<ProblemType extends GA_Individual> {

    public ProblemType getBest();

    public void evolve();

    public int currentGeneration();

    public ProblemType crossPairWeighted();

    public ProblemType crossPairUnweighted();

    public void evaluateAll();

    public void runTournament();

    public void GA_Population(int size);
}
