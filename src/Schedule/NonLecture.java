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
public abstract class NonLecture extends Meeting {

    private Course parentCourse;	// the parent course
    protected boolean evening;		// whether it is an evening class
    
    /**TODO:    figure out if evening Course means all labs and 
     *          tutorials are evening as well
     */
    /**
     * Constructor
     */
    public NonLecture(){
        super();
        this.parentCourse = null;
        this.evening = false;
    }
    
    /*
     *  Getters and setters
     */

    /**
     * Get the parent course
     * 
     * @return The course
     */
    public Course getParentCourse(){
        return parentCourse;
    }
    
    /**
     * Set the parent course
     * 
     * @param c The course
     */
    public void setParentCourse(Course c){
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
    public String getDept(){
        return this.getParentSection().getParentCourse().getDepartment();
    }
    
    /**
     * Get the course number
     * 
     * @return Course number string
     */
    public String getCourseNum(){
        return this.getParentSection().getParentCourse().getNumber();
    }
    
    /**
     * Get the section number
     * 
     * @return Section number string
     */
    public String getSectionNum(){
        return this.getParentSection().getSectionNum();
    }
    
}
