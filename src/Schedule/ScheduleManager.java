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
 * @author 
 *
 */
public class ScheduleManager {
    
	/**
	 * Class for pairs
	 *
	 * @param <F> First element
	 * @param <S> Second element
	 */
	public class Pair<F, S> {
	    public F first;
	    public S second;
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
    
    /**
     * Default constructor
     */
    public ScheduleManager() {
    	this.lslots = new ArrayList<>();
    	this.nlslots = new ArrayList<>();
    	this.courses = new ArrayList<>();
    	this.lectures = new ArrayList<>();
    }
    
    /**
     * Constructor for Constr
     * @param orig Original search data
     * @param tt New timetable
     */
    public ScheduleManager(ScheduleManager orig, TimeTable tt) {
    	this.lslots = orig.getLectureSlots();
    	this.nlslots = orig.getLabSlots();
    	this.courses = orig.getCourses();
    	this.lectures = orig.getLectures();
    	this.nonlectures = orig.getNonLectures();
    	this.tt = tt;
    }
    
    /**
     * Fills the lectures list using the courses list
     */
    private void processLectures() {
    	
    	// for each course
    	for (Course c : courses) {
    		
    		// add the lecture for each section of that course
    		for (int i = 0; i < c.getSections().size(); i++)
    			lectures.add(c.getSections().get(i).getLecture());
    	}
    }
    
    /*
     * getters, setters, adders
     * 
     */
    
    // lecture slots
    public void setLectureSlots(ArrayList<LectureSlot> lecslots) {
    	this.lslots = lecslots;
    }
    
    public ArrayList<LectureSlot> getLectureSlots() {
    	return this.lslots;
    }
    
    // nonlecture slots
    public void setLabSlots(ArrayList<NonLectureSlot> labslots) {
    	this.nlslots = labslots;
    }
    
    public ArrayList<NonLectureSlot> getLabSlots() {
    	return this.nlslots;
    }
    
    // courses
    public void setCourses(ArrayList<Course> cs) {
    	this.courses = cs;
    	processLectures(); // fill the lectures list using the courses list
    }
    
    public ArrayList<Course> getCourses() {
    	return this.courses;
    }
    
    // lectures
    public void setLectures(ArrayList<Lecture> lecs) {
    	this.lectures = lecs;
    }
    
    public ArrayList<Lecture> getLectures() {
    	return this.lectures;
    }
    
    // nonlectures
    public void setNonLectures(ArrayList<NonLecture> nonlecs) {
    	this.nonlectures = nonlecs;
    }
    
    public ArrayList<NonLecture> getNonLectures() {
    	return this.nonlectures;
    }
    
    // timetable
    public void setTimetable(TimeTable timetable) {
    	this.tt = timetable;
    }
    
    public TimeTable getTimetable() {
    	return this.tt;
    }
}
