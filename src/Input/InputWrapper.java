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

import java.util.ArrayList;

public class InputWrapper {
    ArrayList<String> lectureSlotLines;
    ArrayList<String> nonlectureSlotLines;
    ArrayList<String> lectureLines;
    ArrayList<String> nonlectureLines;
    ArrayList<String> notCompatibleLines;
    ArrayList<String> unwantedLines;
    ArrayList<String> preferencesLines;
    ArrayList<String> pairLines;
    ArrayList<String> partialAssignmentLines;
    
    public InputWrapper(){
        lectureSlotLines = new ArrayList<>();
        nonlectureSlotLines = new ArrayList<>();
        lectureLines = new ArrayList<>();
        nonlectureLines = new ArrayList<>();
        notCompatibleLines = new ArrayList<>();
        unwantedLines = new ArrayList<>();
        preferencesLines = new ArrayList<>();
        pairLines = new ArrayList<>();
        partialAssignmentLines = new ArrayList<>();
    }
}
