package Testing;

import Input.FileExaminer;
import Input.InputParser;
import Input.InputWrapper;
import Schedule.LectureSlot;
import Schedule.Schedule;
import java.io.FileNotFoundException;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class InputTests {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testEmptyInputFile() {
        InputWrapper iw = new InputWrapper();
        FileExaminer fe = new FileExaminer("test_files/empty_input_file.txt", iw);
        try{
            fe.init();
        }catch(FileNotFoundException e){
            fail("File not found");
        }
        fe.filter();
        assertFalse(iw.hasContent());
    }

    @Test
    public void testBadSectionHeader() {
        InputWrapper iw = new InputWrapper();
        FileExaminer fe = new FileExaminer("test_files/bad_sections_input_file.txt", iw);
        try{
            fe.init();
        }catch(FileNotFoundException e){
            fail("File not found");
        }
        assertFalse(fe.filter());
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileExists() throws FileNotFoundException{
        InputWrapper iw = new InputWrapper();
        FileExaminer fe = new FileExaminer("test_files/i_dont_exist.txt", iw);
        fe.init();
    }
    
    @Test
    public void testSectionHeaderRecognition() {
        InputWrapper iw = new InputWrapper();
        FileExaminer fe = new FileExaminer("test_files/good_sections_input_file.txt", iw);
        try{
            fe.init();
        }catch(FileNotFoundException e){
            fail("File not found");
        }
        assertTrue(fe.filter());
    }
    
    @Test
    public void testSectionItemFiltering() {
        InputWrapper iw = new InputWrapper();
        FileExaminer fe = new FileExaminer("test_files/add_iw_items_input_file.txt", iw);
        try{
            fe.init();
        }catch(FileNotFoundException e){
            fail("File not found");
        }
        assertTrue(fe.filter());
        
        assertTrue(iw.hasContent());
    }
    
    @Test
    public void testBasicInputParser() {
        InputWrapper iw = new InputWrapper();
        FileExaminer fe = new FileExaminer("test_files/sample_input_3.txt", iw);
        try{
            fe.init();
        }catch(FileNotFoundException e){
            fail("File not found");
        }
        assertTrue(fe.filter());
        
        assertTrue(iw.hasContent());
        
        InputParser ip = new InputParser();
        Schedule schedule = ip.run(iw);
        
        assertTrue(!schedule.getAssignments().isEmpty());
    }
    
    @Test
    public void testLectureSlotActivation() {
        InputWrapper iw = new InputWrapper();
        FileExaminer fe = new FileExaminer("test_files/lecture_slot_activation_tests.txt",iw);
        
        try{
            fe.init();
        }catch(FileNotFoundException fnfe){
            fail("File not found");
        }
        
        assertTrue(fe.filter());
        
        assertTrue(iw.hasContent());
        
        InputParser ip = new InputParser();
        Schedule schedule = ip.run(iw);
        
        assertTrue(!schedule.getAssignments().isEmpty());
        
        LectureSlot ls = schedule.findLectureSlot("MO", 8, 0);
        if(ls == null)
            fail("Could not find a lecture slot");
        assertTrue(ls.isActive());
        assertTrue(ls.getCourseMax() == 3);
        assertTrue(ls.getCourseMin() == 2);
        
        ls = schedule.findLectureSlot("MO", 8, 30);
        assertTrue(ls == null);
        
        ls = schedule.findLectureSlot("MO", 9, 0);
        assertTrue(ls.isActive());
        assertTrue(ls.getCourseMax() == 3);
        assertTrue(ls.getCourseMin() == 2);
        
        ls = schedule.findLectureSlot("TU", 9, 30);
        assertTrue(ls.isActive());
        assertTrue(ls.getCourseMax() == 2);
        assertTrue(ls.getCourseMin() == 1);
        
        ls = schedule.findLectureSlot("TU", 21, 0);
        assertTrue(ls == null);
        
        ls = schedule.findLectureSlot("MO", 23, 0);
        assertTrue(ls == null);
        
        ls = schedule.findLectureSlot("MO", 14, 0);
        assertTrue(ls.isActive());
        assertTrue(ls.getCourseMax() == 6);
        assertTrue(ls.getCourseMin() == 0);
        
        ls = schedule.findLectureSlot("TU", 12, 30);
        assertFalse(ls.isActive());
        assertTrue(ls.getCourseMax() == 0);
        assertTrue(ls.getCourseMin() == 2);
    }
}
