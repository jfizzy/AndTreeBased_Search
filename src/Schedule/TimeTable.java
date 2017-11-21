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

/**
 * Object representing a schedule of assigned meetings to slots
 *
 * @author
 *
 */
public class TimeTable {

    private final ArrayList<Assignment> assignments;	// the list of assignments
    private ArrayList<LectureSlot> lectureSlots;
    private ArrayList<NonLectureSlot> nonLectureSlots;

    /**
     * Default constructor
     */
    public TimeTable() {
        assignments = new ArrayList<>();
        lectureSlots = new ArrayList<>();
        nonLectureSlots = new ArrayList<>();
    }

    /**
     * Constructor
     *
     * @param meetings List of meetings
     * @param lectureSlots
     * @param nonLectureSlots
     */
    public TimeTable(ArrayList<Meeting> meetings, ArrayList<LectureSlot> lectureSlots, ArrayList<NonLectureSlot> nonLectureSlots) {
        this.lectureSlots = lectureSlots;
        this.nonLectureSlots = nonLectureSlots;
        this.assignments = new ArrayList<>();
        meetings.forEach((m) -> {
            assignments.add(new Assignment(m, null));
        });
    }

    /**
     * Constructor for Constr
     *
     * @param a Assignment to check
     * @param tt Existing timetable
     */
    public TimeTable(Assignment a, TimeTable tt) {
        this.assignments = (ArrayList<Assignment>) tt.assignments.clone();
        if (a != null) {
            this.assignments.add(a);
        }
    }

    /**
     * Print the timetable for debugging
     */
    public void printAssignments() {

        // for each assignment
        assignments.stream().map((a) -> {
            System.out.print("Assigned: ");
            return a;
        }).map((a) -> {
            // if lecture
            if (a.getM().getClass() == Lecture.class) {
                Lecture l = (Lecture) a.getM();
                System.out.print(l.toString());
            } // if lab
            else if (a.getM().getClass() == Lab.class) {
                Lab lab = (Lab) a.getM();
                System.out.print(lab.toString()); // using toString to print
            } // if tutorial
            else if (a.getM().getClass() == Tutorial.class) {
                Tutorial tut = (Tutorial) a.getM();
                System.out.print(tut.toString()); // using toString to print
            }
            return a;
        }).map((a) -> {
            // slot
            if (a.getS() != null) {
                System.out.format(" --> %s %02d:%02d - %02d:%02d",
                        a.getS().getDay(), a.getS().getHour(), a.getS().getMinute(),
                        a.getS().getEndHour(), a.getS().getEndMinute());
            }else{
                System.out.print(" --> No Slot");
            }
            return a;
        }).forEachOrdered((_item) -> {
            System.out.print("\n");
        });
    }

    /*
     *  getters and setters
     */
    public ArrayList<Assignment> getAssignments() {
        return assignments;
    }

    public void addAssignment(Assignment a) {
        assignments.add(a);
    }

    public void clearAssignments() {
        assignments.clear();
    }

}
