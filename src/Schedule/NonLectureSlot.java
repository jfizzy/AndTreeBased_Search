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
 * Object representing a time slot into which a nonlecture can be scheduled
 *
 */
public class NonLectureSlot extends Slot {
    
    private int labmax;	// maximum nonlectures for the slot
    private int labmin;	// minimum nonlectures for the slot
    
    /**
     * constructor
     * @param day Day
     * @param h Begin hour
     * @param m Begin minute
     * @param eh End hour
     * @param em End minute
     * @param lmax Lab max
     * @param lmin Lab min
     */
    public NonLectureSlot(String day, int h, int m, int eh, int em, int lmax, int lmin, boolean isEvening){
        super();
        this.day = day;
        this.hour = h;
        this.minute = m;
        this.endhour = eh;
        this.endminute = em;
        this.labmax = lmax;
        this.labmin = lmin;
        this.evening = isEvening;
    }
    
    /*
     *  Getters and setters
     */
    
    /**
     * Get lab minimum
     * 
     * @return The value
     */
    public int getLabMin() {
    	return this.labmin;
    }
    
    /**
     * Get lab maximum
     * @return The value
     */
    public int getLabMax() {
    	return this.labmax;
    }
    
    /**
     * Set lab minimum
     * 
     * @param lmin The value
     */
    public void setLabMin(int lmin) {
    	this.labmin = lmin;
    }
    
    /**
     * Set lab maximum
     * 
     * @param lmax The value
     */
    public void setLabMax(int lmax) {
    	this.labmax = lmax;
    }
    
    /**
     * Check if the slot is active
     * 
     * @return True if the slot is active
     */
    public boolean isActive(){
        return (this.labmax > 0);
    }
    
    /**
     * Activate the slot
     * 
     * @param labmax Lab maximum
     * @param labmin Lab minimum
     */
    public void activate(int labmax, int labmin){
        this.labmax = labmax;
        this.labmin = labmin;
    }
    
}
