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

import Schedule.Assignment;
import Schedule.Course;
import Schedule.Lab;
import Schedule.Lecture;
import Schedule.LectureSlot;
import Schedule.Meeting;
import Schedule.NonLecture;
import Schedule.NonLectureSlot;
import Schedule.Schedule;
import Schedule.Section;
import Schedule.Slot;
import Schedule.Tutorial;
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
     * ScheduleManager - takes a file path as input and will parse the file at
     * said in order to extract the valid lines from it, store the valid lines
     * by nlType in an InputWrapper, then begin parsing the data and generating
     * the overall structural elements of a 'TimeTable' object for the search
     * system
     *
     * @param fp
     * @return search data nlType
     */
    public Schedule run(String fp) {

        iw = new InputWrapper();
        fe = new FileExaminer(fp, iw);
        fe.init();
        fe.parse();

        // Just added this to test new input line functions
        generateTimeTable();
        // comment out to run other code

        Schedule s = new Schedule();
        s.setLectureSlots(activateLectureSlots());
        s.setLabSlots(activateNonLectureSlots());
        s.setCourses(generateSections());
        s.setNonLectures(generateNonLectures(generateSections()));
        return s;
    }

    // this is never used
    private void generateTimeTableObjects() {
        ArrayList<LectureSlot> ls = activateLectureSlots();
        ArrayList<NonLectureSlot> nls = activateNonLectureSlots();
        Schedule s = generateTimeTable();
    }

    private ArrayList<LectureSlot> generateGenericLectureSlots() {
        ArrayList<LectureSlot> slots = new ArrayList<>();
        // only add mondays as per uni constraint
        slots.add(new LectureSlot("MO", 8, 0, 9, 0, 0, 0, false));
        slots.add(new LectureSlot("MO", 9, 0, 10, 0, 0, 0, false));
        slots.add(new LectureSlot("MO", 10, 0, 11, 0, 0, 0, false));
        slots.add(new LectureSlot("MO", 11, 0, 12, 0, 0, 0, false));
        slots.add(new LectureSlot("MO", 12, 0, 13, 0, 0, 0, false));
        slots.add(new LectureSlot("MO", 13, 0, 14, 0, 0, 0, false));
        slots.add(new LectureSlot("MO", 14, 0, 15, 0, 0, 0, false));
        slots.add(new LectureSlot("MO", 15, 0, 16, 0, 0, 0, false));
        slots.add(new LectureSlot("MO", 16, 0, 17, 0, 0, 0, false));
        slots.add(new LectureSlot("MO", 17, 0, 18, 0, 0, 0, false));
        // evening MO/WE/FR slots
        slots.add(new LectureSlot("MO", 18, 0, 19, 0, 0, 0, true));
        slots.add(new LectureSlot("MO", 19, 0, 20, 0, 0, 0, true));
        slots.add(new LectureSlot("MO", 20, 0, 21, 0, 0, 0, true));
        // only add tuesdays as per uni constraint
        slots.add(new LectureSlot("TU", 8, 0, 9, 30, 0, 0, false));
        slots.add(new LectureSlot("TU", 9, 30, 11, 0, 0, 0, false));
        slots.add(new LectureSlot("TU", 11, 0, 12, 30, 0, 0, false));
        slots.add(new LectureSlot("TU", 12, 30, 14, 0, 0, 0, false));
        slots.add(new LectureSlot("TU", 14, 0, 15, 30, 0, 0, false));
        slots.add(new LectureSlot("TU", 15, 30, 17, 0, 0, 0, false));
        slots.add(new LectureSlot("TU", 17, 0, 18, 30, 0, 0, false));
        // evening TU/TR slots
        slots.add(new LectureSlot("TU", 18, 30, 20, 0, 0, 0, true));

        return slots;
    }

    /**
     * activateLectureSlots - generates the lecture slot objects that are
     * specified by the input file lines and returns them as an arraylist of
     * 'LectureSlot's
     *
     * @return slots
     */
    private ArrayList<LectureSlot> activateLectureSlots() {
        ArrayList<LectureSlot> slots = generateGenericLectureSlots();

        // traverse input to activate
        iw.lectureSlotLines.stream().map((line) -> line.split("\\s*,\\s*")).map((parts) -> {
            for (String p : parts) {
                p = p.trim(); // last check for whitespaces
            }
            return parts;
        }).forEachOrdered((parts) -> {
            String slotString = parts[0] + ", " + parts[1];

            LectureSlot s = findLectureSlot(slots, slotString);
            if (s != null) {
                int coursemax = Integer.parseInt(parts[2]);
                int coursemin = Integer.parseInt(parts[3]);
                s.activate(coursemax, coursemin); // activate the slot
                System.out.println("[Activated Lecture Slot - " + s.getDay() + " " + s.printHour() + ":" + s.printMinute() + " " + coursemax + " " + coursemin + "]");
            } else {
                System.out.println("Could not find the lecture slot");
            }
        });
        return slots;
    }

    private ArrayList<NonLectureSlot> generateGenericNonLectureSlots() {
        ArrayList<NonLectureSlot> slots = new ArrayList<>();

        // MO,WE slots
        slots.add(new NonLectureSlot("MO", 8, 0, 9, 0, 0, 0, false));
        slots.add(new NonLectureSlot("MO", 9, 0, 10, 0, 0, 0, false));
        slots.add(new NonLectureSlot("MO", 10, 0, 11, 0, 0, 0, false));
        slots.add(new NonLectureSlot("MO", 11, 0, 12, 0, 0, 0, false));
        slots.add(new NonLectureSlot("MO", 12, 0, 13, 0, 0, 0, false));
        slots.add(new NonLectureSlot("MO", 13, 0, 14, 0, 0, 0, false));
        slots.add(new NonLectureSlot("MO", 14, 0, 15, 0, 0, 0, false));
        slots.add(new NonLectureSlot("MO", 15, 0, 16, 0, 0, 0, false));
        slots.add(new NonLectureSlot("MO", 16, 0, 17, 0, 0, 0, false));
        slots.add(new NonLectureSlot("MO", 17, 0, 18, 0, 0, 0, false));
        slots.add(new NonLectureSlot("MO", 18, 0, 19, 0, 0, 0, true));
        slots.add(new NonLectureSlot("MO", 19, 0, 20, 0, 0, 0, true));
        slots.add(new NonLectureSlot("MO", 20, 0, 21, 0, 0, 0, true));
        // TU,TR slots
        slots.add(new NonLectureSlot("TU", 8, 0, 9, 0, 0, 0, false));
        slots.add(new NonLectureSlot("TU", 9, 0, 10, 0, 0, 0, false));
        slots.add(new NonLectureSlot("TU", 10, 0, 11, 0, 0, 0, false));
        slots.add(new NonLectureSlot("TU", 11, 0, 12, 0, 0, 0, false));
        slots.add(new NonLectureSlot("TU", 12, 0, 13, 0, 0, 0, false));
        slots.add(new NonLectureSlot("TU", 13, 0, 14, 0, 0, 0, false));
        slots.add(new NonLectureSlot("TU", 14, 0, 15, 0, 0, 0, false));
        slots.add(new NonLectureSlot("TU", 15, 0, 16, 0, 0, 0, false));
        slots.add(new NonLectureSlot("TU", 16, 0, 17, 0, 0, 0, false));
        slots.add(new NonLectureSlot("TU", 17, 0, 18, 0, 0, 0, false));
        slots.add(new NonLectureSlot("TU", 18, 0, 19, 0, 0, 0, true));
        slots.add(new NonLectureSlot("TU", 19, 0, 20, 0, 0, 0, true));
        slots.add(new NonLectureSlot("TU", 20, 0, 21, 0, 0, 0, true));
        // FR slots
        slots.add(new NonLectureSlot("FR", 8, 0, 10, 0, 0, 0, false));
        slots.add(new NonLectureSlot("FR", 10, 0, 12, 0, 0, 0, false));
        slots.add(new NonLectureSlot("FR", 12, 0, 14, 0, 0, 0, false));
        slots.add(new NonLectureSlot("FR", 14, 0, 16, 0, 0, 0, false));
        slots.add(new NonLectureSlot("FR", 16, 0, 18, 0, 0, 0, false));
        slots.add(new NonLectureSlot("FR", 18, 0, 20, 0, 0, 0, true));

        return slots;
    }

    /**
     * activateNonLectureSlots - generates the NonLecture slots specified by the
     * input file lines, indifferent of whether they are Labs or Tutorials, and
     * returns them as an arraylist of nlType 'NonLectureSlot'
     *
     * @return slots
     */
    private ArrayList<NonLectureSlot> activateNonLectureSlots() {
        ArrayList<NonLectureSlot> slots = generateGenericNonLectureSlots();
        iw.nonlectureSlotLines.stream().map((line) -> line.split("\\s*,\\s*")).map((parts) -> {
            for (String p : parts) {
                p = p.trim(); // last check for whitespaces
            }
            return parts;
        }).forEachOrdered((parts) -> {
            String slotString = parts[0] + ", " + parts[1];

            NonLectureSlot s = findNonLectureSlot(slots, slotString);
            if (s != null) {
                int labmax = Integer.parseInt(parts[2]);
                int labmin = Integer.parseInt(parts[3]);
                s.activate(labmax, labmin);
                System.out.println("[Activated NonLecture Slot - " + s.getDay() + " " + s.printHour() + ":" + s.printMinute() + " " + labmax + " " + labmin + "]");
            }
        });
        return slots;
    }

    /**
     * generateTimeTable - function that generates the components required for a
     * search instance and to simulate the model.
     *
     * INCOMPLETE
     *
     * @return null
     */
    private Schedule generateTimeTable() {
        //generate list of courses
        System.out.println("--------------------");
        System.out.println("TESTING");
        System.out.println("--------------------");
        ArrayList<LectureSlot> lecSlots = activateLectureSlots();
        ArrayList<NonLectureSlot> nonlecSlots = activateNonLectureSlots();
        ArrayList<Course> courses = generateSections();
        generateNonLectures(courses);
        generateIncompatibilities(courses);
        generateUnwanted(courses, lecSlots, nonlecSlots);

        ArrayList<Meeting> meetings = new ArrayList<>();
        courses.forEach((c) -> {
            c.getSections().forEach((s) -> {
                meetings.add(s.getLecture());
            });
        });
        courses.forEach((c) -> {
            c.getSections().forEach((s) -> {
                s.getLabs().forEach((l) -> {
                    meetings.add(l);
                });
            });
        });
        courses.forEach((c) -> {
            c.getSections().forEach((s) -> {
                s.getTuts().forEach((t) -> {
                    meetings.add(t);
                });
            });
        });
        Schedule tt = new Schedule();
        tt.printAssignments();
        
        generatePreferences(tt, courses, lecSlots, nonlecSlots);
        // have to create the timetable here
        System.out.println("--------------------");
        System.out.println("DONE");
        System.out.println("--------------------");
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
        ArrayList<NonLecture> result = new ArrayList<>();
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
                if ("TUT".equals(nlType)) // check for nlType of NonLecture
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
     *
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
                    // open to all sections
                    if (course.getSections() != null) {
                        // check if first section has an the open nonlecture already
                        s = course.getSections().get(0);
                        if ("TUT".equals(nlType)) {
                            for (Tutorial t : s.getTuts()) {
                                if (t.getTutNum().equals(nlNum) && t.getSectionNum() == null) {
                                    System.out.println("This is a duplicate open tutorial declaration");
                                    return null;
                                }
                            }
                            Tutorial tut = new Tutorial(nlNum, null, false); // null means any section
                            tut.setParentCourse(course);
                            // add tutorial to all sections in course
                            course.getSections().forEach((cs) -> {
                                cs.addTutorial(tut);
                            });
                        } else { // LAB
                            for (Lab l : s.getLabs()) {
                                if (l.getLabNum().equals(nlNum) && l.getSectionNum() == null) {
                                    System.out.println("This is a duplicate open lab declaration");
                                    return null;
                                }
                            }
                            Lab lab = new Lab(nlNum, null, false);
                            lab.setParentCourse(course);
                            // add lab to all sections in course
                            course.getSections().forEach((cs) -> {
                                cs.addLab(lab);
                            });
                        }

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
     * objects to their list of incompatible counterparts incompatibility is
     * symmetric
     *
     * @param courses
     */
    private void generateIncompatibilities(ArrayList<Course> courses) {
        iw.notCompatibleLines.stream().map((line) -> line.split("\\s*,\\s*")).forEachOrdered((halves) -> {
            String left = halves[0];
            String right = halves[1];

            Meeting l, r;
            // find the meetings
            l = findMeeting(courses, left);
            r = findMeeting(courses, right);

            if (l != null && r != null) { // if both are found
                l.addIncompatibility(r); // give them 
                r.addIncompatibility(l); // the same incompatibility
                System.out.println("[Not compatible - " + l.toString() + " =/= " + r.toString() + "]");
            } else {
                System.out.println("Could not find specified meetings for incompatibility");
            }
        });

    }

    /**
     * findMeeting - given an input String that can very generally specify any
     * nlType of 'Meeting' subclass, whether it be a 'Lecture', 'Tutorial', or
     * 'Lab', attempts to return the specific object it resides in for use of
     * the caller
     *
     * @param courses
     * @param dept
     * @param cNum
     * @param sec
     * @param type
     * @param nlNum
     * @return
     */
    private Meeting findMeeting(ArrayList<Course> courses, String meetingString) {

        String mParts[] = meetingString.split("\\s+");

        String dept = mParts[0];
        String cNum = mParts[1];
        String sec = null, nlType = null, nlNum = null;
        if ("LEC".equals(mParts[2])) { // lecture specified
            // looks like 'CPSC 433 LEC ...'
            sec = mParts[3];
            if (mParts.length == 6) {
                // looks like 'CPSC 433 LEC 01 TUT 01'
                nlType = mParts[4];
                nlNum = mParts[5];
            }
        } else { // lecture implied (maybe)
            // looks like 'CPSC 433' OR 'CPSC 433 TUT 01'

            if (mParts.length == 2) {
                // odd case
                // looks like 'CPSC 433'
                //TODO: need to get feedback from TA about ignoring the following two
            } else {
                nlType = mParts[2];
                nlNum = mParts[3];
            }
        }
        for (Course c : courses) {
            if (c.getDepartment().equals(dept) && c.getNumber().equals(cNum)) {
                if (sec == null) { // section not specified, assuming first index
                    if (nlType == null) {
                        // odd case
                        // Lecture was specified
                        //TODO need to ask about this case
                    } else {
                        // NonLecture specified
                        if ("TUT".equals(nlType)) {
                            for (Tutorial tut : c.getSections().get(0).getTuts()) {
                                if (tut.getTutNum().equals(nlNum)) {
                                    return tut; // found the tutorial
                                }
                            }
                        } else if ("LAB".equals(nlType)) {
                            for (Lab lab : c.getSections().get(0).getLabs()) {
                                if (lab.getLabNum().equals(nlNum)) {
                                    return lab; // found the lab
                                }
                            }
                        }
                    }
                } else { // section specified
                    if (nlType == null) {
                        // Lecture was specified
                        for (Section s : c.getSections()) {
                            if (sec.equals(s.getSectionNum())) {
                                return s.getLecture(); // found the lecture
                            }
                        }
                    } else {
                        // NonLecture specified
                        if ("TUT".equals(nlType)) {
                            // Left is a Tutorial
                            for (Section s : c.getSections()) {
                                if (sec.equals(s.getSectionNum())) {
                                    for (Tutorial tut : s.getTuts()) {
                                        if (tut.getTutNum().equals(nlNum)) {
                                            return tut; // found the tutorial
                                        }
                                    }
                                }
                            }
                        } else if ("LAB".equals(nlType)) {
                            // Left is a Lab
                            for (Section s : c.getSections()) {
                                if (sec.equals(s.getSectionNum())) {
                                    for (Lab lab : s.getLabs()) {
                                        if (lab.getLabNum().equals(nlNum)) {
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

    private LectureSlot findLectureSlot(ArrayList<LectureSlot> slots, String slotString) {
        String[] parts = slotString.split("\\s*,\\s*");
        String day = parts[0];
        if (!(day.equalsIgnoreCase("MO") || day.equalsIgnoreCase("TU"))) {
            System.out.println("found an invalid lecture day specifier");
            return null;
        }
        String[] timeParts = parts[1].split(":");
        String hourS = timeParts[0];
        String minS = timeParts[1];
        try {
            int hr = Integer.parseInt(hourS);
            int min = Integer.parseInt(minS);
            for (LectureSlot s : slots) {
                if (s.getDay().equals(day)) { // same day
                    if (s.getHour() == hr) {
                        if (s.getMinute() == min) {
                            return s;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("problem with integer parsing");
            return null;
        }
        System.out.println("Could not find a matching LectureSlot");
        return null;
    }

    private NonLectureSlot findNonLectureSlot(ArrayList<NonLectureSlot> slots, String slotString) {
        String[] parts = slotString.split("\\s*,\\s*");
        String day = parts[0];
        if (!(day.equalsIgnoreCase("MO") || day.equalsIgnoreCase("TU") || day.equalsIgnoreCase("FR"))) {
            System.out.println("found an invalid lecture day specifier");
            return null;
        }
        String[] timeParts = parts[1].split(":");
        String hourS = timeParts[0];
        String minS = timeParts[1];
        try {
            int hr = Integer.parseInt(hourS);
            int min = Integer.parseInt(minS);
            for (NonLectureSlot s : slots) {
                if (s.getDay().equals(day)) { // same day
                    if (s.getHour() == hr) {
                        if (s.getMinute() == min) {
                            return s;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("problem with integer parsing");
            return null;
        }
        System.out.println("Could not find a matching NonLectureSlot");
        return null;
    }

    private void generateUnwanted(ArrayList<Course> courses, ArrayList<LectureSlot> lSlots, ArrayList<NonLectureSlot> nlSlots) {
        iw.unwantedLines.stream().map((line) -> line.split("\\s*,\\s*")).forEachOrdered((parts) -> {
            Meeting m = findMeeting(courses, parts[0]);
            Slot s = null;
            if (m instanceof Lecture) {
                LectureSlot l = findLectureSlot(lSlots, (parts[1] + ", " + parts[2]));
                if (l.isActive()) {
                    s = l;
                }
            } else if (m instanceof NonLecture) {
                NonLectureSlot nl = findNonLectureSlot(nlSlots, (parts[1] + ", " + parts[2]));
                if (nl.isActive()) {
                    s = nl;
                }
            } else {
                System.out.println("oddly enough, not a lec slot or nonlec slot");
            }
            if (s == null) {
                System.out.println("Did not find the specified slot as active");
            } else if (m == null) {
                System.out.println("Could not find the meeting");
            } else {
                m.addUnwanted(s); // set it
                System.out.println("[Unwanted - " + m.toString() + " & " + s.getDay() + " " + s.printHour() + ":" + s.printMinute() + "]");
            }
        });
    }

    private void generatePreferences(Schedule sched, ArrayList<Course> courses, ArrayList<LectureSlot> lSlots, ArrayList<NonLectureSlot> nlSlots) {
        iw.preferencesLines.forEach((line) -> {
            String[] parts = line.split("\\s*,\\s*");
            String slotS = parts[0] + ", " + parts[1];
            String meetingS = parts[2];
            int penalty = Integer.parseInt(parts[3]);

            System.out.println("Slot: "+slotS+", Meeting: "+meetingS+", Penalty: "+penalty);
            
            Meeting m = findMeeting(courses, meetingS);
            if (m == null) {
                System.out.println("Meeting does not exist");
            } else {
                System.out.println("Meeting Exists");
                Assignment assignment = null;
                for (Assignment a : sched.getAssignments()) {
                    if (a.getM().equals(m)) {
                        assignment = a;
                    }
                }
                
                if(assignment == null){
                    System.out.println("could not find the needed assignment");
                    return;
                }
                System.out.println("Assignment was found");
                
                LectureSlot ls = null;
                NonLectureSlot nls = null;
                
                if (m instanceof Lecture) {
                    System.out.println("Meeting is a Lecture");
                    ls = findLectureSlot(lSlots, slotS);
                    if(ls == null){
                        System.out.println("could not find the lecture slot");
                        return;
                    }
                    if(!ls.isActive()){
                        System.out.println("that lecture slot is inactive");
                        return;
                    }
                    assignment.getM().addPreference(ls, penalty);
                    System.out.println("Added the Lecture Slot Preference");
                } else {
                    System.out.println("Meeting is a NonLecture");
                    nls = findNonLectureSlot(nlSlots, slotS);
                    if(nls == null){
                        System.out.println("could not find the non lecture slot");
                        return;
                    }
                    if(!nls.isActive()){
                        System.out.println("that non lecture slot is inactive");
                        return;
                    }
                    assignment.getM().addPreference(nls, penalty);
                    System.out.println("Added the Non Lecture Slot Preference");
                }

            }

        });
    }

}
