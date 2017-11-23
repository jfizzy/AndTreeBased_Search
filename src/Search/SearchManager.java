package Search;

import Schedule.Assignment;
import Schedule.Course;
import Schedule.Lecture;
import Schedule.LectureSlot;
import Schedule.NonLecture;
import Schedule.Schedule;
import Schedule.Section;
import Schedule.Slot;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Object for managing and executing the search
 *
 */
public class SearchManager {
	
	//----------------------------------------------------------------
	// TODO for the actual search:
	
	// create tree/node classes, data structures
	
	// create functions for adding/removing nodes, traversing tree
	
	// implement a way to tell if the goal condition is met
	
	// implement and-tree search (branch and bound):
	
		// start at the root node with no assignments
		
		// generate all possible branches - each represents one added assignment
		//     (branches must satisfy Constr)
		
		// do a depth-first search to determine the bound value
		//     (find the first valid solution quickly, then set bound to its Eval value)
		
		// go back to the root node
		
		// take branch with the lowest Eval
		//     (close off branches if Eval greater than bound)
		
		// generate all possible branches for the new node
		
		// repeat until a solution is found, backtracking if necessary
		
		// if solution Eval < bound, set bound to new Eval value
		
		// return to root node, evaluate all possible solutions with Eval < bound
		//     (final solution = lowest Eval leaf)
	
	//----------------------------------------------------------------
	
	private Schedule schedule; 	// all the data required for the search
	private int bound;			// the bound value
	
	/**
	 * Constructor
	 * @param sd Search schedule
	 */
	public SearchManager(Schedule schedule) {
		this.schedule = schedule;
	}
	
	/**
	 * Run the search
	 */
	public void run() {
		
		// add random test input (noncompatible, etc)
		//addRandomInput();
		
		// assign schedule randomly
		//assignRandom(); 
		// commented this out since the schedule is now being created
                
		// print the assignments
		schedule.printAssignments();
		
		// print eval breakdown
		Eval eval = new Eval(schedule);		
		eval.printBreakdown();
		
		// check if valid (meets hard constraints)
		Constr constr = new Constr(schedule);
		constr.printViolations();
	}
	
	/**
	 * Return the schedule
	 * @return Schedule
	 */
	public Schedule getSchedule() {
		return this.schedule;
	}
	
	/**
	 * Fill the timetable randomly for testing
	 */
	private void assignRandom() {
		int max = 50;	// number of times to try to get a valid assignment
		
		// for each course in schedule
		for (Course c : schedule.getCourses()) {
			
			// for each section in the course
			for (Section s : c.getSections()) {
				Lecture l = s.getLecture();
				
				// try max times to fulfill constr
				for (int i=0; i < max; i++) {
					
					// make a random assignment for the course
					int rand = ThreadLocalRandom.current().nextInt(0, schedule.getLectureSlots().size());
					Slot slot = schedule.getLectureSlots().get(rand);
					Assignment a = new Assignment(l, slot);
					
					// add the assignment, checking if it is valid
					Constr constr = new Constr(a, schedule);
					if (constr.check()) {
						schedule.addAssignment(a);
						break;
					}
					if (i == max-1) {
						System.out.println("Course violated Constr");
						schedule.addAssignment(a); // add anyway
					}
					
				}
			}
		}
		
		// for each nonlecture in schedule
		for (NonLecture nl : schedule.getNonLectures()) {
			
			// try max times to fulfill constr
			for (int i=0; i < max; i++) {
				
				// make a random assignment for the nonlecture
				int rand = ThreadLocalRandom.current().nextInt(0, schedule.getNonLectureSlots().size());
				Slot slot = schedule.getNonLectureSlots().get(rand);
				Assignment a = new Assignment(nl, slot);
				
				// add the assignment, checking if it is valid
				Constr constr = new Constr(a, schedule);
				if (constr.check()) {
					schedule.addAssignment(a);
					break;
				}
				if (i == max-1) {
					System.out.println("Lab violated Constr");
					schedule.addAssignment(a); // add anyway
				}
			}
		}
	}
	
	/**
	 * Add a random entry to each special input list (noncompatible, etc)
	 * 
	 */
	private void addRandomInput() {
		
		// noncompatible (random course, random course)
		int rand = ThreadLocalRandom.current().nextInt(0, schedule.getCourses().size());
		Course c = schedule.getCourses().get(rand);
		rand = ThreadLocalRandom.current().nextInt(0, c.getSections().size());
		Section s = c.getSections().get(rand);
		Lecture l1 = s.getLecture();
		rand = ThreadLocalRandom.current().nextInt(0, schedule.getCourses().size());
		c = schedule.getCourses().get(rand);
		rand = ThreadLocalRandom.current().nextInt(0, c.getSections().size());
		s = c.getSections().get(rand);
		Lecture l2 = s.getLecture();
		l1.addIncompatibility(l2);
		
		// pair (random course, random course)
		rand = ThreadLocalRandom.current().nextInt(0, schedule.getCourses().size());
		c = schedule.getCourses().get(rand);
		rand = ThreadLocalRandom.current().nextInt(0, c.getSections().size());
		s = c.getSections().get(rand);
		l1 = s.getLecture();
		rand = ThreadLocalRandom.current().nextInt(0, schedule.getCourses().size());
		c = schedule.getCourses().get(rand);
		rand = ThreadLocalRandom.current().nextInt(0, c.getSections().size());
		s = c.getSections().get(rand);
		l2 = s.getLecture();
		l1.addPaired(l2);
		
		// unwanted (random course, random slot)
		rand = ThreadLocalRandom.current().nextInt(0, schedule.getCourses().size());
		c = schedule.getCourses().get(rand);
		rand = ThreadLocalRandom.current().nextInt(0, c.getSections().size());
		s = c.getSections().get(rand);
		l1 = s.getLecture();
		rand = ThreadLocalRandom.current().nextInt(0, schedule.getLectureSlots().size());
		LectureSlot ls = schedule.getLectureSlots().get(rand);
		l1.addUnwanted(ls);
		
		// partassign (random course, random slot)
		rand = ThreadLocalRandom.current().nextInt(0, schedule.getCourses().size());
		c = schedule.getCourses().get(rand);
		rand = ThreadLocalRandom.current().nextInt(0, c.getSections().size());
		s = c.getSections().get(rand);
		l1 = s.getLecture();
		rand = ThreadLocalRandom.current().nextInt(0, schedule.getLectureSlots().size());
		ls = schedule.getLectureSlots().get(rand);
		l1.setPartassign(ls);
		
		// preference (random course, random slot, value)
		int pref = 100;
		rand = ThreadLocalRandom.current().nextInt(0, schedule.getCourses().size());
		c = schedule.getCourses().get(rand);
		rand = ThreadLocalRandom.current().nextInt(0, c.getSections().size());
		s = c.getSections().get(rand);
		l1 = s.getLecture();
		rand = ThreadLocalRandom.current().nextInt(0, schedule.getLectureSlots().size());
		ls = schedule.getLectureSlots().get(rand);
		l1.addPreference(ls, pref);
	}
}
