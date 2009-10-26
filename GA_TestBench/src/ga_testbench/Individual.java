package ga_testbench;

abstract class Individual {

    // This should really be static
    public abstract Individual random();

    // Evaluate the instance, as it is
    public abstract float fitness();

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
