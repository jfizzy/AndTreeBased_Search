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
public class NonLectureSlot extends Slot {
    
    private int labmax;	// maximum nonlectures for the slot
    private int labmin;	// minimum nonlectures for the slot
    
    /**
     * constructor
     * @param day
     * @param h
     * @param m
     * @param eh
     * @param em
     * @param lmax
     * @param lmin
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
     *  getters and setters
     */
    
    public int getLabMin() {
    	return this.labmin;
    }
    
    public int getLabMax() {
    	return this.labmax;
    }
    
    public boolean isActive(){
        return (this.labmax > 0);
    }
    
    public void activate(int labmax, int labmin){
        this.labmax = labmax;
        this.labmin = labmin;
    }
    
}
