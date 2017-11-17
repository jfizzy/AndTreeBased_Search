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
 * @author 
 *
 */
public class LectureSlot extends Slot {

    private final int coursemax;	// maximum lectures for the slot
    private final int coursemin;	// minimum lectures for the slot
    
    /**
     * constructor
     * @param day
     * @param h
     * @param m
     * @param eh
     * @param em
     * @param cmax
     * @param cmin
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
