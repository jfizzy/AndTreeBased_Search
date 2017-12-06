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

import Schedule.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Class for parsing input from InputWrapper
 *
 */
public class InputParser {

    private InputWrapper iw;

    /**
     * Constructor
     */
    public InputParser() {
        iw = null;
    }

    /**
     * @param iw InputWrapper
     * @return Schedule
     */
    public Schedule run(InputWrapper iw) {
        this.iw = iw;
        ArrayList<LectureSlot> lecSlots = activateLectureSlots();
        ArrayList<NonLectureSlot> nonlecSlots = activateNonLectureSlots();
        ArrayList<Course> courses = generateSections();
        generateNonLectures(courses);
        Schedule schedule = new Schedule(lecSlots, nonlecSlots, courses);
        //schedule.setNoncompatible(generateIncompatibilities(courses));
        generateIncompatibilities(courses);
        generateUnwanted(courses, lecSlots, nonlecSlots);
        generatePreferences(schedule, lecSlots, nonlecSlots);
        //schedule.setPairs(generatePairs(courses));
        generatePairs(courses);
        if (applyPartialAssignments(schedule)) {
            orderAssignments(schedule);
            return schedule;
        } else {
            return null;
        }
    }

    /**
     * orderAssignments - takes a schedule and orders its list of assignments
     *
     * @param s Schedule
     */
    public void orderAssignments(Schedule s) {
        ArrayList<Assignment> orderedAssignments = new ArrayList<>();
        //pseudo
        ArrayList<Assignment> origAssignments = s.getAssignments();
        ArrayList<Assignment> unAssigned = new ArrayList<>();

        // look for partial assignments first
        origAssignments.forEach((a) -> {
            if (a.getS() != null) {
                // non-null slot - this is a partial assignment
                // no need to order any of these at all
                orderedAssignments.add(a);
            } else {
                // null slot
                unAssigned.add(a);
            }
        });

        // sort all unfilled assignment objects by restrictiveness
        prioritizeAssignments(unAssigned);
        // returns unAssigned in the new & improved order

        /*System.out.println("Already assigned (partial assignment):");
        orderedAssignments.forEach((a)-> {
            System.out.println("assign("+a.getM().toString()+" , "+a.getS().toString()+")");
        });*/
        unAssigned.forEach((a) -> {
            orderedAssignments.add(a);
        });

        s.setAssignments(orderedAssignments);
        //System.out.println("Ordering done");
    }

    /**
     * prioritizeAssignments - takes a list of assignments and orders them based
     * on restrictive priority
     *
     * @param assignments Assignments list
     */
    private void prioritizeAssignments(ArrayList<Assignment> assignments) {

        // compute priority values
        assignments.forEach((a) -> {
            a.setAp(new AssignmentPriority(a.getM()));
        });

        //System.out.println("Before:");
        /*assignments.forEach((a) -> {
            System.out.println(a.getM().toString());
        });*/
        // sort the list
        //System.out.println("[evening, incompat, prefPens, unwanted, pairs, type, courseNum, secNum]");
        Collections.sort(assignments, (a, b) -> a.compareTo(b)); // sort the assignments by the priority scheme

        /*System.out.println("");
        System.out.println("After:");
        assignments.forEach((a) -> {
            System.out.println(a.getM().toString());
        });
        System.out.println("----------------------------------");*/
    }

    /**
     * @param schedule Schedule
     */
    private boolean applyPartialAssignments(Schedule schedule) {
        for (String line : iw.partialAssignmentLines) {
            String[] parts = line.split("\\s*,\\s*");

            Meeting m = ScheduleUtils.findMeeting(schedule.getCourses(), parts[0]);
            if (m == null) {
                System.out.println("[!Partial assignment - could not find the specified meeting]");
                return false;
            }
            String slotString = parts[1] + ", " + parts[2];

            LectureSlot ls = null;
            NonLectureSlot nls = null;

            if (m instanceof Lecture) {
                //TODO check for cpsc 813, 913
                ls = ScheduleUtils.findLectureSlot(schedule.getLectureSlots(), slotString);
                if (ls == null) {
                    System.out.println("[!Partial assignment - no such lecture slot was found]");
                    return false;
                } else {
                    //set assignment to this slot
                    m.getAssignment().setS(ls);
                    System.out.println("[Partial assignment - " + ((Lecture) m).toString() + " <=> " + ls.toString() + "]");
                }
            } else if (m instanceof NonLecture) {
                nls = ScheduleUtils.findNonLectureSlot(schedule.getNonLectureSlots(), slotString);
                if (nls == null) {
                    System.out.println("[!Partial assignment - no such non lecture slot was found]");
                    return false;
                } else {
                    // set assignment to this slot
                    m.getAssignment().setS(nls);
                    System.out.println("[Partial assignment - " + ((NonLecture) m).toString() + " <=> " + nls.toString() + "]");
                }
            }
        }
        return true;
    }

    /**
     * @param courses Courses list
     * @return MeetingPair list
     */
    private ArrayList<MeetingPair> generatePairs(ArrayList<Course> courses) {
        ArrayList<MeetingPair> result = new ArrayList<>();

        iw.pairLines.forEach((line) -> {
            String parts[] = line.split("\\s*,\\s*");
            Meeting l = ScheduleUtils.findMeeting(courses, parts[0]);
            Meeting r = ScheduleUtils.findMeeting(courses, parts[1]);

            if (l != null && r != null) { // if both are found
                boolean duplicate = false;
                for (Meeting m : l.getPaired()) {
                    if (m.equals(r)) {
                        for (Meeting n : r.getPaired()) {
                            if (n.equals(l)) {
                                duplicate = true;
                                System.out.println("[!Pair - duplicate declaration of pair]");
                            }
                        }
                    }
                }
                if (!duplicate) {
                    l.addPaired(r);
                    r.addPaired(l);
                    result.add(new MeetingPair(l, r));
                    System.out.println("[Pair - " + l.toString() + " === " + r.toString() + "]");
                }
            } else {
                System.out.println("[!Pair - could not find at least one meeting]");
            }
        });

        return result;
    }

    /**
     * @param sched Schedule
     * @param lSlots Lecture slots list
     * @param nlSlots Nonlecture slots list
     */
    private void generatePreferences(Schedule sched, ArrayList<LectureSlot> lSlots, ArrayList<NonLectureSlot> nlSlots) {
        iw.preferencesLines.forEach((line) -> {
            String[] parts = line.split("\\s*,\\s*");
            String slotS = parts[0] + ", " + parts[1];
            String meetingS = parts[2];
            int penalty = Integer.parseInt(parts[3]);

            //System.out.println("Slot: " + slotS + ", Meeting: " + meetingS + ", Penalty: " + penalty);
            Meeting m = ScheduleUtils.findMeeting(sched.getCourses(), meetingS);
            if (m == null) {
                System.out.println("[!Preference - no such lecture or lab was found]");
            } else {

                Assignment assignment = null;
                if (m.getAssignment() != null) {
                    assignment = m.getAssignment();
                }
                if (assignment == null) {
                    System.out.println("[!Preference - no assignment with lec/lab was found]");
                } else {
                    //System.out.println("Assignment was found");
                    boolean problem = false;
                    LectureSlot ls = null;
                    NonLectureSlot nls = null;

                    if (m instanceof Lecture) {
                        ls = ScheduleUtils.findLectureSlot(lSlots, slotS);
                        if (ls == null) {
                            System.out.println("[!Preference - no such lecture slot was found]");
                            problem = true;
                        } else if (!ls.isActive()) {
                            System.out.println("[!Preference - no such active lecture slot was found]");
                            problem = true;
                        } else {
                            //System.out.println(ls.toString());
                            // check for duplicates
                            for (Preference p : assignment.getM().getPreferences()) {
                                if (p.getSlot().equals(ls)) {
                                    System.out.println("[!Preference - duplicate preference declaration]");
                                    problem = true;
                                }
                            }
                        }
                        if (!problem) {
                            Preference p = assignment.getM().addPreference(ls, penalty);
                            System.out.println("[Preference - " + assignment.getM().toString() + " -> " + p.getSlot().toString() + " " + p.getValue() + "]");
                        }
                    } else if (m instanceof NonLecture) {
                        nls = ScheduleUtils.findNonLectureSlot(nlSlots, slotS);
                        if (nls == null) {
                            System.out.println("[!Preference - no such lab slot was found]");
                            problem = true;
                        } else if (!nls.isActive()) {
                            System.out.println("[!Preference - no such active lab slot was found]");
                            problem = true;
                        } else {
                            // check for duplicates
                            for (Preference p : assignment.getM().getPreferences()) {
                                if (p.getSlot().equals(nls)) {
                                    System.out.println("[!Preference - duplicate preference declaration]");
                                    problem = true;
                                }
                            }
                        }
                        if (!problem) {
                            Preference p = assignment.getM().addPreference(nls, penalty);
                            System.out.println("[Preference - " + assignment.getM().toString() + " -> " + p.getSlot().toString() + " = " + p.getValue() + "]");
                        }
                    } else {
                        System.out.println("[!Preference - wierd error, not a lecture or a non lecture]");
                    }

                }
            }

        });
    }

    /**
     * @param courses Courses list
     * @param lSlots Lecture slots list
     * @param nlSlots Nonlecture slots list
     */
    private void generateUnwanted(ArrayList<Course> courses, ArrayList<LectureSlot> lSlots, ArrayList<NonLectureSlot> nlSlots) {
        iw.unwantedLines.stream().map((line) -> line.split("\\s*,\\s*")).forEachOrdered((parts) -> {
            Meeting m = ScheduleUtils.findMeeting(courses, parts[0]);
            Slot s = null;
            if (m instanceof Lecture) {
                LectureSlot l = ScheduleUtils.findLectureSlot(lSlots, (parts[1] + ", " + parts[2]));
                if (l.isActive()) {
                    s = l;
                }
            } else if (m instanceof NonLecture) {
                NonLectureSlot nl = ScheduleUtils.findNonLectureSlot(nlSlots, (parts[1] + ", " + parts[2]));
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
                boolean same = false;
                for (Slot os : m.getUnwanted()) {
                    if (os.equals(s)) {
                        same = true;
                    }
                }
                if (same) {
                    System.out.println("[!Unwanted - duplicate unwanted declaration]");
                } else {
                    m.addUnwanted(s); // set it
                    System.out.println("[Unwanted - " + m.toString() + " & " + s.getDay() + " " + s.printHour() + ":" + s.printMinute() + "]");
                }
            }
        });
    }

    /**
     * generateIncompatibilities - generates all of the Not compatible lines
     * specified in the input file; does so by creating a link between 'Meeting'
     * objects to their list of incompatible counterparts incompatibility is
     * symmetric
     *
     * @param courses Courses list
     * @return MeetingPair list
     */
    private ArrayList<MeetingPair> generateIncompatibilities(ArrayList<Course> courses) {
        ArrayList<MeetingPair> result = new ArrayList<>();

        iw.notCompatibleLines.stream().map((line) -> line.split("\\s*,\\s*")).forEachOrdered((halves) -> {
            String left = halves[0];
            String right = halves[1];

            Meeting l, r;
            // find the meetings
            l = ScheduleUtils.findMeeting(courses, left);
            r = ScheduleUtils.findMeeting(courses, right);

            if (l != null && r != null) { // if both are found
                // check for duplicate
                boolean duplicate = false;
                for (Meeting m : l.getIncompatibility()) {
                    if (m.equals(r)) {
                        for (Meeting n : r.getIncompatibility()) {
                            if (n.equals(l)) {
                                duplicate = true;
                                System.out.println("[!Not compatible - duplicate declaration of incompatibility]");
                            }
                        }
                    }
                }
                if (!duplicate) {
                    l.addIncompatibility(r); // give them 
                    r.addIncompatibility(l); // the same incompatibility
                    result.add(new MeetingPair(l, r));
                    System.out.println("[Not compatible - " + l.toString() + " =/= " + r.toString() + "]");
                }
            } else {
                System.out.println("[!Not compatible - could not find at least one meeting]");
            }
        });

        return result;
    }

    /**
     * generateSections - takes the lecture specifying lines of the input file
     * and aims to create all of the sections that were listed by the input file
     *
     * @return courses Courses list
     */
    private ArrayList<Course> generateSections() {
        ArrayList<Course> courses = new ArrayList<>();
        iw.lectureLines.stream().map((line) -> line.split("\\s+")).forEachOrdered((parts) -> {
            // split on whitespace
            String dept = parts[0];
            String courseNum = parts[1];
            // index two is of no use to us
            String sectionString = parts[3];
            generateSection(dept, courseNum, sectionString, courses);
        });
        return courses;
    }

    /**
     * @param dept Department string
     * @param courseNum Course number string
     * @param section Section number string
     * @param courses Courses list
     * @return Section
     */
    private Section generateSection(String dept, String courseNum, String section, ArrayList<Course> courses) {
        boolean existingDept = false;
        for (Course c : courses) {
            if (dept.equals(c.getDepartment())) { // same dept
                existingDept = true;
                //System.out.println("same dept");
                //System.out.println("course nums = " + courseNum + " & " + c.getNumber());
                if (Integer.parseInt(courseNum) == Integer.parseInt(c.getNumber())) { // same num
                    //System.out.println("Same number");
                    //System.out.println("num sections: " + c.getSections().size());
                    for (Section s : c.getSections()) { // iterate sections
                        if (section.equals(s.getSectionNum())) {
                            // we have two Lectures for same section
                            // this means input file is invalid
                            System.out.println("[!Course Section - duplicate section declaration]");
                            return null;
                        }
                    }
                    // Check if lecture section is an Evening section
                    Section s;
                    if (section.charAt(0) == '9') {
                        s = new Section(c, section, true);
                        c.addSection(s);
                        System.out.println("[Added Course Section - " + dept + " " + courseNum + " LEC " + section + " (evening)]");
                    } else {
                        s = new Section(c, section, false);
                        c.addSection(s);
                        System.out.println("[Added Course Section - " + dept + " " + courseNum + " LEC " + section + "]");
                    }
                    return s;
                }
            }
        }
        Section s;
        if (existingDept) {
            // Checks if Course is an "evening" course, then instantiates the course.
            Course c;
            if (section.charAt(0) == '9') {
                // Instantiate a course that is an evening course.
                c = new Course(dept, courseNum, section, true);
                courses.add(c);
                s = c.getSections().get(0);
                System.out.println("[Added Course - " + dept + " " + courseNum + " LEC " + section + " (evening)]");
            } else {
                //Instantiate a course that is NOT an evening course.
                c = new Course(dept, courseNum, section, false);
                courses.add(c);
                s = c.getSections().get(0);
                System.out.println("[Added Course - " + dept + " " + courseNum + " LEC " + section + "]");
            }
        } else {
            // Instantiate a course that is an evening course.
            Course c;
            if (section.charAt(0) == '9') {
                c = new Course(dept, courseNum, section, true);
                courses.add(c);
                s = c.getSections().get(0);
                System.out.println("[Added Course and Dept - " + dept + " " + courseNum + " LEC " + section + " (evening)]");
            } // Instantiate a course that is NOT an evening course.
            else {
                c = new Course(dept, courseNum, section, false);
                courses.add(c);
                s = c.getSections().get(0);
                System.out.println("[Added Course and Dept - " + dept + " " + courseNum + " LEC " + section + "]");
            }

        }
        return s;
    }

    /**
     * generateNonLectures - generate the NonLecture components of course
     * sections that are specified by the input file, takes in the list of
     * courses with which to place each NonLecture
     *
     * @param courses Courses list
     * @return Nonlecture list
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
                    System.out.println("[Added NonLecture - " + ((Tutorial) nl).toString() + "]");
                } else {
                    System.out.println("[Added NonLecture - " + ((Lab) nl).toString() + "]");
                }
            }
        });
        return result;
    }

    /**
     * generateNonLecture - component of the generateNonLectures logic, Takes a
     * single 'NonLecture' and places it in its correct location as long as the
     * parent course exists
     *
     * @param dept Department string
     * @param courseNum Course number string
     * @param section Section number string
     * @param nlType Type string
     * @param nlNum Nonlecture number string
     * @param courses Courses list
     * @return the added NonLecture
     */
    private NonLecture generateNonLecture(String dept, String courseNum, String section, String nlType, String nlNum, ArrayList<Course> courses) {
        for (Course course : courses) {
            if (course.getDepartment().equals(dept) && course.getNumber().equals(courseNum)) {
                // same dept and course number
                Section s = null;
                if (section == null) {
                    // open to all sections
                    if (course.getSections() != null) { // course has to have at least one section TODO
                        if ("TUT".equals(nlType)) {
                            for (Tutorial t : course.getOpenTuts()) {
                                if (t.getTutNum().equals(nlNum) && t.getSectionNum() == null) {
                                    System.out.println("[!NonLecture - duplicate open tutorial declaration]");
                                    return null;
                                }
                            }
                            Tutorial tut = new Tutorial(nlNum, null, false); // null means any section
                            tut.setParentCourse(course);
                            course.addOpenTut(tut); // now adds tutorial to course
                            return tut;
                        } else { // LAB
                            for (Lab l : course.getOpenLabs()) {
                                if (l.getLabNum().equals(nlNum) && l.getSectionNum() == null) {
                                    System.out.println("[!NonLecture - duplicate open lab declaration]");
                                    return null;
                                }
                            }
                            Lab lab = new Lab(nlNum, null, false);
                            lab.setParentCourse(course);
                            // add lab to all sections in course
                            course.addOpenLab(lab); // now adds lab to course
                            return lab;
                        }

                    } else {
                        return null; // no sections exist
                    }
                } else {
                    //System.out.println("finding sections of " + course.getDepartment() + " " + course.getNumber());
                    //System.out.println("num: " + course.getSections().size());
                    for (Section sec : course.getSections()) {
                        if (sec.getSectionNum().equals(section)) {
                            s = sec;
                            break;
                        }
                    }
                    if (s == null) {
                        System.out.println("[!NonLecture - non existent section]");
                        return null; // section does not exist
                    }
                }
                if ("TUT".equals(nlType)) {
                    if (s.getTuts().isEmpty()) {
                        Tutorial tut = new Tutorial(nlNum, s, false);
                        s.addTutorial(tut);
                        return tut;
                    } else {
                        for (Tutorial t : s.getTuts()) {
                            if (t.getTutNum().equals(nlNum)) {
                                //this is a duplicate declaration of a tutorial
                                System.out.println("[!NonLecture - duplicate tutorial declaration]");
                                return null;
                            } else {
                                Tutorial tut = new Tutorial(nlNum, s, false);
                                s.addTutorial(tut);
                                return tut;
                            }
                        }
                    }
                } else {
                    if (s.getLabs().isEmpty()) {
                        Lab lab = new Lab(nlNum, s, false);
                        s.addLab(lab);
                        return lab;
                    } else {
                        for (Lab l : s.getLabs()) {
                            if (l.getLabNum().equals(nlNum)) {
                                //this is a duplicate declaration of a lab
                                System.out.println("[!NonLecture - duplicate lab declaration]");
                                return null;
                            } else {
                                Lab lab = new Lab(nlNum, s, false);
                                s.addLab(lab);
                                return lab;
                            }
                        }
                    }
                }

            }
        }
        System.out.println("[!NonLecture - invalid non lecture declaration]");
        return null; // could not match a course with same dept or number, so ignore
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

            LectureSlot s = ScheduleUtils.findLectureSlot(slots, slotString);
            if (s != null) {
                if (!s.isActive()) {
                    int coursemax = Integer.parseInt(parts[2]);
                    int coursemin = Integer.parseInt(parts[3]);
                    if (coursemax == 0) {
                        System.out.println("[!Lecture Slot - coursemax is zero]");
                        s.setCourseMin(coursemin); // assign coursemin anyways
                    } else {
                        s.activate(coursemax, coursemin); // activate the slot
                        System.out.println("[Activated Lecture Slot - " + s.getDay() + " " + s.printHour() + ":" + s.printMinute() + " " + coursemax + " " + coursemin + "]");
                    }
                }
            } else {
                System.out.println("[!Lecture Slot - Could not find the lecture slot]");
            }
        });
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

            NonLectureSlot s = ScheduleUtils.findNonLectureSlot(slots, slotString);
            if (s != null) {
                if (!s.isActive()) {
                    int labmax = Integer.parseInt(parts[2]);
                    int labmin = Integer.parseInt(parts[3]);
                    if (labmax == 0) {
                        System.out.println("[!NonLecture Slot - labmax is zero]");
                        s.setLabMin(labmin); // assign the penalty anyways
                    } else {
                        s.activate(labmax, labmin);
                        System.out.println("[Activated NonLecture Slot - " + s.getDay() + " " + s.printHour() + ":" + s.printMinute() + " " + labmax + " " + labmin + "]");
                    }
                }
            } else {
                System.out.println("[!NonLecture Slot - could not find the non lecture slot]");
            }
        });
        return slots;
    }

    /**
     * @return Lecture slot list
     */
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
     * @return Nonlecture slot list
     */
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
}
