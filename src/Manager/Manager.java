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
import Search.SearchManager;

/**
 * Coordinates program flow
 * 
 */
class Manager {
    
    /**
     * Main program function (entry point)
     * @param args Command line arguments
     */
    public static void main(String[] args){
    	
        if (args.length == 1) {
            File f = new File(args[0]);
            if (f.exists()) search(args[0]);
            else exit();
        }
        else{
            exit();
        }
    }
    
    /**
     * Perform a search using an input file
     * @param fp File path
     */
    private static void search(String fp){
        System.out.println(">>> Reading input file ["+fp+"] and parsing ...");
        InputManager im = new InputManager();
        Schedule schedule = im.run(fp);
        System.out.println(">>> Done");
        System.out.println(">>> Finding optimal assignments ...");
        SearchManager sm = new SearchManager(schedule);
        sm.run();
        System.out.println(">>> Done");
    }
    
    /**
     * Exit the program
     */
    private static void exit(){
        System.out.println("Exiting...");
        System.exit(0);
    }
}
