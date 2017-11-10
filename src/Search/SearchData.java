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
	private ArrayList<Pair<Slot, Meeting>> preferences;
	private ArrayList<Pair<Meeting, Meeting>> pair;
	private ArrayList<Pair<Meeting, Slot>> partassign;
    
    public SearchData() {
    	
    }
    
    public void setLectureSlots(ArrayList<LectureSlot> lecslots) {
    	this.ls = lecslots;
    }
    
    public void setLabSlots(ArrayList<NonLectureSlot> labslots) {
    	this.nls = labslots;
    }
    
    public void setTimetable(TimeTable timetable) {
    	this.tt = timetable;
    }
    
    // TODO rest of the getter/setters
}
