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

/**
 * Object for managing and executing the search
 *
 */
public class SearchManager {
	
	// the search instance
	private SearchData data;
	
	/**
	 * constructor
	 * @param sd
	 */
	public SearchManager(SearchData sd) {
		this.data = sd;
	}
	
	/**
	 * run the search
	 */
	public void run() {
		
		// add test input
		addRandomInput();
		
		// assign randomly
		assignRandom();
		
		// print timetable
		data.getTimetable().printAssignments();
		
		// print eval breakdown
		Eval eval = new Eval(data);		
		System.out.println("Cmin = "+eval.getCourseMinEval());
		System.out.println("Lmin = "+eval.getLabMinEval());
		System.out.println("Pref = "+eval.getPrefEval());
		System.out.println("Pair = "+eval.getPairEval());
		System.out.println("Sect = "+eval.getSecDiffEval());
		System.out.println("EVAL = "+eval.getEval());
	}
	
	/**
	 * return the search data
	 * @return
	 */
	public SearchData getData() {
		return this.data;
	}
	
	/**
	 * fill the timetable randomly for testing
	 */
	private void assignRandom() {
		
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
	
	/**
	 * add a random entry to each special input list
	 */
	private void addRandomInput() {
		
		// noncompatible
		int rand = ThreadLocalRandom.current().nextInt(0, data.getCourses().size());
		Course c = data.getCourses().get(rand);
		rand = ThreadLocalRandom.current().nextInt(0, c.getSections().size());
		Section s = c.getSections().get(rand);
		Lecture l1 = s.getLecture();
		rand = ThreadLocalRandom.current().nextInt(0, data.getCourses().size());
		c = data.getCourses().get(rand);
		rand = ThreadLocalRandom.current().nextInt(0, c.getSections().size());
		s = c.getSections().get(rand);
		Lecture l2 = s.getLecture();
		data.addNoncompatible(l1, l2);
		
		// pair
		rand = ThreadLocalRandom.current().nextInt(0, data.getCourses().size());
		c = data.getCourses().get(rand);
		rand = ThreadLocalRandom.current().nextInt(0, c.getSections().size());
		s = c.getSections().get(rand);
		l1 = s.getLecture();
		rand = ThreadLocalRandom.current().nextInt(0, data.getCourses().size());
		c = data.getCourses().get(rand);
		rand = ThreadLocalRandom.current().nextInt(0, c.getSections().size());
		s = c.getSections().get(rand);
		l2 = s.getLecture();
		data.addPair(l1, l2);
		
		// unwanted
		rand = ThreadLocalRandom.current().nextInt(0, data.getCourses().size());
		c = data.getCourses().get(rand);
		rand = ThreadLocalRandom.current().nextInt(0, c.getSections().size());
		s = c.getSections().get(rand);
		l1 = s.getLecture();
		rand = ThreadLocalRandom.current().nextInt(0, data.getLectureSlots().size());
		LectureSlot ls = data.getLectureSlots().get(rand);
		data.addUnwanted(l1, ls);
		
		// partassign
		rand = ThreadLocalRandom.current().nextInt(0, data.getCourses().size());
		c = data.getCourses().get(rand);
		rand = ThreadLocalRandom.current().nextInt(0, c.getSections().size());
		s = c.getSections().get(rand);
		l1 = s.getLecture();
		rand = ThreadLocalRandom.current().nextInt(0, data.getLectureSlots().size());
		ls = data.getLectureSlots().get(rand);
		data.addPartassign(l1, ls);
		
		// preference
		int pref = 100;
		rand = ThreadLocalRandom.current().nextInt(0, data.getCourses().size());
		c = data.getCourses().get(rand);
		rand = ThreadLocalRandom.current().nextInt(0, c.getSections().size());
		s = c.getSections().get(rand);
		l1 = s.getLecture();
		rand = ThreadLocalRandom.current().nextInt(0, data.getLectureSlots().size());
		ls = data.getLectureSlots().get(rand);
		data.addPreference(l1, ls, pref);
	}
}
