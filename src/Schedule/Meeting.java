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
import java.util.Objects;

/**
 * Abstrct class representing a Lecture/Lab/Tutorial to be scheduled
 *
 */
public abstract class Meeting {

    // special constraints
    private final ArrayList<Meeting> incompatibility;
    private final ArrayList<Meeting> paired;
    private final ArrayList<Slot> unwanted;
    private Slot partassign;
    private final ArrayList<Preference> preferences;

    // parent assignment and section
    private Assignment assignment;
    private Section parentSection;

    /**
     * Constructor
     */
    public Meeting() {
        incompatibility = new ArrayList<>();
        paired = new ArrayList<>();
        unwanted = new ArrayList<>();
        preferences = new ArrayList<>();
        assignment = null;
        parentSection = null;
    }

    /*
     *  Getters and setters
     */
    /**
     * Get noncompatible list (may not be used)
     *
     * @return Meeting list
     */
    public ArrayList<Meeting> getIncompatibility() {
        return incompatibility;
    }

    /**
     * Add an incompatibility (may not be used)
     *
     * @param m The meeting
     */
    public void addIncompatibility(Meeting m) {
        this.incompatibility.add(m);
    }

    /**
     * Get pairs list (may not be used)
     *
     * @return Meeting list
     */
    public ArrayList<Meeting> getPaired() {
        return paired;
    }

    /**
     * Add a paired meeting (may not be used)
     *
     * @param m The meeting
     */
    public void addPaired(Meeting m) {
        this.paired.add(m);
    }

    /**
     * Get the unwanted slots list
     *
     * @return List of slots
     */
    public ArrayList<Slot> getUnwanted() {
        return unwanted;
    }

    /**
     * Add an unwanted slot
     *
     * @param s The slot
     */
    public void addUnwanted(Slot s) {
        this.unwanted.add(s);
    }

    /**
     * Get partassign slot
     *
     * @return The slot
     */
    public Slot getPartassign() {
        return partassign;
    }

    /**
     * Set partassign slot
     *
     * @param s The slot
     */
    public void setPartassign(Slot s) {
        this.partassign = s;
    }

    /**
     * Get the parent assignment
     *
     * @return Assignment
     */
    public Assignment getAssignment() {
        return assignment;
    }

    /**
     * Set the parent assignment
     *
     * @param assignment Assignment
     */
    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    /**
     * Get preferences list
     *
     * @return Preferences list
     */
    public ArrayList<Preference> getPreferences() {
        return preferences;
    }

    /**
     * Add a preference
     *
     * @param s The slot
     * @param value The preference value
     * @return The preference
     */
    public Preference addPreference(Slot s, int value) {
        Preference p = new Preference(s, value);
        this.preferences.add(p);
        return p;
    }

    /**
     * Get the parent section
     *
     * @return The section
     */
    public Section getParentSection() {
        return parentSection;
    }

    /**
     * Set the parent section
     *
     * @param parentSection The section
     */
    public void setParentSection(Section parentSection) {
        this.parentSection = parentSection;
    }
}
