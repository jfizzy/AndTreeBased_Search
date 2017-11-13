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

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileExaminer {

    private final String fp;
    private File f;
    private BufferedReader br;
    private InputWrapper iw;
    private final Pattern sectionPatt,breakPatt,departmentPatt,slotPatt,lecturePatt,nonlecturePatt,notCompatiblePatt,unwantedPatt,preferencesPatt,courseSlotPatt,labSlotPatt;
    private boolean inSec,courseSlotSec,labSlotSec,lectureSec,nonlectureSec,notCompatibleSec,unwantedSec,preferencesSec,pairSec,partialAssignmentSec;

    public FileExaminer(String fp, InputWrapper iw) {
        this.notCompatiblePatt = Pattern.compile("^\\s*([A-Z][A-Z][A-Z][A-Z]\\s*[0-9][0-9][0-9]\\s*(LEC\\s*[0-9][0-9]\\s*(TUT|LAB)\\s*[0-9][0-9]|LEC\\s*[0-9][0-9]|(TUT|LAB)\\s*[0-9][0-9])\\s*,\\s*[A-Z][A-Z][A-Z][A-Z]\\s*[0-9][0-9][0-9]\\s*(LEC\\s*[0-9][0-9]\\s*(TUT|LAB)\\s*[0-9][0-9]|LEC\\s*[0-9][0-9]|(TUT|LAB)\\s*[0-9][0-9]))$");
        this.lecturePatt = Pattern.compile("^\\s*[A-Z][A-Z][A-Z][A-Z]\\s*[0-9][0-9][0-9]\\s*LEC\\s*[0-9][0-9]$");
        this.nonlecturePatt = Pattern.compile("^\\s*[A-Z][A-Z][A-Z][A-Z]\\s*[0-9][0-9][0-9]\\s*(LEC\\s*[0-9][0-9]\\s*(TUT|LAB)\\s*[0-9][0-9]|(TUT|LAB)\\s*[0-9][0-9])$");
        this.slotPatt = Pattern.compile("^\\s*(MO|TU|FR)\\s*,\\s*[1-2]?[0-9]:[0-5][0-9]\\s*,\\s*[0-9]*\\s*,\\s*[0-9]*$");
        this.departmentPatt = Pattern.compile("^\\s*[a-zA-Z0-9]+$");
        this.breakPatt = Pattern.compile("^\\s*$");
        this.sectionPatt = Pattern.compile("^\\s*([A-Za-z]+(\\s*[A-Za-z]+)*:)$");
        this.unwantedPatt = Pattern.compile("^\\s*([A-Z][A-Z][A-Z][A-Z]\\s*[0-9][0-9][0-9]\\s*(LEC\\s*[0-9][0-9]|LEC\\s*[0-9][0-9]\\s*(TUT|LAB)\\s*[0-9][0-9])\\s*,\\s*(MO|TU|WE|TR|FR)\\s*,\\s*[ 12]?[0-9]:[0-9][0-9])$");
        this.preferencesPatt = Pattern.compile("^\\s*(MO|TU|WE|TR|FR)\\s*,\\s*[1-2]?[0-9]:[0-5][0-9]\\s*,\\s*[A-Z][A-Z][A-Z][A-Z]\\s*[0-9][0-9][0-9]\\s*(LEC\\s*[0-9][0-9]|LEC\\s*[0-9][0-9]\\s*(TUT|LAB)\\s*[0-9][0-9])\\s*,\\s*[0-9]*[0-9]$");
        this.courseSlotPatt = Pattern.compile("^\\s*(MO|TU).*$");
        this.labSlotPatt = Pattern.compile("^\\s*(MO|TU|FR).*$");
        this.fp = fp;
        this.f = null;
        this.br = null;
        this.iw = iw;
        
        this.inSec = false;
        this.courseSlotSec = false;
        this.labSlotSec = false;
        this.lectureSec = false;
        this.nonlectureSec = false;
        this.notCompatibleSec = false;
        this.unwantedSec = false;
        this.preferencesSec = false;
        this.pairSec = false;
        this.partialAssignmentSec = false;
        
    }

    public void init() {
        try {
            f = new File(fp);
            br = new BufferedReader(new FileReader(f));

        } catch (Exception e) {
            System.err.println("Problem when initializing input file reader");
        }
    }

    public boolean parse() {
        try {
            if (!br.ready()) {
                return false;
            }
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim(); // trim leading and trailing whitespace
                Matcher msec = sectionPatt.matcher(line); // section format regex
                Matcher mbrk = breakPatt.matcher(line); // break format regex
                Matcher mdpt = departmentPatt.matcher(line); // department format regex
                Matcher mslt = slotPatt.matcher(line); // slot format regex
                Matcher mlec = lecturePatt.matcher(line); // lecture format regex
                Matcher mnle = nonlecturePatt.matcher(line); // non-lecture format regex
                Matcher mncp = notCompatiblePatt.matcher(line); // not-compatible format regex
                Matcher muwt = unwantedPatt.matcher(line); // unwanted format regex
                Matcher mpre = preferencesPatt.matcher(line); // preferences format regex

                Matcher mcslt = courseSlotPatt.matcher(line); // checks that it has a valid course slot prefix
                Matcher mlslt = labSlotPatt.matcher(line); // checks that it has a valid lab slot prefix
                
                if (msec.find()) { // section
                    // check which section is being read next
                    System.out.println("section line");
                    this.inSec = true;
                    if(courseSlotSec | labSlotSec | lectureSec | nonlectureSec | notCompatibleSec | unwantedSec | preferencesSec | pairSec | partialAssignmentSec)
                            return false;
                    line = line.toLowerCase().split(":")[0]; // handle little spelling mistakes
                    switch (line) {
                        case "name":
                            break;
                        case "course slots":
                            courseSlotSec = true;
                            break;
                        case "lab slots":
                            labSlotSec = true;
                            break;
                        case "courses":
                            lectureSec = true;
                            break;
                        case "labs":
                            nonlectureSec = true;
                            break;
                        case "not compatible":
                            notCompatibleSec = true;
                            break;
                        case "unwanted":
                            unwantedSec = true;
                            break;
                        case "preferences":
                            preferencesSec = true;
                            break;
                        case "pair":
                            pairSec = true;
                            break;
                        case "partial assignments":
                            partialAssignmentSec = true;
                            break;
                        default:
                            System.err.println("unrecognized section line");
                            System.err.println("forcing exit");
                            br.close();
                            return false; // had an unrecognized section
                    }
                } else if (mdpt.find() && inSec) { // department
                    System.out.println("department line");
                } else if (mslt.find() && inSec && ((courseSlotSec && mcslt.find()) || (labSlotSec && mlslt.find()))) { // slot
                    if(courseSlotSec && mcslt.find()){
                        System.out.println("lecture slot line");
                        iw.lectureSlotLines.add(line);
                    }else if(labSlotSec && mlslt.find()){
                        System.out.println("non lecture slot line");
                        iw.nonlectureSlotLines.add(line);
                    }
                } else if (mlec.find() && inSec) { // lecture
                    System.out.println("lecture line");
                    iw.lectureLines.add(line);
                } else if (mnle.find() && inSec) { // lecture
                    System.out.println("non lecture line");
                    iw.nonlectureLines.add(line);
                } else if (mncp.find() && inSec) { // 
                    System.out.println("not compatible line");
                    iw.notCompatibleLines.add(line);
                } else if (muwt.find() && inSec) {
                    if(unwantedSec){
                        System.out.println("unwanted line");
                        iw.unwantedLines.add(line);
                    }else{
                        System.out.println("partial assignments line");
                        iw.partialAssignmentLines.add(line);
                    }
                } else if (mpre.find() && inSec) {
                    System.out.println("preferences line");
                    iw.preferencesLines.add(line);
                } else if (mbrk.find() && inSec) { // break
                    System.out.println("break line");
                    // skip and prepare for new section
                    if(courseSlotSec)
                        courseSlotSec = false;
                    else if(labSlotSec)
                        labSlotSec = false;
                    else if(lectureSec)
                        lectureSec = false;
                    else if(nonlectureSec)
                        nonlectureSec= false;
                    else if(notCompatibleSec)
                        notCompatibleSec = false;
                    else if(unwantedSec)
                        unwantedSec = false;
                    else if(preferencesSec)
                        preferencesSec = false;
                    else if(pairSec)
                        pairSec = false;
                    else if(partialAssignmentSec)
                        partialAssignmentSec = false;
                } else { //not one of our line formats
                    System.out.println("unusable line");
                    /*System.out.println("Poorly formatted input file");
                    return false;*/
                }

            }
            br.close();
            
        } catch (IOException io) {
            System.err.println("There was a problem reading in the input file contents");
            return false;
        }
        
        return true;
    }
}
