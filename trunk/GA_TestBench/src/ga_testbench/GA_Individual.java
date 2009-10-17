package ga_testbench;

interface GA_Individual {

    public GA_Individual random();

    public float evaluate();

    public float pairEvaluate(GA_Individual other);

    public void mutate(float difference);

    public GA_Individual crossWith(GA_Individual other);
}
