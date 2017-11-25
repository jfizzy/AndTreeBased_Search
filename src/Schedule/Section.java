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
 * @author 
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
        this.evening = evening;
        
    }
    
    
   
    
    /*
     *  getters and setters
     */

    // section number
    public String getSectionNum() {
        return sectionNum;
    }

    public void setSectionNum(String sectionNum) {
        this.sectionNum = sectionNum;
    }

    // labs
    public ArrayList<Lab> getLabs() {
        return labs;
    }
    
    public void addLab(Lab l) {
        labs.add(l);
    }
    
    // tutorials
    public ArrayList<Tutorial> getTuts() {
        return tuts;
    }
    
    public void addTutorial(Tutorial t) {
        tuts.add(t);
    }
    
    // parent course
    public Course getParentCourse() {
        return parentCourse;
    }

    // lecture
    public Lecture getLecture() {
        return lecture;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }
    
    public boolean isEvening()
    {
    	return this.evening;
    }
    
}
