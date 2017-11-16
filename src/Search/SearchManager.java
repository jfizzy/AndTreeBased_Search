package Search;

import Schedule.Assignment;
import Schedule.Course;
import Schedule.Lecture;
import Schedule.LectureSlot;
import Schedule.NonLecture;
import Schedule.NonLectureSlot;
import Schedule.Section;
import Schedule.Slot;
import java.util.concurrent.ThreadLocalRandom;

/*
 * Object for managing and executing the search
 */
public class SearchManager {
	
	// the search instance
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
	
	// return the search data
	public SearchData getData() {
		return this.data;
	}
	
	// fill the timetable randomly for testing
	private void addrandom() {
		
		// for each course in data
		for (Course c : data.getCourses()) {
			
			// for each section in the course
			for (Section s : c.getSections()) {
				Lecture l = s.getLecture();
				
				// try 10 times to fulfill constr
				for (int i=0; i < 10; i++) {
					int rand = ThreadLocalRandom.current().nextInt(0, data.getLectureSlots().size());
					Slot slot = data.getLectureSlots().get(rand);
					Assignment a = new Assignment(l, slot);
					Constr constr = new Constr(a, data);
					if (constr.check()) {
						data.getTimetable().addAssignment(a);
						break;
					}
					if (i >= 9) System.out.println("Course violated Constr");
				}
			}
		}
		
		// for each nonlecture in data
		for (NonLecture nl : data.getNonLectures()) {
			
			// try 10 times to fulfill constr
			for (int i=0; i < 10; i++) {
				int rand = ThreadLocalRandom.current().nextInt(0, data.getLabSlots().size());
				Slot slot = data.getLabSlots().get(rand);
				Assignment a = new Assignment(nl, slot);
				Constr constr = new Constr(a, data);
				if (constr.check()) {
					data.getTimetable().addAssignment(a);
					break;
				}
				if (i >= 9) System.out.println("Lab violated Constr");
			}
		}
	}
}
