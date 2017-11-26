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

// NOTE: THIS NEEDS TO BE UPDATED TO USE ARRAYS - eml


/**
 * Slot: abstract class that represents a timeslot within a schedule week. One slot can represent 
 * several different days and times, which is handled using arrays and an index.
 * @author jmaci
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
     * Check if slots are equal (have all the same values)
     *
     * @param s
     * @return True if slots are equal
     */
    public boolean equals(Slot s) {
    	if (s == null) return false;
        if (day == s.getDay() && hour == s.getHour() && minute == s.getMinute()
                && endhour == s.getEndHour() && endminute == s.getEndMinute()) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Check if slots overlap with one another.
     * @param s Another slot to compare against.
     * @return True if times of this slot overlaps with slot s.
     */
    public boolean overlaps(Slot s) {
    	
    	if (s == null) return false;

        // return false if days don't match
    	if ((day.equals("MO") || day.equals("WE") || day.equals("FR")) 
    			&& (s.getDay().equals("TU") || s.getDay().equals("TH")))
    		return false;
    	if ((day.equals("TU") || day.equals("TH")) 
    			&& !(s.getDay().equals("TU") || s.getDay().equals("TH")))
    		return false;

        // combine hours and minutes
        int begin = 60 * hour + minute;
        int end = 60 * endhour + endminute;
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
    
    /**
     * Check if evening
     * 
     * @return True if evening
     */
    public boolean isEvening(){
        return this.evening;
    }

    /*
     *  Getters and setters
     */
    
    /**
     * Get the day
     * 
     * @return Day string
     */
    public String getDay() {
        return day;
    }

    /**
     * Set the day
     * 
     * @param day Day string
     */
    public void setDay(String day) {
        this.day = day;
    }

    /**
     * Get the hour
     * 
     * @return The hour
     */
    public int getHour() {
        return hour;
    }
    
    /**
     * Get the hour as a string
     * 
     * @return Hour string
     */
    public String printHour(){
        String s = "" + hour;
        if(s.length() < 2)
            s = "0"+s;
        return s;
    }

    /**
     * Set the hour
     * 
     * @param hour The hour
     */
    public void setHour(int hour) {
        this.hour = hour;
    }

    /**
     * Get the minute
     * 
     * @return The minute
     */
    public int getMinute() {
        return minute;
    }

    /**
     * Get the minute as a string
     * 
     * @return Minute string
     */
    public String printMinute(){
        String s = "" + minute;
        if(s.length() < 2)
            s = "0"+s;
        return s;
    }
    
    /**
     * Set the minute
     * 
     * @param minute The minute
     */
    public void setMinute(int minute) {
        this.minute = minute;
    }

    /**
     * Get the end hour
     * 
     * @return End hour
     */
    public int getEndHour() {
        return endhour;
    }

    /**
     * Set the end hour
     * 
     * @param hour End hour
     */
    public void setEndHour(int hour) {
        this.endhour = hour;
    }

    /**
     * Get the end minute
     * 
     * @return End minute
     */
    public int getEndMinute() {
        return endminute;
    }

    /**
     * Set the end minute
     * 
     * @param minute End minute
     */
    public void setEndMinute(int minute) {
        this.endminute = minute;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        return getDay()+" "+printHour()+":"+printMinute();
    }
}
