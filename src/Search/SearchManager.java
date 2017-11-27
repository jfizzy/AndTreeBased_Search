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
        // print the assignments
        this.schedule.printAssignments();

        // check if valid (meets hard constraints)
        Constr.printViolations(this.schedule);

        // print eval breakdown
        Eval.printBreakdown(this.schedule);

        if (schedule.isValid()) {
            SearchProcess sp = new SearchProcess(this.schedule);
            Schedule sol = sp.run(); // find the best solution
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
                    Assignment a = new Assignment(l, slot);

                    // add the assignment, checking if it is valid
                    if (Constr.check(this.schedule, a)) {
                        schedule.addAssignment(a);
                        break;
                    }
                    if (i == max - 1) {
                        System.out.println("Course violated Constr");
                        schedule.addAssignment(a); // add anyway
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
                Assignment a = new Assignment(nl, slot);

                // add the assignment, checking if it is valid
                if (Constr.check(this.schedule, a)) {
                    schedule.addAssignment(a);
                    break;
                }
                if (i == max - 1) {
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
