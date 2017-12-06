package Testing;

import Input.FileExaminer;
import Input.InputParser;
import Input.InputWrapper;
import Schedule.LectureSlot;
import Schedule.NonLectureSlot;
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
        
        //test valid monday/wednesday slot
        LectureSlot ls = schedule.findLectureSlot("MO", 8, 0);
        if(ls == null)
            fail("Could not find the lecture slot");
        assertTrue(ls.isActive());
        //ensure max/min were not altered by duplicate declaration
        assertTrue(ls.getCourseMax() == 3);
        assertTrue(ls.getCourseMin() == 2);
        
        //test invalid monday/wednesday slot
        ls = schedule.findLectureSlot("MO", 8, 30);
        assertTrue(ls == null);
        
        //test another valid monday/wednesday slot
        ls = schedule.findLectureSlot("MO", 9, 0);
        if(ls == null)
            fail("Could not find the lecture slot");
        assertTrue(ls.isActive());
        assertTrue(ls.getCourseMax() == 3);
        assertTrue(ls.getCourseMin() == 2);
        
        //arbitrary inactive but valid slot on monday
        ls = schedule.findLectureSlot("MO", 13, 0);
        if(ls == null)
            fail("Could not find the lecture slot");
        assertFalse(ls.isActive());
        
        //valid tuesday/thursday slot
        ls = schedule.findLectureSlot("TU", 9, 30);
        if(ls == null)
            fail("Could not find the lecture slot");
        assertTrue(ls.isActive());
        assertTrue(ls.getCourseMax() == 2);
        assertTrue(ls.getCourseMin() == 1);
        
        //invalid tuesday/thursday slot
        ls = schedule.findLectureSlot("TU", 21, 0);
        assertTrue(ls == null);
        
        //arbitrary inactive but valid slot on tuesday
        ls = schedule.findLectureSlot("TU", 12, 30);
        if(ls == null)
            fail("Could not find the lecture slot");
        assertFalse(ls.isActive());
        
        //out of bounds slot
        ls = schedule.findLectureSlot("MO", 23, 0);
        assertTrue(ls == null);
        
        //valid monday slot
        ls = schedule.findLectureSlot("MO", 14, 0);
        if(ls == null)
            fail("Could not find the lecture slot");
        assertTrue(ls.isActive());
        assertTrue(ls.getCourseMax() == 6);
        assertTrue(ls.getCourseMin() == 0);
        
        //test with coursemax is zero but min is noneg
        ls = schedule.findLectureSlot("TU", 12, 30);
        assertFalse(ls.isActive());
        assertTrue(ls.getCourseMax() == 0);
        
        //test evening slot
        ls = schedule.findLectureSlot("MO", 19, 0);
        if(ls == null)
            fail("Could not find the lecture slot");
        assertTrue(ls.isActive());
        assertTrue(ls.isEvening());
        assertTrue(ls.getCourseMax() == 3);
        assertTrue(ls.getCourseMin() == 0);
        
        //test invalid slot from 11:00-12:30 tuesdays (and therefore thursdays)
        ls = schedule.findLectureSlot("MO", 11, 0);
        if(ls == null)
            fail("Could not find the lecture slot");
        assertFalse(ls.isActive());
        assertTrue(ls.getCourseMax() == 0);
    }
    
    @Test
    public void testNonLectureSlotActivation() {
        InputWrapper iw = new InputWrapper();
        FileExaminer fe = new FileExaminer("test_files/nonlecture_slot_activation_tests.txt",iw);
        
        try{
            fe.init();
        }catch(FileNotFoundException fnfe){
            fail("File not found");
        }
        
        assertTrue(fe.filter());
        
        assertTrue(iw.hasContent());
        
        InputParser ip = new InputParser();
        Schedule schedule = ip.run(iw);
        
        //check the input parsed without a failure
        assertTrue(!schedule.getAssignments().isEmpty());
        
        //test a valid monday/wednesday slot
        NonLectureSlot nls = schedule.findNonLectureSlot("MO", 8, 0);
        if(nls == null)
            fail("Could not find the nonlecture slot");
        assertTrue(nls.isActive());
        //ensure max/min were not replaced by second activation of same slot
        assertTrue(nls.getLabMax() == 4);
        assertTrue(nls.getLabMin() == 2);
        
        //invalid monday/wednesday slot
        nls = schedule.findNonLectureSlot("MO", 7, 0);
        assertTrue(nls == null);
        
        //valid tuesday/thursday slot
        nls = schedule.findNonLectureSlot("TU", 10, 0); 
        if(nls == null)
            fail("Could not find the nonlecture slot");
        assertTrue(nls.isActive());
        assertTrue(nls.getLabMax() == 2);
        assertTrue(nls.getLabMin() == 1);
        
        //another valid
        nls = schedule.findNonLectureSlot("TU", 12, 0); 
        if(nls == null)
            fail("Could not find the nonlecture slot");
        assertTrue(nls.isActive());
        assertTrue(nls.getLabMax() == 3);
        assertTrue(nls.getLabMin() == 0);
        
        //invalid tues/thurs slot
        nls = schedule.findNonLectureSlot("TU", 12, 30); 
        assertTrue(nls== null);
        
        //valid friday 2h slot
        nls = schedule.findNonLectureSlot("FR", 10, 0); 
        if(nls == null)
            fail("Could not find the nonlecture slot");
        assertTrue(nls.isActive());
        assertTrue(nls.getLabMax() == 2);
        assertTrue(nls.getLabMin() == 1);
        
        //invalid fri 2h slot
        nls = schedule.findNonLectureSlot("FR", 13, 0);
        assertTrue(nls== null);
        
        //test for labmax == 0
        nls = schedule.findNonLectureSlot("MO", 14, 0); 
        if(nls == null)
            fail("Could not find the nonlecture slot");
        assertFalse(nls.isActive());
        assertTrue(nls.getLabMax() == 0);
        assertTrue(nls.getLabMin() == 0);
        
        // test for labmax < labmin
        nls = schedule.findNonLectureSlot("FR", 8, 0); 
        if(nls == null)
            fail("Could not find the nonlecture slot");
        assertTrue(nls.isActive());
        assertTrue(nls.getLabMax() == 1);
        assertTrue(nls.getLabMin() == 3);
        
    }
}
