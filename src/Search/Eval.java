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
	 * 				e.g. schedule.evalWith(assignment, 0.5, 1.5, 10, 0);
	 * 
	 * Otherwise:
	 * 
	 * Total eval of a schedule:
	 * 		int value = Eval.getEval(schedule);
	 * 
	 * Total eval of a schedule if an assignment was added:
	 * --> this does not add the assignment to the schedule
	 * 		int value = Eval.getEval(schedule, assignment);
	 * 
	 * Weights are stored on the schedule
	 * 
	 * You can also check individual evals if you need:
	 * 		Eval.getCourseMinEval(schedule)
	 * 		Eval.getLabMinEval(schedule)
	 * 		Eval.getPrefEval(schedule)
	 * 		Eval.getPairEval(schedule)
	 * 		Eval.getSecDiffEval(schedule)
	 */
	
	/**
	 * Get total evaluation
	 * 
	 * @param s
	 * @return Total evaluation of search instance
	 */
	public static int getEval(Schedule s) {
		return getCourseMinEval(s) 
				+ getLabMinEval(s) 
				+ getPrefEval(s) 
				+ getPairEval(s) 
				+ getSecDiffEval(s);
	}
	
	/**
	 * Get total evaluation if an assignment was added to
	 * an existing schedule
	 * 
	 * @param s1
	 * @param a
	 * @return Total evaluation of search instance
	 */
	public static int getEval(Schedule s1, Assignment a) {
		Schedule s2 = new Schedule(a, s1);
		return getEval(s2);
	}
	
	/**
	 * Print a breakdown of all the eval components and then the total
	 * 
	 * @param s
	 */
	public static void printBreakdown(Schedule s) {
		System.out.println("Cmin = "+getCourseMinEval(s));
		System.out.println("Lmin = "+getLabMinEval(s));
		System.out.println("Pref = "+getPrefEval(s));
		System.out.println("Pair = "+getPairEval(s));
		System.out.println("Sect = "+getSecDiffEval(s));
		System.out.println("EVAL = "+getEval(s));
	}
	
	/**
	 * Get coursemin eval component
	 * (penalty if slot has less courses than min)
	 * 
	 * @param schedule
	 * @return Penalty for violating coursemin
	 */
	public static int getCourseMinEval(Schedule schedule) {
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
    			result += (ls.getCourseMin() - count) * schedule.getCourseMinPenalty();
    	}
    	
    	// return weighted result
    	return (int) (schedule.getCourseMinWeight()*result);		
	}
	
	/**
	 * Get labmin eval component
	 * (penalty if slot has less labs than min)
	 * 
	 * @param schedule
	 * @return Penalty for violating labmin
	 */
	public static int getLabMinEval(Schedule schedule) {
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
    			result += (nls.getLabMin() - count) * schedule.getLabMinPenalty();
    	}
    	
    	// return weighted result
    	return (int) (schedule.getLabMinWeight()*result);
	}
	
	/**
	 * Get preference eval component
	 * (penalty if course not assigned to preferred slot)
	 * 
	 * @param schedule
	 * @return Penalty for violating preferences
	 */
	public static int getPrefEval(Schedule schedule) {
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
    	return (int) (schedule.getPrefWeight()*result);
	}
	 
	/**
	 * Get pair eval component
	 * (penalty if courses not assigned to same slot)
	 * 
	 * @param schedule
	 * @return Penalty for violating pairs
	 */
	public static int getPairEval(Schedule schedule) {
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
				result += schedule.getPairPenalty();
		}
    	
    	// return weighted result
		return (int) (schedule.getPairWeight()*result);
	}
	
	/**
	 * Get section difference eval component
	 * (penalty if courses of the same section are assigned to the same slot)
	 * (department constraint)
	 * 
	 * @param schedule
	 * @return Penalty for violating section difference
	 */
	public static int getSecDiffEval(Schedule schedule) {
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
								result += schedule.getSecDiffPenalty();
						}
					}
				}
			}
		}
		
		// return weighted result
		return (int) (schedule.getSecDiffWeight()*result);
	}
}
