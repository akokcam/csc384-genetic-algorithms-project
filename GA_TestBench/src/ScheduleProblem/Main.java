package ScheduleProblem;

public class Main {

    /**
     * This is the main method. It is used for testing right now.
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Hello buh");

        boolean loadedOK = Schedule.initialize("Instance Input Files\\testinitfile.txt");

        if (loadedOK) {
            Schedule.displayInfo();
        }
    }
}
