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
 * Object representing a lecture to be scheduled
 * @author 
 *
 */
public class Lecture extends Meeting{
    
    /**
     * Constructor
     * @param s Section
     */
    public Lecture(Section s) {
        super();
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        String dept = this.getParentSection().getParentCourse().getDepartment();
        String cNum = this.getParentSection().getParentCourse().getNumber();
        String sec = this.getParentSection().getSectionNum();
        return (dept + " " + cNum + " LEC " + sec);
    }  
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o){
        Lecture l;
        try{
            l = (Lecture) o;
        }catch(ClassCastException cce){
            return false;
        }
        return this.getParentSection().getSectionNum().equals(l.getParentSection().getSectionNum())
                && this.getParentSection().getParentCourse().getDepartment().equals(l.getParentSection().getParentCourse().getDepartment())
                && this.getParentSection().getParentCourse().getNumber().equals(l.getParentSection().getParentCourse().getNumber());
    }
}
