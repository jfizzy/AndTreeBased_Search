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
public class Section {

    private String sectionNum;
    private ArrayList<Lab> labs;
    private ArrayList<Tutorial> tuts;
    private Lecture lecture;
    private final Course parentCourse;
    
    /**
     * constructor
     * @param c
     * @param sNum
     */
    public Section(Course c, String sNum) {
        this.parentCourse = c;
        this.sectionNum = sNum;
        this.labs = new ArrayList<>();
        this.tuts = new ArrayList<>();
        this.lecture = new Lecture(this);
    }
    
    // getters and setters

    public String getSectionNum() {
        return sectionNum;
    }

    public void setSectionNum(String sectionNum) {
        this.sectionNum = sectionNum;
    }

    public ArrayList<Lab> getLabs() {
        return labs;
    }

    public ArrayList<Tutorial> getTuts() {
        return tuts;
    }
    
    public void addLab(Lab l) {
        labs.add(l);
    }
    
    public void addTutorial(Tutorial t) {
        tuts.add(t);
    }
    
    public Course getParentCourse() {
        return parentCourse;
    }

    public Lecture getLecture() {
        return lecture;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }
    
}
