package ga_testbench;

public abstract class Individual {

//    // This should really be static
//    public Individual random() {
//        // This must be implemented by the problem instance
//        throw new UnsupportedOperationException("The random method must " +
//                "be implemented by the Individual type class.");
//    }

    // Evaluate the instance, as it is
    public abstract double fitness();

//    // Will be called once by the solver before any fitness tests are done
//    public boolean initialize(String filename) {
//        // This must be implemented by the problem instance
//        throw new UnsupportedOperationException("The initialize method must " +
//                "be implemented by the Individual class.");
//    }

    // This sort of thing really calls for further subclassing
    //public double pairEvaluate(Individual other);

    /* mutate(1.0) should make the individual into a random instance. So if
     * there are n things that could sendibly change, n/difference of them
     * change on a call here
     * */
    public abstract Individual mutate(double difference);

    /* Breed two individuals, regarding them as equals, so we would expect half
     * of the "genes" from both to be present in the offsprint. Note "expect".
     * */
    public abstract Individual crossWith(Individual other);
}
