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
 * Object representing a lecture to be scheduled
 * @author 
 *
 */
public class Lecture extends Meeting{
    
    private Section parentSection;	// lecture's parent section
    
    /**
     * Constructor
     * @param s Section
     */
    public Lecture(Section s) {
        super();
        this.parentSection = s;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        String dept = this.parentSection.getParentCourse().getDepartment();
        String cNum = this.parentSection.getParentCourse().getNumber();
        String sec = this.parentSection.getSectionNum();
        return (dept + " " + cNum + " LEC " + sec);
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
}
