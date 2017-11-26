package Testing;

import Input.FileExaminer;
import Input.InputWrapper;
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
}
