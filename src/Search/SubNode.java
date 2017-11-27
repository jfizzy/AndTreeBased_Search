/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Search;

import Schedule.Schedule;

/**
 *
 * @author
 */
public class SubNode extends Node {

    private final Node parentNode;
    
    public SubNode(Schedule s, Node parentNode) {
        super(s);
        this.parentNode = parentNode;
    }

    public Node getParentNode() {
        return parentNode;
    }
    
     // TODO add some subnode specific functions here
}
