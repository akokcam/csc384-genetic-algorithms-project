package ScheduleProblem;

public class Main {

    /**
     * This is the main method. It is used for testing right now.
     * @param args
     */
    public static void main(String[] args) {

        boolean loadedOK = Schedule.initialize("Instance Input Files\\testinitfile.txt");

        if (loadedOK) {
            Schedule.displayInfo();
        }

        TimeTable x = new TimeTable();
        x.addTime(new Timing(1,2));
        x.addTime(new Timing(3,2));
        x.addTime(new Timing(2,1));
        x.addTime(new Timing(2,2));
        x.addTime(new Timing(1,2));
        x.addTime(new Timing(2,2));
        for (Timing t : x) {
            System.out.println(t);
        }

    }
}
