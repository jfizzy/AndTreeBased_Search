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

package Search;

public class Eval {
	private int evalMinFill;
	private int evalPref;
	private int evalPair;
	private int evalSecDiff;
	
	private int wMin;
	private int wPref;
	private int wPair;
	private int wSecDiff;
	
	
	//default constructor
	public Eval() {
		//defaults to zero evaluation values and equal weights
		this(1,1,1,1);
	}
	
	public Eval(int min, int pref, int pair, int secD) {
		evalMinFill = 0;
		evalPref = 0;
		evalPair = 0;
		evalSecDiff = 0;
		
		wMin = min;
		wPref= pref;
		wPair = pair;
		wSecDiff = secD;
	}
	
	
	//getters and setters
	public int getMinFill() {
		return wMin*evalMinFill;
	}
	
	public int getPref() {
		return wPref*evalPref;
	}
	 
	public int getPair() {
		return wPair*evalPair;
	}
	
	public int getSecDiff() {
		return wSecDiff*evalSecDiff;
	}
	
	public void setMinWeight(int weight) {
		wMin = weight;
	}
	public void setPrefWeight(int weight) {
		wPref = weight;
	}
	public void setPairWeight(int weight) {
		wPair = weight;
	}
	public void setSecDiffWeight(int weight) {
		wSecDiff = weight;
	}
}
