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
    
    private Schedule schedule; 		// all the data required for the search
    private double bound;			// the bound value
    private Stack<Node> nodestack;	// stack for processing nodes
    private Schedule best;			// the best schedule found so far
    private ArrayList<Schedule> solutions;	// list of found solutions

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
        bound = -1;
    }

    /**
     * Run the search
     */
    public Schedule run() {

    	// check if initial schedule is valid and solvable
        if (schedule.isValid() && schedule.isPossible()) {
        	
        	// create the root node
        	Node rootNode = new Node(schedule, this);
        	
        	// solve with stack method
        	nodestack.push(rootNode);
        	Schedule result = stackSolve(); // see below for this function
        	
        	Eval.printBreakdown(result);
        	
        	/* THIS IS FOR THE RECURSIVE METHOD
        	 * (currently not in use)
        	 
        	// get the first solution quickly (depth-first search)
            Schedule first = rootNode.runSearch();
            solutions.get(0).printAssignments();
            Constr.printViolations(solutions.get(0));
            Eval.printBreakdown(solutions.get(0));
            
            // run the whole search using the bound value we got
            // 	best-first search repeatedly until root node is solved (i.e. whole tree solved)
            // 	(each complete valid solution is added to the solutions list)
        	Schedule result = rootNode.runSearch();
        	*/
            
            // get the best solution in the list
            Schedule optimal = null;
            for (Schedule s : solutions) {
            	if (optimal == null || s.eval() < optimal.eval())
            		optimal = s;
            }
            
            // print optimal stuff
            optimal.printAssignments(); // TODO print in alphabetical order
            Constr.printViolations(optimal);
            Eval.printBreakdown(optimal);
            System.out.println("Got "+solutions.size()+" solns total");
            return optimal;
        }
        
        // started with an invalid schedule
        else {
        	System.out.println("Impossible starting schedule");
        	return schedule;
        }
    }
    
    /**
     * Get the optimal solution using the stack method
     * 
     * @return Optimal schedule
     */
    public Schedule stackSolve() {
    	
    	long startTime = System.currentTimeMillis();
    	long maxTime = 5 * 60 * 1000; // 5 minutes in milliseconds
    	
    	// repeat until stack is empty (begins with just root node on the stack)
    	while (!nodestack.isEmpty()) {
    		
    		// pop the top node off the stack
    		Node n = nodestack.pop();
        	
        	// if we have a bound, drop node if worse than bound and initial eval
        	if (bound > -1 && n.getEval() >= bound && n.getEval() > startEval) 
        		continue;
        	
    		// print node info
        	System.out.println("["+n.getDepth()+"] "+n.getID()+" ("+solutions.size()
        						+" solns)  stacksize="+nodestack.size()+"  best="+bound
        						+"  eval="+n.getEval());
        		
        	// check if the node's schedule is fully assigned
    		if (n.getSchedule().isComplete()) {
    			
    			// set the new bound, add the solution to the list
    			if (bound == -1 || n.getEval() < bound) bound = n.getEval();
    			solutions.add(n.getSchedule());
    			//System.out.println("GOT SOLUTION   eval="+n.getEval()+"   ("+solutions.size()+" solns)");
    			
    			// save the best schedule
    			if (best == null || best.eval() > n.getEval())
    				best = n.getSchedule();
    		}
    		
    		// otherwise generate child nodes and add them to the top of the stack
    		// (create all valid children that have eval < bound)
    		else {
    			n.generateNodes(bound);
    			nodestack.addAll(n.getChildNodes());
    		}
    		
    		// end if we have run out of time
    		//if (System.currentTimeMillis() - startTime > maxTime) 
    		//	break;
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
