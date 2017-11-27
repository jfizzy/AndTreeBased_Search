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

import Schedule.Assignment;
import Schedule.Lecture;
import Schedule.LectureSlot;
import Schedule.NonLecture;
import Schedule.NonLectureSlot;
import Schedule.Schedule;
import java.util.ArrayList;

/**
 * Node. This is an abstract class defining the common elements the Node type
 *
 * @author
 */
public class Node {
    //A search tree is composed of a schedule, and it's sub tree, these need to be parameters
    //Additionally, there is a parent to the tree, and root should have a null parent

    private Schedule schedule;
    private boolean solEntry;
    private final ArrayList<Node> children;
    private Node parent;

    /**
     * Constructor for root node
     * 
     * @param s
     */
    public Node(Schedule s) {
        schedule = s;
        solEntry = false;
        children = new ArrayList<>();
        parent = null;
    }
    
    /**
     * Constructor for child nodes
     * 
     * @param s
     */
    public Node(Schedule s, Node n) {
        schedule = s;
        solEntry = false;
        children = new ArrayList<>();
        parent = n;
    }
    
    /**
     * 
     */
    Schedule runSearch(boolean depthFirst, int depth) {
    	
    	//schedule.printAssignments();
    	
    	// pick the first null assignment
        Assignment start = schedule.findFirstNull();
        if (start == null || schedule.isComplete()) {
            System.out.println("the schedule is full!");
            return schedule;
        }
        
        // generate children if we didn't yet for this node
        if (children.size() == 0) {
	        if (start.getM() instanceof Lecture) { // lecture
	            Lecture l = (Lecture) start.getM();
	            
	            // TODO add special assignment possibility for 813, 913, etc
	            for (LectureSlot ls : schedule.getLectureSlots()){ // iterate over lecture slots
	                // try each one
	            	if (!ls.isActive()) continue;
	            	
	            	if (schedule.isValidWith(l, ls)) {
	            		this.addChildNode(new Node(new Schedule(schedule, l, ls), this));
	            	}
	            }
	        }
	        else { // nonlecture
	        	NonLecture nl = (NonLecture) start.getM();
	        	
	        	for (NonLectureSlot nls : schedule.getNonLectureSlots()){ 
	                // try each one
	        		if (!nls.isActive()) continue;
	        		
	            	if (schedule.isValidWith(nl, nls)) {
	            		this.addChildNode(new Node(new Schedule(schedule, nl, nls), this));
	            	}
	            }
	        }
        }
        
        // find possible assignments
        // evaluate leftmost first
        // recurse through all
        
        // depth-first search
        System.out.println(children.size() +" "+depth);
        if (depthFirst) {
        	
        	// either result will be set to something non-null
        	// or it will run out of choices and return out of the function
        	// (so the loop terminates)
        	Schedule result = null;
        	while (result == null) {
        		
        		// choose an unsolved branch
	        	Node choice = null;
	        	for (Node n : children) {
	        		if (!n.isSolved()) {
	        			choice = n;
	        			break;
	        		}
	        	}
	        	
	        	// if no unsolved branches return to parent
	        	if (choice == null) {
	        		this.setSolved();
	        		//children.clear();
	        		return null;
	        	}
	
	        	// recurse search on chosen child node
	        	// if result is null we will loop to the next choice
	        	result = choice.runSearch(true, depth+1);
	        	if (result == null)
	        		choice.setSolved();
        	}
        		
        	return result;
        }
        
        else {
        	// normal search
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
	        
        }
        return null;
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
