package Schedule;

/**
 * Class representing a set of paired meetings
 *
 */
public class MeetingPair {
	
	/* THIS IS NOT CURRENTLY IN USE */

	// the meetings
	private Meeting m1, m2;
	
	/**
	 * Constructor
	 * 
	 * @param m1 First Meeting
	 * @param m2 Second Meeting
	 */
	public MeetingPair(Meeting m1, Meeting m2) {
		this.m1 = m1;
		this.m2 = m2;
	}
	
	/**
	 * Get the first meeting of the pair
	 * 
	 * @return First Meeting
	 */
	public Meeting getFirst() {
		return m1;
	}
	
	/**
	 * Get the second meeting of the pair
	 * 
	 * @return Second Meeting
	 */
	public Meeting getSecond() {
		return m2;
	}
}
