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
 * Class for determining the relative priority of assignments
 * @author jmaci
 */
public class AssignmentPriority{
    
    public boolean evening;				// evening flag
    public int incompatibilities;		// number of incompatibilities
    public int preferencePenaltySum;	// sum of preference penalties
    public int unwanted;				// number of unwanted slots
    public int pairs;					// number of paired courses
    public final int type;				// type of meeting
    public final int courseNum, secNum;	// course and section number
    
    /**
     * Constructor 
     * 
     * @param m Meeting
     */
    public AssignmentPriority (Meeting m){
        
    	// check if evening
        this.evening = false;
        if (m.getParentSection() != null) {
            if (m.getParentSection().isEvening()) {
                this.evening = true;
            }
        }
        
        // check how many incompatibilities
        //this.incompatibilities = 0;
        incompatibilities = m.getIncompatibility().size();//.stream().map((_item) -> 1).reduce(this.incompatibilities, Integer::sum);
        
        // check preference penalties
        this.preferencePenaltySum = 0;
        m.getPreferences().forEach((p) -> {
            this.preferencePenaltySum += p.getValue() * m.getPreferences().size();
        });
        
        // check how many unwanteds
        //this.unwanted = 0;
        this.unwanted = m.getUnwanted().size();//.stream().map((_item) -> 1).reduce(this.unwanted, Integer::sum);
        
        //this.pairs = 0;
        this.pairs = m.getPaired().size();//.stream().map((_item) -> 1).reduce(this.pairs, Integer::sum);
        
        if(m instanceof Lecture)
            this.type = 2; // lecture
        else if(m instanceof Lab)
            this.type = 1; // lab
        else
            this.type = 0; // tutorial
        
        if (m.getParentSection() != null) {
            this.courseNum = Integer.parseInt(m.getParentSection().getParentCourse().getNumber());
            this.secNum = Integer.parseInt(m.getParentSection().getSectionNum());
        }
        else {
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
     * @param ap1 First assignment
     * @param ap2 Second assignment
     * @return Compare value
     */
    public static int compare(AssignmentPriority ap1, AssignmentPriority ap2) {
    	
    	int result = 0;
        
    	// compare sum of preference penalties
        if(ap1.preferencePenaltySum != ap2.preferencePenaltySum){
        	result += 500*(ap1.preferencePenaltySum - ap2.preferencePenaltySum);
        }
        
        // compare evening
        if(ap1.evening != ap2.evening){
            if(ap1.evening)
                result += 1000; // ap1 > ap2
            else
                result += -1000; // ap1 < ap2
        }
        
        // compare num of pairs
        if(ap1.pairs != ap2.pairs){
        	result += 100*(ap1.pairs - ap2.pairs);
        }
        
        // compare number of incompatibilities
        if(ap1.incompatibilities != ap2.incompatibilities){
        	result += 10*(ap1.incompatibilities - ap2.incompatibilities);
        }
        
        // compare num of unwanted
        if(ap1.unwanted != ap2.unwanted){
        	result += (ap1.unwanted - ap2.unwanted);
        }
        
        if (result != 0) return result;
        
        // compare type
        if(ap1.type != ap2.type){
            return Integer.compare(ap1.type, ap2.type);
        }
        
        // compare course number
        if(ap1.courseNum != ap2.courseNum){
            return Integer.compare(ap1.courseNum, ap2.courseNum);
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
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        return this.evening+", "+this.incompatibilities+", "
                +this.preferencePenaltySum+", "+this.unwanted+", "
                +this.pairs+", "+this.type+", "+this.courseNum+", "+this.secNum;
    }
        
}
