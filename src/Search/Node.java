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
import java.util.Comparator;

/**
 * Node class - defines the fields and methods for a node of an And-tree
 *
 * @author
 */
public class Node implements Comparable<Node> {
	
    private ArrayList<Node> children;	// list of child nodes
    private SearchManager sm;	// the search manager that initiated the seach
    private Schedule schedule;	// schedule for this node
    //private boolean solEntry;	// solution entry (true = yes, false = ?)
    private Node parent;		// reference to the parent node (null if root node)
    private Assignment start;	// the assignment to try for this node's branches
    private String id;			// identifier string for the node
    private int depth;			// level of the tree the node is at
    private double eval;		// eval for this node (only calcd the first time, otherwise this var is checked)
    
    /**
     * Constructor for the root node
     *
     * @param s Schedule
     */
    public Node(Schedule s, SearchManager sm) {
        schedule = new Schedule(s);	// make a copy of the given schedule
        this.sm = sm;				// set the manager reference
        parent = null;				// set the parent reference (null for root)
        id = "ROOT";				// set the ID string
        depth = 0;					// set the depth (0 at root)
        eval = Eval.getEval(s);		// get the eval value
        children = new ArrayList<>();			// initialize child list
        start = schedule.findFirstNull();		// get first null assignment
        SearchManager.startEval = getEval();	// set the initial eval
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
        schedule = s;			// set the schedule
        sm = n.getSM();			// set the manager reference
        parent = n;				// set the parent node reference
        this.id = id;			// set the ID string
        this.depth = depth;		// set the depth
        eval = Eval.getEval(s);	// get the eval value
        children = new ArrayList<>();		// initialize child list
        start = schedule.findFirstNull(); 	// get first null assignment
    }
    
    /**
     * Generate all valid child nodes for this node.
     * If bound value is set, don't add nodes that are worse or equal
     */
    public void generateNodes(double bound) {

        // if we haven't generated the children yet
        if (children.size() == 0) {

            // if we are assigning a lecture
            if (start.getM() instanceof Lecture) {
                Lecture l = (Lecture) start.getM();

                // for each lecture slot we could assign it to
                for (LectureSlot ls : schedule.getLectureSlots()) {

                    // skip if the slot is inactive
                    if (!ls.isActive()) continue;

                    // check if the schedule would be valid if we made the assignment
                    if (schedule.isValidWith(l, ls)) {

                        // make the new child node if valid
                        Schedule s = new Schedule(schedule, l, ls);
                        String id = l.toString() + " " + ls.toString();
                        Node n = new Node(s, this, ls, id, depth + 1);

                        // skip making node if we have a bound value and the eval is greater
                        if (bound > -1 && n.getEval() >= bound && n.getEval() >= SearchManager.startEval)
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
                for (NonLectureSlot nls : schedule.getNonLectureSlots()) {

                    // skip if the slot is inactive
                    if (!nls.isActive()) continue;

                    // check if the schedule would be valid if we made the assignment
                    if (schedule.isValidWith(nl, nls)) {

                        // make the new child node if valid
                        Schedule s = new Schedule(schedule, nl, nls);
                        String id = nl.toString() + " " + nls.toString();
                        Node n = new Node(s, this, nls, id, depth + 1);

                        // skip making node if we have a bound value and the eval is greater or equal
                        if (bound > -1 && n.getEval() >= bound && n.getEval() >= SearchManager.startEval)
                            continue;

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

        return (int) -(1000*this.getEval() - 1000*o.getEval());
    }
}
