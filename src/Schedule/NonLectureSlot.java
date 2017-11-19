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
 * @author 
 *
 */
public class NonLectureSlot extends Slot {
    
    private final int labmax;	// maximum nonlectures for the slot
    private final int labmin;	// minimum nonlectures for the slot
    
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
    public NonLectureSlot(String day, int h, int m, int eh, int em, int lmax, int lmin){
        this.day = day;
        this.hour = h;
        this.minute = m;
        this.endhour = eh;
        this.endminute = em;
        this.labmax = lmax;
        this.labmin = lmin;
    }
    
    /*
     *  getters and setters
     */
    
    public int getLabMin() {
    	return this.labmin;
    }
    
    public int getLabMax() {
    	return this.labmax;
    }
    
}
