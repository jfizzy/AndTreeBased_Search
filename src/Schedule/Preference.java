package Schedule;

/**
 * Class representing a preferred slot and the preference value
 * (profs can say how much they prefer their lecture in a certain slot,
 * this is a soft constraint)
 */
public class Preference {
	
	private Slot s;		// the slot
	private int value;	// the preference value

	/**
	 * Constructor
	 * @param s The slot
	 * @param value The value
	 */
	public Preference(Slot s, int value) {
		this.s = s;
		this.value = value;
	}
	
	/*
	 * Getters and setters
	 */
	
	/**
	 * Get the slot
	 * 
	 * @return The slot
	 */
	public Slot getSlot() {
		return s;
	}
	
	/**
	 * Set the slot
	 * 
	 * @param s The slot
	 */
	public void setSlot(Slot s) {
		this.s = s;
	}
	
	/**
	 * Get the value
	 * 
	 * @return The value
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * Set the value
	 * 
	 * @param value The value
	 */
	public void setValue(int value) {
		this.value = value;
	}
        
        @Override
        public String toString(){
            return this.s.toString() + " = (" + this.value+")";
        }
}
