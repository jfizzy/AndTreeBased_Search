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

package Search;

import java.util.ArrayList;

import Schedule.LectureSlot;
import Schedule.NonLectureSlot;
import Schedule.Slot;
import Schedule.TimeTable;
import Schedule.Meeting;

public class SearchData {
	
	public class Pair<F, S> {
	    private F first;
	    private S second;
	}
	
	private ArrayList<LectureSlot> ls;
	private ArrayList<NonLectureSlot> nls;
	private TimeTable tt;
    
	private ArrayList<Pair<Meeting, Meeting>> noncompatible;
	private ArrayList<Pair<Meeting, Slot>> unwanted;
	private ArrayList<Pair<Meeting, Slot>> preferences;
	private ArrayList<Pair<Meeting, Meeting>> pairs;
	private ArrayList<Pair<Meeting, Slot>> partassign;
    
    public SearchData() {
    	this.noncompatible = new ArrayList<Pair<Meeting, Meeting>>();
    	this.unwanted = new ArrayList<Pair<Meeting, Slot>>();
    	this.preferences = new ArrayList<Pair<Meeting, Slot>>();
    	this.pairs = new ArrayList<Pair<Meeting, Meeting>>();
    	this.partassign = new ArrayList<Pair<Meeting, Slot>>();
    }
    
    public void setLectureSlots(ArrayList<LectureSlot> lecslots) {
    	this.ls = lecslots;
    }
    
    public ArrayList<LectureSlot> getLectureSlots() {
    	return this.ls;
    }
    
    public void setLabSlots(ArrayList<NonLectureSlot> labslots) {
    	this.nls = labslots;
    }
    
    public ArrayList<NonLectureSlot> getLabSlots() {
    	return this.nls;
    }
    
    public void setTimetable(TimeTable timetable) {
    	this.tt = timetable;
    }
    
    public TimeTable getTimetable() {
    	return this.tt;
    }
    
    public void addNoncompatible(Meeting a, Meeting b) {
    	Pair<Meeting,Meeting> p = new Pair<Meeting,Meeting>();
    	p.first = a;
    	p.second = b;
    	this.noncompatible.add(p);
    }
    
    public ArrayList<Pair<Meeting, Meeting>> getNoncompatible() {
    	return this.noncompatible;
    }
    
    public void addUnwanted(Meeting a, Slot s) {
    	Pair<Meeting,Slot> p = new Pair<Meeting,Slot>();
    	p.first = a;
    	p.second = s;
    	this.unwanted.add(p);
    }
    
    public ArrayList<Pair<Meeting, Slot>> getUnwanted() {
    	return this.unwanted;
    }
    
    public void addPreference(Meeting a, Slot s) {
    	Pair<Meeting,Slot> p = new Pair<Meeting,Slot>();
    	p.first = a;
    	p.second = s;
    	this.preferences.add(p);
    }
    
    public ArrayList<Pair<Meeting, Slot>> getPreferences() {
    	return this.preferences;
    }
    
    public void addPair(Meeting a, Meeting b) {
    	Pair<Meeting,Meeting> p = new Pair<Meeting,Meeting>();
    	p.first = a;
    	p.second = b;
    	this.pairs.add(p);
    }
    
    public ArrayList<Pair<Meeting, Meeting>> getPairs() {
    	return this.pairs;
    }
    
    public void addPartassign(Meeting a, Slot s) {
    	Pair<Meeting,Slot> p = new Pair<Meeting,Slot>();
    	p.first = a;
    	p.second = s;
    	this.partassign.add(p);
    }
    
    public ArrayList<Pair<Meeting, Slot>> getPartassign() {
    	return this.partassign;
    }
}
