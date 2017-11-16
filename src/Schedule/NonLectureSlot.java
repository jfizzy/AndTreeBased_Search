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

public class NonLectureSlot extends Slot {
    
    private final int labmax;
    private final int labmin;
    
    public NonLectureSlot(String day, int h, int m, int eh, int em, int lmax, int lmin){
        this.day = day;
        this.hour = h;
        this.minute = m;
        this.endhour = eh;
        this.endminute = em;
        this.labmax = lmax;
        this.labmin = lmin;
    }
    
    public int getLabMin() {
    	return this.labmin;
    }
    
    public int getLabMax() {
    	return this.labmax;
    }
    
}
