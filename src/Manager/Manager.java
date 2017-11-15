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

import Input.InputManager;
import Search.SearchData;
import Search.SearchManager;

/**
 *
 * @author 
 */
class Manager {
    
    public static void main(String[] args){
        if (args.length == 1){
            // maybe check if file exists here
            search(args[0]);
        }else{
            exit();
        }
        System.out.println("And-Tree based Search Scheduling Manager initialized.");
    }
    
    private static void search(String fp){
        InputManager im = new InputManager();
        SearchData sd = im.run(fp);
        SearchManager sm = new SearchManager(sd);
        sm.run();
        // need to use resulting input data here
    }
    
    private static void exit(){
        System.out.println("Exitting...");
        System.exit(0);
    }
}
