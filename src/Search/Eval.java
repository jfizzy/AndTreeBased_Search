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
import Schedule.NonLecture;
import Schedule.NonLectureSlot;
import Schedule.ScheduleManager;
import Schedule.Slot;
import Schedule.Meeting;
import Schedule.Meeting.Pair;

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
	private ScheduleManager schedule;
	
	/**
	 * Constructor without weights
	 * @param sd Search schedule
	 */
	public Eval(ScheduleManager sd) {
		this(sd,1,1,1,1);
	}
	
	/**
	 * Constructor with weights
	 * @param sd Search schedule
	 * @param min Weight for minimums
	 * @param pref Weight for preference
	 * @param pair Weight for pair
	 * @param secD Weight for section diff
	 */
	public Eval(ScheduleManager sd, int min, int pref, int pair, int secD) {
		
		pen_coursemin = 1;
		pen_labmin = 1;
		pen_notpaired = 1;
		pen_section = 1;
		
		wMin = min;
		wPref= pref;
		wPair = pair;
		wSecDiff = secD;
		
		schedule = sd;
	}
	
	/**
	 * Get evaluation
	 * @return Total evaluation of search instance
	 */
	public int getEval() {
		
		if (schedule != null) {
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
	 * @return Penalty for violating coursemin
	 */
	public int getCourseMinEval() {
    	int result = 0;
    	
    	// for each lecture slot in schedule
    	for (LectureSlot ls : schedule.getLectureSlots()) {
    		
        	// count how many lecture assignments have that slot
    		int count = 0;
        	for (Assignment a : schedule.getTimetable().getAssignments()) {
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
	 * @return Penalty for violating labmin
	 */
	public int getLabMinEval() {
    	int result = 0;
    	
    	// for each nonlecture slot in schedule
    	for (NonLectureSlot nls : schedule.getLabSlots()) {
    		
        	// count how many nonlecture assignments have that slot
    		int count = 0;
        	for (Assignment a : schedule.getTimetable().getAssignments()) {
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
	 * @return Penalty for violating preferences
	 */
	public int getPrefEval() {
		int result = 0;
		
		// for each assignment
		for (Assignment a : schedule.getTimetable().getAssignments()) {
			
			// for each preference entry of the assignment's meeting
			for (Pair<Slot,Integer> p : a.getM().getPreferences()) {
				
				// add preference value to penalty if slot doesn't match
				if (!a.getS().equals(p.first))
					result += p.second;
			}
		}
    	
    	// return weighted result
    	return wPref*result;
	}
	 
	/**
	 * Pair eval component
	 * penalty if courses not assigned to same slot
	 * @return Penalty for violating pairs
	 */
	public int getPairEval() {
		int result = 0;
		
		// for each assignment
		for (Assignment a : schedule.getTimetable().getAssignments()) {
			
			// for each pair entry of the assignment's meeting
			for (Meeting m : a.getM().getPaired()) {
				
				// for each other assignment
				for (Assignment b : schedule.getTimetable().getAssignments()) {
					if (a == b) continue;
					
					// skip if meeting doesn't match
					if (b.getM() != m) continue;
					
					// add penalty if slot doesn't match
					if (!a.getS().equals(b.getS()))
						result += pen_notpaired;
				}
			}
		}
    	
    	// return weighted result
		return wPair*result;
	}
	
	/**
	 * Section eval component
	 * penalty if courses of the same section are assigned to the same slot
	 * @return Penalty for violating section difference
	 */
	public int getSecDiffEval() {
		int result = 0;
		
		// TODO: there is definitely a cleaner/more efficient way to do this
		
		// for each course in the schedule
		for (Course c : schedule.getCourses()) {
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
				
					// for each assignment in the schedule
					for (Assignment a : schedule.getTimetable().getAssignments()) {
						
						// skip if assignment 1 doesn't match
						if (a.getM() != l1) continue;
						
						// for each other assignment
						for (Assignment b : schedule.getTimetable().getAssignments()) {
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
	
    /*
     * getters, setters, adders
     * 
     */
	
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
