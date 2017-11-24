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
 * Object representing a course to be scheduled (Not a lecture; contains
 * sections of lectures and nonlectures)
 *
 * @author
 *
 */
public class Course {

    private String department;				// name of the department
    private String number;					// course number
    private ArrayList<Section> sections;	// sections within the course
    private ArrayList<Lab> openLabs;    // open lab sections
    private ArrayList<Tutorial> openTuts; // open tutorial sections

    /**
     * Default constructor
     */
    public Course() {
        this.department = null;
        this.number = null;
        this.sections = new ArrayList<>();
        this.openLabs = new ArrayList<>();
        this.openTuts = new ArrayList<>();
    }

    /**
     * Constructor
     *
     * @param dept Department
     * @param num Course number
     * @param sNum Section number
     */
    public Course(String dept, String num, String sNum) {
        this.department = dept;
        this.number = num;
        this.sections = new ArrayList<>();
        this.sections.add(new Section(this, sNum));
        this.openLabs = new ArrayList<>();
        this.openTuts = new ArrayList<>();
    }

    /*
     *  getters and setters
     */
    // department
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    // number
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    // sections
    public ArrayList<Section> getSections() {
        return sections;
    }

    public void addSection(Section s) {
        sections.add(s);
    }

    public ArrayList<Lab> getOpenLabs() {
        return openLabs;
    }

    public void addOpenLab(Lab l) {
        this.openLabs.add(l);
    }

    public ArrayList<Tutorial> getOpenTuts() {
        return openTuts;
    }

    public void addOpenTut(Tutorial t) {
        this.openTuts.add(t);
    }

    /*
     *  functions
     */
    /**
     * getCourseLectures - returns a list of all lectures in this course
     *
     * @return
     */
    public ArrayList<Lecture> getCourseLectures() {
        ArrayList<Lecture> lectures = new ArrayList<>();
        this.sections.forEach((s) -> {
            lectures.add(s.getLecture());
        });
        if (lectures.isEmpty()) {
            return null;
        }
        return lectures;
    }

    /**
     * getCourseLabs - returns a list of all labs in this course (open and
     * section specific)
     *
     * @return
     */
    public ArrayList<Lab> getCourseLabs() {
        ArrayList<Lab> labs = new ArrayList<>();
        this.openLabs.forEach((l) -> {
            labs.add(l);
        });
        this.sections.forEach((s) -> {
            s.getLabs().forEach((l) -> {
                labs.add(l);
            });
        });
        if (labs.isEmpty()) {
            return null;
        }
        return labs;
    }

    /**
     * getCourseTuts - returns a list of all tutorials in this course (open and
     * section specific)
     *
     * @return
     */
    public ArrayList<Tutorial> getCourseTuts() {
        ArrayList<Tutorial> tuts = new ArrayList<>();
        this.openTuts.forEach((t) -> {
            tuts.add(t);
        });
        this.sections.forEach((s) -> {
            s.getTuts().forEach((t) -> {
                tuts.add(t);
            });
        });
        if (tuts.isEmpty()) {
            return null;
        }
        return tuts;
    }

}
