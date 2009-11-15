package ga_testbench;

import java.util.List;

interface Population<T extends Individual> {


    // Constructor can't be declared here
//    public void Population(int size);

//    public void Population(List<SolutionInstance> individuals);

    // Find the best one in the current population
    public T getBest();



    /* This should be weighted by the normalized fitness. I can't think of how
     * to implement a nice quick way to select from the population besides
     * storing them in an augmented interval tree. But I just learned about
     * this in 265. Am I missing an obvious simple way to do it?
     * */
    //public SolutionInstance getIndividual();

    /* Some simple options. These should be enough for most purposes for the
     * population module. Just tells it what weights it should place on the
     * different things for the new generation
     */
//    public void setNextGenerationProportions(double copies, double mutations,
//            double crossovers, double randoms);

    // Generate a new population based on this one and the current settings.
    public void evolve();

    // Populations know how long since they started off random
    public int currentGenerationNumber();

    public double fitnessAverage();
    public double fitnessVariance();

    public int numEvaluations();


    // Should be private
    //public SolutionInstance crossPairWeighted();

    //public SolutionInstance crossPairUnweighted();

    /* This should be inplemented for multiple threads. Generally, fitness
     * evaluation is the most serious bottleneck for GA problems. It may be
     * quite easy to breed them, but many things need to be taken into account
     * when turning a whole individual into a single fitness number.
     */
    //public void evaluateAll();

    //public void runTournament();

}
