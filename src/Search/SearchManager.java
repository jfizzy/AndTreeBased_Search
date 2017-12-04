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
import java.util.Stack;

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
    private double bound;			// the bound value
    private ArrayList<Schedule> solutions;
    
    private Stack<Node> nodestack;
    private Schedule best;

    /**
     * Constructor
     *
     * @param schedule
     */
    public SearchManager(Schedule schedule) {
        this.schedule = schedule;
        solutions = new ArrayList<>();
        nodestack = new Stack<>();
        best = null;
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
        	bound = -1;
        	
        	// solve with stack method
        	nodestack.push(rootNode);
        	Schedule test = stackSolve(); // see below this function
        	
        	/* THIS IS FOR THE RECURSIVE METHOD
        	 * 
        	// get the first solution quickly (depth-first search)
            //Schedule first = rootNode.runSearch();
            //solutions.get(0).printAssignments();
            Constr.printViolations(solutions.get(0));
            Eval.printBreakdown(solutions.get(0));
            
            // run the whole search using the bound value we got
            // 	best-first search repeatedly until root node is solved (i.e. whole tree solved)
            // 	(each complete valid solution is added to the solutions list)
        	Schedule tmp = rootNode.runSearch();
        	System.out.println("DONE");
        	*/
            
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
            System.out.println("Got "+solutions.size()+" solns total");
            return optimal;
        }
        
        // started with an invalid schedule
        else {
        	System.out.println("Impossible starting schedule");
        	return null;
        }
    }
    
    /**
     * Get the optimal solution using the stack method
     * @return Optimal schedule
     */
    public Schedule stackSolve() {
    	
    	// repeat until stack is empty (begins with root node)
    	while (!nodestack.isEmpty()) {
    		
    		// pop the top node off the stack
    		Node n = nodestack.pop();
    		
    		// print node info
        	System.out.println("["+n.getDepth()+"] "+n.getID()+" ("+solutions.size()
        						+" solns) best="+bound+" stacksize="+nodestack.size());
        	
        	// if we have a bound, skip if the node is worse
        	if (bound > -1 && n.getEval() >= bound && n.getEval() < Node.startEval) continue;
        		
        	// if the node's schedule is fully assigned
    		if (n.getSchedule().isComplete()) {
    			
    			// set the new bound, add the solution to the list
    			bound = n.getEval();
    			solutions.add(n.getSchedule());
    			
    			// save the best schedule
    			if (best == null || best.eval() > n.getEval())
    				best = n.getSchedule();
    		}
    		
    		// otherwise generate child nodes and add them to the stack
    		else {
    			n.generateNodes(bound);
    			nodestack.addAll(n.getChildNodes());
    		}
    	}
    	
    	// return the best complete valid schedule we got
    	return best;
    }
    
    /**
     * Return the schedule
     *
     * @return Schedule
     */
    public Schedule getSchedule() { return this.schedule; }
    
    /**
     * Get the bound value
     * 
     * @return Bound value
     */
    public double getBound() { return bound; }
    
    /**
     * Set the bound value
     * 
     * @param bound Bound value
     */
    public void setBound(double bound) { this.bound = bound; }
    
    /**
     * @param s
     */
    public void addSolution(Schedule s) { solutions.add(s); }
    
    /**
     * @return
     */
    public ArrayList<Schedule> getSolutions() { return solutions; }
}
