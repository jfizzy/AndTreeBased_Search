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
 * Abstrct class representing a Lecture/Lab/Tutorial to be scheduled
 * @author 
 *
 */
public abstract class Meeting {
	
	// special constraints
    private final ArrayList<Meeting> incompatibility;
    private final ArrayList<Meeting> paired;
    private final ArrayList<Slot> unwanted;
    private Slot partassign;
    private final ArrayList<Preference> preferences;
    private Assignment assignment;
    private Section parentSection;
    
    /**
     * Constructor
     */
    public Meeting(){
        incompatibility = new ArrayList<>();
        paired = new ArrayList<>();
        unwanted = new ArrayList<>();
        preferences = new ArrayList<>();
        assignment = null;
        parentSection = null;
    }
    
    /*
     *  getters and setters
     */
    
    // noncompatible
    public ArrayList<Meeting> getIncompatibility() {
        return incompatibility;
    }
    
    public void addIncompatibility(Meeting m){
        this.incompatibility.add(m);
    }
    
    // pair
    public ArrayList<Meeting> getPaired() {
        return paired;
    }
    
    public void addPaired(Meeting m){
        this.paired.add(m);
    }
    
    // unwanted
    public ArrayList<Slot> getUnwanted() {
        return unwanted;
    }
    
    public void addUnwanted(Slot s){
        this.unwanted.add(s);
    }
    
    // partassign
    public Slot getPartassign() {
        return partassign;
    }
    
    public void setPartassign(Slot s){
        this.partassign = s;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }
    
    // preference
    public ArrayList<Preference> getPreferences() {
        return preferences;
    }
    
    public Preference addPreference(Slot s, int value){
    	Preference p = new Preference(s, value);
        this.preferences.add(p);
        return p;
    }

    public Section getParentSection() {
        return parentSection;
    }

    public void setParentSection(Section parentSection) {
        this.parentSection = parentSection;
    }
}
