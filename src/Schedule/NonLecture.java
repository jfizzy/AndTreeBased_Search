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
 * @author 
 *
 */
public abstract class NonLecture extends Meeting {

    private Course parentCourse;
    protected boolean evening;			// whether it is an evening class
    
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
     *  getters and setters
     */

    public Course getParentCourse(){
        return parentCourse;
    }
    
    public void setParentCourse(Course c){
        this.parentCourse = c;
    }

    public boolean isEvening() {
        return evening;
    }
    
    public String getDept(){
        return this.getParentSection().getParentCourse().getDepartment();
    }
    
    public String getCourseNum(){
        return this.getParentSection().getParentCourse().getNumber();
    }
    
    public String getSectionNum(){
        return this.getParentSection().getSectionNum();
    }
    
}
