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
public class Lab extends NonLecture{
    
    private final String labNum;	// lab number
    
    /**
     * constructor
     * @param num
     * @param s
     * @param evening
     */
    public Lab(String num, Section s, boolean evening){
        this.labNum = num;
        this.parentSection = s;
        this.evening = evening;
    }
    
    /*
     *  getters and setters
     */

    public String getLabNum() {
        return labNum;
    }

}
