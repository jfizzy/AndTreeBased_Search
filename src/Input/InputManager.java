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
package Input;

import Schedule.Course;
import Schedule.Lab;
import Schedule.LectureSlot;
import Schedule.Meeting;
import Schedule.NonLecture;
import Schedule.NonLectureSlot;
import Schedule.Section;
import Schedule.TimeTable;
import Schedule.Tutorial;
import Search.SearchData;

import java.util.ArrayList;

/**
 *
 * @author
 */
public class InputManager {

    private FileExaminer fe;
    private InputWrapper iw;
    private String deptName;

    /**
     * Constructor
     */
    public InputManager() {
        fe = null;
        iw = null;
    }

    /**
     * SearchData - takes a file path as input and will parse the file at said
     * in order to extract the valid lines from it, store the valid lines by 
     * type in an InputWrapper, then begin parsing the data and generating the 
     * overall structural elements of a 'TimeTable' object for the search system
     * 
     * @param fp
     * @return search data type
     */
    public SearchData run(String fp) {

        iw = new InputWrapper();
        fe = new FileExaminer(fp, iw);
        fe.init();
        fe.parse();
        SearchData sd = new SearchData();
        sd.setLectureSlots(generateLectureSlots());
        sd.setLabSlots(generateNonLectureSlots());
        sd.setLectures(generateSections());
        sd.setNonLectures(generateNonLectures(sd.getLectures()));
        sd.setTimetable(generateTimeTable());
        return sd;
    }

    private void generateTimeTableObjects() {
        ArrayList<LectureSlot> ls = generateLectureSlots();
        ArrayList<NonLectureSlot> nls = generateNonLectureSlots();
        TimeTable tt = generateTimeTable();
    }

    /**
     * generateLectureSlots - generates the lecture slot objects that are 
     * specified by the input file lines and returns them as an arraylist of 
     * 'LectureSlot's
     * 
     * @return results
     */
    private ArrayList<LectureSlot> generateLectureSlots() {
        ArrayList<LectureSlot> results = new ArrayList<>();
        iw.lectureSlotLines.stream().map((line) -> line.split("\\s*,\\s*")).map((parts) -> {
            for (String p : parts) {
                p = p.trim(); // last check for whitespaces
            }
            return parts;
        }).forEachOrdered((parts) -> {
            String day = parts[0];
            String[] times = parts[1].split(":");
            int hour = Integer.parseInt(times[0]);
            int minute = Integer.parseInt(times[1]);
            int coursemax = Integer.parseInt(parts[2]);
            int coursemin = Integer.parseInt(parts[3]);
            results.add(new LectureSlot(day, hour, minute, coursemax, coursemin));
            System.out.println("[Added Lecture Slot - " + day + " " + hour + ":" + minute + " " + coursemax + " " + coursemin + "]");
        });
        return results;
    }

    /**
     * generateNonLectureSlots - generates the NonLecture slots specified by 
     * the input file lines, indifferent of whether they are Labs or Tutorials,
     * and returns them as an arraylist of type 'NonLectureSlot'
     * @return results
     */
    private ArrayList<NonLectureSlot> generateNonLectureSlots() {
        ArrayList<NonLectureSlot> results = new ArrayList<>();
        iw.nonlectureSlotLines.stream().map((line) -> line.split("\\s*,\\s*")).map((parts) -> {
            for (String p : parts) {
                p = p.trim(); // last check for whitespaces
            }
            return parts;
        }).forEachOrdered((parts) -> {
            String day = parts[0];
            String[] times = parts[1].split(":");
            int hour = Integer.parseInt(times[0]);
            int minute = Integer.parseInt(times[1]);
            int labmax = Integer.parseInt(parts[2]);
            int labmin = Integer.parseInt(parts[3]);
            results.add(new NonLectureSlot(day, hour, minute, labmax, labmin));
            System.out.println("[Added Non Lecture Slot - " + day + " " + hour + ":" + minute + " " + labmax + " " + labmin + "]");
        });
        return results;
    }

    /**
     * generateTimeTable - function that generates the components required for 
     * a search instance and to simulate the model.
     * 
     * INCOMPLETE
     * 
     * @return null
     */
    private TimeTable generateTimeTable() {
        //generate list of courses
        ArrayList<Course> courses = generateSections();
        generateNonLectures(courses);
        generateIncompatibilities(courses);
        return null;
    }
    
    /**
     * generateSections - takes the lecture specifying lines of the input file 
     * and aims to create all of the sections that were listed by the input file
     * 
     * @return courses
     */
    private ArrayList<Course> generateSections() {
        ArrayList<Course> courses = new ArrayList<>();
        for (String line : iw.lectureLines) {
            String[] parts = line.split("\\s+"); // split on whitespace
            String dept = parts[0];
            String courseNum = parts[1];
            // index two is of no use to us
            String section = parts[3];
            boolean added = false;
            for (Course c : courses) {
                if (dept.equals(c.getDepartment())) { // same dept
                    if (courseNum.equals(c.getNumber())) { // same num
                        for (Section s : c.getSections()) {
                            if (section.equals(s.getSectionNum())) {
                                // we have two Lectures for same section
                                // this means input file is invalid
                                return null;
                            }
                        }
                        c.addSection(new Section(c, section)); // add the new section
                        System.out.println("[Added Course Section - " + dept + " " + courseNum + " LEC " + section + "]");
                        added = true;
                        break;
                    }
                    courses.add(new Course(c.getDepartment(), courseNum, section));
                    System.out.println("[Added Course - " + dept + " " + courseNum + " LEC " + section + "]");
                    added = true;
                    break;
                }
            }
            if (!added) {
                courses.add(new Course(dept, courseNum, section));
                System.out.println("[Added Course and Dept - " + dept + " " + courseNum + " LEC " + section + "]");
            }
        }
        return courses;
    }

    /**
     * generateNonLectures - generate the NonLecture components of course 
     * sections that are specified by the input file, takes in the list of 
     * courses with which to place each NonLecture
     * 
     * @param courses 
     */
    private ArrayList<NonLecture> generateNonLectures(ArrayList<Course> courses) {
    	ArrayList<NonLecture> result = new ArrayList<NonLecture>();
        iw.nonlectureLines.stream().map((line) -> line.split("\\s+")).forEachOrdered((parts) -> {
            // split on whitespace
            String dept = parts[0];
            String courseNum = parts[1];
            String section = null;
            if ("LEC".equals(parts[2])) {
                section = parts[3];
            }
            String nlType, nlNum;
            if (section == null) {
                nlType = parts[2];
                nlNum = parts[3];
            } else {
                nlType = parts[4];
                nlNum = parts[5];
            }

            NonLecture nl = generateNonLecture(dept, courseNum, section, nlType, nlNum, courses);
            if (nl != null) {
            	result.add(nl);
                if ("TUT".equals(nlType)) // check for type of NonLecture
                {
                    System.out.println("[Added NonLecture - " + nl.getDept() + " " + nl.getCourseNum() + " LEC " + nl.getSectionNum() + " TUT " + ((Tutorial) nl).getTutNum() + "]");
                } else {
                    System.out.println("[Added NonLecture - " + nl.getDept() + " " + nl.getCourseNum() + " LEC " + nl.getSectionNum() + " LAB " + ((Lab) nl).getLabNum() + "]");
                }
            }
        });
        return result;
    }

    /**
     * generateNonLecture - refactored component of the generateNonLectures 
     * logic, Takes a single 'NonLecture' and places it in its correct location
     * as long as the parent course exists
     * @param dept
     * @param courseNum
     * @param section
     * @param nlType
     * @param nlNum
     * @param courses
     * @return the added NonLecture
     */
    private NonLecture generateNonLecture(String dept, String courseNum, String section, String nlType, String nlNum, ArrayList<Course> courses) {
        for (Course course : courses) {
            if (course.getDepartment().equals(dept) && course.getNumber().equals(courseNum)) {
                // same dept and course number
                Section s = null;
                if (section == null) {
                    // look at the first (and hopefully only) section TODO: make sure this is correct
                    if (course.getSections() != null) {
                        s = course.getSections().get(0);
                    } else {
                        return null; // section does not exist
                    }
                } else {
                    for (Section sec : course.getSections()) {
                        if (sec.getSectionNum().equals(section)) {
                            s = sec;
                            break;
                        }
                    }
                    if (s == null) {
                        return null; // section does not exist
                    }
                }
                if ("TUT".equals(nlType)) {
                    if (s.getTuts().isEmpty()) {
                        Tutorial tut = new Tutorial(nlNum, s, false);
                        s.addTutorial(tut);
                        return tut; // TODO need to revisit setting of evening
                    } else {
                        for (Tutorial t : s.getTuts()) {
                            if (t.getTutNum().equals(nlNum)) {
                                //this is a duplicate declaration of a tutorial
                                return null;
                            } else {
                                Tutorial tut = new Tutorial(nlNum, s, false);
                                s.addTutorial(tut);
                                return tut; // TODO need to revisit setting of evening
                            }
                        }
                    }
                } else {
                    if (s.getLabs().isEmpty()) {
                        Lab lab = new Lab(nlNum, s, false);
                        s.addLab(lab);
                        return lab; // TODO need to revisit setting of evening
                    } else {
                        for (Lab l : s.getLabs()) {
                            if (l.getLabNum().equals(nlNum)) {
                                //this is a duplicate declaration of a lab
                                return null;
                            } else {
                                Lab lab = new Lab(nlNum, s, false);
                                s.addLab(lab);
                                return lab; // TODO need to revisit setting of evening
                            }
                        }
                    }
                }

            }
        }
        return null; // could not match a course with same dept or number, so ignore
    }

    /**
     * generateIncompatibilities - generates all of the Not compatible lines 
     * specified in the input file; does so by creating a link between 'Meeting'
     * objects to their list of incompatible counterparts
     * incompatibility is symmetric
     * 
     * @param courses 
     */
    private void generateIncompatibilities(ArrayList<Course> courses) {
        iw.notCompatibleLines.stream().map((line) -> line.split("\\s*,\\s*")).forEachOrdered((halves) -> {
            String left = halves[0];
            String right = halves[1];
            String[] lParts = left.split("\\s+");
            String[] rParts = right.split("\\s+");
            // left parsing
            String lDept = lParts[0];
            String lCNum = lParts[1];
            String lSec = null, lNType = null, lNNum = null;
            if ("LEC".equals(lParts[2])) { // lecture specified
                lSec = lParts[3];
                if (lParts.length == 6) {
                    lNType = lParts[4];
                    lNNum = lParts[5];
                }
            } else { // lecture implied (maybe)
                //TODO: need to get feedback from TA about ignoring the following two
                lNType = lParts[2];
                lNNum = lParts[3];
            }
            // right parsing
            String rDept = rParts[0];
            String rCNum = rParts[1];
            String rSec = null, rNType = null, rNNum = null;
            if ("LEC".equals(rParts[2])) {
                rSec = rParts[3];
                if (rParts.length == 6) {
                    rNType = rParts[4];
                    rNNum = rParts[5];
                }
            } else {
                rNType = rParts[2];
                rNNum = rParts[3];
            }
            Meeting l, r;
            l = findMeeting(courses, lDept, lCNum, lSec, lNType, lNNum);
            r = findMeeting(courses, rDept, rCNum, rSec, rNType, rNNum);
            if (l != null && r != null) { //both are found
                l.addIncompatibility(r); // give them 
                r.addIncompatibility(l); // the same incompatibility
                System.out.println("[Not compatible - " + lDept + " " + lCNum + "... =/= " + rDept + " " + rCNum + "..." + "]");
            } else {
                System.out.println("Could not find specified meetings for incompatibility");
            }
        });

    }

    /**
     * findMeeting - given inputs that can very generally specify any type of 
     * 'Meeting' subclass, whether it be a 'Lecture', 'Tutorial', or 'Lab',
     * attempts to return the specific object it resides in for use of the 
     * caller
     * 
     * @param courses
     * @param dept
     * @param cNum
     * @param sec
     * @param type
     * @param nlNum
     * @return 
     */
    private Meeting findMeeting(ArrayList<Course> courses, String dept, String cNum, String sec, String type, String nlNum) {
        for (Course c : courses) {
            if (c.getDepartment().equals(dept) && c.getNumber().equals(cNum)) {
                if (sec == null) { // section not specified, assuming first index
                    if (type == null) {
                        // Lecture was specified
                        //TODO need to ask about this case
                    } else {
                        // NonLecture specified
                        if ("TUT".equals(type)) {
                            for (Tutorial tut : c.getSections().get(0).getTuts()) {
                                if (nlNum.equals(tut.getTutNum())) {
                                    return tut; // found the tutorial
                                }
                            }
                        } else if ("LAB".equals(type)) {
                            for (Lab lab : c.getSections().get(0).getLabs()) {
                                if (nlNum.equals(lab.getLabNum())) {
                                    return lab; // found the lab
                                }
                            }
                        }
                    }
                } else { // section specified
                    if (type == null) {
                        // Lecture was specified
                        for (Section s : c.getSections()) {
                            if (sec.equals(s.getSectionNum())) {
                                return s.getLecture(); // found the lecture
                            }
                        }
                    } else {
                        // NonLecture specified
                        if ("TUT".equals(type)) {
                            // Left is a Tutorial
                            for (Section s : c.getSections()) {
                                if (sec.equals(s.getSectionNum())) {
                                    for (Tutorial tut : s.getTuts()) {
                                        if (nlNum.equals(tut.getTutNum())) {
                                            return tut; // found the tutorial
                                        }
                                    }
                                }
                            }
                        } else if ("LAB".equals(type)) {
                            // Left is a Lab
                            for (Section s : c.getSections()) {
                                if (sec.equals(s.getSectionNum())) {
                                    for (Lab lab : s.getLabs()) {
                                        if (nlNum.equals(lab.getLabNum())) {
                                            return lab; // found the lab
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null; // couldnt find a match
    }

}
