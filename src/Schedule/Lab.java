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
 * Object representing a lab to be scheduled
 *
 */
public class Lab extends NonLecture {

    private final String labNum;	// lab number

    /**
     * Constructor
     *
     * @param num Lab number
     * @param s Section number
     * @param evening Evening course flag
     */
    public Lab(String num, Section s, boolean evening) {
        this.labNum = num;
        this.setParentSection(s);
        this.evening = evening;
    }

    /**
     * Get the lab number
     *
     * @return The lab number string
     */
    public String getLabNum() {
        return labNum;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (this.getParentSection() == null) {
            String dept = this.getParentCourse().getDepartment();
            String cNum = this.getParentCourse().getNumber();
            return (dept + " " + cNum + " LAB " + this.labNum);
        } else {
            String dept = this.getParentSection().getParentCourse().getDepartment();
            String cNum = this.getParentSection().getParentCourse().getNumber();
            String sec = this.getParentSection().getSectionNum();
            return (dept + " " + cNum + " LEC " + sec + " LAB " + this.labNum);
        }

    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
        Lab l;
        try {
            l = (Lab) o;
        } catch (ClassCastException cce) {
            return false;
        }
        if (this.labNum.equals(l.getLabNum())) {
            if (this.getParentSection() != null && l.getParentSection() != null) {
                return this.getParentSection().getSectionNum().equals(l.getParentSection().getSectionNum())
                        && this.getParentSection().getParentCourse().getDepartment().equals(l.getParentSection().getParentCourse().getDepartment())
                        && this.getParentSection().getParentCourse().getNumber().equals(l.getParentSection().getParentCourse().getNumber());
            } else if (this.getParentSection() == null && l.getParentSection() == null) {
                return this.getParentCourse().getDepartment().equals(l.getParentCourse().getDepartment())
                        && this.getParentCourse().getNumber().equals(l.getParentCourse().getNumber());
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}
