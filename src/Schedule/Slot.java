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
 * Abstract class representing a time slot
 * @author
 *
 */
public abstract class Slot {

    protected String day;		// slot day string
    protected int hour;			// slot begin hour
    protected int minute;		// slot begin minute
    protected int endhour;		// slot end hour
    protected int endminute;	// slot end minute
    protected boolean evening;
    /**
     * Default constructor
     */
    public Slot() {
        day = null;
        hour = -1;
        minute = -1;
        endhour = -1;
        endminute = -1;
        evening = false;
    }

    /**
     * check if slots are equal (have all the same values)
     *
     * @param s
     * @return True if slots are equal
     */
    public boolean equals(Slot s) {
        if (day == s.getDay() && hour == s.getHour() && minute == s.getMinute()
                && endhour == s.getEndHour() && endminute == s.getEndMinute()) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean isEvening(){
        return this.evening;
    }
    
    /**
     * check if slots overlap in time
     *
     * @param s
     * @return True if slots overlap in time
     */
    public boolean overlaps(Slot s) {

        // return false if days don't match
    	if ((day.equals("MO") || day.equals("WE") || day.equals("FR")) 
    			&& (s.getDay().equals("TU") || s.getDay().equals("TH")))
    		return false;
    	if ((day.equals("TU") || day.equals("TH")) 
    			&& !(s.getDay().equals("TU") || s.getDay().equals("TH")))
    		return false;

        // combine hours and minutes
        int begin = 60 * hour + minute;
        int end = 60 * endhour - endminute;
        int sbegin = 60 * s.getHour() + s.getMinute();
        int send = 60 * s.getEndHour() + s.getEndMinute();

        // check for overlap
        if ((begin < sbegin && end > sbegin)
                || (begin < send && end > send)
                || (begin == sbegin && end == send)) {
            return true;
        }

        // if we got here no overlap
        return false;
    }

    /*
     *  getters and setters
     */
    // day
    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    // hour
    public int getHour() {
        return hour;
    }
    
    public String printHour(){
        String s = "" + hour;
        if(s.length() < 2)
            s = "0"+s;
        return s;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    // minute
    public int getMinute() {
        return minute;
    }

    public String printMinute(){
        String s = "" + minute;
        if(s.length() < 2)
            s = "0"+s;
        return s;
    }
    
    public void setMinute(int minute) {
        this.minute = minute;
    }

    // end hour
    public int getEndHour() {
        return endhour;
    }

    public void setEndHour(int hour) {
        this.endhour = hour;
    }

    // end minute
    public int getEndMinute() {
        return endminute;
    }

    public void setEndMinute(int minute) {
        this.endminute = minute;
    }
}
