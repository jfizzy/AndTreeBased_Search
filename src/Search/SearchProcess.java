package Search;

import Schedule.Meeting;
import Schedule.Schedule;

//implement and-tree search (branch and bound):

		// start at the root node with no assignments - done
		
		// generate all possible branches - each represents one added assignment
		//     (branches must satisfy Constr) 
		//don't want to do this, it will take too long to generate everything first. You can generate locally. 
		
		// do a depth-first search to determine the bound value
		//     (find the first valid solution quickly, then set bound to its Eval value)
		
		// go back to the root node
		
		// take branch with the lowest Eval
		//     (close off branches if Eval greater than bound)
		
		// generate all possible branches for the new node
		
		// if solution Eval < bound, set bound to new Eval value
		
		// return to root node, evaluate all possible solutions with Eval < bound
		//     (final solution = lowest Eval leaf)

public class SearchProcess {
	private AndSearchTreeNode root;
	private AndSearchTreeNode current;
	private Schedule state;
	private int bestW = -1;
	private Meeting toAdd;
	
	public SearchProcess(Schedule state) {
		this.state = state;
		//Create the root node
		root = new AndSearchTreeNode(state);
		//pointer to the current node
		current = root;
	}
	
	/**
	 * A main method which runs all supporting methods. 
	 * @return the solution schedule
	 */
	public Schedule run() {
		//first thing to do is find a valid solution to base future eval judgments on
		//inside while loop until the solution is found
	
		
		//it is all garbage right now
		for (Assignment next : state.getAssignments()) {
			if(next.getS() == null) {
				toAdd
			}
		}
			
	
		
		return state;
	}
	
	private void findFirst() {
		//TODO find the first valid solution
	}
	
	private void improve() {
		//TODO improve upon the first solution
	}
	/**
	 * call this method after finding any better eval in case we need to terminate
	 */
	private void write() {
		//TODO add a method that prints the current state and the eval 
		//value of it to a file in case the search is terminated before ending
	}

}
