package ga_testbench;

interface GA_Individual {

    // This should really be static
    public GA_Individual random();

    // Evaluate the instance, as it is
    public float fitness();

    // This sort of thing really calls for further subclassing
    public float pairEvaluate(GA_Individual other);

    /* mutate(1.0) should make the individual into a random instance. So if
     * there are n things that could sendibly change, n/difference of them
     * change on a call here
     * */
    public void mutate(float difference);

    /* Breed two individuals, regarding them as equals, so we would expect half
     * of the "genes" from both to be present in the offsprint. Note "expect".
     * */
    public GA_Individual crossWith(GA_Individual other);
}
