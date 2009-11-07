package ScheduleProblem;

import java.util.Iterator;

interface TimeTableInterface {

    public void addTime(Timing time);

    public Timing getEarliest();

    public int numHits(Timing timing);

    public Iterator<Timing> iterator();
}
