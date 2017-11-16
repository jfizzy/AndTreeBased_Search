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
public abstract class Slot {

    protected String day;
    protected int hour;
    protected int minute;
    protected int endhour;
    protected int endminute;

    /**
     * default constructor
     */
    public Slot() {
        day = null;
        hour = -1;
        minute = -1;
        endhour = -1;
        endminute = -1;
    }
    
    /**
     * check if slots are equal
     * (have all the same values)
     * @param s
     * @return
     */
    public boolean equals(Slot s) {
    	if (day == s.getDay() && hour == s.getHour() && minute == s.getMinute()
    			&& endhour == s.getEndHour() && endminute == s.getEndMinute())
    		return true;
    	else
    		return false;
    }
    
    /**
     * check if slots overlap in time
     * @param s
     * @return
     */
    public boolean overlaps(Slot s) {
    	
    	// return false if days don't match
    	// TODO: this is going to have problems
    	// like checking if a Fr lab overlaps a Mo(WeFr) lecture
    	if (!day.equals(s.getDay()))
    		return false;
    	
    	// combine hours and minutes
    	int begin = 60 * hour + minute;
    	int end = 60 * endhour - endminute;
    	int sbegin = 60 * s.getHour() + s.getMinute();
    	int send = 60 * s.getEndHour() + s.getEndMinute();
    	
    	// check for overlap
    	if ((begin < sbegin && end > sbegin) 
    			|| (begin < send && end > send)
    			|| (begin == sbegin && end == send))
    		return true;
    	
    	// if we got here no overlap
    	return false;
    }
    
    // getters and setters
    
    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
    
    public int getEndHour() {
        return endhour;
    }

    public void setEndHour(int hour) {
        this.endhour = hour;
    }

    public int getEndMinute() {
        return endminute;
    }

    public void setEndMinute(int minute) {
        this.endminute = minute;
    }
    
}
