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
package Manager;

import java.io.File;

import Input.InputManager;
import Schedule.Schedule;
import Input.ScheduleVisualizer;
import Search.SearchManager;
import java.io.FileNotFoundException;

/**
 * Coordinates program flow
 *
 */
class Manager {

    /**
     * Main program function (entry point)
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {

        // one argument or 6 (the file name and then 5 weights) 
        try {
            if (args.length == 1 || args.length == 6) {

                // parse input file arg
                File f = new File(args[0]);
                if (!f.exists()) {
                    usage();
                }

                // parse weights if applicable
                double w1 = 1;
                double w2 = 1;
                double w3 = 1;
                double w4 = 1;
                double w5 = 1;
                if (args.length == 6) {
                    try {
                        w1 = Double.parseDouble(args[1]);
                        w2 = Double.parseDouble(args[2]);
                        w3 = Double.parseDouble(args[3]);
                        w4 = Double.parseDouble(args[4]);
                        w5 = Double.parseDouble(args[5]);
                    } catch (NumberFormatException e) {
                        // TODO
                        usage();
                    }
                }

                // run the search
                search(args[0], w1, w2, w3, w4, w5);
            } else {
                usage();
            }
        } catch (Exception e) {
            e.printStackTrace();
            exit();
        }
    }

    private static void usage() { 
        System.out.println("Acceptable args are: inputfile [CourseMin weight] [LabMin weight] [Pref weight] [Pair weight] [Diff weight]");
        exit();
    }

    /**
     * Perform a search using an input file
     *
     * @param fp File path
     */
    private static void search(String fp, double w1, double w2, double w3, double w4, double w5) throws FileNotFoundException {
        System.out.println(">>> Reading input file [" + fp + "] and parsing ...");
        //try{
        InputManager im = new InputManager();
        Schedule schedule = im.run(fp);
        if(schedule == null){
            System.out.println("There were invalid lines in the input file.");
            exit();
        }
        schedule.setWeights(w1, w2, w3, w4, w5);
        System.out.println(">>> Done");
        System.out.println(">>> Finding optimal assignments ...");
        SearchManager sm = new SearchManager(schedule);
        //Run the schedule, and instantiate finalSchedule to its result.
        Schedule finalSchedule = new Schedule(sm.run());
        //} catch(FileNotFoundException e){
        //    throw e;
        //}
        System.out.println(">>> Done");
        
        ScheduleVisualizer vis = new ScheduleVisualizer(finalSchedule);
        vis.run();
    }

    /**
     * Exit the program
     */
    private static void exit() {
        System.out.println(">>> Exiting...");
        System.exit(0);
    }
}
