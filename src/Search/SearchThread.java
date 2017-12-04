package Search;

import Schedule.Lecture;
import Schedule.LectureSlot;
import Schedule.NonLecture;
import Schedule.NonLectureSlot;
import Schedule.Schedule;

/**
 * @author g
 *
 */
public class SearchThread implements Runnable {
	
	private Thread t;
	private Node node;
	private volatile Schedule result;
	private boolean finished;
	
	/**
	 * @param node
	 */
	public SearchThread(Node node) {
		this.node = node;
	}
	
	/**
	 * 
	 */
	public void start() {
		
		System.out.println("Starting thread");
	    if (t == null) {
	    	t = new Thread ();
	        t.start();
	    }
	}
	
	/**
     * Main recursive search function which is run on each node
     *
     * @param bound Bound value
     * @return The schedule
     */
	@Override
    public void run() {

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
        int bound = node.getSM().getBound();
        
        // abort and return null if eval is worse than current bound
        if (bound > -1 && node.getEval() >= bound && node.getEval() < Node.startEval) {
        	result = null;
        	return;
        }
    	
    	// print node stuff 
        System.out.println("["+node.getDepth()+"] " + node.getID() + " ("+node.getSM().getSolutions().size()+" solns) bound="+bound);
        
        // print the assignments for this node
        //schedule.printAssignments();

        // check if the schedule is valid and complete, if so return it
        if (node.getSchedule().isComplete()) {

            // set this node solved
        	node.setSolved();
            
            // add the schedule to the solutions list
            if (node.getSchedule().isValid()) { // && (getEval() < bound || bound == -1)) {
            	node.getSM().addSolution(node.getSchedule());
            	
            	// if the eval is better than bound, reset the bound
                if (bound > node.getEval() || bound == -1) {
                	node.getSM().setBound(node.getEval());
                    bound = node.getEval();
                }
            }

            System.out.println("the schedule is done!");
            result = node.getSchedule();
        }

        // generate child nodes if we didn't yet for this node, sort by lowest eval
        generateNodes(bound);

        // depth-first search (get the first solution quickly to get a bound value)
        if (node.getSM().getBound() == -1)
            result = depthFirstSearch();
        
        // normal search with bound value (go through the entire tree)
        else result = andTreeSearch();
        finished = true;
    }

    /**
     * Generate all valid child nodes for this node.
     * If bound value is set, don't add nodes that are worse or equal
     */
    private void generateNodes(int bound) {

        // if we haven't generated the children yet and if the node isn't solved
        if (node.getChildNodes().size() == 0 && !node.isSolved()) {

            // if we are assigning a lecture
            if (node.getStart().getM() instanceof Lecture) {
                Lecture l = (Lecture) node.getStart().getM();

                // for each lecture slot we could assign it to
                for (LectureSlot ls : node.getSchedule().getLectureSlots()) {

                    // skip if the slot is inactive
                    if (!ls.isActive()) {
                        continue;
                    }

                    // check if the schedule would be valid if we made the assignment
                    if (node.getSchedule().isValidWith(l, ls)) {

                        // make the new child node if valid
                        Schedule s = new Schedule(node.getSchedule(), l, ls);
                        String id = l.toString() + " " + ls.toString();
                        Node n = new Node(s, node, ls, id, node.getDepth() + 1);

                        // skip making node if we have a bound value and the eval is greater
                        if (bound > -1 && n.getEval() >= bound) {
                            continue;
                        }

                        // add the new node
                        node.addChildNode(n);
                    }
                }
            } 
            
            // if we are assigning a nonlecture
            else {
                NonLecture nl = (NonLecture) node.getStart().getM();

                // for each nonlecture slot we could assign it to
                for (NonLectureSlot nls : node.getSchedule().getNonLectureSlots()) {

                    // skip if the slot is inactive
                    if (!nls.isActive()) {
                        continue;
                    }

                    // check if the schedule would be valid if we made the assignment
                    if (node.getSchedule().isValidWith(nl, nls)) {

                        // make the new child node if valid
                        Schedule s = new Schedule(node.getSchedule(), nl, nls);
                        String id = nl.toString() + " " + nls.toString();
                        Node n = new Node(s, node, nls, id, node.getDepth() + 1);

                        // skip making node if we have a bound value and the eval is greater or equal
                        if (bound > -1 && n.getEval() >= bound) {
                            continue;
                        }

                        // add the new node
                        node.addChildNode(n);
                    }
                }
            }
            
            // sort child nodes by lowest eval first
            node.getChildNodes().sort(Node.NodeComparator);
        }
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
        
        for (Node n : node.getChildNodes()) {
        	
        	// get the current bound value
        	int bound = node.getSM().getBound();
        	
        	// skip child node if worse than current bound
        	if (n.getEval() > bound && n.getEval() < Node.startEval) {
        		n.setSolved();
        		continue;
        	}

            // run the search on the child node
            Schedule result = n.runSearchThreaded();
            
            // if we got a result
            if (result != null) {
            	
            	// set the node to solved
                n.setSolved();
            	
            	// set the bound value if the result is complete and better
                int resulteval = result.eval();
            	if (result.isComplete() && result.isValid() && resulteval < bound) 
            		node.getSM().setBound(resulteval);

                // save the best of the results
                if (best == null || (resulteval < best.eval())) 
                    best = result;
            }
        }
        
        // set solved and return the best schedule from the child nodes (or null)
        node.setSolved();
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
            for (Node n : node.getChildNodes()) {
                if (!n.isSolved()) {
                    choice = n;
                    break;
                }
            }

            // if we did not find an unsolved branch, set this node solved,
            // 	clear this node's children, and return to parent
            
            if (choice == null) {
            	node.setSolved();
                //children.clear();
                return null;
            }

            // recurse search on chosen child node:
            // 	if result is null (meaning all branches of that node are solved),
            // 	we will loop to the next choice (if any remain)
            
            result = choice.runSearchThreaded();
            if (result == null) {
                choice.setSolved();
                //children.remove(choice);
            }
        }

        // return the result we got
        return result;
    }
    
    /**
     * @return
     */
    public Schedule getResult() { return result; }
    
    /**
     * @return
     */
    public boolean isFinished() { return finished; }
}
