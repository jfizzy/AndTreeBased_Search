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

import Search.SearchData.Pair;

/**
 * @author 
 *
 */
public abstract class Meeting {
	
	// class for pairs
	public class Pair<F, S> {
	    public F first;
	    public S second;
	}
	
	// special constraints
    private final ArrayList<Meeting> incompatibility;
    private final ArrayList<Meeting> paired;
    private final ArrayList<Slot> unwanted;
    private Slot partassign;
    private final ArrayList<Pair<Slot,Integer>> preferences;
    
    /**
     * constructor
     */
    public Meeting(){
        incompatibility = new ArrayList<>();
        paired = new ArrayList<>();
        unwanted = new ArrayList<>();
        //partassign = new ArrayList<>();
        preferences = new ArrayList<>();
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
    
    // preference
    public ArrayList<Pair<Slot,Integer>> getPreferences() {
        return preferences;
    }
    
    public void addPreference(Slot s, int value){
    	Pair<Slot,Integer> p = new Pair<Slot,Integer>();
    	p.first = s;
    	p.second = value;
        this.preferences.add(p);
    }
}
