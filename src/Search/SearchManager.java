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
	
    static double startEval;		// the eval of the initial schedule (root node schedule)
    private Schedule schedule; 		// all the data required for the search, plus assignments list
    private Stack<Node> nodeStack;	// stack for processing nodes
    private double bound;			// the bound value (best solution eval so far)
    private Schedule best;			// the best complete schedule found so far
    private ArrayList<Schedule> solutions;	// list of found solutions

    /**
     * Constructor
     *
     * @param schedule
     */
    public SearchManager(Schedule schedule) {
        this.schedule = schedule;
        solutions = new ArrayList<>();
        nodeStack = new Stack<>();
        best = null;
        bound = -1;
    }

    /**
     * Run the search
     */
    public Schedule run() {
    	
    	// assign meetings with 3+ preferences first
    	schedule.assignPreferences();

    	// check if initial schedule is valid and solvable
        if (schedule.isValid() && schedule.isPossible()) {
        	
        	// create the root node
        	Node rootNode = new Node(schedule, this);
        	
        	// solve with stack method
        	nodeStack.push(rootNode);
        	stackSolve();
            
            // get the best solution in the list
            Schedule optimal = null;
            for (Schedule s : solutions) {
            	if (optimal == null || s.eval() < optimal.eval())
            		optimal = s;
            }
            
            // print optimal schedule and eval breakdown
            optimal.printAssignments(); // TODO print in alphabetical order
            Constr.printViolations(optimal);
            Eval.printBreakdown(optimal);
            
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
     * 
     * @return Optimal schedule
     */
    public Schedule stackSolve() {
    	
    	// get the initial time
    	long startTime = System.currentTimeMillis();
    	long maxTime = 5 * 60 * 1000; // 5 minutes in milliseconds
    	
    	// repeat until stack is empty (begins with just root node on the stack)
    	while (!nodeStack.isEmpty()) {
    		
    		// pop the top node off the stack
    		Node n = nodeStack.pop();
        	
        	// if we have a bound, drop node if worse than bound and initial eval
        	if (bound > -1 && n.getEval() >= bound && n.getEval() > startEval) 
        		continue;
        	
    		// print node info
        	System.out.println("["+n.getDepth()+"] "+n.getID()+" ("+solutions.size()
        						+" solns)  stacksize="+nodeStack.size()+"  best="+bound
        						+"  eval="+n.getEval());
        		
        	// check if the node's schedule is fully assigned
    		if (n.getSchedule().isComplete()) {
    			
    			// set the new bound, add the solution to the list
    			if (bound == -1 || n.getEval() < bound) {
    				bound = n.getEval();
        			solutions.add(n.getSchedule());
    			}
    			//System.out.println("GOT SOLUTION   eval="+n.getEval()+"   ("+solutions.size()+" solns)");
    			
    			// save the best schedule
    			if (best == null || best.eval() > n.getEval())
    				best = n.getSchedule();
    		}
    		
    		// otherwise generate child nodes and add them to the top of the stack
    		// (create all valid children that have eval < bound)
    		else {
    			n.generateNodes(bound);
    			nodeStack.addAll(n.getChildNodes());
    		}
    		
    		// end if we have run out of time or if best eval is zero
    		if (System.currentTimeMillis() - startTime > maxTime || bound == 0) 
    			break;
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
