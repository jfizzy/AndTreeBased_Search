package Search;

import Schedule.Assignment;
import Schedule.TimeTable;
import Schedule.Lecture;
import Schedule.LectureSlot;
import Schedule.NonLecture;
import Schedule.NonLectureSlot;
import Schedule.Section;

public class Constr {
	
	private SearchData data;
	
	// constructor for checking if adding an assignment to a search is valid
	public Constr(Assignment a, SearchData sd) {
		this(new SearchData(sd, new TimeTable(a, sd.getTimetable())));
	}

	// constructor for checking if a search is valid
	public Constr(SearchData sd) {
		this.data = sd;
	}

	// return true if all hard constraints are met
	public boolean check() {
		return courseMax() && labMax() && labsDifferent() && noncompatible() 
				&& partassign() && unwanted() && eveningClasses()
				&& over500Classes() && specificTimes() && specialClasses();
	}
	
	// individual constraints
	
	private boolean courseMax() {
		
		// for each assignment
		for (Assignment a : data.getTimetable().getAssignments()) {
			
			// skip if not a lecture
			if (a.getM().getClass() != Lecture.class) continue;
			if (a.getS().getClass() != LectureSlot.class) continue;
			if (a.getS() == null) continue;
			
			// count how many others have the same slot
			int count = 1;
			for (Assignment b : data.getTimetable().getAssignments()) {
				
				// skip if not a lecture or same
				if (b.getM().getClass() != Lecture.class) continue;
				if (b.getS().getClass() != LectureSlot.class) continue;
				if (b.getS() == null) continue;
				if (a == b) continue;
				
				if (a.getS().equals(b.getS()))
					count++;
			}
			
			// return false if count is greater than slot's coursemax
			LectureSlot ls = (LectureSlot) a.getS();
			if (count > ls.getCourseMax()) return false;
		}
		return true;
	}
	
	private boolean labMax() {
		
		// for each assignment
		for (Assignment a : data.getTimetable().getAssignments()) {
			
			// skip if a lecture
			if (a.getM().getClass() != NonLecture.class) continue;
			if (a.getS().getClass() != NonLectureSlot.class) continue;
			if (a.getS() == null) continue;
			
			// count how many others have the same slot
			int count = 1;
			for (Assignment b : data.getTimetable().getAssignments()) {
				
				// skip if a lecture or same
				if (b.getM().getClass() != NonLecture.class) continue;
				if (b.getS().getClass() != NonLectureSlot.class) continue;
				if (b.getS() == null) continue;
				if (a == b) continue;
				
				if (a.getS().equals(b.getS()))
					count++;
			}
			
			// return false if count is greater than slot's labmax
			NonLectureSlot ls = (NonLectureSlot) a.getS();
			if (count > ls.getLabMax()) return false;
		}
		return true;
	}
	
	private boolean labsDifferent() {
		
		// for each assignment
		for (Assignment a : data.getTimetable().getAssignments()) {
			
			// skip if not a lab
			if (a.getM().getClass() != NonLecture.class) continue;
			if (a.getS().getClass() != NonLectureSlot.class) continue;
			if (a.getS() == null) continue;
			
			// for each other assignment
			for (Assignment b : data.getTimetable().getAssignments()) {
				
				// skip if not a lecture or slot is different
				if (b.getM().getClass() != Lecture.class) continue;
				if (b.getS().getClass() != LectureSlot.class) continue;
				if (b.getS() == null) continue;
				if (a == b) continue;
				if (!a.getS().equals(b.getS())) continue;
				
				// return false if section is the same
				Lecture l = (Lecture) b.getM();
				NonLecture nl = (NonLecture) a.getM();
				if (l.getParentSection() == nl.getParentSection())
					return false;
			}
		}
		return true;
	}
	
	private boolean noncompatible() {
		// assign a != assign b if non-compatible(a,b)
		return true;
	}
	
	private boolean partassign() {
		// assign a = partassign a
		return true;
	}
	
	private boolean unwanted() {
		// assign a != s if unwanted(a,s)
		return true;
	}
	
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
		
		return true;
	}
	
	private boolean over500Classes() {
		
		// for each assignment
		for (Assignment a : data.getTimetable().getAssignments()) {
			
			// skip if not a lecture
			if (a.getM().getClass() != Lecture.class) continue;
			if (a.getS().getClass() != LectureSlot.class) continue;
			if (a.getS() == null) continue;
			
			// skip if course number < 500
			Lecture l1 = (Lecture) a.getM();
			int cnum1 = Integer.parseInt(l1.getParentSection().getParentCourse().getNumber());
			if (cnum1 < 500) continue;
			
			// for each other assignment
			for (Assignment b : data.getTimetable().getAssignments()) {
				
				// skip if not a lecture or slot is different
				if (b.getM().getClass() != NonLecture.class) continue;
				if (b.getS().getClass() != NonLectureSlot.class) continue;
				if (b.getS() == null) continue;
				if (a == b) continue;
				if (!a.getS().equals(b.getS())) continue;
				
				// skip if course number < 500
				Lecture l2 = (Lecture) b.getM();
				int cnum2 = Integer.parseInt(l2.getParentSection().getParentCourse().getNumber());
				if (cnum2 < 500) continue;
				
				// if this is reached the classes >500 have overlap
				return false;
			}
		}
		
		return true;
	}
	
	private boolean specificTimes() {
		
		// for each assignment
		for (Assignment a : data.getTimetable().getAssignments()) {
			
			// return false if slot is Tuesday at 11:00
			if (a.getS().getDay().equals("TU") && a.getS().getHour() == 11)
				return false;
		}
		
		return true;
	}
	
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
			else continue; // TODO: labs as well
			
			// return false if not scheduled TuTh 18:00
			if (!a.getS().getDay().equals("TU") || a.getS().getHour() != 18 || a.getS().getMinute() != 0)
				return false;
			
			// TODO: cpsc 813 not allowed to overlap any sections/tuts of 313 or other courses not allowed to overlap 313
			// TODO: cpsc 913 not allowed to overlap any sections/tuts of 413 or other courses not allowed to overlap 413
		}
		
		return true;
	}	
}