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
import Schedule.LectureSlot;
import Schedule.NonLectureSlot;
import Schedule.Section;
import Schedule.TimeTable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author 
 */
public class InputManager {
    
    private FileExaminer fe;
    private InputWrapper iw;
    private String deptName;
    
    
    public InputManager(){
        fe = null;
        iw = null;
    }
    
    public void run(String fp){
        iw = new InputWrapper();
        fe = new FileExaminer(fp, iw);
        fe.init();
        fe.parse();
        generateLectureSlots();
        generateNonLectureSlots();
        generateTimeTable();
    }
    
    private void generateTimeTableObjects(){
        ArrayList<LectureSlot> ls = generateLectureSlots();
        ArrayList<NonLectureSlot> nls = generateNonLectureSlots();
        TimeTable tt = generateTimeTable();
    }
    
    private ArrayList<LectureSlot> generateLectureSlots(){
        ArrayList<LectureSlot> results = new ArrayList<>();
        iw.lectureSlotLines.stream().map((line) -> line.split("\\s*,\\s*")).map((parts) -> {
            for(String p : parts){
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
            results.add(new LectureSlot(day,hour,minute,coursemax,coursemin));
            System.out.println("[Added Lecture Slot - "+day+" "+hour+":"+minute+" "+coursemax+" "+coursemin+"]");
        });
        return results;
    }
    
    private ArrayList<NonLectureSlot> generateNonLectureSlots(){
        ArrayList<NonLectureSlot> results = new ArrayList<>();
        iw.nonlectureSlotLines.stream().map((line) -> line.split("\\s*,\\s*")).map((parts) -> {
            for(String p : parts){
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
            results.add(new NonLectureSlot(day,hour,minute,labmax,labmin));
            System.out.println("[Added Non Lecture Slot - "+day+" "+hour+":"+minute+" "+labmax+" "+labmin+"]");
        });
        return results;
    }
    
    private TimeTable generateTimeTable(){
        //generate list of courses
        ArrayList<Course> courses = generateSections();
        
        return null;
    }
    
    private ArrayList<Course> generateSections(){
        ArrayList<Course> courses = new ArrayList<>();
        for (String line : iw.lectureLines) {
            String[] parts = line.split("\\s+"); // split on whitespace
            String dept = parts[0];
            String courseNum = parts[1];
            // index two is of no use to us
            String section = parts[3];
            boolean added = false;
            for(Course c : courses){
                if(dept.equals(c.getDepartment())){ // same dept
                    if(courseNum.equals(c.getNumber())){ // same num
                        for(Section s : c.getSections()){
                            if(section.equals(s.getSectionNum())){ 
                                // we have two Lectures for same section
                                // this means input file is invalid
                                return null;
                            }
                        }
                        c.addSection(new Section(c, section)); // add the new section
                        System.out.println("[Added Course Section - "+dept+" "+courseNum+" LEC "+section+"]");
                        added = true;
                        break;
                    }
                    courses.add(new Course(c.getDepartment(),courseNum,section));
                    System.out.println("[Added Course - "+dept+" "+courseNum+" LEC "+section+"]");
                    added = true;
                    break;
                }
            }
            if(!added){
                courses.add(new Course(dept, courseNum, section));
                System.out.println("[Added Course and Dept - "+dept+" "+courseNum+" LEC "+section+"]");
            }
        }
        return courses;
    }
    
    private boolean generateNonLectures(){
        for(String line : iw.nonlectureLines){
            String[] parts = line.split("\\s+"); // split on whitespace
            
        }
        return false;
    }
    
}
