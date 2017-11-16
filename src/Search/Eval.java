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

/**
 * Object for calculating how well a schedule fulfills soft constraints
 *
 */
public class Eval {
	
	// penalties
	private int pen_coursemin;
	private int pen_labmin;
	private int pen_notpaired;
	private int pen_section;
	
	// weights
	private int wMin;		// shouldn't weights be decimals?
	private int wPref;
	private int wPair;
	private int wSecDiff;
	
	// instance
	private SearchData data;
	
	/**
	 * constructor without weights
	 * @param sd
	 */
	public Eval(SearchData sd) {
		this(sd,1,1,1,1);
	}
	
	/**
	 * constructor with weights
	 * @param sd
	 * @param min
	 * @param pref
	 * @param pair
	 * @param secD
	 */
	public Eval(SearchData sd, int min, int pref, int pair, int secD) {
		
		pen_coursemin = 1;
		pen_labmin = 1;
		pen_notpaired = 1;
		pen_section = 1;
		
		wMin = min;
		wPref= pref;
		wPair = pair;
		wSecDiff = secD;
		
		data = sd;
	}
	
	/**
	 * returns total evaluation
	 * @return
	 */
	public int getEval() {
		
		if (data != null) {
			return getCourseMinEval() 
					+ getLabMinEval() 
					+ getPrefEval() 
					+ getPairEval() 
					+ getSecDiffEval();
		}
		else return 0;
	}
	
	/**
	 * Coursemin eval component
	 * penalty if slot has less courses than min
	 * @return
	 */
	public int getCourseMinEval() {
    	int result = 0;
    	
    	// for each lecture slot in data
    	for (LectureSlot ls : data.getLectureSlots()) {
    		
        	// count how many lecture assignments have that slot
    		int count = 0;
        	for (Assignment a : data.getTimetable().getAssignments()) {
        		if (a.getM().getClass() != Lecture.class) continue;
        		if (a.getS().equals(ls))
        			count++;
        	}
        	
        	// add penalty for each course less than coursemin
    		if (count < ls.getCourseMin()) {
    			result += (ls.getCourseMin() - count) * pen_coursemin;
    		}
    	}
    	
    	// return weighted result
    	return wMin*result;		
	}
	
	/**
	 * Labmin eval component
	 * penalty if slot has less labs than min
	 * @return
	 */
	public int getLabMinEval() {
    	int result = 0;
    	
    	// for each nonlecture slot in data
    	for (NonLectureSlot nls : data.getLabSlots()) {
    		
        	// count how many nonlecture assignments have that slot
    		int count = 0;
        	for (Assignment a : data.getTimetable().getAssignments()) {
        		if (a.getM().getClass() != NonLecture.class) continue;
        		if (a.getS().equals(nls))
        			count++;
        	}
        	
        	// add penalty for each nonlecture less than labmin
    		if (count < nls.getLabMin()) {
    			result += (nls.getLabMin() - count) * pen_labmin;
    		}
    	}
    	
    	// return weighted result
    	return wMin*result;
	}
	
	/**
	 * Preference eval component
	 * penalty if course not assigned to preferred slot
	 * @return
	 */
	public int getPrefEval() {
		int result = 0;
    	
		// for each preference in data
//    	for (Tri<Meeting, Slot, Integer> t : data.getPreferences()) {
//    		
//    		// for each assignment
//    		for (Assignment a : data.getTimetable().getAssignments()) {
//    			
//    			// add penalty if the course is not assigned to the preferred slot
//    			if (a.getM() == t.first && !a.getS().equals(t.second))
//    				result += t.third;
//    		}
//    	}
    	
    	// return weighted result
    	return wPref*result;
	}
	 
	/**
	 * Pair eval component
	 * penalty if courses not assigned to same slot
	 * @return
	 */
	public int getPairEval() {
		int result = 0;
    	
		// for each pair of courses in data
//    	for (Pair<Meeting, Meeting> p : data.getPairs()) {
//    		
//    		// for each assignment
//    		for (Assignment a : data.getTimetable().getAssignments()) {
//    			
//    			// if a course matches the first of the pair
//    			if (a.getM() == p.first){
//    				
//    				// for each other assignment
//    				for (Assignment b : data.getTimetable().getAssignments()) {
//    					if (a == b) continue; // skip if same
//    					
//    					// add penalty if a course matches the second of the pair and has a different slot
//    					if (b.getM() == p.second && !a.getS().equals(b.getS()))
//    						result += pen_notpaired;
//    				}
//    			}
//    		}
//    	}
    	
    	// return weighted result
		return wPair*result;
	}
	
	/**
	 * Section eval component
	 * penalty if courses of the same section are assigned to the same slot
	 * TODO: there is definitely a cleaner/more efficient way to do this
	 * @return
	 */
	public int getSecDiffEval() {
		int result = 0;
		
		// for each course in the data
		for (Course c : data.getCourses()) {
			int nsections = c.getSections().size();
			
			// skip if there is only one section
			if (nsections < 2) continue;
			
			// for each section in the course
			for (int i = 0; i < nsections; i++) {
				Lecture l1 = c.getSections().get(i).getLecture();
				
				// for each other section in the course
				for (int j = 0; j < nsections; j++) {
					
					if (i == j) continue;	// skip if same section
					Lecture l2 = c.getSections().get(j).getLecture();
				
					// for each assignment in the data
					for (Assignment a : data.getTimetable().getAssignments()) {
						
						// skip if assignment 1 doesn't match
						if (a.getM() != l1) continue;
						
						// for each other assignment
						for (Assignment b : data.getTimetable().getAssignments()) {
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
	
	// getters and setters
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
