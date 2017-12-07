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
 * Abstract class representing Labs/Tutorials to be scheduled
 *
 */
public class NonLecture extends Meeting {

    private Course parentCourse;	// the parent course
    protected boolean evening;		// whether it is an evening class
    private boolean special;

    /*
     * TODO: figure out if evening Course means all labs and tutorials are
     * evening as well
     */
    /**
     * Constructor
     */
    public NonLecture() {
        super();
        parentCourse = null;
        evening = false;
        special = false;
    }

    /*
     *  Getters and setters
     */
    /**
     * Get the parent course
     *
     * @return The course
     */
    public Course getParentCourse() {
        return parentCourse;
    }

    /**
     * Set the parent course
     *
     * @param c The course
     */
    public void setParentCourse(Course c) {
        this.parentCourse = c;
    }

    /**
     * Check if an evening nonlecture
     *
     * @return True if evening nonlecture
     */
    public boolean isEvening() {
        return evening;
    }

    /**
     * Get the department
     *
     * @return Department string
     */
    public String getDept() {
        return this.getParentCourse().getDepartment();
    }

    /**
     * Get the course number
     *
     * @return Course number string
     */
    public String getCourseNum() {
        if (this.getParentSection() == null) {
            return this.getParentCourse().getNumber();
        }
        return this.getParentCourse().getNumber();
    }

    /**
     * Get the section number
     *
     * @return Section number string
     */
    public String getSectionNum() {
        if (this.getParentSection() == null) {
            return null;
        } else {
            return this.getParentSection().getSectionNum();
        }
    }
    
    /**
     * 
     */
    public void setSpecial() { special = true; }
    
    /**
     * @return
     */
    public boolean isSpecial() { return special; }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String dept = this.getParentCourse().getDepartment();
        String cNum = this.getParentCourse().getNumber();
        return (dept + " " + cNum);
    }
}
