package ScheduleProblem;

interface ScheduleInterface {

    Timing getTime(Course course);
//    Course getCourse(String courseName);
    TimeTable getTimes(Room room);
}
