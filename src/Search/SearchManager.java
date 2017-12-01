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

import java.util.ArrayList;
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
    private ArrayList<Schedule> solutions;

    /**
     * Constructor
     *
     * @param schedule
     */
    public SearchManager(Schedule schedule) {
        this.schedule = schedule;
        solutions = new ArrayList<>();
    }

    /**
     * Run the search
     */
    public Schedule run() {

    	// find the best solution
        if (schedule.isValid() && schedule.isPossible()) {
        	
        	// convert assignments list to array
        	//schedule.generateAssignmentArray();
        	//schedule.clearAssignments();
        	
        	// create the root node
        	Node rootNode = new Node(schedule, this);
        	
        	// get the first solution quickly (depth-first search)
        	bound = -1;
            Schedule first = rootNode.runSearch();
            solutions.get(0).printAssignments();
            
            // check if valid (meets hard constraints)
            Constr.printViolations(solutions.get(0));

            // print eval breakdown
            Eval.printBreakdown(solutions.get(0));
            
            // run the whole search using the bound value we got
            // 	best-first search repeatedly until root node is solved (i.e. whole tree solved)
            // 	(each complete valid solution is added to the solutions list)
            bound = solutions.get(0).eval();//first.eval();
            Schedule tmp = null;
            do {
            	tmp = rootNode.runSearch();
            	System.out.println("DONE");
            } while (!rootNode.isSolved());
            
            // get the optimal solution
            Schedule optimal = null;
            for (Schedule s : solutions) {
            	if (optimal == null || s.eval() < optimal.eval())
            		optimal = s;
            }
            
            // print optimal stuff
            optimal.printAssignments();
            Constr.printViolations(optimal);
            Eval.printBreakdown(optimal);
            
            //for (Node child : rootNode.getChildNodes()) {
            //	System.out.println("\n"+child.getID());
            //	Eval.printBreakdown(child.getSchedule());
            //}
            
            System.out.println("Got "+solutions.size()+" solns total");
            return optimal;
        }
        
        else {
        	System.out.println("Impossible starting schedule");
        	return null;
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
     * Get the bound value
     * 
     * @return Bound value
     */
    public int getBound() {
    	return bound;
    }
    
    /**
     * Set the bound value
     * 
     * @param bound Bound value
     */
    public void setBound(int bound) {
    	this.bound = bound;
    }
    
    /**
     * @param s
     */
    public void addSolution(Schedule s) {
    	solutions.add(s);
    }
    
    public ArrayList<Schedule> getSolutions() {
    	return solutions;
    }
}
