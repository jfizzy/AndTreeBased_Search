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
 * Class for determining whether a schedule satisfies hard constraints
 *
 */
public class Constr {
	
	/* How to use:
	 * --------------
	 * 
	 * Quick way:	schedule.isValid()
	 * 				schedule.isValidWith(assignment)
	 * 				- checks if valid without actually adding the assignment
	 * 
	 * Otherwise:
	 * 
	 * Constr.check(schedule) == true if schedule meets all hard constraints
	 * 
	 * Constr.check(schedule, assignment) == true if adding assignment to schedule 
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
	 * @return True if all hard constraints are satisfied
	 */
	public static boolean check(Schedule s) {
		// TODO reorder by most likely to be violated/running time cost
		return checkEveningClasses(s) && checkSpecificTimes(s) && checkNoncompatible(s)
				&& checkUnwanted(s) && checkCourseMax(s) && checkLabMax(s) 
				&& checkLabsDifferent(s) //&& checkPartassign(s) 
				&& checkOver500Classes(s) && checkSpecialClasses(s);
	}
	
	/**
	 * Check all hard constraints
	 * 
	 * @return True if all hard constraints are satisfied
	 */
	public static boolean checkArr(Schedule s) {
		// TODO reorder by most likely to be violated/running time cost
		return checkEveningClassesArr(s) && checkNoncompatibleArr(s) && checkSpecificTimesArr(s) 
				&& checkUnwantedArr(s) && checkCourseMaxArr(s) && checkLabMaxArr(s) 
				&& checkLabsDifferentArr(s) //&& checkPartassignArr(s) 
				&& checkOver500ClassesArr(s) && checkSpecialClassesArr(s);
	}
	
	/**
	 * Check all hard constraints if an assignment was added
	 * to an existing schedule
	 * 
	 * @return True if all hard constraints are satisfied
	 */
	public static boolean check(Schedule s1, Meeting m, Slot s) {
		
		Schedule s2 = new Schedule(s1, m, s);
		return check(s2);
	}
	
	/**
	 * Check all hard constraints if an assignment was added
	 * to an existing schedule
	 * 
	 * @return True if all hard constraints are satisfied
	 */
	public static boolean checkArr(Schedule s1, Meeting m, Slot s) {
		
		Schedule s2 = new Schedule(s1, m, s);
		return checkArr(s2);
	}
	
	/**
	 * Prints which constraints were violated for debugging
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
	
	/**
	 * Prints which constraints were violated for debugging
	 */
	public static void printViolationsArr(Schedule s) {
		if (!checkCourseMaxArr(s))
			System.out.println("Coursemax violated");
		if (!checkLabMaxArr(s))
			System.out.println("Labmax violated");
		if (!checkLabsDifferentArr(s))
			System.out.println("Labs different from lecture violated");
		if (!checkNoncompatibleArr(s))
			System.out.println("Noncompatible violated");
		if (!checkPartassignArr(s))
			System.out.println("Partassign violated");
		if (!checkUnwantedArr(s))
			System.out.println("Unwanted violated");
		if (!checkEveningClassesArr(s))
			System.out.println("Evening classes violated");
		if (!checkOver500ClassesArr(s))
			System.out.println("No overlap in >500 courses violated");
		if (!checkSpecificTimesArr(s))
			System.out.println("Tuesday 11:00 constraint violated");
		if (!checkSpecialClassesArr(s))
			System.out.println("CPSC 813/913 constraint violated");
	}
	
	/*
	 *  Individual hard constraints
	 */
	
	/**
	 * Check course maximum constraint
	 * 
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
	 * Check course maximum constraint
	 * 
	 * @return True if course max constraint is met
	 */
	public static boolean checkCourseMaxArr(Schedule schedule) {
		
		// for each assignment
		Assignment[] as = schedule.getAssignmentsArr();
		for (int i = 0; i < as.length; i++) {
			
			// skip if not a lecture or not assigned
			if (as[i].getS() == null 
					|| as[i].getM().getClass() != Lecture.class
					|| as[i].getS().getClass() != LectureSlot.class) 
				continue;
			
			// count how many others have the same slot
			int count = 1;
			for (int j = 0; j < as.length; j++) {
				if (i == j) continue; // skip if same
				
				// skip if not a lecture or not assigned
				if (as[j].getS() == null 
						|| as[j].getM().getClass() != Lecture.class
						|| as[j].getS().getClass() != LectureSlot.class)
					continue;
				
				// increment count if slots match
				if (as[i].getS().equals(as[j].getS()))
					count++;
			}
			
			// return false if count is greater than slot's coursemax
			LectureSlot ls = (LectureSlot) as[i].getS();
			if (count > ls.getCourseMax()) 
				return false;
		}
		
		// if this is reached the constraint is satisfied
		return true;
	}
	
	/**
	 * Check lab maximum constraint
	 * 
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
	 * Check lab maximum constraint
	 * 
	 * @return True if lab max constraint is met
	 */
	public static boolean checkLabMaxArr(Schedule schedule) {
		
		// for each assignment
		Assignment[] as = schedule.getAssignmentsArr();
		for (int i = 0; i < as.length; i++) {
			
			// skip if not nonlecture or not assigned
			if (as[i].getS() == null 
					|| as[i].getM().getClass() == Lecture.class
					|| as[i].getS().getClass() == LectureSlot.class) 
				continue;
			
			// count how many others have the same slot
			int count = 1;
			for (int j = 0; j < as.length; j++) {
				if (i == j) continue; // skip if same
				
				// skip if not nonlecture or not assigned
				if (as[j].getS() == null 
						|| as[j].getM().getClass() == Lecture.class
						|| as[j].getS().getClass() == LectureSlot.class) 
					continue;
				
				// increment count if slots match
				if (as[i].getS().equals(as[j].getS()))
					count++;
			}
			
			// return false if count is greater than slot's labmax
			NonLectureSlot ls = (NonLectureSlot) as[i].getS();
			if (count > ls.getLabMax()) 
				return false;
		}
		
		// if this is reached the constraint is satisfied
		return true;
	}
	
	/**
	 * Check if labs not assigned to same slots as lectures of the same section
	 * 
	 * @return True if labs assignment constraint is met
	 */
	public static boolean checkLabsDifferent(Schedule schedule) {
		//if (true) return true;
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
				if (lsec.equals(nlsec))
						//|| lsec.getParentCourse().getOpenLabs().contains(a.getM())
						//|| lsec.getParentCourse().getOpenTuts().contains(a.getM()));
					return false;
			}
		}
		
		// if this is reached the constraint is satisfied
		return true;
	}
	
	/**
	 * Check if labs not assigned to same slots as lectures of the same section
	 * 
	 * @return True if labs assignment constraint is met
	 */
	public static boolean checkLabsDifferentArr(Schedule schedule) {
		
		// for each assignment
		Assignment[] as = schedule.getAssignmentsArr();
		for (int i = 0; i < as.length; i++) {
			
			// skip if not a nonlecture or not assigned
			if (as[i].getS() == null 
					|| as[i].getM().getClass() == Lecture.class
					|| as[i].getS().getClass() == LectureSlot.class) 
				continue;
			
			// for each other assignment
			for (int j = 0; j < as.length; j++) {
				if (i == j) continue; // skip if same
				
				// skip if unassigned, not a lecture or slot is different
				if (as[j].getS() == null 
						|| as[j].getM().getClass() != Lecture.class
						|| as[j].getS().getClass() != LectureSlot.class
						|| !as[i].getS().overlaps(as[j].getS())) 
					continue;
				
				// return false if section is the same, or open lab/tut matches first section
				Lecture l = (Lecture) as[j].getM();
				Section lsec = l.getParentSection();
				NonLecture nl = (NonLecture) as[i].getM();
				Section nlsec = nl.getParentSection();
				if (lsec.equals(nlsec)
						|| (lsec.getParentCourse().equals(nl.getParentCourse())
								&& (lsec.getParentCourse().getOpenLabs().contains(as[i].getM())
										|| lsec.getParentCourse().getOpenTuts().contains(as[i].getM()))));
					return false;
			}
		}
		
		// if this is reached the constraint is satisfied
		return true;
	}
	
	/**
	 * Check non-compatible constraint:
	 * non-compatible(a,b) => assign a != assign b
	 * 
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
		
		// TODO delete one
		
		// for each pair in the noncompatible list
		/*for (MeetingPair mp : schedule.getNoncompatible()) {
			
			// skip if unassigned
			if (mp.getFirst().getAssignment() == null
					|| mp.getSecond().getAssignment() == null)
				continue;
			Slot s1 = mp.getFirst().getAssignment().getS();
			Slot s2 = mp.getSecond().getAssignment().getS();
			if (s1 == null || s2 == null)
				continue;
			
			// return false if slots overlap
			if (s1.overlaps(s2)) {
				System.out.println(mp.getFirst().toString() + "   " + mp.getSecond().toString() + "   " + s1.toString() + "   " + s2.toString());
				return false;
			}
		}*/
		
		// if this is reached the constraint is satisfied
		return true;
	}
	
	/**
	 * Check non-compatible constraint:
	 * non-compatible(a,b) => assign a != assign b
	 * 
	 * @return True if non-compatible constraint is met
	 */
	public static boolean checkNoncompatibleArr(Schedule schedule) {

		// for each assignment
		Assignment[] as = schedule.getAssignmentsArr();
		for (int i = 0; i < as.length; i++) {
			
			// skip if unassigned
			if (as[i].getS() == null) continue;
			
			// for each noncompatible entry of the assignment's meeting
			for (Meeting m : as[i].getM().getIncompatibility()) {
				
				// for each other assignment
				for (int j = 0; j < as.length; j++) {
					if (i == j) continue; // skip if same
					
					// skip if unassigned or meeting doesn't match
					if (as[j].getS() == null || as[j].getM() != m) 
						continue;
					
					// return false if slots match
					if (as[i].getS().overlaps(as[j].getS()))
						return false;
				}
			}
		}
		
		// if this is reached the constraint is satisfied
		return true;
	}
	
	/**
	 * Check partassign constraint:
	 * partassign a => assign a
	 * 
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
	 * Check partassign constraint:
	 * partassign a => assign a
	 * 
	 * @return True if partassign constraint is met
	 */
	public static boolean checkPartassignArr(Schedule schedule) {
		
		// for each assignment
		Assignment[] as = schedule.getAssignmentsArr();
		for (int i = 0; i < as.length; i++) {
			Slot partassign = as[i].getM().getPartassign();
			
			// skip if unassigned or partassign is not set
			if (as[i].getS() == null || partassign == null) continue;
			
			// return false if slot doesn't match
			if (!as[i].getS().equals(partassign))
				return false;
		}
		
		// if this is reached the constraint is satisfied
		return true;
	}
	
	/**
	 * Check unwanted constraint:
	 * unwanted a,s => assign a != s
	 * 
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
	 * Check unwanted constraint:
	 * unwanted a,s => assign a != s
	 * 
	 * @return True if unwanted constraint is met
	 */
	public static boolean checkUnwantedArr(Schedule schedule) {
		
		// for each assignment
		Assignment[] as = schedule.getAssignmentsArr();
		for (int i = 0; i < as.length; i++) {
			
			// skip if unassigned
			if (as[i].getS() == null) continue;
			
			// for each unwanted entry of the assignment's meeting
			for (Slot s : as[i].getM().getUnwanted()) {
				
				// return false if slot matches
				if (as[i].getS().equals(s))
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
			if (evening) { // snum.substring(0, 1).equals("9")
				
				// return false if not scheduled in the evening
				if (a.getS().getHour() < 18)
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
	 * @return True if evening classes constraint is met
	 */
	public static boolean checkEveningClassesArr(Schedule schedule) {
		
		// for each assignment
		Assignment[] as = schedule.getAssignmentsArr();
		for (int i = 0; i < as.length; i++) {
			
			// skip if unassigned
			if (as[i].getS() == null) continue;
			
			// get section number
			String snum = null;
			boolean evening = false;
			if (as[i].getM().getClass() == Lecture.class) {
				Lecture l = (Lecture) as[i].getM();
				snum = l.getParentSection().getSectionNum();
				evening = l.getParentSection().isEvening();
			}
			else if (as[i].getM().getClass() == Lab.class) {
				Lab nl = (Lab) as[i].getM();
				snum = nl.getLabNum();
				evening = nl.isEvening();
			}
			else if (as[i].getM().getClass() == Tutorial.class) {
				Tutorial nl = (Tutorial) as[i].getM();
				snum = nl.getTutNum();
				evening = nl.isEvening();
			}
			else continue;
			
			// check section number begins with 9
			if (evening) { // snum.substring(0, 1).equals("9")
				
				// return false if not scheduled in the evening
				if (as[i].getS().getHour() < 18)
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
	 * @return True if 500-level constraint is met
	 */
	public static boolean checkOver500Classes(Schedule schedule) {
		//if (true) return true;
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
	 * Check if 500 level classes don't have the same slot
	 * (department constraint)
	 * 
	 * @return True if 500-level constraint is met
	 */
	public static boolean checkOver500ClassesArr(Schedule schedule) {
		
		// for each assignment
		Assignment[] as = schedule.getAssignmentsArr();
		for (int i = 0; i < as.length; i++) {
			
			// skip if unassigned or not a lecture
			if (as[i].getS() == null 
					|| as[i].getM().getClass() != Lecture.class
					|| as[i].getS().getClass() != LectureSlot.class) 
				continue;
			
			// skip if course number < 500
			Lecture l1 = (Lecture) as[i].getM();
			int cnum1 = Integer.parseInt(l1.getParentSection().getParentCourse().getNumber());
			if (cnum1 < 500 || cnum1 > 599) continue;
			
			// for each other assignment
			for (int j = 0; j < as.length; j++) {
				if (i == j) continue;
				
				// skip if unassigned, not a lecture or slot is different
				if (as[j].getS() == null 
						|| as[j].getM().getClass() != Lecture.class
						|| as[j].getS().getClass() != LectureSlot.class
						|| !as[i].getS().overlaps(as[j].getS())) 
					continue;
				
				// skip if course number < 500
				Lecture l2 = (Lecture) as[j].getM();
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
	 * Check if no courses Tuesday 11:00-12:30
	 * (department constraint)
	 * 
	 * @return True if Tues 11:00 constraint is met
	 */
	public static boolean checkSpecificTimesArr(Schedule schedule) {
		
		// for each assignment
		Assignment[] as = schedule.getAssignmentsArr();
		for (int i = 0; i < as.length; i++) {
			
			// skip if unassigned or not a lecture
			if (as[i].getS() == null || as[i].getM().getClass() != Lecture.class) 
				continue;
			
			// return false if slot is Tuesday at 11:00
			if (as[i].getS().getDay().equals("TU") && as[i].getS().getHour() == 11)
				return false;
		}
		
		// if this is reached the constraint is satisfied
		return true;
	}
	
	/**
	 * Check special requirements for CPSC 813/913
	 * (department constraint)
	 * 
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
	
	/**
	 * Check special requirements for CPSC 813/913
	 * (department constraint)
	 * 
	 * @return True if CPSC813/913 constraint is met
	 */
	public static boolean checkSpecialClassesArr(Schedule schedule) {
		
		// for each assignment
		Assignment[] as = schedule.getAssignmentsArr();
		for (int i = 0; i < as.length; i++) {
			String first = "";
			String second = "";
			
			// skip if unassigned
			if (as[i].getS() == null) continue;
			
			// get course number
			if (as[i].getM().getClass() == Lecture.class) {	// if lecture
				Lecture l = (Lecture) as[i].getM();
				if (!l.getParentSection().getParentCourse().getDepartment().equals("CPSC"))
					continue;
				first = l.getParentSection().getParentCourse().getNumber();
			}
			else if (as[i].getM().getClass() == NonLecture.class) {	// if nonlecture
				NonLecture nl = (NonLecture) as[i].getM();
				if (!nl.getDept().equals("CPSC"))
					continue;
				first = nl.getCourseNum();
				
			}
			else continue;
			
			// skip if not CPSC 813 or 913
			if (!first.equals("813") && !first.equals("913"))
				continue;
			
			// return false if not scheduled TuTh 18:00
			if ((!as[i].getS().getDay().equals("TU") || as[i].getS().getHour() != 18 || as[i].getS().getMinute() != 0))
				return false;
			
			// cpsc 813 not allowed to overlap any sections/tuts of 313 or other courses not allowed to overlap 313
			// cpsc 913 not allowed to overlap any sections/tuts of 413 or other courses not allowed to overlap 413
			
			// for each other assignment
			for (int j = 0; j < as.length; j++) {
				if (i == j) continue;
				
				// skip if unassigned
				if (as[j].getS() == null) continue;
				
				// get course number
				if (as[j].getM().getClass() == Lecture.class) { // if lecture
					Lecture l = (Lecture) as[j].getM();
					if (!l.getParentSection().getParentCourse().getDepartment().equals("CPSC"))
						continue;
					second = l.getParentSection().getParentCourse().getNumber();
				}
				else if (as[j].getM().getClass() == NonLecture.class) { // if nonlecture
					NonLecture nl = (NonLecture) as[j].getM();
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
					if (as[i].getS().overlaps(as[j].getS()))
						return false;
				}
				else if (first.equals("913") && second.equals("413")) {
					if (as[i].getS().overlaps(as[j].getS()))
						return false;
				}
				
				// also can't overlap other courses that are not allowed to overlap 313/413
				
				// if the first course is 813 and second is 313
				if (first.equals("813") && second.equals("313")) {
					
					// for each non-compatible entry of 313
					for (Meeting m : as[j].getM().getIncompatibility()) {
						
						// for each other assignment
						for (int k = 0; k < as.length; k++) {
							if (k == i || k == j) continue;
							
							// skip if unassigned or meeting doesn't match
							if (as[k].getS() == null || as[k].getM() != m) continue;
							
							// return false if slots overlap
							if (as[i].getS().overlaps(as[k].getS()))
								return false;
						}
					}
				}
				
				// if the first course is 913 and second is 413
				if (first.equals("913") && second.equals("413")) {
					
					// for each non-compatible entry of 413
					for (Meeting m : as[j].getM().getIncompatibility()) {
						
						// for each other assignment
						for (int k = 0; k < as.length; k++) {
							if (k == i || k == j) continue;
							
							// skip if unassigned or meeting doesn't match
							if (as[k].getS() == null || as[k].getM() != m) continue;
							
							// return false if slots overlap
							if (as[i].getS().overlaps(as[k].getS()))
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
