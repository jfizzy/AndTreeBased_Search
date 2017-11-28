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

/**
 * Node class - defines the fields and methods for a node of an And-tree
 *
 * @author
 */
public class Node {
	
    // A search tree is composed of a schedule, and it's sub tree, these need to be parameters
    // Additionally, there is a parent to the tree, and root should have a null parent

	private final ArrayList<Node> children;	// list of child nodes
    private Schedule schedule;	// schedule for this node
    private boolean solEntry;	// solution entry (true = yes, false = ?)
    private Node parent;		// reference to the parent node (null if root node)
    private Assignment start;	// the assignment to try for this node's branches
    private String id;			// identifier string for the node
    private int depth;			// level of the tree the node is at

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
        start = schedule.findFirstNull();
        id = "ROOT";
        depth = 0;
    }
    
    /**
     * Constructor for child nodes
     * 
     * @param s Schedule
     * @param n Parent node
     * @param id Node identifier string
     */
    public Node(Schedule s, Node n, String id, int depth) {
        schedule = s;
        solEntry = false;
        children = new ArrayList<>();
        parent = n;
        start = schedule.findFirstNull();
        this.id = id;
        this.depth = depth;
    }
    
    /**
     * Main recursive search function which is run on each node
     * 
     * @param depthFirst
     * @param depth
     * @return
     */
    Schedule runSearch(int bound) {
    	
    	//schedule.printAssignments();
    	
    	// check if the schedule is complete
        if (start == null || schedule.isComplete()) {
            System.out.println("the schedule is full!");
            return schedule;
        }
        
        // generate children if we didn't yet for this node
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
	            		
	            		// add the new child node
	            		Schedule s = new Schedule(schedule, l, ls);
	            		String id = l.toString()+" "+ls.toString();
	            		Node n = new Node(s, this, id, depth+1);
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
	            		
	            		// add the new child node
	            		Schedule s = new Schedule(schedule, nl, nls);
	            		String id = nl.toString()+" "+nls.toString();
	            		Node n = new Node(s, this, id, depth+1);
	            		this.addChildNode(n);
	            	}
	            }
	        }
        }
        
        // depth-first search (get the first solution quickly)
        if (bound == 0) {
        	
        	// print number of children, depth in tree, id of current node (meeting and slot)
            System.out.println(children.size() +" "+depth+" "+id);
        	
        	// loop until we get a result or we run out of unsolved child nodes to try
        	// 	(either result will be set to something non-null
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
	        	
	        	// if we did not find an unsolved branch, set solved,
	        	// 	clear children and return to parent
	        	if (choice == null) {
	        		
	        		//System.out.println("x "+depth);
	        		this.setSolved();
	        		children.clear();
	        		return null;
	        	}
	
	        	// recurse search on chosen child node:
	        	// 	if result is null (meaning all branches of that node solved),
	        	// 	we will loop to the next choice
	        	result = choice.runSearch(0);
	        	if (result == null) {
	        		
	        		choice.setSolved();
	        		//children.remove(choice);
	        		System.out.println("- "+depth);
	        	}
        	}
        		
        	return result;
        }
        
        // normal search (go through entire tree)
        else {
        	
        	// TODO
        	/*
        	SubNode choice = null;
	        for (Node ch : this.getChildNodes()) {
	        	
	        	SubNode child = (SubNode) ch;
	        	if (child.isSolved())
	        		continue;
	        	else if (choice == null) 
	        		choice = child;
	        	else if (choice.eval() > child.eval())
	        		choice = child;
	        }
	        */
        	
	        return schedule;
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
}
