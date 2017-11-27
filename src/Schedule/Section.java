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
 * Object representing a section of a course, containing lecures and nonlectures
 *
 */
public class Section {

    private String sectionNum;			// the section number
    private ArrayList<Lab> labs;		// labs for the section
    private ArrayList<Tutorial> tuts;	// tutorials for the section
    private Lecture lecture;			// lecture for the section
    private final Course parentCourse;	// parent course of the section
    private final boolean evening; 		// Whether or not the section is an evening section (defined by LEC 9x in input)
    
    /**
     * Constructor
     * @param c Course
     * @param sNum Section number
     */
    public Section(Course c, String sNum, boolean evening) {
        this.parentCourse = c;
        this.sectionNum = sNum;
        this.labs = new ArrayList<>();
        this.tuts = new ArrayList<>();
        this.lecture = new Lecture(this);
        this.lecture.setParentSection(this);
        this.evening = evening;
        
    }
    
    /*
     *  Getters and setters
     */

    /**
     * Get the section number
     * 
     * @return Section number string
     */
    public String getSectionNum() {
        return sectionNum;
    }

    /**
     * Set the section number
     * 
     * @param sectionNum Section number string
     */
    public void setSectionNum(String sectionNum) {
        this.sectionNum = sectionNum;
    }

    /**
     * Get the labs list
     * 
     * @return Lab list
     */
    public ArrayList<Lab> getLabs() {
        return labs;
    }
    
    /**
     * Add a lab
     * 
     * @param l The lab
     */
    public void addLab(Lab l) {
        labs.add(l);
    }
    
    /**
     * Get the tutorials list
     * 
     * @return Tutorial lsit
     */
    public ArrayList<Tutorial> getTuts() {
        return tuts;
    }
    
    /**
     * Add a tutorial
     * 
     * @param t The tutorial
     */
    public void addTutorial(Tutorial t) {
        tuts.add(t);
    }
    
    /**
     * Get the parent course
     * 
     * @return The course
     */
    public Course getParentCourse() {
        return parentCourse;
    }

    /**
     * Get the lecture
     * 
     * @return The lecture
     */
    public Lecture getLecture() {
        return lecture;
    }

    /**
     * Set the lecture
     * 
     * @param lecture The lecture
     */
    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }
    
    /**
     * Check if evening
     * 
     * @return True if evening
     */
    public boolean isEvening() {
    	return this.evening;
    }
    
    public String toString()
    {
        // Lecture data:
        String lecString = lecture.toString();
        lecString.concat("\n");
        
        //Tutorial's data:
        String tutString = "";
        for(Tutorial t : this.tuts)
        {
            tutString.concat(t.toString() + "\n");
        }
        
        //Lab data:
        String labString = "";
        for (Lab l : this.labs)
        {
            labString.concat(l.toString() + "\n");
        }
        
        return (lecString + tutString + labString);
    }
    
}
