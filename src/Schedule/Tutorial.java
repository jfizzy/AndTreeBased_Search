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
public class Tutorial extends NonLecture {

    private final String tutNum;	// tutorial number

    /**
     * Constructor
     *
     * @param num Tutorial number
     * @param s Section number
     * @param evening Evening flag
     */
    public Tutorial(String num, Section s, boolean evening) {
        super();
        this.tutNum = num;
        this.setParentSection(s);
        this.evening = evening;
    }

    /*
     *  getters and setters
     */
    public String getTutNum() {
        return tutNum;
    }

    @Override
    public String toString() {
        if (this.getParentSection() == null) {
            String dept = this.getParentCourse().getDepartment();
            String cNum = this.getParentCourse().getNumber();
            return (dept + " " + cNum + " TUT " + this.tutNum);
        } else {
            String dept = this.getParentSection().getParentCourse().getDepartment();
            String cNum = this.getParentSection().getParentCourse().getNumber();
            String sec = this.getParentSection().getSectionNum();
            return (dept + " " + cNum + " LEC " + sec + " TUT " + this.tutNum);
        }
    }

}
