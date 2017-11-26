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

import Schedule.Schedule;
import java.io.FileNotFoundException;

/**
 * Class for managing parsing of input data from file
 *
 */
public class InputManager {

    private FileExaminer fe;
    private InputWrapper iw;
    private InputParser ip;
    private String deptName;

    /**
     * Constructor
     */
    public InputManager() {
        fe = null;
        iw = null;
        ip = null;
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
     * @throws java.io.FileNotFoundException
     */
    public Schedule run(String fp) throws FileNotFoundException {
        try {
            iw = new InputWrapper();
            fe = new FileExaminer(fp, iw);
            fe.init();
            fe.filter();
            // sorted input file lines by type
            ip = new InputParser();
            Schedule schedule = ip.run(iw);

            // Just added this to test new input line functions
            return schedule;
        } catch (FileNotFoundException e) {
            throw e;
        }
    }
}
