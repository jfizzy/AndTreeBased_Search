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

public abstract class NonLecture extends Meeting {

    protected Section parentSection;
    protected boolean evening;

    public Section getParentSection() {
        return parentSection;
    }

    public boolean isEvening() {
        return evening;
    }
    
    /**TODO:    figure out if evening Course means all labs and 
     *          tutorials are evening as well
     */
    public NonLecture(){
        super();
        this.parentSection = null;
        this.evening = false;
    }
    
    public String getDept(){
        return parentSection.getParentCourse().getDepartment();
    }
    
    public String getCourseNum(){
        return parentSection.getParentCourse().getNumber();
    }
    
    public String getSectionNum(){
        return parentSection.getSectionNum();
    }
    
}
