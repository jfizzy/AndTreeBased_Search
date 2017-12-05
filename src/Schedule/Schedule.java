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

import java.util.ArrayList;
import java.util.Collections;

import Search.Constr;
import Search.Eval;

/**
 * Object representing a schedule of courses, slots, and assignments
 *
 */
public class Schedule {

    private ArrayList<Assignment> assignments;          // list of assignments
    private Assignment[] assignArray;			// array of assignments
    private ArrayList<LectureSlot> lslots;		// list of lecture slots
    private ArrayList<NonLectureSlot> nlslots;      	// list of nonlecture slots
    private ArrayList<Course> courses;                  // list of courses
    private ArrayList<Lecture> lectures;                // list of lectures (filled from courses)
    private ArrayList<Lab> labs;			// list of labs (filled from courses)
    private ArrayList<Tutorial> tuts;                   // list of tutorials (filled from courses)
    private ArrayList<MeetingPair> pairs;		// list of paired courses
    private ArrayList<MeetingPair> noncompatible;	// list of incompatible courses
    
	// penalties
	private double pen_coursemin;
	private double pen_labmin;
	private double pen_notpaired;
	private double pen_section;
	
	// weights
	private double wMin;
	private double wPref;
	private double wPair;
	private double wSecDiff;
	
    /**
     * Constructor with slots and courses lists
     *
     * @param lslots
     * @param nlslots
     * @param courses
     */
    public Schedule(ArrayList<LectureSlot> lslots,
            ArrayList<NonLectureSlot> nlslots,
            ArrayList<Course> courses) {

    	// initialize lists
        this.lslots = lslots;
        this.nlslots = nlslots;
        this.courses = courses;
        this.lectures = new ArrayList<>();
        labs = new ArrayList<>();
        tuts = new ArrayList<>();
        assignments = new ArrayList<>();
        pairs = new ArrayList<>();
        noncompatible = new ArrayList<>();
        
        // process the meetings into lists
        processLectures();
        processLabs();
        processTuts();
        
        // create assignments list with null slots
        generateAssignments();
        
        // initialize penalties and weights
        pen_coursemin = 1;
        pen_labmin = 1;
        pen_notpaired = 1;
        pen_section = 1;
        wMin = 1;
        wPref = 1;
        wPair = 1;
        wSecDiff = 1;
    }
    
    /**
     * Constructor for copying a schedule
     * 
     * @param orig The schedule
     */
    public Schedule(Schedule orig) {
    	
    	// keep original lists
        lslots = orig.getLectureSlots();
        nlslots = orig.getNonLectureSlots();
        courses = orig.getCourses();
        lectures = orig.getLectures();
        labs = orig.getLabs();
        tuts = orig.getTuts();
        pairs = orig.getPairs();
        noncompatible = orig.getNoncompatible();

        // keep original penalties and weights
        pen_coursemin = orig.getCourseMinPenalty();
        pen_labmin = orig.getLabMinPenalty();
        pen_notpaired = orig.getPairPenalty();
        pen_section = orig.getSecDiffPenalty();
        wMin = orig.getMinWeight();
        wPref = orig.getPrefWeight();
        wPair = orig.getPairWeight();
        wSecDiff = orig.getSecDiffWeight();
        
        // make a copy of assignments list
        if (orig.getAssignments() != null) {
	        assignments = new ArrayList<>();
	        for (int i = 0; i < orig.getAssignments().size(); i++) {
	        	assignments.add(new Assignment(orig.getAssignments().get(i)));
	        }
        }
    }

    /**
     * Constructor for Constr/Eval
     * (makes a new schedule from the old with an added assignment)
     *
     * @param m
     * @param s
     * @param orig Original schedule
     */
    public Schedule(Schedule orig, Meeting m, Slot s) {
    	
    	// keep original lists
        lslots = orig.getLectureSlots();
        nlslots = orig.getNonLectureSlots();
        courses = orig.getCourses();
        lectures = orig.getLectures();
        labs = orig.getLabs();
        tuts = orig.getTuts();
        pairs = orig.getPairs();
        noncompatible = orig.getNoncompatible();

        // keep original penalties and weights
        pen_coursemin = orig.getCourseMinPenalty();
        pen_labmin = orig.getLabMinPenalty();
        pen_notpaired = orig.getPairPenalty();
        pen_section = orig.getSecDiffPenalty();
        wMin = orig.getMinWeight();
        wPref = orig.getPrefWeight();
        wPair = orig.getPairWeight();
        wSecDiff = orig.getSecDiffWeight();
        
        // make a copy of assignments list and add the new assignment
        if (orig.getAssignments() != null) {
	        assignments = new ArrayList<>();
	        for (int i = 0; i < orig.getAssignments().size(); i++) {
	        	assignments.add(new Assignment(orig.getAssignments().get(i)));
	        }
	        updateAssignment(m, s);
        }
        
        // make a copy of assignments array and add the new assignment
        if (orig.getAssignmentsArr() != null) {
	        int n = orig.getAssignmentsArr().length;
	        assignArray = new Assignment[n];
	        for (int i = 0; i < n; i++) {
	        	assignArray[i] = new Assignment(orig.getAssignmentsArr()[i]);
	        }
	        updateAssignmentArr(m, s);
        }
    }

    /**
     * Fills the lectures list using the courses list
     */
    private void processLectures() {
    	
        // for each course
        this.courses.forEach((c) -> {
        	
        	// for each lecture
            c.getCourseLectures().forEach((l) -> {
                this.lectures.add(l);
            });
        });
    }

    /**
     * Fills the labs list using the courses list
     */
    private void processLabs() {
    	
    	// for each course
        this.courses.forEach((c) -> {

        	// if the course has labs
            if (c.getCourseLabs() != null) {

            	// for each lab
                c.getCourseLabs().forEach((l) -> {
                    this.labs.add(l);
                });
            }
        });
    }

    /**
     * Fills the tutorials list using the courses list
     */
    private void processTuts() {
    	
    	// for each course
        this.courses.forEach((c) -> {
            
        	// if the course has tutorials
            if (c.getCourseTuts() != null) {

            	// for each tutorial
                c.getCourseTuts().forEach((t) -> {
                    this.tuts.add(t);
                });
            }
        });
    }

    /**
     * Generates the set of assignments for all of our meetings
     */
    private void generateAssignments() {
    	
    	// for each lecture
        this.lectures.forEach((l) -> {
            assignments.add(new Assignment(l, null));
        });
        
        // for each lab
        this.labs.forEach((l) -> {
            assignments.add(new Assignment(l, null));
        });
        
        // for each tutorial
        this.tuts.forEach((t) -> {
            assignments.add(new Assignment(t, null));
        });
    }
    
    /**
     * Attempts to assign preferences according to their highest values
     */
    public void assignPreferences() {
    	
    	for (Assignment a : assignments) {
    		
    		ArrayList<Preference> prefs = a.getM().getPreferences();
    		if (prefs.size() < 3)
    			continue;
    		
    		prefs.sort(Preference.PrefComparator);
    		Meeting m = a.getM();
    		
    		Slot best = null;
    		for (Preference p : m.getPreferences()) {
    			
    			if (this.isValidWith(m, p.getSlot()) && (best == null || 
    					this.evalWith(m, p.getSlot()) < this.evalWith(m, best))) {
        			best = p.getSlot();
    			}
    		}
    		if (best != null)
    			updateAssignment(m, best);
    	}
    }
    
    /**
     * Get the first null assignment
     * 
     * @return The assignment or null if all assigned
     */
    public Assignment findFirstNull() {
    	
    	// for each assignment
    	for (Assignment a : assignments) {
    		
    		// return the assignment if slot is null
    		if (a.getS() == null) 
    			return a;
    	}
    	
    	return null;
    }
    
    /**
     * Get the first null assignment
     * 
     * @return The assignment or null if all assigned
     */
    public Assignment findFirstNullArr() {
    	
    	// for each assignment
    	for (int i = 0; i < assignArray.length; i++) {
    		
    		// return the assignment if slot is null
    		if (assignArray[i].getS() == null) 
    			return assignArray[i];
    	}
    	
    	return null;
    }

    /**
     * Check if the schedule is completely assigned in a valid way
     * 
     * @return True if all courses have an assignment, false if there is work left to do
     */
    public boolean isComplete() {
    	
    	// for each assignment
		for (Assignment a: this.assignments) {
			
			// return false if slot is null
			if (a.getS() == null)
				return false;
		}
		
		// if we got here no slots are null
		return this.isValid();
    }
    
    /**
     * Check if the schedule is completely assigned
     * 
     * @return True if all courses have an assignment, false if there is work left to do
     */
    public boolean isCompleteArr() {
    	
    	// for each assignment
		for (int i = 0; i < assignArray.length; i++) {
			
			// return false if slot is null
			if (assignArray[i].getS() == null)
				return false;
		}
		
		// if we got here no slots are null
		return true;
    }
    
    /**
     * Checks if the schedule meets all hard constraints
     *
     * @return True if all hard constraints are met
     */
    public boolean isValid() {
        return Constr.check(this);
    }

    /**
     * Checks if adding an assignment to the schedule would meet hard
     * constraints
     *
     * @param a The assignment
     * @return True if the schedule with the assignment meets all hard
     * constraints
     */
    public boolean isValidWith(Assignment a) {
        return Constr.check(this, a.getM(), a.getS());
    }
    
    /**
     * Checks if adding an assignment to the schedule would meet hard
     * constraints
     *
     * @param m Meeting
     * @param s New Slot
     * @return True if the schedule with the assignment meets all hard
     * constraints
     */
    public boolean isValidWith(Meeting m, Slot s) {
        return Constr.check(this, m, s);
    }
    
    public boolean isValidWithArr(Meeting m, Slot s) {
        return Constr.checkArr(this, m, s);
    }
    
    /**
     * @param s
     * @return
     */
    public boolean isValidWith(Slot s) {
    	Meeting m = findFirstNull().getM();
    	return Constr.check(this, m, s);
    }

    /**
     * Get the evaluation without weights
     *
     * @return The evaluation of the schedule
     */
    public double eval() {
        return Eval.getEval(this);
    }

    /**
     * Get the evaluation with added assignment without weights
     *
     * @param a The assignment
     * @return The evaluation of the schedule with the assignment
     */
    public double evalWith(Assignment a) {
        return Eval.getEval(this, a.getM(), a.getS());
    }
    
    /**
     * Get the evaluation with added assignment without weights
     * 
     * @param m
     * @param s
     * @return The evaluation of the schedule with the assignment
     */
    public double evalWith(Meeting m, Slot s) {
        return Eval.getEval(this, m, s);
    }

    /**
     * Print the timetable for debugging
     */
    public void printAssignments() {

        // for each assignment
        assignments.stream().map((a) -> {
            System.out.print("Assigned: ");
            return a;
        }).map((a) -> {
            // if lecture
            if (a.getM().getClass() == Lecture.class) {
                Lecture l = (Lecture) a.getM();
                System.out.print(l.toString());
            } // if lab
            else if (a.getM().getClass() == Lab.class) {
                Lab lab = (Lab) a.getM();
                System.out.print(lab.toString()); // using toString to print
            } // if tutorial
            else if (a.getM().getClass() == Tutorial.class) {
                Tutorial tut = (Tutorial) a.getM();
                System.out.print(tut.toString()); // using toString to print
            }
            return a;
        }).map((a) -> {
            // slot
            if (a.getS() != null) {
                System.out.format(" --> %s %02d:%02d - %02d:%02d",
                        a.getS().getDay(), a.getS().getHour(), a.getS().getMinute(),
                        a.getS().getEndHour(), a.getS().getEndMinute());
            } else {
                System.out.print(" --> No Slot");
            }
            return a;
        }).forEachOrdered((_item) -> {
            System.out.print("\n");
        });
    }
    
    /**
     * Check if the number of meetings and number of slots
     * can possibly make a valid schedule
     * 
     * @return True if possible
     */
    public boolean isPossible() {
    	
    	int lmax = 0;
    	for (LectureSlot ls : lslots)
    		lmax += ls.getCourseMax();
    	int nlmax = 0;
    	for (NonLectureSlot nls : nlslots)
    		nlmax += nls.getLabMax();
    	return (lectures.size() <= lmax && (labs.size() + tuts.size()) <= nlmax);
    }
    
    /**
     * Generate array of assignments from list
     */
    public void generateAssignmentArray() {
    	int n = assignments.size();
    	assignArray = new Assignment[n];
    	for (int i = 0; i < n; i++) {
    		assignArray[i] = assignments.get(i); 
    	}
    }

    /*
     *  Getters and setters
     */
    
    /**
     * Get assignments list
     * 
     * @return Assignments list
     */
    public ArrayList<Assignment> getAssignments() {
        return assignments;
    }
    
    /**
     * Get assignments list
     * 
     * @return Assignments list
     */
    public Assignment[] getAssignmentsArr() {
        return assignArray;
    }

    /**
     * Set assignments list
     * @param assignments 
     */
    public void setAssignments(ArrayList<Assignment> assignments) {
        this.assignments = assignments;
    }

    /**
     * Add or update an assignment
     * 
     * @param a The assignment
     */
    public void addAssignment(Meeting m, Slot s) {
    	
    	// don't assign if slot is not in list and not null
    	if (s != null && !lslots.contains(s) && !nlslots.contains(s))
    		return;
    	
    	// for each assignment
    	for (Assignment a : assignments) {
    		
    		// skip if meeting doesn't match
    		if (a.getM() != m)
    			continue;
    		
    		// otherwise set slot and return
    		a.setS(s);
    		return;
    	}
    	
    	// if we got here meeting was not found, so add it
    	assignments.add(new Assignment(m, s));
    }
    
    /**
     * Update the assignment for a given meeting
     * (do nothing if assignment not found)
     * 
     * @param m The meeting
     * @param s The slot to assign it to
     */
    public void updateAssignment(Meeting m, Slot s) {
    	
    	// don't assign if slot is not in list and not null
    	if (s != null && !lslots.contains(s) && !nlslots.contains(s))
    		return;
    	
    	// for each assignment
    	for (Assignment a : assignments) {
    		
    		// skip if meeting doesn't match
    		if (a.getM() != m)
    			continue;
    		
    		a.getM().setAssignment(a);
    		
    		// otherwise set slot and return
    		a.setS(s);
    		return;
    	}
    }
    
    /**
     * Update the assignment for a given meeting
     * (do nothing if assignment not found)
     * 
     * @param m The meeting
     * @param s The slot to assign it to
     */
    public void updateAssignmentArr(Meeting m, Slot s) {
    	
    	// don't assign if slot is not in list and not null
    	if (s != null && !lslots.contains(s) && !nlslots.contains(s))
    		return;
    	
    	// for each assignment
    	for (int i = 0; i < assignArray.length; i++) {
    		
    		// skip if meeting doesn't match
    		if (assignArray[i].getM() != m)
    			continue;
    		
    		// otherwise set slot and return
    		assignArray[i].setS(s);
    		return;
    	}
    }

    /**
     * Clear the assignments list
     */
    public void clearAssignments() {
        assignments = null;
    }

    /**
     * Set the lecture slots list
     * 
     * @param lecslots LectureSlot list
     */
    public void setLectureSlots(ArrayList<LectureSlot> lecslots) {
        this.lslots = lecslots;
    }

    /**
     * Get the lecture slots list
     * 
     * @return LectureSlot list
     */
    public ArrayList<LectureSlot> getLectureSlots() {
        return this.lslots;
    }
    
    /**
     * Returns a lecture slot in the list that matches the day/time
     * 
     * @param day The day
     * @param hour The start hour
     * @param min The start minute
     * @return The LectureSlot
     */
    public LectureSlot findLectureSlot(String day, int hour, int min) {
    	LectureSlot result = null;
    	
    	// for each LectureSlot
    	for (LectureSlot ls : lslots) {
    		
    		// skip if doesn't match
    		if (!day.equals(ls.getDay()) || hour != ls.getHour() || min != ls.getMinute())
    			continue;
    		
    		result = ls;
    		break;
    	}
    	
    	return result;
    }

    /**
     * Set the nonlecture slots list
     * 
     * @param labslots NonLectureSlot list
     */
    public void setLabSlots(ArrayList<NonLectureSlot> labslots) {
        this.nlslots = labslots;
    }

    /**
     * Get the nonlecture slots list
     * 
     * @return NonLectureSlot list
     */
    public ArrayList<NonLectureSlot> getNonLectureSlots() {
        return this.nlslots;
    }
    
    /**
     * Returns a nonlecture slot in the list that matches the day/time
     * 
     * @param day The day
     * @param hour The start hour
     * @param min The start minute
     * @return The NonLectureSlot
     */
    public NonLectureSlot findNonLectureSlot(String day, int hour, int min) {
    	NonLectureSlot result = null;
    	
    	// for each LectureSlot
    	for (NonLectureSlot nls : nlslots) {
    		
    		// skip if doesn't match
    		if (!day.equals(nls.getDay()) || hour != nls.getHour() || min != nls.getMinute())
    			continue;
    		
    		result = nls;
    		break;
    	}
    	
    	return result;
    }

    /**
     * Set the courses list
     * 
     * @param cs Course list
     */
    public void setCourses(ArrayList<Course> cs) {
        this.courses = cs;
        processLectures(); // fill the lectures list using the courses list
    }

    /**
     * Get the courses list
     * 
     * @return Course list
     */
    public ArrayList<Course> getCourses() {
        return this.courses;
    }

    /**
     * Set the lectures list
     * 
     * @param lecs Lecture list
     */
    public void setLectures(ArrayList<Lecture> lecs) {
        this.lectures = lecs;
    }

    /**
     * Get the lectures list
     * 
     * @return Lecture list
     */
    public ArrayList<Lecture> getLectures() {
        return this.lectures;
    }

    /**
     * Get labs list
     * 
     * @return Lab list
     */
    public ArrayList<Lab> getLabs() {
        return labs;
    }

    /**
     * Get tutorials list
     * 
     * @return Tutorial list
     */
    public ArrayList<Tutorial> getTuts() {
        return tuts;
    }

    /**
     * Get nonlectures list
     * 
     * @return NonLecture list
     */
    public ArrayList<NonLecture> getNonLectures() {
        ArrayList<NonLecture> nonLectures = new ArrayList<>();
        this.labs.forEach((l) -> {
            nonLectures.add(l);
        });
        this.tuts.forEach((t) -> {
            nonLectures.add(t);
        });
        return nonLectures;
    }

    /**
     * Get the list of paired courses
     * 
     * @return List of MeetingPair
     */
    public ArrayList<MeetingPair> getPairs() {
    	return pairs;
    }
    
    /**
     * Add to the paired courses list
     * 
     * @param m1 First Meeting
     * @param m2 Second Meeting
     */
    public void addPair(Meeting m1, Meeting m2) {
    	pairs.add(new MeetingPair(m1, m2));
    }
    
    /**
     * Set paired courses list
     * 
     * @param pairs List of MeetingPair
     */
    public void setPairs(ArrayList<MeetingPair> pairs) {
    	this.pairs = pairs;
    }
    
    /**
     * Get the list of non-compatible courses
     * 
     * @return List of MeetingPair
     */
    public ArrayList<MeetingPair> getNoncompatible() {
    	return noncompatible;
    }
    
    /**
     * Add to the non-compatible courses list
     * 
     * @param m1 First Meeting
     * @param m2 Second Meeting
     */
    public void addNoncompatible(Meeting m1, Meeting m2) {
    	noncompatible.add(new MeetingPair(m1, m2));
    }
    
    /**
     * Set the noncompatible courses list
     * 
     * @param noncompatible List of MeetingPair
     */
    public void setNoncompatible(ArrayList<MeetingPair> noncompatible) {
    	this.noncompatible = noncompatible;
    }
    
    /**
	 * Sets the weight values for the different evaluations
	 * 
	 * @param cmin
	 * @param lmin
	 * @param pref
	 * @param pair
	 * @param secdiff
	 */
	public void setWeights(double min, double pref, double pair, double secdiff) {
		wMin = min;
		wPref = pref;
		wPair = pair;
		wSecDiff = secdiff;
	}
	
	/**
	 * Set weight for coursemin
	 * 
	 * @param weight Weight value
	 */
	public void setMinWeight(double weight) {
		wMin = weight;
	}	
	
	/**
	 * Set weight for preferences
	 * 
	 * @param weight Weight value
	 */
	public void setPrefWeight(double weight) {
		wPref = weight;
	}
	
	/**
	 * Set weight for pairs
	 * 
	 * @param weight Weight value
	 */
	public void setPairWeight(double weight) {
		wPair = weight;
	}
	
	/**
	 * Set weight for section difference
	 * 
	 * @param weight Weight value
	 */
	public void setSecDiffWeight(double weight) {
		wSecDiff = weight;
	}
	
	/**
	 * Get course min weight
	 * 
	 * @return Weight value
	 */
	public double getMinWeight() {
		return wMin;
	}
	
	/**
	 * Get preference weight
	 * 
	 * @return Weight value
	 */
	public double getPrefWeight() {
		return wPref;
	}
	
	/**
	 * Get pair weight
	 * 
	 * @return Weight value
	 */
	public double getPairWeight() {
		return wPair;
	}
	
	/**
	 * Get section difference weight
	 * 
	 * @return Weight value
	 */
	public double getSecDiffWeight() {
		return wSecDiff;
	}
	
	/**
	 * Sets the penalty values for the different evaluations
	 * 
	 * @param cmin pen_coursemin
	 * @param lmin pen_labmin
	 * @param pair pen_notpaired
	 * @param secdiff pen_section
	 */
	public void setPenalties(double cmin, double lmin, double pair, double secdiff) {
		pen_coursemin = cmin;
		pen_labmin = lmin;
		pen_notpaired = pair;
		pen_section = secdiff;
	}
	
	/**
	 * Set penalty for coursemin
	 * 
	 * @param p Penalty
	 */
	public void setCourseMinPenalty(double p) {
		pen_coursemin = p;
	}	
	
	/**
	 * Set penalty for labmin
	 * 
	 * @param p Penalty
	 */
	public void setLabMinPenalty(double p) {
		pen_labmin = p;
	}
	
	/**
	 * Set penalty for pairs
	 * 
	 * @param p Penalty
	 */
	public void setPairPenalty(double p) {
		pen_notpaired = p;
	}
	
	/**
	 * Set penalty for section difference
	 * 
	 * @param p Penalty
	 */
	public void setSecDiffPenalty(double p) {
		pen_section = p;
	}
	
	/**
	 * Get course min penalty
	 * 
	 * @return Penalty
	 */
	public double getCourseMinPenalty() {
		return pen_coursemin;
	}
	
	/**
	 * Get lab min penalty
	 * 
	 * @return Penalty
	 */
	public double getLabMinPenalty() {
		return pen_labmin;
	}
	
	/**
	 * Get pair penalty
	 * 
	 * @return Penalty
	 */
	public double getPairPenalty() {
		return pen_notpaired;
	}
	
	/**
	 * Get section difference penalty
	 * 
	 * @return Penalty
	 */
	public double getSecDiffPenalty() {
		return pen_section;
	}
}
