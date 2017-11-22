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
 * @author jmaci
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
    }
    
    /*
     *  Getters and Setters
     */

    public Meeting getM() {
        return m;
    }

     void setM(Meeting m) {
        this.m = m;
    }

    public Slot getS() {
        return s;
    }

    public void setS(Slot s) {
        this.s = s;
    }
    

    
}
