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

package Search;

import Schedule.Assignment;
import Schedule.Course;
import Schedule.Lecture;
import Schedule.LectureSlot;
import Schedule.Meeting;
import Schedule.NonLecture;
import Schedule.NonLectureSlot;
import Schedule.Section;
import Schedule.Slot;
import Schedule.TimeTable;
import Search.SearchData.Pair;
import Search.SearchData.Tri;

public class Eval {
	//private int evalMinFill;
	//private int evalPref;
	//private int evalPair;
	//private int evalSecDiff;
	
	private int pen_coursemin;
	private int pen_labmin;
	private int pen_notpaired;
	private int pen_section;
	
	private int wMin;		// shouldn't weights be decimals?
	private int wPref;
	private int wPair;
	private int wSecDiff;
	
	private SearchData sdata;
	
	//default constructor
	public Eval() {
		//defaults to zero evaluation values and equal weights
		this(null, 1,1,1,1);
	}
	
	public Eval(SearchData sd) {
		//defaults to zero evaluation values and equal weights
		this(sd, 1,1,1,1);
	}
	
	public Eval(SearchData sd, int min, int pref, int pair, int secD) {
		//evalMinFill = 0;
		//evalPref = 0;
		//evalPair = 0;
		//evalSecDiff = 0;
		
		pen_coursemin = 1;
		pen_labmin = 1;
		pen_notpaired = 1;
		pen_section = 1;
		
		wMin = min;
		wPref= pref;
		wPair = pair;
		wSecDiff = secD;
		
		sdata = sd;
	}
	
	//getters and setters
	public int getEval() {
		if (sdata != null)
			return getCourseMinEval() 
					+ getLabMinEval() 
					+ getPrefEval() 
					+ getPairEval() 
					+ getSecDiffEval();
		else return 0;
	}
	
	// Coursemin eval component
	public int getCourseMinEval() {
    	int result = 0;
    	
    	// go through all assignments
    	for (Assignment a : sdata.getTimetable().getAssignments()) {
    		
    		// skip if not a lecture
    		if (a.getM().getClass() != Lecture.class) continue;
    		if (a.getS().getClass() != LectureSlot.class) continue;
    		
    		// skip if unassigned
    		if (a.getS() == null) continue;
    		
    		// count how many have the same slot
    		int count = 0;
    		for (Assignment b : sdata.getTimetable().getAssignments()) {
    			if (a.getS() == b.getS())
    				count++;
    		}
    		
    		// add penalty for each course less than coursemin
    		LectureSlot ls = (LectureSlot) a.getS();
    		if (count < ls.getCourseMin()) {
    			result += (ls.getCourseMin() - count) * pen_coursemin;
    		}
    	}
    	
    	return wMin*result;		
	}
	
	// Labmin eval component
	public int getLabMinEval() {
    	int result = 0;
    	
    	// go through all assignments
    	for (Assignment a : sdata.getTimetable().getAssignments()) {
    		
    		// skip if lecture
    		if (a.getM().getClass() != NonLecture.class) continue;
    		if (a.getS().getClass() != NonLectureSlot.class) continue;
    		
    		// skip if unassigned
    		if (a.getS() == null) continue;
    		
    		// count how many have the same slot
    		int count = 0;
    		for (Assignment b : sdata.getTimetable().getAssignments()) {
    			if (a.getS() == b.getS())
    				count++;
    		}
    		
    		// add penalty for each course less than coursemin
    		NonLectureSlot ls = (NonLectureSlot) a.getS();
    		if (count < ls.getLabMin()) {
    			result += (ls.getLabMin() - count) * pen_labmin;
    		}
    	}
    	
    	return wMin*result;
	}
	
	// Preference eval component
	public int getPrefEval() {
		int result = 0;
    	
		// go through preference list
    	for (Tri<Meeting, Slot, Integer> t : sdata.getPreferences()) {
    		
    		// go through all assignments
    		for (Assignment a : sdata.getTimetable().getAssignments()) {
    			
    			// add penalty if the course is not assigned to the preferred slot
    			if (a.getM() == t.first && !a.getS().equals(t.second))
    				result += t.third;
    		}
    	}
    	
    	return wPref*result;
	}
	 
	// Pair eval component
	public int getPairEval() {
		int result = 0;
    	
		// go through list of paired courses
    	for (Pair<Meeting, Meeting> p : sdata.getPairs()) {
    		
    		// go through all assignments
    		for (Assignment a : sdata.getTimetable().getAssignments()) {
    			
    			// if a course matches the first of the pair
    			if (a.getM() == p.first){
    				
    				// go through all assignments again
    				for (Assignment b : sdata.getTimetable().getAssignments()) {
    					
    					// penalty if a course matches the second of the pair and has a different slot
    					if (b.getM() == p.second && !a.getS().equals(b.getS()))
    						result += pen_notpaired;
    				}
    			}
    		}
    	}
    	
		return wPair*result;
	}
	
	// Section eval component
	// TODO: there is definitely a cleaner/more efficient way to do this
	public int getSecDiffEval() {
		int result = 0;
		
		// for each course in the data
		for (Course c : sdata.getCourses()) {
			int nsections = c.getSections().size();
			
			// skip if there is only one section
			if (nsections < 2) continue;
			
			// for each section in the course
			for (int i = 0; i < nsections; i++) {
				Lecture l1 = c.getSections().get(i).getLecture();
				
				// for each other section in the course
				for (int j = 0; j < nsections; j++) {
					//System.out.println("other");
					if (i == j) continue;	// skip if same section
					Lecture l2 = c.getSections().get(j).getLecture();
				
					// for each assignment in the data
					for (Assignment a : sdata.getTimetable().getAssignments()) {
						
						// skip if assignment 1 doesn't match
						if (a.getM() != l1) continue;
						
						// for each other assignment
						for (Assignment b : sdata.getTimetable().getAssignments()) {
							if (a == b) continue;	// skip if same assignment
							
							// skip if assignment 2 doesn't match
							if (b.getM() != l2) continue;
							
							// add penalty if slots match
							if (a.getS().equals(b.getS()))
								result += pen_section;
						}
					}
				}
			}
		}
		
		return wSecDiff*result;
	}
	
	public void setMinWeight(int weight) {
		wMin = weight;
	}
	
	public void setPrefWeight(int weight) {
		wPref = weight;
	}
	
	public void setPairWeight(int weight) {
		wPair = weight;
	}
	
	public void setSecDiffWeight(int weight) {
		wSecDiff = weight;
	}
}
