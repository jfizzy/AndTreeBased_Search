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

/**
 * Object representing an assignment of a Meeting to a Slot
 * @author jmaci
 */
public class Assignment {
    
    private Meeting m;	// course, lab, tut
    private Slot s;		// slot it is assigned to
    
    /**
     * Constructor
     * @param m Meeting
     * @param s Slot
     */
    public Assignment(Meeting m, Slot s){
        this.m = m;
        this.s = s;
    }
    
    /*
     *  getters and setters
     */

    // meeting
    public Meeting getM() {
        return m;
    }

    public void setM(Meeting m) {
        this.m = m;
    }

    // slot
    public Slot getS() {
        return s;
    }

    public void setS(Slot s) {
        this.s = s;
    }
    

    
}
