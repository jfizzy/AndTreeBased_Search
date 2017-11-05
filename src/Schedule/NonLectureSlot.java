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
 *
 * @author jmaci
 */
public class NonLectureSlot extends Slot {
    
    public NonLectureSlot(String day, int h, int m){
        this.day = day;
        this.hour = h;
        this.minute = m;
    }
    
}
