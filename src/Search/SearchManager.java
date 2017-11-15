package Search;

import Schedule.Assignment;
import Schedule.Course;
import Schedule.Lecture;
import Schedule.NonLecture;
import Schedule.Section;
import Schedule.Slot;

public class SearchManager {
	
	SearchData data;
	
	// constructor
	public SearchManager(SearchData sd) {
		this.data = sd;
	}
	
	// run the search
	public void run() {
		addrandom();
		data.getTimetable().printAssignments();
	}
	
	// make a random timetable for testing
	private void addrandom() {
		
		// for each course in data
		for (Course c : data.getCourses()) {
			
			// for each section in the course
			for (Section s : c.getSections()) {
				Lecture l = new Lecture(s);
				Slot slot = null;//data.getLectureSlots().get(0);
				Assignment a = new Assignment(l, slot);
				data.getTimetable().addAssignment(a);
			}
		}
		
		// for each nonlecture in data
		for (NonLecture nl : data.getNonLectures()) {
			Slot slot = null;//data.getLabSlots().get(0);
			Assignment a = new Assignment(nl, slot);
			data.getTimetable().addAssignment(a);
		}
	}
}
