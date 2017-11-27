/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Search;

import Schedule.*;

/**
 *
 * @author
 */
public class RootNode extends Node {
    
    private int boundVal;
    
    /**
     * @param s
     */
    public RootNode(Schedule s) {
        super(s);
    }

    /**
     * @param boundVal
     * @return
     */
    public Schedule initSearch(int boundVal) {
    	
        this.boundVal = boundVal;
        return this.runSearch(true, 0);
        
        //return null;
    }
}
