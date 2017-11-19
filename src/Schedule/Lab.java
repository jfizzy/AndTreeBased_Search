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
 * Object representing a lab to be scheduled
 * @author
 *
 */
public class Lab extends NonLecture {

    private final String labNum;	// lab number

    /**
     * Constructor
     *
     * @param num Lab number
     * @param s Section number
     * @param evening Evening course flag
     */
    public Lab(String num, Section s, boolean evening){
        this.labNum = num;
        this.parentSection = s;
        this.evening = evening;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        String dept = this.parentSection.getParentCourse().getDepartment();
        String cNum = this.parentSection.getParentCourse().getNumber();
        String sec = this.parentSection.getSectionNum();
        return (dept + " " + cNum + " LEC " + sec + " LAB " + this.labNum);
    }
    
    /*
     *  getters and setters
     */
    public String getLabNum() {
        return labNum;
    }

    @Override
    public String toString() {
        if (this.parentSection == null) {
            String dept = this.getParentCourse().getDepartment();
            String cNum = this.getParentCourse().getNumber();
            return (dept + " " + cNum + " LAB " + this.labNum);
        } else {
            String dept = this.parentSection.getParentCourse().getDepartment();
            String cNum = this.parentSection.getParentCourse().getNumber();
            String sec = this.parentSection.getSectionNum();
            return (dept + " " + cNum + " LEC " + sec + " LAB " + this.labNum);
        }

    }

}
