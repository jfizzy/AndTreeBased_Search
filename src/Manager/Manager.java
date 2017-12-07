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
    	if (args.length != 1 && args.length != 5 && args.length != 9)
        	usage();
    	
        try {
        	
            // parse input file arg
            File f = new File(args[0]);
            if (!f.exists()) {
                usage();
            }

            // parse weights if applicable
            double p1 = 1;
            double p2 = 1;
            double p3 = 1;
            double p4 = 1;
            if (args.length == 5) {
                try {
                    p1 = Double.parseDouble(args[1]);
                    p2 = Double.parseDouble(args[2]);
                    p3 = Double.parseDouble(args[3]);
                    p4 = Double.parseDouble(args[4]);
                } catch (NumberFormatException e) {
                    // TODO specific error not usage
                    usage();
                }
            }
            
            // parse weights if applicable
            double w1 = 1;
            double w2 = 1;
            double w3 = 1;
            double w4 = 1;
            if (args.length == 9) {
                try {
                    w1 = Double.parseDouble(args[5]);
                    w2 = Double.parseDouble(args[6]);
                    w3 = Double.parseDouble(args[7]);
                    w4 = Double.parseDouble(args[8]);
                } catch (NumberFormatException e) {
                    // TODO specific error not usage
                    usage();
                }
            }

            // run the search
            search(args[0], p1, p2, p3, p4, w1, w2, w3, w4);
        } catch (Exception e) {
            e.printStackTrace();
            exit();
        }
    }

    /**
     * Prints the usage statement and exits
     */
    private static void usage() { 
        System.out.println("Acceptable args are: inputfile <penalties> <weights>");
        System.out.println("Penalties: [] [] [] []");
        System.out.println("Penalties: [] [] [] []");
        exit();
    }

    /**
     * Perform a search using an input file
     *
     * @param fp File path
     */
    private static void search(String fp, 
    		double p1, double p2, double p3, double p4, 
    		double w1, double w2, double w3, double w4) throws FileNotFoundException {
    	
        System.out.println(">>> Reading input file [" + fp + "] and parsing ...");
        
        // parse the input file
        InputManager im = new InputManager();
        Schedule schedule = im.run(fp);
        if(schedule == null){
            System.out.println("There were invalid lines in the input file.");
            exit();
        }
        
        // set the penalties and weights for eval components
        schedule.setPenalties(p1, p2, p3, p4);
        schedule.setWeights(w1, w2, w3, w4);
        
        System.out.println(">>> Done");
        System.out.println(">>> Finding optimal assignments ...");
        
        // Run the schedule, and instantiate finalSchedule to its result.
        SearchManager sm = new SearchManager(schedule);
        Schedule tmp = sm.run();
        if(tmp == null)
            exit();
        Schedule finalSchedule = new Schedule(tmp);
        if(finalSchedule == null)
            exit();
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
