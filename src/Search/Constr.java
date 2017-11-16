package Search;

import Schedule.Assignment;
import Schedule.TimeTable;
import Search.SearchData.Pair;
import Schedule.Lecture;
import Schedule.LectureSlot;
import Schedule.Meeting;
import Schedule.NonLecture;
import Schedule.NonLectureSlot;
import Schedule.Section;
import Schedule.Slot;

/*
 * Object for determining whether a schedule satisfies hard constraints
 */
public class Constr {
	
	// the instance
	private SearchData data;
	
	// constructor for checking if adding an assignment to a search is valid
	public Constr(Assignment a, SearchData sd) {
		this(new SearchData(sd, new TimeTable(a, sd.getTimetable())));
	}

	// constructor for checking if a search is valid
	public Constr(SearchData sd) {
		this.data = sd;
	}

	// returns true if all hard constraints are met
	public boolean check() {
		return courseMax() && labMax() && labsDifferent() && noncompatible() 
				&& partassign() && unwanted() && eveningClasses()
				&& over500Classes() && specificTimes() && specialClasses();
	}
	
	
	// individual hard constraints
	
	// course maximum
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
	
	// lab maximum
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
	
	// labs not assigned to same slot as lectures of the same section
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
						|| !a.getS().equals(b.getS())) 
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
	
	// non-compatible(a,b) => assign a != assign b
	private boolean noncompatible() {
		
		// for each noncompatible entry
		for (Pair<Meeting, Meeting> p : data.getNoncompatible()) {
			
			// for each assignment
			for (Assignment a : data.getTimetable().getAssignments()) {
				
				// skip if meeting doesn't match first
				if (a.getM() != p.first) continue;
				
				// for each other assignment
				for (Assignment b : data.getTimetable().getAssignments()) {
					if (a == b) continue;
					
					// skip if meeting doesn't match second
					if (b.getM() != p.second) continue;
					
					// return false if the slot is the same
					if (a.getS().equals(b.getS()))
						return false;
				}
			}
		}
		
		// if this is reached the constraint is satisfied
		return true;
	}
	
	// partassign a => assign a
	private boolean partassign() {
		
		// for each partassign entry
		for (Pair<Meeting, Slot> p : data.getPartassign()) {
			
			// for each assignment
			for (Assignment a : data.getTimetable().getAssignments()) {
				
				// skip if meeting doesn't match first
				if (a.getM() != p.first) continue;
					
				// return false if the slot doesn't match second
				if (!a.getS().equals(p.second))
					return false;
			}
		}
		
		// if this is reached the constraint is satisfied
		return true;
	}
	
	// unwanted a,s => assign a != s
	private boolean unwanted() {
		
		// for each unwanted entry
		for (Pair<Meeting, Slot> p : data.getUnwanted()) {
			
			// for each assignment
			for (Assignment a : data.getTimetable().getAssignments()) {
				
				// skip if meeting doesn't match first
				if (a.getM() != p.first) continue;
					
				// return false if the slot matches second
				if (a.getS().equals(p.second))
					return false;
			}
		}
		
		// if this is reached the constraint is satisfied
		return true;
	}
	
	// sections with numbers starting with 9 must get evening slots
	// department constraint
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
	
	// 500 level classes can't get the same slot
	// department constraint
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
						|| !a.getS().equals(b.getS())) 
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
	
	// no courses Tuesday 11:00-12:30
	// department constraint
	private boolean specificTimes() {
		
		// for each assignment
		for (Assignment a : data.getTimetable().getAssignments()) {
			
			// return false if slot is Tuesday at 11:00
			if (a.getS().getDay().equals("TU") && a.getS().getHour() == 11)
				return false;
		}
		
		// if this is reached the constraint is satisfied
		return true;
	}
	
	// special requirements for CPSC 813/913
	// department constraint
	private boolean specialClasses() {
		
		// for each assignment
		for (Assignment a : data.getTimetable().getAssignments()) {
			
			// skip if not CPSC 813/913
			if (a.getM().getClass() == Lecture.class) {
				Lecture l = (Lecture) a.getM();
				if (!l.getParentSection().getParentCourse().getDepartment().equals("CPSC"))
					continue;
				if (!l.getParentSection().getParentCourse().getNumber().equals("813")
						&& !l.getParentSection().getParentCourse().getNumber().equals("913"))
					continue;
			}
			else if (a.getM().getClass() == NonLecture.class) {
				NonLecture nl = (NonLecture) a.getM();
				if (!nl.getDept().equals("CPSC"))
					continue;
				if (!nl.getCourseNum().equals("813") && !nl.getCourseNum().equals("913"))
					continue;
			}
			else continue;
			
			// return false if not scheduled TuTh 18:00
			if (!a.getS().getDay().equals("TU") || a.getS().getHour() != 18 || a.getS().getMinute() != 0)
				return false;
			
			// TODO: cpsc 813 not allowed to overlap any sections/tuts of 313 or other courses not allowed to overlap 313
			// TODO: cpsc 913 not allowed to overlap any sections/tuts of 413 or other courses not allowed to overlap 413
		}
		
		// if this is reached the constraint is satisfied
		return true;
	}	
}
