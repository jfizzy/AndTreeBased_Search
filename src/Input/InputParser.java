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
import Schedule.Preference;
import Schedule.Schedule;
import Schedule.ScheduleUtils;
import Schedule.Section;
import Schedule.Slot;
import Schedule.Tutorial;
import java.util.ArrayList;

/**
 *
 * @author
 */
public class InputParser {

    private InputWrapper iw;

    public InputParser() {
        iw = null;
    }

    public Schedule run(InputWrapper iw) {
        this.iw = iw;
        ArrayList<LectureSlot> lecSlots = activateLectureSlots();
        ArrayList<NonLectureSlot> nonlecSlots = activateNonLectureSlots();
        ArrayList<Course> courses = generateSections();
        generateNonLectures(courses);
        generateIncompatibilities(courses);
        generateUnwanted(courses, lecSlots, nonlecSlots);
        Schedule schedule = new Schedule(lecSlots, nonlecSlots, courses);
        generatePreferences(schedule, lecSlots, nonlecSlots);
        generatePairs(courses);
        // need to order the list of assignments
        return schedule;
    }

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
                m.addUnwanted(s); // set it
                System.out.println("[Unwanted - " + m.toString() + " & " + s.getDay() + " " + s.printHour() + ":" + s.printMinute() + "]");
            }
        });
    }

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
                //System.out.println("Meeting Exists");
                Assignment assignment = null;
                if (m.getAssignment() != null) {
                    assignment = m.getAssignment();
                }

                if (assignment == null) {

                    return;
                }
                //System.out.println("Assignment was found");

                LectureSlot ls = null;
                NonLectureSlot nls = null;

                if (m instanceof Lecture) {
                    //System.out.println("Meeting is a Lecture");
                    ls = ScheduleUtils.findLectureSlot(lSlots, slotS);
                    if (ls == null) {
                        System.out.println("[!Preference - no such lecture slot was found]");
                        return;
                    }
                    if (!ls.isActive()) {
                        System.out.println("[!Preference - no such active lecture slot was found]");
                        //System.out.println("that lecture slot is inactive");
                        return;
                    }
                    Preference p = assignment.getM().addPreference(ls, penalty);
                    System.out.println("[Preference - " + assignment.getM().toString() + " -> " + p.getSlot().toString() + " " + p.getValue() + "]");
                } else if (m instanceof NonLecture) {
                    //System.out.println("Meeting is a NonLecture");
                    nls = ScheduleUtils.findNonLectureSlot(nlSlots, slotS);
                    if (nls == null) {
                        System.out.println("[!Preference - no such lab slot was found]");
                        return;
                    }
                    if (!nls.isActive()) {
                        System.out.println("[!Preference - no such active lab slot was found]");
                        //System.out.println("that non lecture slot is inactive");
                        return;
                    }
                    Preference p = assignment.getM().addPreference(nls, penalty);
                    System.out.println("[Preference - " + assignment.getM().toString() + " -> " + p.getSlot().toString() + " = " + p.getValue() + "]");
                } else {
                    System.out.println("Wierd error, not a lecture or a non lecture");
                }

            }

        });
    }

    private void generatePairs(ArrayList<Course> courses) {
        iw.pairLines.forEach((line) -> {
            String parts[] = line.split("\\s*,\\s*");
            Meeting l = ScheduleUtils.findMeeting(courses, parts[0]);
            Meeting r = ScheduleUtils.findMeeting(courses, parts[1]);

            if (l != null && r != null) { // if both are found
                l.addPaired(r);
                r.addPaired(l);
                System.out.println("[Pair - " + l.toString() + " === " + r.toString() + "]");
            } else {
                System.out.println("[!Pair - could not find at least one meeting]");
            }
        });

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
            l = ScheduleUtils.findMeeting(courses, left);
            r = ScheduleUtils.findMeeting(courses, right);

            if (l != null && r != null) { // if both are found
                l.addIncompatibility(r); // give them 
                r.addIncompatibility(l); // the same incompatibility
                System.out.println("[Not compatible - " + l.toString() + " =/= " + r.toString() + "]");
            } else {
                System.out.println("[!Not compatible - could not find at least one meeting]");
            }
        });

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
                    System.out.println("[Added NonLecture - " + ((Tutorial) nl).toString() + "]");
                } else {
                    System.out.println("[Added NonLecture - " + ((Lab) nl).toString() + "]");
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
                    if (course.getSections() != null) { // course has to have at least one section TODO
                        if ("TUT".equals(nlType)) {
                            for (Tutorial t : course.getOpenTuts()) {
                                if (t.getTutNum().equals(nlNum) && t.getSectionNum() == null) {
                                    System.out.println("This is a duplicate open tutorial declaration");
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
                                    System.out.println("This is a duplicate open lab declaration");
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
                int labmax = Integer.parseInt(parts[2]);
                int labmin = Integer.parseInt(parts[3]);
                s.activate(labmax, labmin);
                System.out.println("[Activated NonLecture Slot - " + s.getDay() + " " + s.printHour() + ":" + s.printMinute() + " " + labmax + " " + labmin + "]");
            }
        });
        return slots;
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
