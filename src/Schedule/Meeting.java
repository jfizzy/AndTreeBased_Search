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

public abstract class Meeting {
    private ArrayList<Meeting> incompatibility;
    
    public ArrayList<Meeting> getIncompatibility() {
        return incompatibility;
    }
    
    public void addIncompatibility(Meeting m){
        this.incompatibility.add(m);
    }
    
    public Meeting(){
        incompatibility = new ArrayList<>();
    }
}
