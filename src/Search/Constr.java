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

// TODO labs different constraint
// TODO confirm evening check works
// TODO 813/913 constraint

/**
 * Class for determining whether a schedule satisfies hard constraints
 *
 */
public class Constr {
	
	/* How to use:
	 * --------------
	 * 
	 * Quick way:	schedule.isValid()
	 * 				schedule.isValidWith(meeting, slot)
	 * 				- checks if valid without actually adding the assignment
	 * 
	 * Otherwise:
	 * 
	 * Constr.check(schedule) == true if schedule meets all hard constraints
	 * 
	 * Constr.check(schedule, meeting, slot) == true if adding assignment to schedule 
	 * 											would meet all hard constraints
	 * 		--> this does not add the assignment to the schedule
	 * 
	 * You can also check individual constraints if you need:
	 * 		Constr.checkCourseMax(schedule)
	 * 		Constr.checkLabMax(schedule)
	 * 		Constr.checkLabsDifferent(schedule)
	 * 		Constr.checkNoncompatible(schedule)
	 * 		Constr.checkPartassign(schedule)
	 * 		Constr.checkUnwanted(schedule)
	 * 		Constr.checkEveningClasses(schedule)
	 * 		Constr.checkOver500Classes(schedule)
	 * 		Constr.checkSpecificTimes(schedule)
	 * 		Constr.checkSpecialClasses(schedule)
	 */

	/**
	 * Check all hard constraints
	 * 
	 * @param s Schedule
	 * @return True if all hard constraints are satisfied
	 */
	public static boolean check(Schedule s) {
		
		return checkEveningClasses(s) && checkSpecificTimes(s) && checkNoncompatible(s)
				&& checkUnwanted(s) && checkCourseMax(s) && checkLabMax(s) 
				&& checkLabsDifferent(s) //&& checkPartassign(s) 
				&& checkOver500Classes(s) && checkSpecialClasses(s);
	}
	
	/**
	 * Check all hard constraints if an assignment was added
	 * 
	 * @param s1 Original schedule
	 * @param m Meeting to assign
	 * @param s Slot to assign
	 * @return True if all hard constraints are satisfied
	 */
	public static boolean check(Schedule s1, Meeting m, Slot s) {
		
		Schedule s2 = new Schedule(s1, m, s);
		return check(s2);
	}
	
	/**
	 * Prints which constraints were violated for debugging
	 * 
	 * @param s Schedule
	 */
	public static void printViolations(Schedule s) {
		if (!checkCourseMax(s))
			System.out.println("Coursemax violated");
		if (!checkLabMax(s))
			System.out.println("Labmax violated");
		if (!checkLabsDifferent(s))
			System.out.println("Labs different from lecture violated");
		if (!checkNoncompatible(s))
			System.out.println("Noncompatible violated");
		if (!checkPartassign(s))
			System.out.println("Partassign violated");
		if (!checkUnwanted(s))
			System.out.println("Unwanted violated");
		if (!checkEveningClasses(s))
			System.out.println("Evening classes violated");
		if (!checkOver500Classes(s))
			System.out.println("No overlap in >500 courses violated");
		if (!checkSpecificTimes(s))
			System.out.println("Tuesday 11:00 constraint violated");
		if (!checkSpecialClasses(s))
			System.out.println("CPSC 813/913 constraint violated");
	}
	
	/*
	 *  Individual hard constraints
	 */
	
	/**
	 * Check course maximum constraint
	 * 
	 * @param schedule Schedule
	 * @return True if course max constraint is met
	 */
	public static boolean checkCourseMax(Schedule schedule) {
		
		// for each assignment
		for (Assignment a : schedule.getAssignments()) {
			
			// skip if not a lecture or not assigned
			if (a.getS() == null 
					|| a.getM().getClass() != Lecture.class
					|| a.getS().getClass() != LectureSlot.class) 
				continue;
			
			// count how many others have the same slot
			int count = 1;
			for (Assignment b : schedule.getAssignments()) {
				if (a == b) continue; // skip if same
				
				// skip if not a lecture or not assigned
				if (b.getS() == null 
						|| b.getM().getClass() != Lecture.class
						|| b.getS().getClass() != LectureSlot.class)
					continue;
				
				// increment count if slots match
				if (a.getS().equals(b.getS()))
					count++;
			}
			
			// return false if count is greater than slot's coursemax
			LectureSlot ls = (LectureSlot) a.getS();
			if (count > ls.getCourseMax()) 
				return false;
		}
		
		// if this is reached the constraint is satisfied
		return true;
	}
	
	/**
	 * Check lab maximum constraint
	 * 
	 * @param schedule Schedule
	 * @return True if lab max constraint is met
	 */
	public static boolean checkLabMax(Schedule schedule) {
		
		// for each assignment
		for (Assignment a : schedule.getAssignments()) {
			
			// skip if not nonlecture or not assigned
			if (a.getS() == null 
					|| a.getM().getClass() == Lecture.class
					|| a.getS().getClass() == LectureSlot.class) 
				continue;
			
			// count how many others have the same slot
			int count = 1;
			for (Assignment b : schedule.getAssignments()) {
				if (a == b) continue; // skip if same
				
				// skip if not nonlecture or not assigned
				if (b.getS() == null 
						|| b.getM().getClass() == Lecture.class
						|| b.getS().getClass() == LectureSlot.class) 
					continue;
				
				// increment count if slots match
				if (a.getS().equals(b.getS()))
					count++;
			}
			
			// return false if count is greater than slot's labmax
			NonLectureSlot ls = (NonLectureSlot) a.getS();
			if (count > ls.getLabMax()) 
				return false;
		}
		
		// if this is reached the constraint is satisfied
		return true;
	}
	
	/**
	 * Check if labs not assigned to same slots as lectures of the same section
	 * 
	 * @param schedule Schedule
	 * @return True if labs assignment constraint is met
	 */
	public static boolean checkLabsDifferent(Schedule schedule) {
		
		// for each assignment
		for (Assignment a : schedule.getAssignments()) {
			
			// skip if not a nonlecture or not assigned
			if (a.getS() == null 
					|| a.getM().getClass() == Lecture.class
					|| a.getS().getClass() == LectureSlot.class) 
				continue;
			
			// for each other assignment
			for (Assignment b : schedule.getAssignments()) {
				if (a == b) continue; // skip if same
				
				// skip if unassigned, not a lecture or slot is different
				if (b.getS() == null 
						|| b.getM().getClass() != Lecture.class
						|| b.getS().getClass() != LectureSlot.class
						|| !a.getS().overlaps(b.getS())) 
					continue;
				
				// return false if section is the same, or open lab/tut matches first section
				Lecture l = (Lecture) b.getM();
				Section lsec = l.getParentSection();
				NonLecture nl = (NonLecture) a.getM();
				Section nlsec = nl.getParentSection();
				boolean open = false;
				if (nlsec == null)
					open = true;
				if ((!open && lsec.equals(nlsec)) 
						|| (open && nl.getParentCourse().equals(lsec.getParentCourse())))
					return false;
			}
		}
		
		// if this is reached the constraint is satisfied
		return true;
	}
	
	/**
	 * Check non-compatible constraint:
	 * non-compatible(a,b) implies assign a != assign b
	 * 
	 * @param schedule Schedule
	 * @return True if non-compatible constraint is met
	 */
	public static boolean checkNoncompatible(Schedule schedule) {
		
		// for each assignment
		for (Assignment a : schedule.getAssignments()) {
			
			// for each noncompatible entry of the assignment's meeting
			for (Meeting m : a.getM().getIncompatibility()) {
				
				// for each other assignment
				for (Assignment b : schedule.getAssignments()) {
					if (a == b) continue; // skip if same
					
					// skip if meeting doesn't match
					if (b.getM() != m) continue;
					
					// return false if slots match
					if (a.getS() != null && a.getS().overlaps(b.getS()))
						return false;
				}
			}
		}
		
		// if this is reached the constraint is satisfied
		return true;
	}
	
	/**
	 * Check partassign constraint:
	 * partassign a implies assign a
	 * 
	 * @param schedule Schedule
	 * @return True if partassign constraint is met
	 */
	public static boolean checkPartassign(Schedule schedule) {
		
		// for each assignment
		for (Assignment a : schedule.getAssignments()) {
			Slot partassign = a.getM().getPartassign();
			
			// skip if unassigned or partassign is not set
			if (a.getS() == null || partassign == null) continue;
			
			// return false if slot doesn't match
			if (!a.getS().equals(partassign))
				return false;
		}
		
		// if this is reached the constraint is satisfied
		return true;
	}
	
	/**
	 * Check unwanted constraint:
	 * unwanted a,s implies assign a != s
	 * 
	 * @param schedule Schedule
	 * @return True if unwanted constraint is met
	 */
	public static boolean checkUnwanted(Schedule schedule) {
		
		// for each assignment
		for (Assignment a : schedule.getAssignments()) {
			
			// skip if unassigned
			if (a.getS() == null) continue;
			
			// for each unwanted entry of the assignment's meeting
			for (Slot s : a.getM().getUnwanted()) {
				
				// return false if slot matches
				if (a.getS().equals(s))
					return false;
			}
		}
		
		// if this is reached the constraint is satisfied
		return true;
	}
	
	/**
	 * Check if sections with numbers starting with 9 get evening slots
	 * (department constraint)
	 * 
	 * @param schedule Schedule
	 * @return True if evening classes constraint is met
	 */
	public static boolean checkEveningClasses(Schedule schedule) {
		
		// for each assignment
		for (Assignment a : schedule.getAssignments()) {
			
			// skip if unassigned
			if (a.getS() == null) continue;
			
			// get section number
			String snum = null;
			boolean evening = false;
			if (a.getM().getClass() == Lecture.class) {
				Lecture l = (Lecture) a.getM();
				snum = l.getParentSection().getSectionNum();
				evening = l.getParentSection().isEvening();
			}
			else if (a.getM().getClass() == Lab.class) {
				Lab nl = (Lab) a.getM();
				snum = nl.getLabNum();
				evening = nl.isEvening();
			}
			else if (a.getM().getClass() == Tutorial.class) {
				Tutorial nl = (Tutorial) a.getM();
				snum = nl.getTutNum();
				evening = nl.isEvening();
			}
			else continue;
			
			// check section number begins with 9
			if (evening) { // TODO confirm this works properly
				
				// return false if not scheduled in the evening
				if (a.getS().getHour() < 18)
					return false;
			}
		}
		
		// if this is reached the constraint is satisfied
		return true;
	}
	
	/**
	 * Check if 500 level classes don't have the same slot
	 * (department constraint)
	 * 
	 * @param schedule Schedule
	 * @return True if 500-level constraint is met
	 */
	public static boolean checkOver500Classes(Schedule schedule) {
		
		// for each assignment
		for (Assignment a : schedule.getAssignments()) {
			
			// skip if unassigned or not a lecture
			if (a.getS() == null 
					|| a.getM().getClass() != Lecture.class
					|| a.getS().getClass() != LectureSlot.class) 
				continue;
			
			// skip if course number < 500
			Lecture l1 = (Lecture) a.getM();
			int cnum1 = Integer.parseInt(l1.getParentSection().getParentCourse().getNumber());
			if (cnum1 < 500 || cnum1 > 599) continue;
			
			// for each other assignment
			for (Assignment b : schedule.getAssignments()) {
				if (a == b) continue;
				
				// skip if unassigned, not a lecture or slot is different
				if (b.getS() == null 
						|| b.getM().getClass() != Lecture.class
						|| b.getS().getClass() != LectureSlot.class
						|| !a.getS().overlaps(b.getS())) 
					continue;
				
				// skip if course number < 500
				Lecture l2 = (Lecture) b.getM();
				int cnum2 = Integer.parseInt(l2.getParentSection().getParentCourse().getNumber());
				if (cnum2 < 500 || cnum2 > 599) continue;
				
				// if this is reached classes >500 have overlap
				return false;
			}
		}
		
		// if this is reached the constraint is satisfied
		return true;
	}
	
	/**
	 * Check if no courses Tuesday 11:00-12:30
	 * (department constraint)
	 * 
	 * @param schedule Schedule
	 * @return True if Tues 11:00 constraint is met
	 */
	public static boolean checkSpecificTimes(Schedule schedule) {
		
		// for each assignment
		for (Assignment a : schedule.getAssignments()) {
			
			// skip if unassigned or not a lecture
			if (a.getS() == null || a.getM().getClass() != Lecture.class) 
				continue;
			
			// return false if slot is Tuesday at 11:00
			if (a.getS().getDay().equals("TU") && a.getS().getHour() == 11)
				return false;
		}
		
		// if this is reached the constraint is satisfied
		return true;
	}
	
	/**
	 * Check special requirements for CPSC 813/913
	 * (department constraint)
	 * 
	 * @param schedule Schedule
	 * @return True if CPSC813/913 constraint is met
	 */
	public static boolean checkSpecialClasses(Schedule schedule) {
		
		// for each assignment
		for (Assignment a : schedule.getAssignments()) {
			String first = "";
			String second = "";
			
			// skip if unassigned
			if (a.getS() == null) continue;
			
			// get course number
			if (a.getM().getClass() == Lecture.class) {	// if lecture
				Lecture l = (Lecture) a.getM();
				if (!l.getParentSection().getParentCourse().getDepartment().equals("CPSC"))
					continue;
				first = l.getParentSection().getParentCourse().getNumber();
			}
			else if (a.getM().getClass() == NonLecture.class) {	// if nonlecture
				NonLecture nl = (NonLecture) a.getM();
				if (!nl.getDept().equals("CPSC"))
					continue;
				first = nl.getCourseNum();
				
			}
			else continue;
			
			// skip if not CPSC 813 or 913
			if (!first.equals("813") && !first.equals("913"))
				continue;
			
			// return false if not scheduled TuTh 18:00
			if ((!a.getS().getDay().equals("TU") || a.getS().getHour() != 18 || a.getS().getMinute() != 0))
				return false;
			
			// cpsc 813 not allowed to overlap any sections/tuts of 313 or other courses not allowed to overlap 313
			// cpsc 913 not allowed to overlap any sections/tuts of 413 or other courses not allowed to overlap 413
			
			// for each other assignment
			for (Assignment b : schedule.getAssignments()) {
				if (a == b) continue;
				
				// skip if unassigned
				if (b.getS() == null) continue;
				
				// get course number
				if (b.getM().getClass() == Lecture.class) { // if lecture
					Lecture l = (Lecture) b.getM();
					if (!l.getParentSection().getParentCourse().getDepartment().equals("CPSC"))
						continue;
					second = l.getParentSection().getParentCourse().getNumber();
				}
				else if (b.getM().getClass() == NonLecture.class) { // if nonlecture
					NonLecture nl = (NonLecture) b.getM();
					if (!nl.getDept().equals("CPSC"))
						continue;
					second = nl.getCourseNum();
				}
				else continue;
				
				// skip if not CPSC 313/413
				if (!second.equals("313") && !second.equals("413"))
					continue;
				
				// return false if 313 overlaps 813 or 413 overlaps 913
				if (first.equals("813") && second.equals("313")) {
					if (a.getS().overlaps(b.getS()))
						return false;
				}
				else if (first.equals("913") && second.equals("413")) {
					if (a.getS().overlaps(b.getS()))
						return false;
				}
				
				// also can't overlap other courses that are not allowed to overlap 313/413
				
				// if the first course is 813 and second is 313
				if (first.equals("813") && second.equals("313")) {
					
					// for each non-compatible entry of 313
					for (Meeting m : b.getM().getIncompatibility()) {
						
						// for each other assignment
						for (Assignment c : schedule.getAssignments()) {
							if (c == a || c == b) continue;
							
							// skip if unassigned or meeting doesn't match
							if (c.getS() == null || c.getM() != m) continue;
							
							// return false if slots overlap
							if (a.getS().overlaps(c.getS()))
								return false;
						}
					}
				}
				
				// if the first course is 913 and second is 413
				if (first.equals("913") && second.equals("413")) {
					
					// for each non-compatible entry of 413
					for (Meeting m : b.getM().getIncompatibility()) {
						
						// for each other assignment
						for (Assignment c : schedule.getAssignments()) {
							if (c == a || c == b) continue;
							
							// skip if unassigned or meeting doesn't match
							if (c.getS() == null || c.getM() != m) continue;
							
							// return false if slots overlap
							if (a.getS().overlaps(c.getS()))
								return false;
						}
					}
				}
			}
		}
		
		// if this is reached the constraint is satisfied
		return true;
	}
	
}
