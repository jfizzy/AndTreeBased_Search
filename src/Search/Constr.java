package Search;

import Schedule.Assignment;
import Schedule.TimeTable;
import Schedule.Lecture;
import Schedule.LectureSlot;
import Schedule.Meeting;
import Schedule.NonLecture;
import Schedule.NonLectureSlot;
import Schedule.Section;
import Schedule.Slot;

/**
 * Object for determining whether a schedule satisfies hard constraints
 *
 */
public class Constr {
	
	// the instance
	private SearchData data;
	
	/**
	 * constructor for checking if adding an assignment to a search is valid
	 * @param a
	 * @param sd
	 */
	public Constr(Assignment a, SearchData sd) {
		this(new SearchData(sd, new TimeTable(a, sd.getTimetable())));
	}

	/**
	 * constructor for checking if a search is valid
	 * @param sd
	 */
	public Constr(SearchData sd) {
		this.data = sd;
	}

	/**
	 * returns true if all hard constraints are met
	 * @return
	 */
	public boolean check(boolean show) {
		if (show) printViolations();
		return courseMax() && labMax() && labsDifferent() && noncompatible() 
				&& partassign() && unwanted() && eveningClasses()
				&& over500Classes() && specificTimes() && specialClasses();
	}
	
	/**
	 * prints which constraints were violated for debugging
	 */
	public void printViolations() {
		if (!courseMax())
			System.out.println("Coursemax violated");
		if (!labMax())
			System.out.println("Labmax violated");
		if (!labsDifferent())
			System.out.println("Labs different from lecture violated");
		if (!noncompatible())
			System.out.println("Noncompatible violated");
		if (!partassign())
			System.out.println("Partassign violated");
		if (!unwanted())
			System.out.println("Unwanted violated");
		if (!eveningClasses())
			System.out.println("Evening classes violated");
		if (!over500Classes())
			System.out.println("No overlap in >500 courses violated");
		if (!specificTimes())
			System.out.println("Tuesday 11:00 constraint violated");
		if (!specialClasses())
			System.out.println("CPSC 813/913 constraint violated");
	}
	
	/*
	 *  individual hard constraints
	 */
	
	/**
	 * course maximum
	 * @return
	 */
	private boolean courseMax() {
		
		// for each assignment
		for (Assignment a : data.getTimetable().getAssignments()) {
			
			// skip if not a lecture or not assigned
			if (a.getM().getClass() != Lecture.class
					|| a.getS().getClass() != LectureSlot.class
					|| a.getS() == null) 
				continue;
			
			// count how many others have the same slot
			int count = 1;
			for (Assignment b : data.getTimetable().getAssignments()) {
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
	 * lab maximum
	 * @return
	 */
	private boolean labMax() {
		
		// for each assignment
		for (Assignment a : data.getTimetable().getAssignments()) {
			
			// skip if not nonlecture or not assigned
			if (a.getM().getClass() != NonLecture.class
					|| a.getS().getClass() != NonLectureSlot.class
					|| a.getS() == null) 
				continue;
			
			// count how many others have the same slot
			int count = 1;
			for (Assignment b : data.getTimetable().getAssignments()) {
				if (a == b) continue;
				
				// skip if not nonlecture or not assigned
				if (b.getM().getClass() != NonLecture.class
						|| b.getS().getClass() != NonLectureSlot.class
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
	 * labs not assigned to same slot as lectures of the same section
	 * @return
	 */
	private boolean labsDifferent() {
		
		// for each assignment
		for (Assignment a : data.getTimetable().getAssignments()) {
			
			// skip if not a nonlecture or not assigned
			if (a.getM().getClass() != NonLecture.class
					|| a.getS().getClass() != NonLectureSlot.class
					|| a.getS() == null) 
				continue;
			
			// for each other assignment
			for (Assignment b : data.getTimetable().getAssignments()) {
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
				if (l.getParentSection() == nl.getParentSection())
					return false;
			}
		}
		
		// if this is reached the constraint is satisfied
		return true;
	}
	
	/**
	 * non-compatible constraint
	 * non-compatible(a,b) => assign a != assign b
	 * @return
	 */
	private boolean noncompatible() {
		
		// for each assignment
		for (Assignment a : data.getTimetable().getAssignments()) {
			
			// for each noncompatible entry of the assignment's meeting
			for (Meeting m : a.getM().getIncompatibility()) {
				
				// for each other assignment
				for (Assignment b : data.getTimetable().getAssignments()) {
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
	 * partassign constraint
	 * partassign a => assign a
	 * @return
	 */
	private boolean partassign() {
		
		// for each assignment
		for (Assignment a : data.getTimetable().getAssignments()) {
			
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
	 * unwanted constraint
	 * unwanted a,s => assign a != s
	 * @return
	 */
	private boolean unwanted() {
		
		// for each assignment
		for (Assignment a : data.getTimetable().getAssignments()) {
			
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
	 * sections with numbers starting with 9 must get evening slots
	 * department constraint
	 * @return
	 */
	private boolean eveningClasses() {
		
		// for each assignment
		for (Assignment a : data.getTimetable().getAssignments()) {
			
			// get parent section
			Section s = null;
			if (a.getM().getClass() == Lecture.class) {
				Lecture l = (Lecture) a.getM();
				s = l.getParentSection();
			}
			else if (a.getM().getClass() == NonLecture.class) {
				NonLecture nl = (NonLecture) a.getM();
				s = nl.getParentSection();
			}
			
			// check section number begins with 9
			if (s != null && s.getSectionNum().substring(0, 1).equals("9")) {
				
				// return false if not scheduled in the evening
				if (a.getS().getHour() < 18)
					return false;
			}
		}
		
		// if this is reached the constraint is satisfied
		return true;
	}
	
	/**
	 * 500 level classes can't get the same slot
	 * department constraint
	 * @return
	 */
	private boolean over500Classes() {
		
		// for each assignment
		for (Assignment a : data.getTimetable().getAssignments()) {
			
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
			for (Assignment b : data.getTimetable().getAssignments()) {
				if (a == b) continue;
				
				// skip if not a lecture or slot is different
				if (b.getM().getClass() != NonLecture.class
						|| b.getS().getClass() != NonLectureSlot.class
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
	 * no courses Tuesday 11:00-12:30
	 * department constraint
	 * @return
	 */
	private boolean specificTimes() {
		
		// for each assignment
		for (Assignment a : data.getTimetable().getAssignments()) {
			
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
	 * special requirements for CPSC 813/913
	 * department constraint
	 * @return
	 */
	private boolean specialClasses() {
		
		// for each assignment
		for (Assignment a : data.getTimetable().getAssignments()) {
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
			
			// skip if not CPSC 813/913
			if (!first.equals("813") && !first.equals("913"))
				continue;
			
			// return false if not scheduled TuTh 18:00
			if (!a.getS().getDay().equals("TU") || a.getS().getHour() != 18 || a.getS().getMinute() != 0)
				return false;
			
			// cpsc 813 not allowed to overlap any sections/tuts of 313 or other courses not allowed to overlap 313
			// cpsc 913 not allowed to overlap any sections/tuts of 413 or other courses not allowed to overlap 413
			
			// for each other assignment
			for (Assignment b : data.getTimetable().getAssignments()) {
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
				if (first.equals("313") && second.equals("813")) {
					if (a.getS().overlaps(b.getS()))
						return false;
				}
				else if (first.equals("413") && second.equals("913")) {
					if (a.getS().overlaps(b.getS()))
						return false;
				}
				
				// TODO: also other courses not allowed to overlap 313/413
			}
		}
		
		// if this is reached the constraint is satisfied
		return true;
	}	
}
