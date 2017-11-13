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

public class Tutorial extends NonLecture{
    
    private final String tutNum;

    public String getTutNum() {
        return tutNum;
    }
    
    public Tutorial(String num, Section s, boolean evening){
        super();
        this.tutNum = num;
        this.parentSection = s;
        this.evening = evening;
    }
}
