package ga_testbench;

interface GA_Population<SolutionInstance extends GA_Individual> {

    public SolutionInstance getBest();

    /* This should be weighted by the normalized fitness. I can't think of how
     * to implement a nice quick way to select from the population besides
     * storing them in an augmented interval tree. But I just learned about
     * this in 265. Am I missing an obvious simple way to do it?
     * */
    public SolutionInstance getRandom();

    /* Some simple options. These should be enough for most purposes for the
     * population module. Just tells it what weights it should place on the
     * different things for the new generation
     */
    public void setNextGenerationProportions(float copies, float mutations,
            float crossovers, float randoms);

    public void evolve();

    public int currentGenerationNumber();

    public SolutionInstance crossPairWeighted();

    public SolutionInstance crossPairUnweighted();

    /* This should be inplemented for multiple threads. Generally, fitness
     * evaluation is the most serious bottleneck for GA problems. It may be
     * quite easy to breed them, but many things need to be taken into account
     * when turning a whole individual into a single fitness number.
     */
    public void evaluateAll();

    public void runTournament();

    public void GA_Population(int size);
}
