/* 
 * CPSC 433 Fall 2017
 * 
 * And-Tree-Based Scheduling Problem Solver
 * 
 * Created by:
 * 
 * Evan Loughlin
 * Geordie Tait
 * James MacIsaac
 * Luke Kissick
 * Sidney Shane Dizon
 */
package Search;

import Schedule.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Object for managing and executing the search
 *
 */
public class SearchManager {

    //----------------------------------------------------------------
    // TODO for the actual search:
	
    // create tree/node classes, data structures, done, AndSearchTreeNode
    // create functions for adding/removing nodes, traversing tree, in AndSearchTreeNode
    // implement a way to tell if the goal condition is met, done, in Schedule
	
	//implement and-tree search (branch and bound):
	// start at the rootNode node with no assignments - done
	// generate all possible branches - each represents one added assignment
	//	     (branches must satisfy Constr) 
	//don't want to do this, it will take too long to generate everything first. You can generate locally. 
	// do a depth-first search to determine the bound value
	//	     (find the first valid solution quickly, then set bound to its Eval value)
	// go back to the rootNode node
	// take branch with the lowest Eval
	//	     (close off branches if Eval greater than bound)
	// generate all possible branches for the new node
	// if solution Eval < bound, set bound to new Eval value
	// return to rootNode node, evaluate all possible solutions with Eval < bound
	//	     (final solution = lowest Eval leaf)
	
    //----------------------------------------------------------------
    private Schedule schedule; 	// all the data required for the search
    private int bound;			// the bound value

    /**
     * Constructor
     *
     * @param schedule
     */
    public SearchManager(Schedule schedule) {
        this.schedule = schedule;
    }

    /**
     * Run the search
     */
    public void run() {

    	// find the best solution
        if (schedule.isValid() && schedule.isPossible()) {
        	
        	// convert assignments list to array
        	//schedule.generateAssignmentArray();
        	//schedule.clearAssignments();
        	
        	// get the first solution quickly (depth-first search)
        	// runSearch arg: bound = 0 for first run
        	Node rootNode = new Node(schedule);
            Schedule first = rootNode.runSearch(0);
            int bound = first.eval();
            first.printAssignments();
            
            // check if valid (meets hard constraints)
            Constr.printViolations(first);

            // print eval breakdown
            Eval.printBreakdown(first);
            
            // run the whole search using the bound value we got
            //Schedule optimal = rootNode.runSearch(bound);
            
            // check if valid (meets hard constraints)
            //Constr.printViolations(optimal);

            // print eval breakdown
            //Eval.printBreakdown(optimal);
        }
        
        else {
        	System.out.println("Impossible starting schedule");
        	return;
        }
    }

    /**
     * Return the schedule
     *
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
                for (int i = 0; i < max; i++) {

                    // make a random assignment for the course
                    int rand = ThreadLocalRandom.current().nextInt(0, schedule.getLectureSlots().size());
                    Slot slot = schedule.getLectureSlots().get(rand);

                    // add the assignment, checking if it is valid
                    if (Constr.check(this.schedule, l, slot)) {
                        schedule.addAssignment(l, slot);
                        break;
                    }
                    if (i == max - 1) {
                        System.out.println("Course violated Constr");
                        schedule.addAssignment(l, slot); // add anyway
                    }

                }
            }
        }

        // for each nonlecture in schedule
        for (NonLecture nl : schedule.getNonLectures()) {

            // try max times to fulfill constr
            for (int i = 0; i < max; i++) {

                // make a random assignment for the nonlecture
                int rand = ThreadLocalRandom.current().nextInt(0, schedule.getNonLectureSlots().size());
                Slot slot = schedule.getNonLectureSlots().get(rand);

                // add the assignment, checking if it is valid
                if (Constr.check(this.schedule, nl, slot)) {
                    schedule.addAssignment(nl, slot);
                    break;
                }
                if (i == max - 1) {
                    System.out.println("Lab violated Constr");
                    schedule.addAssignment(nl, slot); // add anyway
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
