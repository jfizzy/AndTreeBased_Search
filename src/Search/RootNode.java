/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Search;

import Schedule.Assignment;
import Schedule.Lecture;
import Schedule.LectureSlot;
import Schedule.Schedule;

/**
 *
 * @author
 */
public class RootNode extends Node {
    
    private int boundVal;
    
    public RootNode(Schedule s) {
        super(s);
    }

    public Schedule initSearch(int boundVal){
        this.boundVal = boundVal;
        Assignment start = null;
        for (Assignment a : this.getSchedule().getAssignments()) {
            if (a.getS() == null) {
                start = a;
            }
        }
        if (start == null) {
            System.out.println("the schedule is already full!");
            return null;
        }
        
        if(start.getM() instanceof Lecture){
            Lecture l = ((Lecture) start.getM());
            // TODO add special assignment possibility for 813, 913, etc
            for(LectureSlot ls : this.getSchedule().getLectureSlots()){ // iterate over lecture slots
                // try each one
            }
        }else{ // nonlecture
            
        }
        
        // find possible assignments
        // evaluate leftmost first
        //recurse through all
        return null;
    }
}
