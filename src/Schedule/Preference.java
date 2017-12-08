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

import Search.Node;

/**
 * Class representing a preferred slot and the preference value
 * (profs can say how much they prefer their lecture in a certain slot,
 * this is a soft constraint)
 */
public class Preference implements Comparable<Preference> {
	
	private Slot s;		// the slot
	private int value;	// the preference value

	/**
	 * Constructor
	 * @param s The slot
	 * @param value The value
	 */
	public Preference(Slot s, int value) {
		this.s = s;
		this.value = value;
	}
	
	/*
	 * Getters and setters
	 */
	
	/**
	 * Get the slot
	 * 
	 * @return The slot
	 */
	public Slot getSlot() {
		return s;
	}
	
	/**
	 * Set the slot
	 * 
	 * @param s The slot
	 */
	public void setSlot(Slot s) {
		this.s = s;
	}
	
	/**
	 * Get the value
	 * 
	 * @return The value
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * Set the value
	 * 
	 * @param value The value
	 */
	public void setValue(int value) {
		this.value = value;
	}
        
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        return this.s.toString() + " = (" + this.value+")";
    }
    
    /**
    * Comparator for Preference class
    */
   public static Comparator<Preference> PrefComparator = (Preference p1, Preference p2) -> p1.compareTo(p2);

	@Override
	public int compareTo(Preference o) {
		return this.getValue() - o.getValue();
	}
}
