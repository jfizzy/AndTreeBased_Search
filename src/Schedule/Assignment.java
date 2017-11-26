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
 * Assignment: Class which, when instantiated, pairs a given meeting (lecture, lab, or tutorial), with a given slot.
 * 
 */
public class Assignment {
    
    private Meeting m;
    private Slot s;
    
    /**
     * Constructor class for Assignment
     * @param m Meeting (lecture, lab, or tutorial) to be assigned.
     * @param s Slot to be assigned.
     */
    public Assignment(Meeting m, Slot s){
        this.m = m;
        this.s = s;
        m.setAssignment(this); // need this for backref
    }
    
    /*
     *  Getters and Setters
     */

    /**
     * Get the meeting
     * 
     * @return The meeting
     */
    public Meeting getM() {
        return m;
    }

     /**
      * Set the meeting
      * 
     * @param m The meeting
     */
    void setM(Meeting m) {
        this.m = m;
    }

    /**
     * Get the slot
     * 
     * @return The slot
     */
    public Slot getS() {
        return s;
    }

    /**
     * Set the slot
     * 
     * @param s The slot
     */
    public void setS(Slot s) {
        this.s = s;
    }
}
