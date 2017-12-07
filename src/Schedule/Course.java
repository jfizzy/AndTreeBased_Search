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
    private ArrayList<Lab> openLabs;    	// open lab sections
    private ArrayList<Tutorial> openTuts; 	// open tutorial sections

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
     * @param evening Evening flag
     */
    public Course(String dept, String num, String sNum, boolean evening) {
        this.department = dept;
        this.number = num;
        this.sections = new ArrayList<>();
        this.sections.add(new Section(this, sNum, evening));
        this.openLabs = new ArrayList<>();
        this.openTuts = new ArrayList<>();
    }

    /*
     *  Getters and setters
     */
    
    /**
     * Get the department
     * 
     * @return The department string
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Set the department
     * 
     * @param department The department string
     */
    public void setDepartment(String department) {
        this.department = department;
    }
    
    /**
     * Get the course number
     * 
     * @return The number string
     */
    public String getNumber() {
        return number;
    }

    /**
     * Set the course number
     * 
     * @param number The number string
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * Get the sections
     * 
     * @return The sections list
     */
    public ArrayList<Section> getSections() {
        return sections;
    }
    
    /**
     * @param index Index of the desired Section
     * @return Section at a particular index.
     */
    
    public Section getSection(int index)
    {
        return sections.get(index);
    }

    /**
     * Add a section
     * 
     * @param s The section
     */
    public void addSection(Section s) {
        sections.add(s);
    }

    /**
     * Get the open labs
     * 
     * @return Open lab list
     */
    public ArrayList<Lab> getOpenLabs() {
        return openLabs;
    }

    /**
     * Add an open lab
     * 
     * @param l The lab
     */
    public void addOpenLab(Lab l) {
        this.openLabs.add(l);
    }

    /**
     * Get the open tutorials
     * 
     * @return Open tutorial list
     */
    public ArrayList<Tutorial> getOpenTuts() {
        return openTuts;
    }

    /**
     * Add an open tutorial
     * 
     * @param t The tutorial
     */
    public void addOpenTut(Tutorial t) {
        this.openTuts.add(t);
    }

    /**
     * getCourseLectures - returns a list of all lectures in this course
     *
     * @return Lectures list
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
     * @return Labs list
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
     * @return Tutorials list
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

    /**
     * Prints out all lectures, labs, and tutorials for a given course
     */
    public void print()
    {
        System.out.println(this.getDepartment() + this.getNumber() + ":");
        for(Section s : this.sections)
        {
            System.out.println(s.toString());
        }
        
        if(!this.openLabs.isEmpty())
        {
            System.out.println("Open Labs (All Sections):");
            for(Lab labs : this.openLabs)
            {
                System.out.println(labs.toString());
            }
        }
        
        if(!this.openTuts.isEmpty())
        {
            System.out.println("Open Tutorials (All Sections):");
            for(Tutorial tuts : this.openTuts)
            {
                System.out.println(tuts.toString());
            }
        }
    }

}
