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

public class TimeTable {
    
    private final ArrayList<Meeting> meetings; // must be ordered
    private ArrayList<Assignment> assignments;
    
    
    public ArrayList<Assignment> getAssignments() {
        return assignments;
    }
    
    public TimeTable(ArrayList<Meeting> meetings){
        this.meetings = meetings;
        assignments = new ArrayList<>();
        meetings.forEach((l) -> {
            assignments.add(new Assignment(l,null));
        });
    }
    
    // Evaluation function for the timetable
    public int eval() {
    	int result = EvalCourseMin() + EvalLabsMin() + EvalNotPaired() + EvalPreference();
    	return result;
    }
    
    // Coursemin eval component
    private int EvalCourseMin() {
    	int pen_coursemin = 1;	// TODO this should be global or something
    	int result = 0;
    	
    	// go through all assignments
    	for (Assignment a : this.assignments) {
    		
    		// skip if not a lecture
    		if (a.getM().getClass() != Lecture.class) continue;
    		
    		// skip if unassigned
    		if (a.getS() == null) continue;
    		
    		// count how many have the same slot
    		int count = 0;
    		for (Assignment b : this.assignments) {
    			if (a.getS() == b.getS())
    				count++;
    		}
    		
    		// add penalty for each course less than coursemin
    		LectureSlot ls = (LectureSlot) a.getS();
    		if (count < ls.getCourseMin()) {
    			result += (ls.getCourseMin() - count) * pen_coursemin;
    		}
    	}
    	
    	return result;
    }
    
    // Labmin eval component
    private int EvalLabsMin() {
    	int pen_labmin = 1;		// TODO this should be global or something
    	int result = 0;
    	
    	// go through all assignments
    	for (Assignment a : this.assignments) {
    		
    		// skip if lecture
    		if (a.getM().getClass() != NonLecture.class) continue;
    		
    		// skip if unassigned
    		if (a.getS() == null) continue;
    		
    		// count how many have the same slot
    		int count = 0;
    		for (Assignment b : this.assignments) {
    			if (a.getS() == b.getS())
    				count++;
    		}
    		
    		// add penalty for each course less than coursemin
    		NonLectureSlot ls = (NonLectureSlot) a.getS();
    		if (count < ls.getLabMin()) {
    			result += (ls.getLabMin() - count) * pen_labmin;
    		}
    	}
    	
    	return result;
    }
    
    // Notpaired eval component
    private int EvalNotPaired() {
    	int pen_notpaired = 1;	// TODO this should be global or something
    	int result = 0;
    	
    	for (Assignment a : this.assignments) {
    		for (Assignment b : this.assignments) {
    			if (false && a.getS() != b.getS()) // replace false with pair(a,b)
    				result += pen_notpaired;
    		}
    	}
    	// TODO it would be better to loop through a list of pair(a,b) when we have that
    	
    	return result;
    }
    
    // Preference eval component
    private int EvalPreference() {
    	int result = 0;
    	
    	for (Assignment a : this.assignments) {
    		if (false && a.getS() == null) { // replace false with preference, null with the preferred slot
    			int preference = 1;	// replace with preference value
    			result += preference;
    		}
    	}
    	// TODO it would be better to loop through a list when we have that
    	
    	return result;
    }
    
}
