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
import Search.Constr;
import Search.Eval;

/**
 * Object representing a schedule of assigned meetings to slots
 *
 * @author
 *
 */
public class Schedule {

    private ArrayList<Assignment> assignments;	// list of assignments
    private ArrayList<LectureSlot> lslots;		// list of lecture slots
    private ArrayList<NonLectureSlot> nlslots;	// list of nonlecture slots
    private ArrayList<Course> courses;		// list of courses
    private ArrayList<Lecture> lectures;	// list of lectures (filled from courses)
    private ArrayList<Lab> labs;			// list of labs (filled from courses)
    private ArrayList<Tutorial> tuts;		// list of tutorials (filled from courses)
    private ArrayList<MeetingPair> pairs;			// 
    private ArrayList<MeetingPair> noncompatible;	// 

    /**
     * Default constructor
     */
    public Schedule() {
        assignments = new ArrayList<>();
        lslots = new ArrayList<>();
        nlslots = new ArrayList<>();
        courses = new ArrayList<>();
        lectures = new ArrayList<>();
        labs = new ArrayList<>();
        tuts = new ArrayList<>();
        pairs = new ArrayList<>();
        noncompatible = new ArrayList<>();
    }

    /**
     * Constructor
     *
     * @param lslots
     * @param nlslots
     * @param courses
     */
    public Schedule(ArrayList<LectureSlot> lslots,
            ArrayList<NonLectureSlot> nlslots,
            ArrayList<Course> courses) {

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
    }

    /**
     * Constructor for Constr/Eval
     *
     * @param a
     * @param orig
     */
    public Schedule(Assignment a, Schedule orig) {
    	
    	// keep original lists
        lslots = orig.getLectureSlots();
        nlslots = orig.getNonLectureSlots();
        courses = orig.getCourses();
        lectures = orig.getLectures();
        labs = orig.getLabs();
        tuts = orig.getTuts();
        pairs = orig.getPairs();
        noncompatible = orig.getNoncompatible();
        
        // make a copy of assignments list and add the new one
        assignments = (ArrayList<Assignment>) orig.getAssignments().clone();
        assignments.add(a);
    }

    /**
     * Fills the lectures list using the courses list
     */
    private void processLectures() {
        // for each course
        this.courses.forEach((c) -> {
            c.getCourseLectures().forEach((l) -> {
                this.lectures.add(l);
            });
        });
    }

    /**
     * Fills the labs list
     */
    private void processLabs() {
        this.courses.forEach((c) -> {
            //System.out.println("course: " + c.getDepartment() + " " + c.getNumber());
            if (c.getCourseLabs() != null) {
                //System.out.println("labs: " + c.getCourseLabs().size());
                c.getCourseLabs().forEach((l) -> {
                    //System.out.println("lab: " + l.toString());
                    this.labs.add(l);
                });
            } else {
                //System.out.println("labs: 0");
            }
        });
    }

    /**
     * Fills the tutorials list
     */
    private void processTuts() {
        this.courses.forEach((c) -> {
            //System.out.println("course "+ c.getDepartment() + " " + c.getNumber());
            if (c.getCourseTuts() != null) {
                //System.out.println("tutorials: "+c.getCourseTuts().size());
                c.getCourseTuts().forEach((t) -> {
                    this.tuts.add(t);
                });
            }else{
                //System.out.println("tutorials: 0");
            }
        });
    }

    /**
     * Generates the set of assignments for all of our meetings
     */
    private void generateAssignments() {
        this.lectures.forEach((l) -> {
            assignments.add(new Assignment(l, null));
        });
        this.labs.forEach((l) -> {
            assignments.add(new Assignment(l, null));
        });
        this.tuts.forEach((t) -> {
            assignments.add(new Assignment(t, null));
        });
    }

    /**
     * Checks if the schedule meets all hard constraints
     *
     * @return True if all hard constraints are met
     */
    public boolean isValid() {
        Constr c = new Constr(this);
        return c.check();
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
        Constr c = new Constr(a, this);
        return c.check();
    }

    /**
     * Get the evaluation without weights
     *
     * @return The evaluation of the schedule
     */
    public int eval() {
        Eval e = new Eval(this);
        return e.getEval();
    }

    /**
     * Get the evaluation with weights
     *
     * @param w1
     * @param w2
     * @param w3
     * @param w4
     * @return The evaluation of the schedule
     */
    public int eval(double w1, double w2, double w3, double w4) {
        Eval e = new Eval(this, w1, w2, w3, w4);
        return e.getEval();
    }

    /**
     * Get the evaluation with added assignment without weights
     *
     * @param a The assignment
     * @return The evaluation of the schedule with the assignment
     */
    public int evalWith(Assignment a) {
        Eval e = new Eval(a, this);
        return e.getEval();
    }

    /**
     * Get the evaluation with added assignment and weights
     *
     * @param a The assignment
     * @param w1
     * @param w2
     * @param w3
     * @param w4
     * @return The evaluation of the schedule with the assignment
     */
    public int evalWith(Assignment a, double w1, double w2, double w3, double w4) {
        Eval e = new Eval(a, this, w1, w2, w3, w4);
        return e.getEval();
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
     * Add an assignment to the list
     * 
     * @param a The assignment
     */
    public void addAssignment(Assignment a) {
        assignments.add(a);
    }
    
    /**
     * Update the assignment for a given meeting
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
    		
    		// otherwise set slot and return
    		a.setS(s);
    		return;
    	}
    	
    	// if we got here meeting was not found, so add it
    	assignments.add(new Assignment(m, s));
    }

    /**
     * Clear the assignments list
     */
    public void clearAssignments() {
        assignments.clear();
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
}
