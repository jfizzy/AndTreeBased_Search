package Testing;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import Schedule.*;

/**
 * @author 
 *
 */
public class ScheduleTests {
	
	// TODO: more tests
	// Most of the functionality is covered in Constr and Eval testing

	/**
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * 
	 */
	@Test
	public void overallTest1() {
		
		ArrayList<LectureSlot> ls = new ArrayList<>();
		ArrayList<NonLectureSlot> nls = new ArrayList<>();
		ArrayList<Course> cs = new ArrayList<>();
		
		// make slots
		LectureSlot ls1 = new LectureSlot("MO", 8, 0, 9, 0, 5, 3, false);
		ls.add(ls1);
		LectureSlot ls2 = new LectureSlot("TU", 11, 0, 12, 30, 5, 3, false);
		ls.add(ls2);
		LectureSlot ls3 = new LectureSlot("MO", 14, 0, 15, 0, 5, 3, false);
		ls.add(ls3);
		LectureSlot ls4 = new LectureSlot("TU", 18, 0, 19, 30, 5, 3, true);
		ls.add(ls4);
		NonLectureSlot nls1 = new NonLectureSlot("MO", 8, 0, 9, 0, 5, 3, false);
		nls.add(nls1);
		NonLectureSlot nls2 = new NonLectureSlot("TU", 11, 0, 12, 0, 5, 3, false);
		nls.add(nls2);
		NonLectureSlot nls3 = new NonLectureSlot("WE", 14, 0, 15, 0, 5, 3, false);
		nls.add(nls3);
		NonLectureSlot nls4 = new NonLectureSlot("FR", 18, 0, 19, 0, 5, 3, true);
		nls.add(nls4);
		
		// make courses
		Course c1 = new Course("CPSC", "201", "01");
		Section s1 = c1.getSections().get(0);
		Lecture l1 = s1.getLecture();
		Lab b1 = new Lab("01", s1, false);
		s1.addLab(b1);
		Tutorial t1 = new Tutorial("02", s1, false);
		s1.addTutorial(t1);
		cs.add(c1);
		
		Course c2 = new Course("SENG", "301", "01");
		Section s2 = c2.getSections().get(0);
		Lecture l2 = s2.getLecture();
		Lab b2 = new Lab("01", s2, false);
		s2.addLab(b2);
		Tutorial t2 = new Tutorial("02", s2, false);
		s2.addTutorial(t2);
		cs.add(c2);
		
		l1.addPreference(ls2, 10);
		l2.addPreference(ls1, 11);
		b1.addPreference(nls1, 12);
		b1.addPreference(nls2, 13);
		l1.addUnwanted(ls1);
		b2.addUnwanted(nls2);
		
		// make schedule
		Schedule schedule = new Schedule(ls, nls, cs);
		
		schedule.updateAssignment(l1, ls1);
		schedule.updateAssignment(l2, ls2);
		schedule.updateAssignment(b1, nls1);
		schedule.updateAssignment(b2, nls2);
		schedule.updateAssignment(t1, nls3);
		schedule.updateAssignment(t2, nls4);
		
		schedule.addNoncompatible(b1, b2);
		schedule.addPair(l1, l2);
		
		// test lecture slots
		assertEquals(schedule.getLectureSlots().size(), 4);
		
		// test nonlecture slots
		assertEquals(schedule.getNonLectureSlots().size(), 4);
		
		// test courses
		assertEquals(schedule.getCourses().size(), 2);
		
		assertEquals(schedule.getCourses().get(0).getCourseLabs().size(), 1);
		assertEquals(schedule.getCourses().get(0).getCourseTuts().size(), 1);
		assertEquals(schedule.getCourses().get(1).getCourseLabs().size(), 1);
		assertEquals(schedule.getCourses().get(1).getCourseTuts().size(), 1);
		
		// test sections
		assertEquals(schedule.getCourses().get(0).getSections().size(), 1);
		assertEquals(schedule.getCourses().get(1).getSections().size(), 1);
		
		// test lectures		
		assertEquals(schedule.getLectures().size(), 2);
		
		// test labs		
		assertEquals(schedule.getLabs().size(), 2);
		
		// test tutorials		
		assertEquals(schedule.getTuts().size(), 2);
		
		// test assignments
		assertEquals(schedule.getAssignments().size(), 6);
		
		// test preference
		int count = 0;
		for (Lecture l : schedule.getLectures()) {
			for (Preference p : l.getPreferences())
				count++;
		}
		for (NonLecture nl : schedule.getNonLectures()) {
			for (Preference p : nl.getPreferences())
				count++;
		}
		assertEquals(count, 4);
		
		// test unwanted
		count = 0;
		for (Lecture l : schedule.getLectures()) {
			for (Slot s : l.getUnwanted())
				count++;
		}
		for (NonLecture nl : schedule.getNonLectures()) {
			for (Slot s : nl.getUnwanted())
				count++;
		}
		assertEquals(count, 2);
		
		// test noncompatible
		assertEquals(schedule.getNoncompatible().size(), 1);
		
		// test pair
		assertEquals(schedule.getPairs().size(), 1);
	}
}
