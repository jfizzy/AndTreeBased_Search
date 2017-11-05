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
    
    private FileExaminer fe;
    private InputWrapper iw;
    private String deptName;
    
    
    public InputManager(){
        fe = null;
        iw = null;
    }
    
    public void run(String fp){
        iw = new InputWrapper();
        fe = new FileExaminer(fp, iw);
        fe.init();
        fe.parse();
    }
    
}
