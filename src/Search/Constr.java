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
import Schedule.Lab;
import Schedule.Tutorial;
import Schedule.Lecture;
import Schedule.LectureSlot;
import Schedule.Meeting;
import Schedule.NonLecture;
import Schedule.NonLectureSlot;
import Schedule.Schedule;
import Schedule.Slot;

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
	 * Constr c = new Constr(schedule);
	 * c.check() == true if schedule meets all hard constraints
	 * 
	 * Constr c = new Constr(assignment, schedule);
	 * c.check() == true if adding assignment to schedule would meet all hard constraints
	 * 		--> this does not add the assignment to the schedule
	 * 
	 * You can also check individual constraints if you need:
	 * 		c.checkCourseMax()
	 * 		c.checkLabMax()
	 * 		c.checkLabsDifferent()
	 * 		c.checkNoncompatible()
	 * 		c.checkPartassign()
	 * 		c.checkUnwanted()
	 * 		c.checkEveningClasses()
	 * 		c.checkOver500Classes()
	 * 		c.checkSpecificTimes()
	 * 		c.checkSpecialClasses()
	 */
	
	// the schedule data
	private Schedule schedule;
	
	/**
	 * Constructor for checking if adding an assignment to a search is valid
	 * 
	 * *** Use this to check if an assignment would be valid but WITHOUT 
	 * actually adding it to the timetable ***
	 * 
	 * @param a Assignment
	 * @param schedule Schedule data
	 */
	public Constr(Assignment a, Schedule schedule) {
		this(new Schedule(a, schedule));
	}

	/**
	 * Constructor for checking if a search is valid
	 * 
	 * @param schedule Schedule data
	 */
	public Constr(Schedule schedule) {
		this.schedule = schedule;
	}

	/**
	 * Check all hard constraints
	 * 
	 * @return True if all hard constraints are satisfied
	 */
	public boolean check() {
		return checkCourseMax() && checkLabMax() && checkLabsDifferent() && checkNoncompatible() 
				&& checkPartassign() && checkUnwanted() && checkEveningClasses()
				&& checkOver500Classes() && checkSpecificTimes() && checkSpecialClasses();
	}
	
	/**
	 * Prints which constraints were violated for debugging
	 */
	public void printViolations() {
		if (!checkCourseMax())
			System.out.println("Coursemax violated");
		if (!checkLabMax())
			System.out.println("Labmax violated");
		if (!checkLabsDifferent())
			System.out.println("Labs different from lecture violated");
		if (!checkNoncompatible())
			System.out.println("Noncompatible violated");
		if (!checkPartassign())
			System.out.println("Partassign violated");
		if (!checkUnwanted())
			System.out.println("Unwanted violated");
		if (!checkEveningClasses())
			System.out.println("Evening classes violated");
		if (!checkOver500Classes())
			System.out.println("No overlap in >500 courses violated");
		if (!checkSpecificTimes())
			System.out.println("Tuesday 11:00 constraint violated");
		if (!checkSpecialClasses())
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
	public boolean checkCourseMax() {
		
		// for each assignment
		for (Assignment a : schedule.getAssignments()) {
			
			// skip if not a lecture or not assigned
			if (a.getM().getClass() != Lecture.class
					|| a.getS().getClass() != LectureSlot.class
					|| a.getS() == null) 
				continue;
			
			// count how many others have the same slot
			int count = 1;
			for (Assignment b : schedule.getAssignments()) {
				if (a == b) continue;
				
				// skip if not a lecture or not assigned
				if (b.getM().getClass() != Lecture.class
						|| b.getS().getClass() != LectureSlot.class
						|| b.getS() == null)
					continue;
				
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
	 * @return True if lab max constraint is met
	 */
	public boolean checkLabMax() {
		
		// for each assignment
		for (Assignment a : schedule.getAssignments()) {
			
			// skip if not nonlecture or not assigned
			if (a.getM().getClass() == Lecture.class
					|| a.getS().getClass() == LectureSlot.class
					|| a.getS() == null) 
				continue;
			
			// count how many others have the same slot
			int count = 1;
			for (Assignment b : schedule.getAssignments()) {
				if (a == b) continue;
				
				// skip if not nonlecture or not assigned
				if (b.getM().getClass() == Lecture.class
						|| b.getS().getClass() == LectureSlot.class
						|| b.getS() == null) 
					continue;
				
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
	 * @return True if labs assignment constraint is met
	 */
	public boolean checkLabsDifferent() {
		
		// for each assignment
		for (Assignment a : schedule.getAssignments()) {
			
			// skip if not a nonlecture or not assigned
			if (a.getM().getClass() == Lecture.class
					|| a.getS().getClass() == LectureSlot.class
					|| a.getS() == null) 
				continue;
			
			// for each other assignment
			for (Assignment b : schedule.getAssignments()) {
				if (a == b) continue;
				
				// skip if not a lecture or slot is different
				if (b.getM().getClass() != Lecture.class
						|| b.getS().getClass() != LectureSlot.class
						|| b.getS() == null
						|| !a.getS().overlaps(b.getS())) 
					continue;
				
				// return false if section is the same
				Lecture l = (Lecture) b.getM();
				NonLecture nl = (NonLecture) a.getM();
				
				if (l.getParentSection().equals(nl.getParentSection()));
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
	public boolean checkNoncompatible() {
		
		// for each assignment
		for (Assignment a : schedule.getAssignments()) {
			
			// for each noncompatible entry of the assignment's meeting
			for (Meeting m : a.getM().getIncompatibility()) {
				
				// for each other assignment
				for (Assignment b : schedule.getAssignments()) {
					if (a == b) continue;
					
					// skip if meeting doesn't match
					if (b.getM() != m) continue;
					
					// return false if slots match
					if (a.getS().overlaps(b.getS()))
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
	public boolean checkPartassign() {
		
		// for each assignment
		for (Assignment a : schedule.getAssignments()) {
			
			// skip if partassign is not set
			if (a.getM().getPartassign() == null) continue;
			
			// return false if slot doesn't match
			if (!a.getS().equals(a.getM().getPartassign()))
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
	public boolean checkUnwanted() {
		
		// for each assignment
		for (Assignment a : schedule.getAssignments()) {
			
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
	 * @return True if evening classes constraint is met
	 */
	public boolean checkEveningClasses() {
		
		// for each assignment
		for (Assignment a : schedule.getAssignments()) {
			
			// get section number
			String snum = null;
			if (a.getM().getClass() == Lecture.class) {
				Lecture l = (Lecture) a.getM();
				snum = l.getParentSection().getSectionNum();
			}
			else if (a.getM().getClass() == Lab.class) {
				Lab nl = (Lab) a.getM();
				snum = nl.getLabNum();
			}
			else if (a.getM().getClass() == Tutorial.class) {
				Tutorial nl = (Tutorial) a.getM();
				snum = nl.getTutNum();
			}
			else continue;
			
			// check section number begins with 9
			if (snum.substring(0, 1).equals("9")) {
				
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
	 * @return True if 500-level constraint is met
	 */
	public boolean checkOver500Classes() {
		
		// for each assignment
		for (Assignment a : schedule.getAssignments()) {
			
			// skip if not a lecture or not assigned
			if (a.getM().getClass() != Lecture.class
					|| a.getS().getClass() != LectureSlot.class
					|| a.getS() == null) 
				continue;
			
			// skip if course number < 500
			Lecture l1 = (Lecture) a.getM();
			int cnum1 = Integer.parseInt(l1.getParentSection().getParentCourse().getNumber());
			if (cnum1 < 500) continue;
			
			// for each other assignment
			for (Assignment b : schedule.getAssignments()) {
				if (a == b) continue;
				
				// skip if not a lecture or slot is different
				if (b.getM().getClass() != Lecture.class
						|| b.getS().getClass() != LectureSlot.class
						|| b.getS() == null
						|| !a.getS().overlaps(b.getS())) 
					continue;
				
				// skip if course number < 500
				Lecture l2 = (Lecture) b.getM();
				int cnum2 = Integer.parseInt(l2.getParentSection().getParentCourse().getNumber());
				if (cnum2 < 500) continue;
				
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
	public boolean checkSpecificTimes() {
		
		// for each assignment
		for (Assignment a : schedule.getAssignments()) {
			
			// skip if not a lecture
			if (a.getM().getClass() != Lecture.class) continue;
			
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
	 * @return True if CPSC813/913 constraint is met
	 */
	public boolean checkSpecialClasses() {
		
		// for each assignment
		for (Assignment a : schedule.getAssignments()) {
			String first = "";
			String second = "";
			
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
			if (!a.getS().getDay().equals("TU") || a.getS().getHour() != 18 || a.getS().getMinute() != 0)
				return false;
			
			// cpsc 813 not allowed to overlap any sections/tuts of 313 or other courses not allowed to overlap 313
			// cpsc 913 not allowed to overlap any sections/tuts of 413 or other courses not allowed to overlap 413
			
			// for each other assignment
			for (Assignment b : schedule.getAssignments()) {
				if (a == b) continue;
				
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
							
							// skip if meeting doesn't match
							if (c.getM() != m) continue;
							
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
							
							// skip if meeting doesn't match
							if (c.getM() != m) continue;
							
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
