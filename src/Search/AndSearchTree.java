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

import Schedule.TimeTable;

public class AndSearchTree {
	//A search tree is composed of a state, and it's sub tree, these need to be parameters
	//Additionally, there is a parent to the tree, and root should have a null parent
	
	private TimeTable state;
	private AndSearchTree parent;
	private ArrayList<AndSearchTree> subTree;
	private int eValue = 1000000000;
	
	//Constructors

	//This constructor will be used for the root node.
	public AndSearchTree(TimeTable tt) {
		state = tt;
		parent = null;
		//TO-DO (maybe?) add child trees to array list?
	}
	
	public AndSearchTree(TimeTable tt, AndSearchTree parent) {
		state = tt;
		this.parent = parent;
	}
	
	//Operations
	
	public boolean isRoot() {
		return (this.parent == null);
	}
	
	public boolean hasChildren() {
		return (this.subTree.isEmpty());
	}
	
	public ArrayList<AndSearchTree> getSubTree() {
		return this.subTree;
	}
	
	public AndSearchTree getParent() {
		return this.parent;
	}
	
	public TimeTable getState() {
		return this.state;
	}
	
	public void addChild(AndSearchTree child) {
		this.subTree.add(child);
	}

	//To bound the tree call this function
	//returns the parent node
	public AndSearchTree cullBranch() {
		//clear the subtree of this tree first
		this.subTree.clear();
		//set up a reference to the parent to return
		 AndSearchTree parentTree = this.parent;
		//remove the reference to this tree in the parent
		this.parent.subTree.remove(this);
		return parentTree;
	}
	
	public boolean isSolution() {
		//To-do
		return true;
	}
	

}
