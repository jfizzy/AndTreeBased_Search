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
public class Tutorial extends NonLecture{
    
    private final String tutNum;	// tutorial number
    
    /**
     * constructor
     * @param num
     * @param s
     * @param evening
     */
    public Tutorial(String num, Section s, boolean evening){
        super();
        this.tutNum = num;
        this.parentSection = s;
        this.evening = evening;
    }
    
    /*
     *  getters and setters
     */

    public String getTutNum() {
        return tutNum;
    }
    
    @Override
    public String toString(){
        String dept = this.parentSection.getParentCourse().getDepartment();
        String cNum = this.parentSection.getParentCourse().getNumber();
        String sec = this.parentSection.getSectionNum();
        return (dept + " " + cNum + " LEC " + sec + " TUT " + this.tutNum);
    }
    
}
