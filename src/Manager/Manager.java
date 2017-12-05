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

        // one argument or 5 (the file name and then 4 weights) 
    	// TODO also have to read penalties as input (penalties and weights are different)
        try {
            if (args.length == 1 || args.length == 5) {

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
                if (args.length == 5) {
                    try {
                        w1 = Double.parseDouble(args[1]);
                        w2 = Double.parseDouble(args[2]);
                        w3 = Double.parseDouble(args[3]);
                        w4 = Double.parseDouble(args[4]);
                    } catch (NumberFormatException e) {
                        // TODO specific error not usage
                        usage();
                    }
                }

                // run the search
                search(args[0], w1, w2, w3, w4);
            } else {
                usage();
            }
        } catch (Exception e) {
            e.printStackTrace();
            exit();
        }
    }

    /**
     * Prints the usage statement and exits
     */
    private static void usage() { 
        System.out.println("Acceptable args are: inputfile [MinFilled weight] [Pref weight] [Pair weight] [Diff weight]");
        exit();
    }

    /**
     * Perform a search using an input file
     *
     * @param fp File path
     */
    private static void search(String fp, double w1, double w2, double w3, double w4) throws FileNotFoundException {
    	
        System.out.println(">>> Reading input file [" + fp + "] and parsing ...");
        
        // parse the input file
        InputManager im = new InputManager();
        Schedule schedule = im.run(fp);
        if(schedule == null){
            System.out.println("There were invalid lines in the input file.");
            exit();
        }
        
        // set the weights for eval components
        schedule.setWeights(w1, w2, w3, w4);
        
        System.out.println(">>> Done");
        System.out.println(">>> Finding optimal assignments ...");
        
        // Run the schedule, and instantiate finalSchedule to its result.
        SearchManager sm = new SearchManager(schedule);
        Schedule finalSchedule = new Schedule(sm.run());
        System.out.println(">>> Done");
        
        // produce graphical HTML schedule
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
