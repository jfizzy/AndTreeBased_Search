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

    private final int coursemax;	// maximum lectures for the slot
    private final int coursemin;	// minimum lectures for the slot
    
    /**
     * Constructor
     * @param day Day
     * @param h Begin hour
     * @param m Begin minute
     * @param eh End hour
     * @param em End minute
     * @param cmax Course max
     * @param cmin Course min
     */
    public LectureSlot(String day, int h, int m, int eh, int em, int cmax, int cmin){
        this.day = day;
        this.hour = h;
        this.minute = m;
        this.endhour = eh;
        this.endminute = em;
        this.coursemax = cmax;
        this.coursemin = cmin;
    }
    
    /*
     *  getters and setters
     */
    
    public int getCourseMin() {
    	return this.coursemin;
    }
    
    public int getCourseMax() {
    	return this.coursemax;
    }
    
}
