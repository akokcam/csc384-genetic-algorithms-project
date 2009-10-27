package ScheduleProblem;

/**
 * students and courses are both evaluators. They can tell you how much they
 * like a schedule.
 * @author Dave
 */
interface Evaluator {

    public float evaluate(Schedule s);
}
