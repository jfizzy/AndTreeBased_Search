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

/**
 * Class with utility functions for Schedule package
 */
public class ScheduleUtils {
    
    /**
     * findMeeting - given an input String that can very generally specify any
     * nlType of 'Meeting' subclass, whether it be a 'Lecture', 'Tutorial', or
     * 'Lab', attempts to return the specific object it resides in for use of
     * the caller
     *
     * @param courses
     * @param meetingString
     * @return
     */
    public static Meeting findMeeting(ArrayList<Course> courses, String meetingString) {
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
        } else { // lecture not given
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
                            for (Tutorial tut : c.getOpenTuts()) { // its an open tutorial
                                if (tut.getTutNum().equals(nlNum)) {
                                    return tut; // found the tutorial
                                }
                            }
                        } else if ("LAB".equals(nlType)) { // its an open lab
                            for (Lab lab : c.getOpenLabs()) {
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
    
    /**
     * @param slots
     * @param slotString
     * @return
     */
    public static LectureSlot findLectureSlot(ArrayList<LectureSlot> slots, String slotString) {
        String[] parts = slotString.split("\\s*,\\s*");
        String day = parts[0];
        if (!(day.equalsIgnoreCase("MO") || day.equalsIgnoreCase("TU"))) {
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
            System.out.println("problem with integer parsing");
            return null;
        }
        //TODO print out warning that no valid lecture slot was found
        return null;
    }

    /**
     * @param slots
     * @param slotString
     * @return
     */
    public static NonLectureSlot findNonLectureSlot(ArrayList<NonLectureSlot> slots, String slotString) {
        String[] parts = slotString.split("\\s*,\\s*");
        String day = parts[0];
        if (!(day.equalsIgnoreCase("MO") || day.equalsIgnoreCase("TU") || day.equalsIgnoreCase("FR"))) {
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
            System.out.println("problem with integer parsing");
            return null;
        }
        return null;
    }
    
}
