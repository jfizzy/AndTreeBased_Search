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
	
	public Slot getSlot() {
		return s;
	}
	
	public void setSlot(Slot s) {
		this.s = s;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
}
