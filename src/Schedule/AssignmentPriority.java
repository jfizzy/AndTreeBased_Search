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
public class AssignmentPriority{
    
    public boolean evening;
    public int incompatibilities;
    public int preferencePenaltySum;
    public int unwanted;
    public int pairs;
    public final int type;
    public final int courseNum, secNum;
    /**
     * Constructor 
     * @param m
     */
    public AssignmentPriority (Meeting m){
        
        this.evening = false;
        if (m.getParentSection() != null) {
            if (m.getParentSection().isEvening()) {
                this.evening = true;
            }
        }
        //check how many incompatibilities
        this.incompatibilities = 0;
        incompatibilities = m.getIncompatibility().stream().map((_item) -> 1).reduce(this.incompatibilities, Integer::sum);
        
        // check preference penalties
        this.preferencePenaltySum = 0;
        m.getPreferences().forEach((p) -> {
            this.preferencePenaltySum= this.preferencePenaltySum + p.getValue();
        });
        
        // check how many unwanteds
        this.unwanted = 0;
        this.unwanted = m.getUnwanted().stream().map((_item) -> 1).reduce(this.unwanted, Integer::sum);
        
        this.pairs = 0;
        this.pairs = m.getPaired().stream().map((_item) -> 1).reduce(this.pairs, Integer::sum);
        
        if(m instanceof Lecture)
            this.type = 2; // lecture
        else if(m instanceof Lab)
            this.type = 1; // lab
        else
            this.type = 0; // tutorial
        
        if(m.getParentSection() != null){
            this.courseNum = Integer.parseInt(m.getParentSection().getParentCourse().getNumber());
            this.secNum = Integer.parseInt(m.getParentSection().getSectionNum());
        }else{
            NonLecture nl = ((NonLecture) m);
            this.courseNum = Integer.parseInt(nl.getCourseNum());
            this.secNum = -1;
        }
    }

    /**
     * compare.
     * compares two AssignmentPriority objects and picks the most 
     * restrictive of the two. When there is a tie on all restrictive
     * attributes, decides on a best using tie breaking functionality
     * 
     * @param ap1
     * @param ap2
     * @return
     */
    public static int compare(AssignmentPriority ap1, AssignmentPriority ap2) {
        // compare evening
        if(ap1.evening != ap2.evening){
            if(ap1.evening)
                return 1; // ap1 > ap2
            else
                return -1; // ap1 < ap2
        }
        // compare number of incompatibilities
        if(ap1.incompatibilities != ap2.incompatibilities){
            return Integer.compare(ap1.incompatibilities, ap2.incompatibilities);
        }
        // compare sum of preference penalties
        if(ap1.preferencePenaltySum != ap2.preferencePenaltySum){
            return Integer.compare(ap1.preferencePenaltySum, ap2.preferencePenaltySum);
        }
        // compare num of unwanted
        if(ap1.unwanted != ap2.unwanted){
            return Integer.compare(ap1.unwanted, ap2.unwanted);
        }
        // compare num of pairs
        if(ap1.pairs != ap2.pairs){
            return Integer.compare(ap1.pairs, ap2.pairs);
        }
        // compare type
        if(ap1.type != ap2.type){
            return Integer.compare(ap1.type, ap2.type);
        }
        // compare course number
        if(ap1.courseNum != ap2.courseNum){
            Integer.compare(ap1.courseNum, ap2.courseNum);
        }
        // compare section number
        boolean ap1NoSec = (ap1.courseNum < 1);
        boolean ap2NoSec = (ap2.courseNum < 1);
        if(ap1NoSec || ap2NoSec){
            if(ap1NoSec && ap2NoSec){
                
            } else if (ap1NoSec){ // ap2 has a section
                return -1; // ap1 < ap2
            } else { // ap2NoSec - ap1 has a section
                return 1; // ap1 > ap2
            }
        }else{ // both have sections
            return Integer.compare(ap1.secNum, ap2.secNum);
        }
        // tiebreaker - return the left item as better
        return 1;
    }
    
    @Override
    public String toString(){
        return this.evening+", "+this.incompatibilities+", "
                +this.preferencePenaltySum+", "+this.unwanted+", "
                +this.pairs+", "+this.type+", "+this.courseNum+", "+this.secNum;
    }
        
}
