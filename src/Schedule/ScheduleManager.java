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

import Search.Constr;
import Search.Eval;

/**
 * Class for containing all the data for a schedule 
 *
 */
public class ScheduleManager {
	
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
     * Constructor
     * 
     * @param lslots LectureSlot list
     * @param nlslots NonLectureSlot list 
     * @param courses Course list
     * @param lectures Lecture list
     * @param nonlectures NonLecture list
     * @param tt TimeTable
     */
    public ScheduleManager(ArrayList<LectureSlot> lslots, 
    		ArrayList<NonLectureSlot> nlslots,
    		ArrayList<Course> courses, 
    		ArrayList<Lecture> lectures, 
    		ArrayList<NonLecture> nonlectures,
    		TimeTable tt) {
    	
    	this.lslots = lslots;
    	this.nlslots = nlslots;
    	this.courses = courses;
    	this.lectures = lectures;
    	this.tt = tt;
    }
    
    /**
     * Constructor for Constr
     * (copies a schedule but with a new timetable) 
     * 
     * @param orig Original schedule
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
    
    /**
     * Checks if the schedule meets all hard constraints
     * 
     * @return True if all hard constraints are met
     */
    public boolean isValid() {
    	Constr c = new Constr(this);
    	return c.check();
    }
    
    /**
     * Checks if adding an assignment to the schedule would meet hard constraints
     * 
     * @param a The assignment
     * @return True if the schedule with the assignment meets all hard constraints
     */
    public boolean isValidWith(Assignment a) {
    	Constr c = new Constr(a, this);
    	return c.check();
    }
    
    /**
     * Get the evaluation without weights
     * 
     * @return The evaluation of the schedule
     */
    public int getEval() {
    	Eval e = new Eval(this);
    	return e.getEval();
    }
    
    /**
     * Get the evaluation with added assignment without weights
     * 
     * @param a The assignment
     * @return The evaluation of the schedule with the assignment
     */
    public int getEvalWith(Assignment a) {
    	Eval e = new Eval(a, this);
    	return e.getEval();
    }
    
    /**
     * Get the evaluation with weights
     * 
     * @param w1
     * @param w2
     * @param w3
     * @param w4
     * @return The evaluation of the schedule
     */
    public int getEval(double w1, double w2, double w3, double w4) {
    	Eval e = new Eval(this, w1, w2, w3, w4);
    	return e.getEval();
    }

    /**
     * Get the evaluation with added assignment with weights
     * 
     * @param a The assignment
     * @param w1
     * @param w2
     * @param w3
     * @param w4
     * @return The evaluation of the schedule with the assignment
     */
    public int getEvalWith(Assignment a, double w1, double w2, double w3, double w4) {
    	Eval e = new Eval(a, this, w1, w2, w3, w4);
    	return e.getEval();
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
