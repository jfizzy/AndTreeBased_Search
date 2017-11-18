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
public class Course {

    
    private String department;				// name of the department
    private String number;					// course number
    private ArrayList<Section> sections;	// sections within the course
    
    /**
     * default constructor
     */
    public Course() {
        this.department = null;
        this.number = null;
        this.sections = new ArrayList<>();
    }
    
    /**
     * constructor
     * @param dept
     * @param num
     * @param sNum
     */
    public Course(String dept, String num, String sNum){
        this.department = dept;
        this.number = num;
        this.sections = new ArrayList<>();
        this.sections.add(new Section(this,sNum));
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
    
}
