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
import java.util.Collections;

/**
 * Node class - defines the fields and methods for a node of an And-tree
 *
 * @author
 */
public class Node {
	
    // A search tree is composed of a schedule, and it's sub tree, these need to be parameters
    // Additionally, there is a parent to the tree, and root should have a null parent
	
	private static int bound;

	private final ArrayList<Node> children;	// list of child nodes
    private Schedule schedule;	// schedule for this node
    private boolean solEntry;	// solution entry (true = yes, false = ?)
    private Node parent;		// reference to the parent node (null if root node)
    private Assignment start;	// the assignment to try for this node's branches
    private String id;			// identifier string for the node
    private int depth;			// level of the tree the node is at
    private int eval;

    /**
     * Constructor for the root node
     * 
     * @param s Schedule
     */
    public Node(Schedule s) {
        schedule = s;
        solEntry = false;
        children = new ArrayList<>();
        parent = null;
        start = schedule.findFirstNull(); // get first null assignment
        id = "ROOT";
        depth = 0;
        eval = -1;
        bound = 0;
    }
    
    /**
     * Constructor for child nodes
     * 
     * @param s Schedule
     * @param n Parent node
     * @param id Node identifier string
     * @param depth The depth within the tree
     */
    public Node(Schedule s, Node n, String id, int depth) {
        schedule = s;
        solEntry = false;
        children = new ArrayList<>();
        parent = n;
        start = schedule.findFirstNull(); // get first null assignment
        this.id = id;
        this.depth = depth;
        eval = -1;
    }
    
    /**
     * Main recursive search function which is run on each node.
     * 
     * @param bound Bound value
     * @return The schedule
     */
    Schedule runSearch(int boundVal) {
    	
    	/*
		 * The first time, it is run on the root node.
		 * 
		 * The function calls itself on child nodes to progress down the tree.
		 * 
		 * If there are no options to go down the tree, the function returns null,
		 * which is detected by the parent node (which called the function), then
		 * the next option at the parent node is taken.
		 * 
		 * If there are no other options to take, then the parent node's function
		 * returns null and ITS parent takes another option.
		 * 
		 * Each recursive call ultimately returns the schedule found by its recursive
		 * call, so that in the end the original function call will return the
		 * solved schedule.
    	 */
    	
    	if (bound > boundVal || bound == 0) 
    		bound = boundVal;
    	
    	// print the assignments for this node
    	//schedule.printAssignments();
    	
    	// check if the schedule is complete, if so return it
        if (start == null || schedule.isComplete()) {
            System.out.println("the schedule is full!");
            
            // if the eval is better than bound, reset the bound
            if (bound > this.getEval() || bound == 0) 
        		bound = this.getEval();
            
            return schedule;
        }
        
        // generate child nodes if we didn't yet for this node
        // and if this node is not solved yet
        if (children.size() == 0 && !this.isSolved()) {
        	
        	// if we are assigning a lecture
	        if (start.getM() instanceof Lecture) {
	            Lecture l = (Lecture) start.getM();
	            
	            // TODO add special assignment possibility for 813, 913, etc
	            
	            // for each lecture slot we could assign it to
	            for (LectureSlot ls : schedule.getLectureSlots()){
	            	
	                // skip if the slot is inactive
	            	if (!ls.isActive()) continue;
	            	
	            	// check if the schedule would be valid if we made the assignment
	            	if (schedule.isValidWith(l, ls)) {
	            		
	            		// make the new child node if valid
	            		Schedule s = new Schedule(schedule, l, ls);
	            		String id = l.toString()+" "+ls.toString();
	            		Node n = new Node(s, this, id, depth+1);
	            		
	            		// skip making node if we have a bound value and the eval is greater
	            		if (bound > 0 && n.getEval() >= bound)
	            			continue;
	            		
	            		// add the new node
	            		this.addChildNode(n);
	            	}
	            }
	        }
	        
	        // if we are assigning a nonlecture
	        else {
	        	NonLecture nl = (NonLecture) start.getM();
	        	
	        	// for each nonlecture slot we could assign it to
	        	for (NonLectureSlot nls : schedule.getNonLectureSlots()){ 
	        		
	                // skip if the slot is inactive
	        		if (!nls.isActive()) continue;
	        		
	        		// check if the schedule would be valid if we made the assignment
	            	if (schedule.isValidWith(nl, nls)) {
	            		
	            		// make the new child node if valid
	            		Schedule s = new Schedule(schedule, nl, nls);
	            		String id = nl.toString()+" "+nls.toString();
	            		Node n = new Node(s, this, id, depth+1);
	            		
	            		// skip making node if we have a bound value and the eval is greater
	            		if (bound > 0 && n.getEval() >= bound)
	            			continue;
	            		
	            		// add the new node
	            		this.addChildNode(n);
	            	}
	            }
	        }
        }
        
        // depth-first search (get the first solution quickly to get a bound value)
        if (bound == 0) {
        	
        	// print :
        	// number of children,
        	// depth in tree, 
        	// id of current node (meeting and slot)
            System.out.println(children.size() +" "+depth+" "+id);
        	
        	// loop until we get a result or we run out of unsolved child nodes to try
        	// 	(either result will be set to a non-null schedule
        	// 	or it will run out of choices and return out of the function)
        	Schedule result = null;
        	while (result == null) {
        		
        		// for the depth first search, solved can only mean that we
        		// 	couldn't find a solution down that branch of the tree
        		
        		// choose the first unsolved branch
	        	Node choice = null;
	        	for (Node n : children) {
	        		if (!n.isSolved()) {
	        			choice = n;
	        			break;
	        		}
	        	}
	        	
	        	// if we did not find an unsolved branch, set this node solved,
	        	// 	clear this node's children, and return to parent
	        	if (choice == null) {
	        		
	        		//System.out.println("x "+depth);
	        		solEntry = true;
	        		children.clear();
	        		return null;
	        	}
	
	        	// recurse search on chosen child node:
	        	// 	if result is null (meaning all branches of that node are solved),
	        	// 	we will loop to the next choice (if any remain)
	        	result = choice.runSearch(0);
	        	if (result == null) {
	        		choice.setSolved();
	        		//children.remove(choice);
	        		System.out.println("- "+depth);
	        		//if (parent != null) return null;
	        	}
        	}
        		
        	// return the result we got
        	return result;
        }
        
        // normal search with bound value (go through the entire tree)
        else {
        	
        	// print :
        	// number of children,
        	// depth in tree, 
        	// id of current node (meeting and slot)
            System.out.println(children.size() +" "+depth+" "+id+" "+bound);
        	
        	// for each child node
        	Schedule best = null;
        	//Collections.shuffle(children);
        	for (Node n : children) {
        		
        		// skip if solved
        		if (n.isSolved()) continue;
        		
        		// run search for the node
        		Schedule tmp = n.runSearch(bound);
        		
        		// reset bound value if complete and better
        		int t;
        		if (tmp != null) {
        			
        			if (tmp.isComplete() && (t = tmp.eval()) < bound) 
        				bound = t;
        		
        			// save the best of the schedules
        			if (best == null || tmp.eval() < best.eval())
        				best = tmp;
        		}
        		
        		// set the node to solved
        		n.setSolved();
        	}
        	
        	// if there are no unsolved child nodes, set solved,
        	// clear child nodes, and return to parent
        	if (best == null) {
        		solEntry = true;
        		//children.clear();
        		return null;
        	}
        	
        	solEntry = true;
	        return best;
        }
    }

    /*
     * Getters and Setters
     */
    
    /**
     * @return
     */
    public Schedule getSchedule() {
        return this.schedule;
    }

    /**
     * @param s
     */
    public void setSchedule(Schedule s) {
        this.schedule = s;
    }
    
    /**
     * @return
     */
    public boolean isSolved() {
        return this.solEntry;
    }
    
    /**
     * 
     */
    public void setSolved(){
        this.solEntry = true;
    }

    /**
     * @param n
     */
    public void addChildNode(Node n){
        this.children.add(n);
    }
    
    /**
     * @return
     */
    public ArrayList<Node> getChildNodes(){
        return this.children;
    }
    
    /**
     * @param n
     */
    public void setParent(Node n) {
    	parent = n;
    }
    
    /**
     * @return
     */
    public Node getParent() {
    	return parent;
    }
    
    /**
     * @return
     */
    public int getEval() {
    	
    	if (eval == -1)
    		eval = schedule.eval();
    	return eval;
    }
}
