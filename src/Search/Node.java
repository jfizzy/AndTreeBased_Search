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
import java.util.Comparator;
import java.util.Iterator;

/**
 * Node class - defines the fields and methods for a node of an And-tree
 *
 * @author
 */
public class Node implements Comparable<Node> {

    // A search tree is composed of a schedule, and it's sub tree, these need to be parameters
    // Additionally, there is a parent to the tree, and root should have a null parent
	
    private ArrayList<Node> children;	// list of child nodes
    private SearchManager sm;	// the search manager that initiated the seach
    private Schedule schedule;	// schedule for this node
    private boolean solEntry;	// solution entry (true = yes, false = ?)
    private Node parent;		// reference to the parent node (null if root node)
    private Assignment start;	// the assignment to try for this node's branches
    private String id;			// identifier string for the node
    private int depth;			// level of the tree the node is at
    private double eval;		// eval for this node (only calcd the first time, otherwise this var is checked)
    private Slot slot;			// the slot we are attempting to assign for this node
    
    /**
     * Constructor for the root node
     *
     * @param s Schedule
     */
    public Node(Schedule s, SearchManager sm) {
        schedule = new Schedule(s);
        this.sm = sm;
        solEntry = false;
        parent = null;
        slot = null;
        id = "ROOT";
        depth = 0;
        eval = Eval.getEval(s);
        children = new ArrayList<>();
        start = schedule.findFirstNull(); // get first null assignment
        SearchManager.startEval = getEval();
        
        //Collections.shuffle(schedule.getAssignments());
    }

    /**
     * Constructor for child nodes
     *
     * @param s Schedule
     * @param n Parent node
     * @param id Node identifier string
     * @param depth The depth within the tree
     */
    public Node(Schedule s, Node n, Slot slot, String id, int depth) {
        schedule = s;
        sm = n.getSM();
        solEntry = false;
        parent = n;
        this.slot = slot;
        this.id = id;
        this.depth = depth;
        eval = Eval.getEval(s);
        children = new ArrayList<>();
        start = schedule.findFirstNull(); // get first null assignment
    }
    
    /**
     * Generate all valid child nodes for this node.
     * If bound value is set, don't add nodes that are worse or equal
     */
    public void generateNodes(double bound) {

        // if we haven't generated the children yet and if the node isn't solved
        if (children.size() == 0 && !solEntry) {
        	

            // if we are assigning a lecture
            if (start.getM() instanceof Lecture) {
                Lecture l = (Lecture) start.getM();

                // for each lecture slot we could assign it to
                for (LectureSlot ls : schedule.getLectureSlots()) {

                    // skip if the slot is inactive
                    if (!ls.isActive()) {
                        continue;
                    }

                    // check if the schedule would be valid if we made the assignment
                    if (schedule.isValidWith(l, ls)) {

                        // make the new child node if valid
                        Schedule s = new Schedule(schedule, l, ls);
                        String id = l.toString() + " " + ls.toString();
                        Node n = new Node(s, this, ls, id, depth + 1);

                        // skip making node if we have a bound value and the eval is greater
                        if (bound > -1 && n.getEval() >= bound && n.getEval() > SearchManager.startEval) {
                            continue;
                        }

                        // add the new node
                        this.addChildNode(n);
                    }
                }
            } 
            
            // if we are assigning a nonlecture
            else {
                NonLecture nl = (NonLecture) start.getM();

                // for each nonlecture slot we could assign it to
                for (NonLectureSlot nls : schedule.getNonLectureSlots()) {

                    // skip if the slot is inactive
                    if (!nls.isActive()) {
                        continue;
                    }

                    // check if the schedule would be valid if we made the assignment
                    if (schedule.isValidWith(nl, nls)) {

                        // make the new child node if valid
                        Schedule s = new Schedule(schedule, nl, nls);
                        String id = nl.toString() + " " + nls.toString();
                        Node n = new Node(s, this, nls, id, depth + 1);

                        // skip making node if we have a bound value and the eval is greater or equal
                        if (bound > -1 && n.getEval() >= bound && n.getEval() > SearchManager.startEval) {
                            continue;
                        }

                        // add the new node
                        this.addChildNode(n);
                    }
                }
            }
            
            // sort child nodes by lowest eval first
            children.sort(NodeComparator);
        }
    }
    
    /* 
     * NOTE: Functions below are for the recursive method,
     * 			these are currently not in use!
     * 
     * See SearchManager for stack based method which is much simpler
     */
    
    /**
     * Main recursive search function which is run on each node
     *
     * @param bound Bound value
     * @return The schedule
     */
    public Schedule runSearch() {

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
    	
        // get the current bound value
        double bound = sm.getBound();
        
        // abort and return null if eval is worse than current bound
        if (bound > -1 && getEval() >= bound && getEval() < SearchManager.startEval) return null;
    	
    	// print node stuff 
        System.out.println("["+depth+"] " + id + " ("+sm.getSolutions().size()+" solns) bound="+bound);
        
        // print the assignments for this node
        //schedule.printAssignments();

        // check if the schedule is valid and complete, if so return it
        if (schedule.isComplete()) {

            // set this node solved
            this.setSolved();
            
            // add the schedule to the solutions list
            if (schedule.isValid()) { // && (getEval() < bound || bound == -1)) {
            	sm.addSolution(schedule);
            	
            	// if the eval is better than bound, reset the bound
                if (bound > getEval() || bound == -1) {
                    sm.setBound(getEval());
                    bound = getEval();
                }
            }

            System.out.println("the schedule is done!");
            return schedule;
        }

        // generate child nodes if we didn't yet for this node, sort by lowest eval
        generateNodes(bound);

        // depth-first search (get the first solution quickly to get a bound value)
        if (sm.getBound() == -1)
            return depthFirstSearch();
        
        // normal search with bound value (go through the entire tree)
        else return andTreeSearch();
    }
    
    /**
     * Run the And-Tree search at this node
     *
     * @param bound Bound value
     * @return The found schedule
     */
    private Schedule andTreeSearch() {
    	
        // try to get the best child node's search result
        Schedule best = null;
        
        for (Node n : children) {
        	
        	// get the current bound value
        	double bound = sm.getBound();
        	
        	// skip child node if worse than current bound
        	if (n.getEval() > bound && n.getEval() < SearchManager.startEval) {
        		n.setSolved();
        		continue;
        	}

            // run the search on the child node
            Schedule result = n.runSearch();
            
            // if we got a result
            if (result != null) {
            	
            	// set the node to solved
                n.setSolved();
            	
            	// set the bound value if the result is complete and better
                double resulteval = result.eval();
            	if (result.isComplete() && result.isValid() && resulteval < bound) 
	                sm.setBound(resulteval);

                // save the best of the results
                if (best == null || (resulteval < best.eval())) 
                    best = result;
            }
        }
        
        // set solved and return the best schedule from the child nodes (or null)
        this.setSolved();
        return best;
    }

    /**
     * Run the depth first search at this node
     *
     * @return The found schedule
     */
    private Schedule depthFirstSearch() {
    	
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
                this.setSolved();
                //children.clear();
                return null;
            }

            // recurse search on chosen child node:
            // 	if result is null (meaning all branches of that node are solved),
            // 	we will loop to the next choice (if any remain)
            
            result = choice.runSearch();
            if (result == null) {
                choice.setSolved();
                //children.remove(choice);
            }
        }

        // return the result we got
        return result;
    }

    /*
     * Getters and Setters
     */
    
    /**
     * @return
     */
    public double getEval() { return eval; }
    
    /**
     * @return
     */
    public Schedule getSchedule() { return schedule; }

    /**
     * @param s
     */
    public void setSchedule(Schedule s) { schedule = s; }

    /**
     * @return
     */
    public boolean isSolved() { return solEntry; }

    /**
     *
     */
    public void setSolved() { solEntry = true; }

    /**
     * @param n
     */
    public void addChildNode(Node n) { children.add(n); }

    /**
     * @return
     */
    public ArrayList<Node> getChildNodes() { return children; }

    /**
     * @param n
     */
    public void setParent(Node n) { parent = n; }

    /**
     * @return
     */
    public Node getParent() { return parent; }
    
    /**
     * @return
     */
    public SearchManager getSM() { return sm; }
    
    /**
     * @return
     */
    public String getID() { return id; }
    
    /**
     * @return
     */
    public int getDepth() { return depth; }
    
    /**
     * @return
     */
    public Assignment getStart() { return start; }

    /**
     *
     */
    public static Comparator<Node> NodeComparator = (Node n1, Node n2) -> n1.compareTo(n2);

    /* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Node o) {

        return (int)-(this.getEval() - o.getEval());// + this.getChildNodes().size() - o.getChildNodes().size();
    }
}
