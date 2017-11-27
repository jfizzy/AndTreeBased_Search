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

    public Node(Schedule s) {
        schedule = s;
        solEntry = false;
        children = new ArrayList<>();
    }

    //Getters and Setters
    public Schedule getSchedule() {
        return this.schedule;
    }

    public void setSchedule(Schedule s) {
        this.schedule = s;
    }
    
    public boolean isSolved() {
        return this.solEntry;
    }
    
    public void setSolved(){
        this.solEntry = true;
    }

    public void addChildNode(Node n){
        this.children.add(n);
    }
    
    public ArrayList<Node> getChildNodes(){
        return this.children;
    }
}
