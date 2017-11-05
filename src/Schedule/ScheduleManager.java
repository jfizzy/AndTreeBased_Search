/*
 * CPSC 433 Fall 2017
 * 
 * And-Tree-Based Scheduling Problem Solver
 * 
 * Created by:
 * 
 * Evan Loughlin
 * Geordie Tait
 * James MacIsaac
 * Luke Kissick
 * Sidney Shane Dizon
 */
package Schedule;

import java.util.ArrayList;

public class ScheduleManager {
    
    private TimeTable timeTable;
    private ArrayList<LectureSlot> lectureSlots;
    private ArrayList<NonLectureSlot> nonlectureSlots;

    public TimeTable getTimeTable() {
        return timeTable;
    }

    public ArrayList<LectureSlot> getLectureSlots() {
        return lectureSlots;
    }

    public ArrayList<NonLectureSlot> getNonlectureSlots() {
        return nonlectureSlots;
    }
    
    public ScheduleManager(ArrayList<Meeting> meetings, ArrayList<LectureSlot> lectureSlots, ArrayList<NonLectureSlot> nonlectureSlots) {
        this.timeTable = new TimeTable(meetings);
        this.lectureSlots = lectureSlots;
        this.nonlectureSlots = nonlectureSlots;
    }
}
