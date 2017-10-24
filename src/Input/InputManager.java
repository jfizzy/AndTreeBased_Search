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
