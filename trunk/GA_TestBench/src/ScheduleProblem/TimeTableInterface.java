package ScheduleProblem;

interface TimeTableInterface {

    public void addTime(Timing time);

    public Timing getEarliest();

    public Timing getNext(Timing timing);
}
