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

public abstract class Slot {

    String day;
    int hour;
    int minute;

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
    
    public Slot() {
        day = null;
        hour = -1;
        minute = -1;
    }
    
    public boolean equals(Slot s) {
    	if (this.day.equals(s.day) && this.hour == s.hour && this.minute == s.minute) {
    		return true;
    	}
    	else return false;
    }
    
}
