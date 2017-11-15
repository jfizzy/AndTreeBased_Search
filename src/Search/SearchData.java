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

import Schedule.Course;
import Schedule.Lecture;
import Schedule.LectureSlot;
import Schedule.NonLectureSlot;
import Schedule.Slot;
import Schedule.TimeTable;
import Schedule.Meeting;
import Schedule.NonLecture;

public class SearchData {
	
	// classes for tuples
	public class Pair<F, S> {
	    public F first;
	    public S second;
	}
	public class Tri<F, S, T> {
	    public F first;
	    public S second;
	    public T third;
	}
	
	// slots
	private ArrayList<LectureSlot> lslots;
	private ArrayList<NonLectureSlot> nlslots;
	
	// meetings
	private ArrayList<Course> courses;
	private ArrayList<Lecture> lectures;
	private ArrayList<NonLecture> nonlectures;
	
	// the timetable
	private TimeTable tt;
    
	// input lists
	private ArrayList<Pair<Meeting, Meeting>> noncompatible;
	private ArrayList<Pair<Meeting, Slot>> unwanted;
	private ArrayList<Tri<Meeting, Slot, Integer>> preferences;
	private ArrayList<Pair<Meeting, Meeting>> pairs;
	private ArrayList<Pair<Meeting, Slot>> partassign;
    
	// constructor
    public SearchData() {
    	this.noncompatible = new ArrayList<Pair<Meeting, Meeting>>();
    	this.unwanted = new ArrayList<Pair<Meeting, Slot>>();
    	this.preferences = new ArrayList<Tri<Meeting, Slot, Integer>>();
    	this.pairs = new ArrayList<Pair<Meeting, Meeting>>();
    	this.partassign = new ArrayList<Pair<Meeting, Slot>>();
    }
    
    // getters, setters, adders
    public void setLectureSlots(ArrayList<LectureSlot> lecslots) {
    	this.lslots = lecslots;
    }
    
    public ArrayList<LectureSlot> getLectureSlots() {
    	return this.lslots;
    }
    
    public void setLabSlots(ArrayList<NonLectureSlot> labslots) {
    	this.nlslots = labslots;
    }
    
    public ArrayList<NonLectureSlot> getLabSlots() {
    	return this.nlslots;
    }
    
    public void setCourses(ArrayList<Course> cs) {
    	this.courses = cs;
    }
    
    public ArrayList<Course> getCourses() {
    	return this.courses;
    }
    
    public void setLectures(ArrayList<Lecture> lecs) {
    	this.lectures = lecs;
    }
    
    public ArrayList<Lecture> getLectures() {
    	return this.lectures;
    }
    
    public void setNonLectures(ArrayList<NonLecture> nonlecs) {
    	this.nonlectures = nonlecs;
    }
    
    public ArrayList<NonLecture> getNonLectures() {
    	return this.nonlectures;
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
    
    public void addPreference(Meeting a, Slot s, int value) {
    	Tri<Meeting, Slot, Integer> t = new Tri<Meeting,Slot,Integer>();
    	t.first = a;
    	t.second = s;
    	t.third = value;
    	this.preferences.add(t);
    }
    
    public ArrayList<Tri<Meeting, Slot, Integer>> getPreferences() {
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
