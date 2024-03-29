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
	 * 				schedule.evalWith(meeting, slot);
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
	 * 		int value = Eval.getEval(schedule, meeting, slot);
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
	 * @param s Schedule
	 * @return Total evaluation of search instance
	 */
	public static double getEval(Schedule s) {
		return getCourseMinEval(s, s.getMinWeight()) 
				+ getLabMinEval(s, s.getMinWeight()) 
				+ getPrefEval(s, s.getPrefWeight()) 
				+ getPairEval(s, s.getPairWeight())
				+ getSecDiffEval(s, s.getSecDiffWeight());
	}
	
	/**
	 * Get total evaluation if an assignment was added to
	 * an existing schedule
	 * 
	 * @param s1 Original schedule
	 * @param m Meeting to assign
	 * @param s Slot to assign
	 * @return Total evaluation of search instance
	 */
	public static double getEval(Schedule s1, Meeting m, Slot s) {
		Schedule s2 = new Schedule(s1, m, s);
		return getEval(s2);
	}
	
	/**
	 * Print a breakdown of all the eval components and then the total
	 * 
	 * @param s Schedule
	 */
	public static void printBreakdown(Schedule s) {
		String pen = "";
		if (s.getCourseMinPenalty() != 1)
			pen = " (pen_coursemin = "+s.getCourseMinPenalty()+")";
		System.out.println("Cmin = "+getCourseMinEval(s, 1)+" * "
				+s.getMinWeight()+" = "+getCourseMinEval(s, s.getMinWeight())+pen);
		
		pen = "";
		if (s.getLabMinPenalty() != 1)
			pen = " (pen_labmin = "+s.getLabMinPenalty()+")";
		System.out.println("Lmin = "+getLabMinEval(s, 1)+" * "
				+s.getMinWeight()+" = "+getLabMinEval(s, s.getMinWeight())+pen);
		
		System.out.println("Pref = "+getPrefEval(s, 1)+" * "
				+s.getPrefWeight()+" = "+getPrefEval(s, s.getPrefWeight()));
		
		pen = "";
		if (s.getPairPenalty() != 1)
			pen = " (pen_notpaired = "+s.getPairPenalty()+")";
		System.out.println("Pair = "+getPairEval(s, 1)+" * "
				+s.getPairWeight()+" = "+getPairEval(s, s.getPairWeight())+pen);
		
		pen = "";
		if (s.getSecDiffPenalty() != 1)
			pen = " (pen_section = "+s.getSecDiffPenalty()+")";
		System.out.println("Sect = "+getSecDiffEval(s, 1)+" * "
				+s.getSecDiffWeight()+" = "+getSecDiffEval(s, s.getSecDiffWeight())+pen);
		
		System.out.println("EVAL = "+getEval(s));
	}
	
	/**
	 * Get coursemin eval component
	 * (penalty if slot has less courses than min)
	 * 
	 * @param schedule Schedule
	 * @return Penalty for violating coursemin
	 */
	public static double getCourseMinEval(Schedule schedule, double weight) {
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
    	return schedule.getMinWeight()*result;		
	}
	
	/**
	 * Get labmin eval component
	 * (penalty if slot has less labs than min)
	 * 
	 * @param schedule Schedule
	 * @return Penalty for violating labmin
	 */
	public static double getLabMinEval(Schedule schedule, double weight) {
    	double result = 0.0;
    	
    	// for each nonlecture slot in schedule
    	for (NonLectureSlot nls : schedule.getNonLectureSlots()) {
    		
        	// count how many nonlecture assignments have that slot
    		int count = 0;
        	for (Assignment a : schedule.getAssignments()) {
        		
        		// skip if unassigned or not a nonlecture
        		if (a.getS() == null || a.getM().getClass() == Lecture.class) continue;
        		
        		// skip if 813/913
    			NonLecture nl1 = (NonLecture) a.getM();
    			if (nl1.isSpecial()) continue;
        		
        		// increment count if slots equal
        		if (a.getS().equals(nls))
        			count++;
        	}
        	
        	// add penalty for each nonlecture less than labmin
    		if (count < nls.getLabMin())
    			result += (nls.getLabMin() - count) * schedule.getLabMinPenalty();
    	}
    	
    	// return weighted result
    	return schedule.getMinWeight()*result;
	}
	
	/**
	 * Get preference eval component
	 * (penalty if course not assigned to preferred slot)
	 * 
	 * @param schedule Schedule
	 * @return Penalty for violating preferences
	 */
	public static double getPrefEval(Schedule schedule, double weight) {
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
    	return schedule.getPrefWeight()*result;
	}
	 
	/**
	 * Get pair eval component
	 * (penalty if courses not assigned to same slot)
	 * 
	 * @param schedule Schedule
	 * @return Penalty for violating pairs
	 */
	public static double getPairEval(Schedule schedule, double weight) {
		double result = 0.0;
		
		// for each assignment
		for (Assignment a : schedule.getAssignments()) {
			
			// skip if unassigned
			if (a.getS() == null) continue;
			
			// for each pair entry of the assignment's meeting
			for (Meeting m : a.getM().getPaired()) {
				
				Assignment b = m.getAssignment();
				if (b == null || b.getS() == null) continue;
					
				// add penalty if slot doesn't match
				// (this has to be 0.5 because each member of the pair is counted twice)
				if (!a.getS().equals(b.getS())) {
					result += 0.5 * schedule.getPairPenalty();
				}
			}
		}
		
    	// return weighted result
		return schedule.getPairWeight()*result;
	}
	
	/**
	 * Get section difference eval component
	 * (penalty if courses of the same section are assigned to the same slot)
	 * (department constraint)
	 * 
	 * @param schedule Schedule
	 * @return Penalty for violating section difference
	 */
	public static double getSecDiffEval(Schedule schedule, double weight) {
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
		return schedule.getSecDiffWeight()*result;
	}
}
