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

public class LectureSlot extends Slot {

    private final int coursemax;
    private final int coursemin;
    
    public LectureSlot(String day, int h, int m, int cmax, int cmin){
        this.day = day;
        this.hour = h;
        this.minute = m;
        this.coursemax = cmax;
        this.coursemin = cmin;
    }
    
}
