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

import Schedule.*;

/**
 * Class for calculating how well a schedule fulfills soft constraints
 *
 */
public class Eval {
	
	/* How to use:
	 * --------------
	 * 
	 * Quick way:	schedule.eval();
	 * 				schedule.evalWith(assignment);
	 * 				- returns the eval without actually adding the assignment
	 * 
	 * 				To use weights, add them to the end of the parameters
	 * 				e.g. schedulemanager.evalWith(assignment, 0.5, 1.5, 10, 0);
	 * 
	 * Otherwise:
	 * 
	 * Total eval of a schedule:
	 * 		Eval e = new Eval(schedule);
	 * 		int value = e.getEval();
	 * 
	 * Total eval of a schedule if an assignment was added:
	 * --> this does not add the assignment to the schedule
	 * 		Eval e = new Eval(assignment, schedule);
	 * 		int value = e.getEval();
	 * 
	 * To use weights, add them to the end of the parameters
	 * e.g. Eval e = new Eval(schedule, 0.5, 1.5, 10, 0);
	 * 
	 * You can also check individual evals if you need:
	 * 		e.getCourseMinEval()
	 * 		e.getLabMinEval()
	 * 		e.getPrefEval()
	 * 		e.getPairEval()
	 * 		e.getSecDiffEval()
	 */
	
	// penalties
	private double pen_coursemin;
	private double pen_labmin;
	private double pen_notpaired;
	private double pen_section;
	
	// weights
	private double wCMin;
	private double wLMin;
	private double wPref;
	private double wPair;
	private double wSecDiff;
	
	// instance
	private Schedule schedule;
	
	/**
	 * Constructor without weights
	 * 
	 * @param schedule Schedule data
	 */
	public Eval(Schedule schedule) {
		this(schedule,1,1,1,1,1);
	}
	
	/**
	 * Constructor with weights
	 * 
	 * @param schedule Schedule data
	 * @param min Weight for minimums
	 * @param pref Weight for preference
	 * @param pair Weight for pair
	 * @param secD Weight for section diff
	 */
	public Eval(Schedule schedule, double cmin, double lmin, double pref, double pair, double secD) {
		
		pen_coursemin = 1;
		pen_labmin = 1;
		pen_notpaired = 1;
		pen_section = 1;
		
		wCMin = cmin;
		wLMin = lmin;
		wPref= pref;
		wPair = pair;
		wSecDiff = secD;
		
		this.schedule = schedule;
	}
	
	/**
	 * Constructor for getting evaluation if an assignment was added
	 * 
	 * @param a Assignment
	 * @param schedule Schedule data
	 */
	 // *** Use this to get eval of schedule if an assignment was added but WITHOUT 
	 // actually adding it to the timetable ***
	public Eval(Assignment a, Schedule schedule) {
		this(new Schedule(a, schedule));
	}
	
	/**
	 * Constructor for getting evaluation if an assignment was added
	 * (with weights)
	 * 
	 * @param a Assignment
	 * @param schedule Schedule data
	 */
	 // *** Use this to get eval of schedule if an assignment was added but WITHOUT 
	 // actually adding it to the timetable ***
	public Eval(Assignment a, Schedule schedule, double cmin,
			double lmin, double pref, double pair, double secD) {
		this(new Schedule(a, schedule), cmin, lmin, pref, pair, secD);
	}
	
	/**
	 * Get total evaluation
	 * 
	 * @return Total evaluation of search instance
	 */
	public int getEval() {
		return getCourseMinEval() 
				+ getLabMinEval() 
				+ getPrefEval() 
				+ getPairEval() 
				+ getSecDiffEval();
	}
	
	/**
	 * Print a breakdown of all the eval components and then the total
	 */
	public void printBreakdown() {
		System.out.println("Cmin = "+getCourseMinEval());
		System.out.println("Lmin = "+getLabMinEval());
		System.out.println("Pref = "+getPrefEval());
		System.out.println("Pair = "+getPairEval());
		System.out.println("Sect = "+getSecDiffEval());
		System.out.println("EVAL = "+getEval());
	}
	
	/**
	 * Get coursemin eval component
	 * (penalty if slot has less courses than min)
	 * 
	 * @return Penalty for violating coursemin
	 */
	public int getCourseMinEval() {
    	double result = 0.0;
    	
    	// for each lecture slot in schedule
    	for (LectureSlot ls : schedule.getLectureSlots()) {
    		
        	// count how many lecture assignments have that slot
    		int count = 0;
        	for (Assignment a : schedule.getAssignments()) {
        		
        		// skip if unassigned or not a lecture
        		if (a.getS() == null || a.getM().getClass() != Lecture.class) 
        			continue;
        		
        		// increment count if slot is equal
        		if (a.getS().equals(ls))
        			count++;
        	}
        	
        	// add penalty for each course less than coursemin
    		if (count < ls.getCourseMin())
    			result += (ls.getCourseMin() - count) * pen_coursemin;
    	}
    	
    	// return weighted result
    	return (int) (wCMin*result);		
	}
	
	/**
	 * Get labmin eval component
	 * (penalty if slot has less labs than min)
	 * 
	 * @return Penalty for violating labmin
	 */
	public int getLabMinEval() {
    	double result = 0.0;
    	
    	// for each nonlecture slot in schedule
    	for (NonLectureSlot nls : schedule.getNonLectureSlots()) {
    		
        	// count how many nonlecture assignments have that slot
    		int count = 0;
        	for (Assignment a : schedule.getAssignments()) {
        		
        		// skip if unassigned or not a nonlecture
        		if (a.getS() == null || a.getM().getClass() == Lecture.class) continue;
        		
        		// increment count if slots equal
        		if (a.getS().equals(nls))
        			count++;
        	}
        	
        	// add penalty for each nonlecture less than labmin
    		if (count < nls.getLabMin())
    			result += (nls.getLabMin() - count) * pen_labmin;
    	}
    	
    	// return weighted result
    	return (int) (wLMin*result);
	}
	
	/**
	 * Get preference eval component
	 * (penalty if course not assigned to preferred slot)
	 * 
	 * @return Penalty for violating preferences
	 */
	public int getPrefEval() {
		double result = 0.0;
		
		// for each assignment
		for (Assignment a : schedule.getAssignments()) {
			
			// skip if unassigned
			if (a.getS() == null) continue;
			
			// for each preference entry of the assignment's meeting
			for (Preference p : a.getM().getPreferences()) {
				
				// add preference value to penalty if slot doesn't match
				if (!a.getS().equals(p.getSlot()))
					result += p.getValue();
			}
		}
    	
    	// return weighted result
    	return (int) (wPref*result);
	}
	 
	/**
	 * Get pair eval component
	 * (penalty if courses not assigned to same slot)
	 * 
	 * @return Penalty for violating pairs
	 */
	public int getPairEval() {
		double result = 0.0;
		
		// TODO: the values are double what the spec says
		/*
		// for each assignment
		for (Assignment a : schedule.getAssignments()) {
			
			// skip if unassigned
			if (a.getS() == null) continue;
			
			// for each pair entry of the assignment's meeting
			for (Meeting m : a.getM().getPaired()) {
				
				// for each other assignment
				for (Assignment b : schedule.getAssignments()) {
					if (a == b) continue;
					
					// skip if unassigned or meeting doesn't match
					if (b.getS() == null || b.getM() != m) continue;
					
					// add penalty if slot doesn't match
					if (!a.getS().equals(b.getS())) {
						result += pen_notpaired;
						//System.out.println(a.getM().toString() + "   " + b.getM().toString());
					}
				}
			}
		}
		*/
		// TODO delete above
		
		// for each pair in the pairs list
		for (MeetingPair mp : schedule.getPairs()) {
				
			// skip if unassigned
			if (mp.getFirst().getAssignment() == null
					|| mp.getSecond().getAssignment() == null)
				continue;
			Slot s1 = mp.getFirst().getAssignment().getS();
			Slot s2 = mp.getSecond().getAssignment().getS();
			if (s1 == null || s2 == null)
				continue;
			
			// return false if slots overlap
			if (!s1.equals(s2))
				result += pen_notpaired;
		}
    	
    	// return weighted result
		return (int) (wPair*result);
	}
	
	/**
	 * Get section difference eval component
	 * (penalty if courses of the same section are assigned to the same slot)
	 * (department constraint)
	 * 
	 * @return Penalty for violating section difference
	 */
	public int getSecDiffEval() {
		double result = 0.0;
		
		// for each course in the schedule
		for (Course c : schedule.getCourses()) {
			int nsections = c.getSections().size();
			
			// skip if there is only one section
			if (nsections < 2) continue;
			
			// for each section in the course
			for (int i = 0; i < nsections; i++) {
				Lecture l1 = c.getSections().get(i).getLecture();
				
				// for each other section in the course
				for (int j = i+1; j < nsections; j++) {
					
					//if (i == j) continue;	// skip if same section
					Lecture l2 = c.getSections().get(j).getLecture();
				
					// for each assignment in the schedule
					for (Assignment a : schedule.getAssignments()) {
						
						// skip if unassigned or assignment 1 doesn't match
						if (a.getS() == null || a.getM() != l1) continue;
						
						// for each other assignment
						for (Assignment b : schedule.getAssignments()) {
							if (a == b) continue;	// skip if same assignment
							
							// skip if unassigned or assignment 2 doesn't match
							if (b.getS() == null || b.getM() != l2) continue;
							
							// add penalty if slots match
							if (a.getS().equals(b.getS()))
								result += pen_section;
						}
					}
				}
			}
		}
		
		// return weighted result
		return (int) (wSecDiff*result);
	}
	
    /*
     * Getters, setters, adders
     * 
     */
	
	/**
	 * Sets the weight values for the different evaluations
	 * 
	 * @param cmin
	 * @param lmin
	 * @param pref
	 * @param pair
	 * @param secdiff
	 */
	public void setWeights(double cmin, double lmin, double pref, double pair, double secdiff) {
		wCMin = cmin;
		wLMin = lmin;
		wPref = pref;
		wPair = pair;
		wSecDiff = secdiff;
	}
	
	/**
	 * Set weight for coursemin
	 * 
	 * @param weight Weight value
	 */
	public void setCourseMinWeight(double weight) {
		wCMin = weight;
	}	
	
	/**
	 * Set weight for labmin
	 * 
	 * @param weight Weight value
	 */
	public void setLabMinWeight(double weight) {
		wLMin = weight;
	}
	
	/**
	 * Set weight for preferences
	 * 
	 * @param weight Weight value
	 */
	public void setPrefWeight(double weight) {
		wPref = weight;
	}
	
	/**
	 * Set weight for pairs
	 * 
	 * @param weight Weight value
	 */
	public void setPairWeight(double weight) {
		wPair = weight;
	}
	
	/**
	 * Set weight for section difference
	 * 
	 * @param weight Weight value
	 */
	public void setSecDiffWeight(double weight) {
		wSecDiff = weight;
	}
	
	/**
	 * Get course min weight
	 * 
	 * @return Weight value
	 */
	public double getCourseMinWeight() {
		return wCMin;
	}
	
	/**
	 * Get lab min weight
	 * 
	 * @return Weight value
	 */
	public double getLabMinWeight() {
		return wLMin;
	}
	
	/**
	 * Get preference weight
	 * 
	 * @return Weight value
	 */
	public double getPrefWeight() {
		return wPref;
	}
	
	/**
	 * Get pair weight
	 * 
	 * @return Weight value
	 */
	public double getPairWeight() {
		return wPair;
	}
	
	/**
	 * Get section difference weight
	 * 
	 * @return Weight value
	 */
	public double getSecDiffWeight() {
		return wSecDiff;
	}
}
