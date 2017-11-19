package Search;

import Schedule.Assignment;
import Schedule.Course;
import Schedule.Lecture;
import Schedule.LectureSlot;
import Schedule.NonLecture;
import Schedule.Section;
import Schedule.Slot;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Object for managing and executing the search
 *
 */
public class SearchManager {
	
	// the search instance with all the data needed
	private SearchData data;
	
	/**
	 * Constructor
	 * @param sd Search data
	 */
	public SearchManager(SearchData sd) {
		this.data = sd;
	}
	
	/**
	 * Run the search
	 */
	public void run() {
		
		// add random test input (noncompatible, etc)
		//addRandomInput();
		
		// assign schedule randomly
		assignRandom();
		
		
		// TODO for the actual search:
		
		// start at root node with no assignments
		
		// generate all possible branches - one additional assignment each
		//     (branches must satisfy Constr)
		
		// depth-first search to determine bound value
		//     (find first valid solution quickly, bound = its Eval)
		
		// go back to root node
		
		// determine which other branches to take 
		//     (using Eval, don't take if greater than bound)
		
		// evaluate all possibilities with Eval < bound
		// repeat until optimal solution is found
		
		
		// print the assignments
		data.getTimetable().printAssignments();
		
		// print eval breakdown
		Eval eval = new Eval(data);		
		System.out.println("Cmin = "+eval.getCourseMinEval());
		System.out.println("Lmin = "+eval.getLabMinEval());
		System.out.println("Pref = "+eval.getPrefEval());
		System.out.println("Pair = "+eval.getPairEval());
		System.out.println("Sect = "+eval.getSecDiffEval());
		System.out.println("EVAL = "+eval.getEval());
		
		// check if valid (meets hard constraints)
		Constr constr = new Constr(data);
		constr.check(true); // true means print any violations
	}
	
	/**
	 * Return the search data
	 * @return Search data
	 */
	public SearchData getData() {
		return this.data;
	}
	
	/**
	 * Fill the timetable randomly for testing
	 */
	private void assignRandom() {
		int max = 50;	// number of times to try to get a valid assignment
		
		// for each course in data
		for (Course c : data.getCourses()) {
			
			// for each section in the course
			for (Section s : c.getSections()) {
				Lecture l = s.getLecture();
				
				// try max times to fulfill constr
				for (int i=0; i < max; i++) {
					
					// make a random assignment for the course
					int rand = ThreadLocalRandom.current().nextInt(0, data.getLectureSlots().size());
					Slot slot = data.getLectureSlots().get(rand);
					Assignment a = new Assignment(l, slot);
					
					// add the assignment, checking if it is valid
					Constr constr = new Constr(a, data);
					if (constr.check(false)) {
						data.getTimetable().addAssignment(a);
						break;
					}
					if (i == max-1) {
						System.out.println("Course violated Constr");
						data.getTimetable().addAssignment(a); // add anyway
					}
					
				}
			}
		}
		
		// for each nonlecture in data
		for (NonLecture nl : data.getNonLectures()) {
			
			// try max times to fulfill constr
			for (int i=0; i < max; i++) {
				
				// make a random assignment for the nonlecture
				int rand = ThreadLocalRandom.current().nextInt(0, data.getLabSlots().size());
				Slot slot = data.getLabSlots().get(rand);
				Assignment a = new Assignment(nl, slot);
				
				// add the assignment, checking if it is valid
				Constr constr = new Constr(a, data);
				if (constr.check(false)) {
					data.getTimetable().addAssignment(a);
					break;
				}
				if (i == max-1) {
					System.out.println("Lab violated Constr");
					data.getTimetable().addAssignment(a); // add anyway
				}
			}
		}
	}
	
	/**
	 * Add a random entry to each special input list (noncompatible, etc)
	 * for random meetings
	 */
	private void addRandomInput() {
		
		// noncompatible (random course, random course)
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
		l1.addIncompatibility(l2);
		
		// pair (random course, random course)
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
		l1.addPaired(l2);
		
		// unwanted (random course, random slot)
		rand = ThreadLocalRandom.current().nextInt(0, data.getCourses().size());
		c = data.getCourses().get(rand);
		rand = ThreadLocalRandom.current().nextInt(0, c.getSections().size());
		s = c.getSections().get(rand);
		l1 = s.getLecture();
		rand = ThreadLocalRandom.current().nextInt(0, data.getLectureSlots().size());
		LectureSlot ls = data.getLectureSlots().get(rand);
		l1.addUnwanted(ls);
		
		// partassign (random course, random slot)
		rand = ThreadLocalRandom.current().nextInt(0, data.getCourses().size());
		c = data.getCourses().get(rand);
		rand = ThreadLocalRandom.current().nextInt(0, c.getSections().size());
		s = c.getSections().get(rand);
		l1 = s.getLecture();
		rand = ThreadLocalRandom.current().nextInt(0, data.getLectureSlots().size());
		ls = data.getLectureSlots().get(rand);
		l1.setPartassign(ls);
		
		// preference (random course, random slot, value)
		int pref = 100;
		rand = ThreadLocalRandom.current().nextInt(0, data.getCourses().size());
		c = data.getCourses().get(rand);
		rand = ThreadLocalRandom.current().nextInt(0, c.getSections().size());
		s = c.getSections().get(rand);
		l1 = s.getLecture();
		rand = ThreadLocalRandom.current().nextInt(0, data.getLectureSlots().size());
		ls = data.getLectureSlots().get(rand);
		l1.addPreference(ls, pref);
	}
}
