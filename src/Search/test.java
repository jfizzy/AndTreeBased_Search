/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Search;
import Schedule.*;

/**
 *
 * @author emlou
 */
public class test {
    
    
    public static void main(String args[])
    {
        Course c1 = new Course("CPSC", "313", "01", false);
        Section c1s1 = c1.getSection(0);
        Tutorial c1s1t1 = new Tutorial("01", c1s1, false);
        c1s1.addTutorial(c1s1t1);

        c1.print();
        
        System.out.println(c1s1.toString());
        System.out.println(c1s1.getTuts().size());
        
    }
    
}
