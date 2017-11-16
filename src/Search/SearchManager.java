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
		
		// assign randomly
		addrandom();
		
		// print timetable
		data.getTimetable().printAssignments();
		
		// print eval breakdown
		Eval eval = new Eval(data);
		int e1 = eval.getCourseMinEval();
		int e2 = eval.getLabMinEval();
		int e3 = eval.getPrefEval();
		int e4 = eval.getPairEval();
		int e5 = eval.getSecDiffEval();
		int sum = e1+e2+e3+e4+e5;
		
		System.out.println("Cmin = "+e1);
		System.out.println("Lmin = "+e2);
		System.out.println("Pref = "+e3);
		System.out.println("Pair = "+e4);
		System.out.println("Sect = "+e5);
		System.out.println("EVAL = "+sum);
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
