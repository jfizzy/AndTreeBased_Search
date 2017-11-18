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

/**
 * @author 
 *
 */
public class TimeTable {
    
    private final ArrayList<Meeting> meetings; // must be ordered
    private ArrayList<Assignment> assignments;	// the list of assignments
    
    /**
     * default constructor
     */
    public TimeTable() {
    	meetings = null;
    	assignments = new ArrayList<Assignment>();
    }
    
    /**
     * constructor
     * @param meetings
     */
    public TimeTable(ArrayList<Meeting> meetings){
        this.meetings = meetings;
        assignments = new ArrayList<>();
        meetings.forEach((l) -> {
            assignments.add(new Assignment(l,null));
        });
    }
    
    /**
     * constructor for constr
     * @param a
     * @param tt
     */
    public TimeTable(Assignment a, TimeTable tt) {
    	this.meetings = null;//(ArrayList<Meeting>) tt.meetings.clone();
    	this.assignments = (ArrayList<Assignment>) tt.assignments.clone();
    	if (a != null)
    		this.assignments.add(a);
    }
    
    /**
     * print the timetable for debugging
     */
    public void printAssignments() {
    	
    	// for each assignment
    	for (Assignment a : assignments) {
    		System.out.print("Assigned: ");
    		
    		// if lecture
    		if (a.getM().getClass() == Lecture.class) {
    			Lecture l = (Lecture) a.getM();
    			System.out.print(l.getParentSection().getParentCourse().getDepartment()
    					+" "+l.getParentSection().getParentCourse().getNumber()
    					+" LEC"+l.getParentSection().getSectionNum());
    		}
    		
    		// if lab
    		else if (a.getM().getClass() == Lab.class) {
    			Lab lab = (Lab) a.getM();
    			System.out.print(lab.getDept()+" "+lab.getCourseNum()
    					+" LAB"+lab.getLabNum());
    		}
    		
    		// if tutorial
    		else if (a.getM().getClass() == Tutorial.class) {
    			Tutorial tut = (Tutorial) a.getM();
    			System.out.print(tut.getDept()+" "+tut.getCourseNum()
    					+" TUT"+tut.getTutNum());
    		}
    		
    		// slot
    		if (a.getS() != null) {
    			System.out.print(" --> "+a.getS().getDay()+" "
    					+a.getS().getHour()+":"+a.getS().getMinute()
    					+" - "+a.getS().getEndHour()+":"+a.getS().getEndMinute());
    		}
    		System.out.print("\n");
    	}
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
