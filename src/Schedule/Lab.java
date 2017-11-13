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

public class Lab extends NonLecture{
    
    private final String labNum;

    public String getLabNum() {
        return labNum;
    }
    
    public Lab(String num, Section s, boolean evening){
        this.labNum = num;
        this.parentSection = s;
        this.evening = evening;
    }
}
