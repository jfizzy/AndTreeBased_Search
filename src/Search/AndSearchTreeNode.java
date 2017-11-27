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

import java.util.ArrayList;

import Schedule.Schedule;

public class AndSearchTreeNode {
	//A search tree is composed of a state, and it's sub tree, these need to be parameters
	//Additionally, there is a parent to the tree, and root should have a null parent
	
	private Schedule state;
	private AndSearchTreeNode parent;
	private ArrayList<AndSearchTreeNode> children;
	private int eValue = 1000000000;
	private boolean isSolved;
	
	//Constructors

	//This constructor will be used for the root node.
	public AndSearchTreeNode(Schedule s) {
		state = s;
		parent = null;
		isSolved = false;
		//TO-DO (maybe?) add child trees to array list?
	}
	
	private AndSearchTreeNode(Schedule s, AndSearchTreeNode parent) {
		state = s;
		this.parent = parent;
		isSolved = false;
	}
	
	//Operations
	
	public boolean isRoot() {
		return (this.parent == null);
	}
	
	public boolean hasChildren() {
		return (!this.children.isEmpty());
	}
	
	public ArrayList<AndSearchTreeNode> getSubTree() {
		return this.children;
	}
	
	public AndSearchTreeNode getParent() {
		return this.parent;
	}
	
	public Schedule getState() {
		return this.state;
	}
	
	public void addChild(Schedule state) {
		AndSearchTreeNode child = new AndSearchTreeNode(state, this);
		this.children.add(child);
	}

	//To bound the branch call this function
	//changes the sol entry to true
	public void solved(boolean state) {
		isSolved = state;
	}
}
