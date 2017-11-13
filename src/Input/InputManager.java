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

    public InputManager() {
        fe = null;
        iw = null;
    }

    public SearchData run(String fp) {

        iw = new InputWrapper();
        fe = new FileExaminer(fp, iw);
        fe.init();
        fe.parse();
        SearchData sd = new SearchData();
        sd.setLectureSlots(generateLectureSlots());
        sd.setLabSlots(generateNonLectureSlots());
        sd.setTimetable(generateTimeTable());
        return sd;
    }

    private void generateTimeTableObjects() {
        ArrayList<LectureSlot> ls = generateLectureSlots();
        ArrayList<NonLectureSlot> nls = generateNonLectureSlots();
        TimeTable tt = generateTimeTable();
    }

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

    private TimeTable generateTimeTable() {
        //generate list of courses
        ArrayList<Course> courses = generateSections();
        System.out.println("About to gen nonlecs");
        ArrayList<NonLecture> nonLectures = generateNonLectures(courses);
        return null;
    }

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

    private ArrayList<NonLecture> generateNonLectures(ArrayList<Course> courses) {
        ArrayList<NonLecture> nonLectures = new ArrayList<>();
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
            System.out.println("Type: "+ nlType);
            System.out.println("About to generate a nonlec from parsed line");
            NonLecture nl = generateNonLecture(dept, courseNum, section, nlType, nlNum, courses);
            if (nl != null) {
                nonLectures.add(nl);
                if ("TUT".equals(nlType)) // check for type of NonLecture
                {
                    System.out.println("[Added NonLecture - " + nl.getDept() + " " + nl.getCourseNum() + " LEC " + nl.getSectionNum() + " TUT " + ((Tutorial) nl).getTutNum() + "]");
                } else {
                    System.out.println("[Added NonLecture - " + nl.getDept() + " " + nl.getCourseNum() + " LEC " + nl.getSectionNum() + " LAB " + ((Lab) nl).getLabNum() + "]");
                }
            } else {
                System.out.println("Could not create a nonlec");
            }
        });
        return nonLectures;
    }

    private NonLecture generateNonLecture(String dept, String courseNum, String section, String nlType, String nlNum, ArrayList<Course> courses) {
        System.out.println(dept + "," + courseNum + "," + section + "," + nlType + "," + nlNum);
        for (Course course : courses) {
            if (course.getDepartment().equals(dept) && course.getNumber().equals(courseNum)) {
                // same dept and course number
                Section s = null;
                if (section == null) {
                    System.out.println("No Section given - assuming there is only one");
                    // look at the first (and hopefully only) section TODO: make sure this is correct
                    if (course.getSections() != null) {
                        s = course.getSections().get(0);
                    } else {
                        return null; // section does not exist
                    }
                } else {
                    System.out.println("About to try to find the right section");
                    for (Section sec : course.getSections()) {
                        if (sec.getSectionNum().equals(section)) {
                            System.out.println("Found the right section: " + sec.getSectionNum());
                            s = sec;
                            break;
                        }
                    }
                    if (s == null) {
                        System.out.println("No correct section was found. Invalid nonlec entry");
                        return null; // section does not exist
                    }
                }
                if ("TUT".equals(nlType)) {
                    System.out.println("We are trying to create a Tutorial");
                    if (s.getTuts().isEmpty()) {
                        Tutorial tut = new Tutorial(nlNum, s, false);
                        s.addTutorial(tut);
                        System.out.println("Added tutorial");
                        return tut; // TODO need to revisit setting of evening
                    } else {
                        for (Tutorial t : s.getTuts()) {
                            if (t.getTutNum().equals(nlNum)) {
                                //this is a duplicate declaration of a tutorial
                                System.out.println("This tutorial was previously created");
                                return null;
                            } else {
                                Tutorial tut = new Tutorial(nlNum, s, false);
                                s.addTutorial(tut);
                                System.out.println("Added tutorial");
                                return tut; // TODO need to revisit setting of evening
                            }
                        }
                    }
                } else {
                    System.out.println("We are trying to create a Lab");
                    if (s.getLabs().isEmpty()) {
                        Lab lab = new Lab(nlNum, s, false);
                        s.addLab(lab);
                        System.out.println("Added lab");
                        return lab; // TODO need to revisit setting of evening
                    } else {
                        for (Lab l : s.getLabs()) {
                            if (l.getLabNum().equals(nlNum)) {
                                //this is a duplicate declaration of a lab
                                System.out.println("This lab was previously created");
                                return null;
                            } else {
                                Lab lab = new Lab(nlNum, s, false);
                                s.addLab(lab);
                                System.out.println("Added lab");
                                return lab; // TODO need to revisit setting of evening
                            }
                        }
                    }
                }

            }
        }
        return null; // could not match a course with same dept or number, so ignore
    }

}
