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
 * Object representing a time slot into which a lecture can be scheduled
 * @author 
 *
 */
public class LectureSlot extends Slot {

    private int coursemax;	// maximum lectures for the slot
    private int coursemin;	// minimum lectures for the slot
    
    /**
     * Constructor
     * 
     * @param day Day
     * @param h Start hour
     * @param m Start minute
     * @param eh End hour
     * @param em End minute
     * @param cmax Coursemax
     * @param cmin Coursemin
     * @param isEvening Evening flag
     */
    public LectureSlot(String day, int h, int m, int eh, int em, int cmax, int cmin, boolean isEvening){
        this.day = day;
        this.hour = h;
        this.minute = m;
        this.endhour = eh;
        this.endminute = em;
        this.coursemax = cmax;
        this.coursemin = cmin;
        this.evening = isEvening;
    }
    
    /*
     *  Getters and setters
     */
    
    /**
     * Get course minimum
     * 
     * @return Course minimum
     */
    public int getCourseMin() {
    	return this.coursemin;
    }
    
    /**
     * Get course maximum
     * 
     * @return Course maximum
     */
    public int getCourseMax() {
    	return this.coursemax;
    }
    
    /**
     * Set course minimum
     * 
     * @param cmin The value
     */
    public void setCourseMin(int cmin) {
    	this.coursemin = cmin;
    }
    
    /**
     * Set course maximum
     * 
     * @param cmax The value
     */
    public void setCourseMax(int cmax) {
    	this.coursemax = cmax;
    }
    
    /**
     * Checks if the slot is active
     * 
     * @return True if slot is active
     */
    public boolean isActive(){
        return (this.coursemax > 0);
    }
    
    /**
     * Activate the slot
     * 
     * @param cmax Course max
     * @param cmin Course min
     */
    public void activate(int cmax, int cmin){
        this.coursemax = cmax;
        this.coursemin = cmin;
    }
    
    /**
     * equals() Overrides the default implementation.
     * compares two NonLectureSlots for equality
     * TODO may want to add functionality for comparing coursemin
     * coursemax
     * @param o Object to compare to
     * @return true if same, false otherwise
     */
    public boolean equals(Object o){
        LectureSlot ls;
        try{
            ls = (LectureSlot) o;
        }catch(ClassCastException cce){
            return false;
        }
        return this.day.equals(ls.day) && this.hour == ls.hour
                && this.minute == ls.minute && this.endhour == ls.endhour 
                && this.endminute == ls.endminute;
    }
    
}
