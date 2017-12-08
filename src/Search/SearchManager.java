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
     * @param schedule The schedule
     */
    public SearchManager(Schedule schedule) {
        this.schedule = schedule;		// set the schedule
        solutions = new ArrayList<>();	// create solutions list
        nodeStack = new Stack<>();		// create stack
        best = null;	// initialize best solution
        bound = -1;		// initialize bound value
    }

    /**
     * Run the search
     * 
     * @return Solved schedule
     */
    public Schedule run() {

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
            if(optimal == null){
                System.out.println("!!!Unsolvable Problem");
                return null;
            }
            
            // print optimal schedule and eval breakdown
            Constr.printViolations(optimal);
            Eval.printBreakdown(optimal);
            optimal.printAssignments();
         
            
            return optimal;
        }
        
        // started with an invalid schedule
        else {
        	System.out.println("!!!Impossible Starting Schedule");
        	Constr.printViolations(schedule);
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
    	long maxTime = 1 * 60 * 1000; // 5 minutes in milliseconds
    	
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
    		else if (bound == -1 || System.currentTimeMillis() - startTime < maxTime) {
    			n.generateNodes(bound);
    			nodeStack.addAll(n.getChildNodes());
    		}
    		
    		// end if best eval is zero
    		if (bound == 0) break;
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
     * Add a solution to the list
     * 
     * @param s Solved schedule
     */
    public void addSolution(Schedule s) { solutions.add(s); }
    
    /**
     * Get the solutions list
     * 
     * @return Solutions list
     */
    public ArrayList<Schedule> getSolutions() { return solutions; }
}
