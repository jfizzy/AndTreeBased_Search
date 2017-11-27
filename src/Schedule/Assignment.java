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

import java.util.Comparator;

/**
 * Assignment: Class which, when instantiated, pairs a given meeting (lecture, lab, or tutorial), with a given slot.
 * 
 */
public class Assignment implements Comparable{
    
    private Meeting m;
    private Slot s;
    private AssignmentPriority ap;
    
    /**
     * Constructor class for Assignment
     * @param m Meeting (lecture, lab, or tutorial) to be assigned.
     * @param s Slot to be assigned.
     */
    public Assignment(Meeting m, Slot s){
        this.m = m;
        this.s = s;
        m.setAssignment(this); // need this for backref
        ap = null;
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

    public AssignmentPriority getAp() {
        return ap;
    }

    public void setAp(AssignmentPriority ap) {
        this.ap = ap;
    }
    
    public static Comparator<Assignment> AssignmentComparator = (Assignment a1, Assignment a2) -> a1.compareTo(a2);

    @Override
    public int compareTo(Object a2) {
        if(!(a2 instanceof Assignment)){
            System.out.println("Problem with objects in assignment arraylist");
            System.exit(1);
        }
        //System.out.println("["+this.getAp().toString()+"]("+this.getM().toString()+")");
        //System.out.println("["+((Assignment) a2).getAp().toString()+"] ("+((Assignment) a2).getM().toString()+")");
        int value = AssignmentPriority.compare(((Assignment) a2).getAp(), this.ap);
        //System.out.println("Result: "+value);
        return value;
    }
    
}
