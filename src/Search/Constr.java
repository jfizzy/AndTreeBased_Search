package Search;

import Schedule.Assignment;
import Schedule.TimeTable;
import Schedule.Lecture;
import Schedule.LectureSlot;
import Schedule.NonLecture;
import Schedule.NonLectureSlot;

public class Constr {
	
	private TimeTable tt;
	
	// constructor for checking if an assignment to a timetable is valid
	public Constr(Assignment a, TimeTable tt) {
		this(new TimeTable(a,tt));
	}

	// constructor for checking if a timetable is valid
	public Constr(TimeTable tt) {
		this.tt = tt;
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
		for (Assignment a : tt.getAssignments()) {
			
			// skip if not a lecture
			if (a.getM().getClass() != Lecture.class) continue;
			if (a.getS().getClass() != LectureSlot.class) continue;
			if (a.getS() == null) continue;
			
			// count how many others have the same slot
			int count = 0;
			for (Assignment b : tt.getAssignments()) {
				
				// skip if not a lecture or same
				if (a.getM().getClass() != Lecture.class) continue;
				if (a.getS().getClass() != LectureSlot.class) continue;
				if (a.getS() == null) continue;
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
		for (Assignment a : tt.getAssignments()) {
			
			// skip if a lecture
			if (a.getM().getClass() != NonLecture.class) continue;
			if (a.getS().getClass() != NonLectureSlot.class) continue;
			if (a.getS() == null) continue;
			
			// count how many others have the same slot
			int count = 0;
			for (Assignment b : tt.getAssignments()) {
				
				// skip if a lecture or same
				if (a.getM().getClass() != NonLecture.class) continue;
				if (a.getS().getClass() != NonLectureSlot.class) continue;
				if (a.getS() == null) continue;
				if (a == b) continue;
				
				if (a.getS().equals(b.getS()))
					count++;
			}
			
			// return false if count is greater than slot's labemax
			NonLectureSlot ls = (NonLectureSlot) a.getS();
			if (count > ls.getLabMax()) return false;
		}
		return true;
	}
	
	private boolean labsDifferent() {
		return true;
	}
	
	private boolean noncompatible() {
		return true;
	}
	
	private boolean partassign() {
		return true;
	}
	
	private boolean unwanted() {
		return true;
	}
	
	private boolean eveningClasses() {
		return true;
	}
	
	private boolean over500Classes() {
		return true;
	}
	
	private boolean specificTimes() {
		return true;
	}
	
	private boolean specialClasses() {
		return true;
	}	
}
