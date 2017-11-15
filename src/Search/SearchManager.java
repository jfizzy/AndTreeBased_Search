package Search;

import Schedule.Assignment;
import Schedule.Course;
import Schedule.Lecture;
import Schedule.LectureSlot;
import Schedule.NonLecture;
import Schedule.NonLectureSlot;
import Schedule.Section;
import Schedule.Slot;

public class SearchManager {
	
	private SearchData data;
	
	// constructor
	public SearchManager(SearchData sd) {
		this.data = sd;
	}
	
	// run the search
	public void run() {
		addrandom();
		data.getTimetable().printAssignments();
		Eval eval = new Eval(data);
        System.out.print("Eval = ");
        System.out.print(eval.getEval());
        System.out.print("\n");
	}
	
	public SearchData getData() {
		return this.data;
	}
	
	// make a random timetable for testing
	private void addrandom() {
		
		// for each course in data
		for (Course c : data.getCourses()) {
			
			// for each section in the course
			for (Section s : c.getSections()) {
				Lecture l = s.getLecture();
				Slot slot = new LectureSlot("MWF", 8, 0, 10, 1);//data.getLectureSlots().get(0);
				Assignment a = new Assignment(l, slot);
				data.getTimetable().addAssignment(a);
			}
		}
		
		// for each nonlecture in data
		for (NonLecture nl : data.getNonLectures()) {
			Slot slot = new NonLectureSlot("M", 10, 0, 5, 0);//data.getLabSlots().get(0);
			Assignment a = new Assignment(nl, slot);
			data.getTimetable().addAssignment(a);
		}
	}
}
