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

public class TimeTable {
    
    private final ArrayList<Meeting> meetings; // must be ordered
    private ArrayList<Assignment> assignments;
    
    
    public ArrayList<Assignment> getAssignments() {
        return assignments;
    }
    
    public TimeTable(ArrayList<Meeting> meetings){
        this.meetings = meetings;
        assignments = new ArrayList<>();
        meetings.forEach((l) -> {
            assignments.add(new Assignment(l,null));
        });
    }
    
}
