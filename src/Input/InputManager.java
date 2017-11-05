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

/**
 *
 * @author 
 */
public class InputManager {
    
    private FileExaminer fr;
    //need a storage medium for the file contents
    
    private String deptName;
    
    public InputManager(){
        fr = null;
    }
    
    public void run(String fp){
        fr = new FileExaminer(fp);
        fr.init();
        fr.parse();
    }
    
}
