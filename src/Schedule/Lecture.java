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

/**
 * @author 
 *
 */
public class Lecture extends Meeting{
    
    private Section parentSection;	// lecture's parent section
    
    /**
     * constructor
     * @param s
     */
    public Lecture(Section s) {
        super();
        this.parentSection = s;
    }
    
    /*
     *  getters and setters
     */

    public Section getParentSection() {
        return parentSection;
    }

    public void setParentSection(Section parentSection) {
        this.parentSection = parentSection;
    }
    
    @Override
    public String toString(){
        String dept = this.parentSection.getParentCourse().getDepartment();
        String cNum = this.parentSection.getParentCourse().getNumber();
        String sec = this.parentSection.getSectionNum();
        return (dept + " " + cNum + " LEC " + sec);
    }
    
}
