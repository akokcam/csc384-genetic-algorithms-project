package ga_testbench;

public abstract class Individual {

    // This should really be static
    public abstract Individual random();

    // Evaluate the instance, as it is
    public abstract float fitness();

    // Will be called once by the solver before any fitness tests are done
    public static void initialize(String filename) {
        // This must be implemented by the problem instance
        throw new UnsupportedOperationException("The initialize method must " +
                "be implemented by an Individual in whatever way it wants.");
    }

    // This sort of thing really calls for further subclassing
    //public float pairEvaluate(Individual other);

    /* mutate(1.0) should make the individual into a random instance. So if
     * there are n things that could sendibly change, n/difference of them
     * change on a call here
     * */
    public abstract void mutate(float difference);

    /* Breed two individuals, regarding them as equals, so we would expect half
     * of the "genes" from both to be present in the offsprint. Note "expect".
     * */
    public abstract Individual crossWith(Individual other);
}
