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
package Schedule;

import java.util.ArrayList;

/**
 * @author 
 *
 */
public abstract class Meeting {
	
    private ArrayList<Meeting> incompatibility;
    
    /**
     * constructor
     */
    public Meeting(){
        incompatibility = new ArrayList<>();
    }
    
    // getters and setters
    
    public ArrayList<Meeting> getIncompatibility() {
        return incompatibility;
    }
    
    public void addIncompatibility(Meeting m){
        this.incompatibility.add(m);
    }
    
    
}
