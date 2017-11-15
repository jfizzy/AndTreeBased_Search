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
import Schedule.Section;

public class TimeTable {
    
    private final ArrayList<Meeting> meetings; // must be ordered
    private ArrayList<Assignment> assignments;
    
    public TimeTable() {
    	meetings = null;
    	assignments = new ArrayList<Assignment>();
    }
    
    public TimeTable(ArrayList<Meeting> meetings){
        this.meetings = meetings;
        assignments = new ArrayList<>();
        meetings.forEach((l) -> {
            assignments.add(new Assignment(l,null));
        });
    }
    
    public TimeTable(Assignment a, TimeTable tt) {
    	this.meetings = null;//(ArrayList<Meeting>) tt.meetings.clone();
    	this.assignments = (ArrayList<Assignment>) tt.assignments.clone();
    	if (a != null)
    		this.assignments.add(a);
    }
    
    // print the timetable for debugging
    public void printAssignments() {
    	
    	// for each assignment
    	for (Assignment a : assignments) {
    		System.out.print("Assigned: ");
    		
    		// if lecture
    		if (a.getM().getClass() == Lecture.class) {
    			Lecture l = (Lecture) a.getM();
    			System.out.print(l.getParentSection().getParentCourse().getDepartment());
    			System.out.print(" ");
    			System.out.print(l.getParentSection().getParentCourse().getNumber());
    			System.out.print(" LEC");
    			System.out.print(l.getParentSection().getSectionNum());
    		}
    		
    		// if nonlecture
    		else {//if (a.getM().getClass() == NonLecture.class) {
    			NonLecture nl = (NonLecture) a.getM();
    			System.out.print(nl.getDept());
    			System.out.print(" ");
    			System.out.print(nl.getCourseNum());
    			System.out.print(" LAB");
    			System.out.print(nl.getSectionNum());
    		}
    		
    		// slot
    		if (a.getS() != null) {
    			System.out.print(" --> ");
    			System.out.print(a.getS().getDay());
    			System.out.print(" ");
    			System.out.print(a.getS().getHour());
    			System.out.print(":");
    			System.out.print(a.getS().getMinute());
    		}
    		System.out.print("\n");
    	}
    }
    
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
