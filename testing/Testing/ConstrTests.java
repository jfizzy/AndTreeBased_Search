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

package Testing;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import Schedule.*;
import Search.Constr;


/**
 * JUnit test class for Constr
 *
 */
public class ConstrTests {
	
	ArrayList<LectureSlot> ls;
	ArrayList<NonLectureSlot> nls;
	ArrayList<Course> courses;

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

	@Before
	public void setUp() throws Exception {
		ls = new ArrayList<>();
		nls = new ArrayList<>();
		courses = new ArrayList<>();
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
	public void testCheckCourseMax() {
		clearLists();
		
		// coursemax = 2
		// make slot
		LectureSlot slot = new LectureSlot("MO", 8, 0, 9, 0, 2, 0, false);
		ls.add(slot);
		
		// valid
		Course c = new Course("ABCD", "001", "01", false);
		Section sect = new Section(c, "01", false);
		c.addSection(sect);
		courses.add(c);
		
		Schedule schedule = new Schedule(ls, nls, courses);
		schedule.addAssignment(sect.getLecture(), slot);

		assertTrue(Constr.checkCourseMax(schedule));
		assertTrue(schedule.isValid());
		
		// valid
		sect = new Section(c, "02", false);
		courses.get(0).addSection(sect);
		
		schedule.setCourses(courses);
		schedule.addAssignment(sect.getLecture(), slot);
		
		assertTrue(Constr.checkCourseMax(schedule));
		assertTrue(schedule.isValid());
		
		// invalid
		sect = new Section(c, "03", false);
		courses.get(0).addSection(sect);
		
		schedule.setCourses(courses);
		schedule.addAssignment(sect.getLecture(), slot);
		
		assertFalse(Constr.checkCourseMax(schedule));
		assertFalse(schedule.isValid());
	}

	/**
	 * 
	 */
	@Test
	public void testCheckLabMax() {
		clearLists();

		// labmax = 2
		// make slot
		NonLectureSlot slot = new NonLectureSlot("MO", 8, 0, 9, 0, 2, 0, false);
		nls.add(slot);
		
		// valid
		Course c = new Course("ABCD", "001", "01", false);
		Section sect = new Section(c, "01", false);
		Lab lab1 = new Lab("01", sect, false);
		sect.addLab(lab1);
		c.addSection(sect);
		courses.add(c);
		
		Schedule schedule = new Schedule(ls, nls, courses);
		schedule.addAssignment(lab1, slot);
		
		assertTrue(Constr.checkLabMax(schedule));
		assertTrue(schedule.isValid());
		
		// valid
		Lab lab2 = new Lab("02", sect, false);
		courses.get(0).getSections().get(0).addLab(lab2);
		
		//schedule.setNonLectures(nonlecs);
		schedule.setCourses(courses);
		schedule.addAssignment(lab2, slot);
		
		assertTrue(Constr.checkLabMax(schedule));
		assertTrue(schedule.isValid());
		
		// invalid
		Lab lab3 = new Lab("03", sect, false);
		courses.get(0).getSections().get(0).addLab(lab3);
		
		//schedule.setNonLectures(nonlecs);
		schedule.setCourses(courses);
		schedule.addAssignment(lab3, slot);
		
		assertFalse(Constr.checkLabMax(schedule));
		assertFalse(schedule.isValid());
	}

	/**
	 * 
	 */
	@Test
	public void testCheckLabsDifferent() {
		clearLists();
		
		// make slots
		LectureSlot lslot1 = new LectureSlot("MO", 8, 0, 9, 0, 2, 0, false);
		ls.add(lslot1);
		LectureSlot lslot2 = new LectureSlot("MO", 12, 0, 13, 0, 2, 0, false);
		ls.add(lslot2);
		NonLectureSlot nlslot_bad1 = new NonLectureSlot("MO", 8, 0, 9, 0, 2, 0, false);
		nls.add(nlslot_bad1);
		NonLectureSlot nlslot_bad2 = new NonLectureSlot("MO", 12, 0, 13, 0, 2, 0, false);
		nls.add(nlslot_bad2);
		NonLectureSlot nlslot_good1 = new NonLectureSlot("MO", 18, 0, 19, 30, 2, 0, false);
		nls.add(nlslot_good1);
		NonLectureSlot nlslot_good2 = new NonLectureSlot("TU", 8, 0, 9, 30, 2, 0, false);
		nls.add(nlslot_good2);
		
		// make course
		Course c = new Course("ABCD", "001", "01", false);
		Section sect1 = new Section(c, "01", false);
		Lecture l1 = sect1.getLecture();
		c.addSection(sect1);
		Section sect2 = new Section(c, "02", false);
		Lecture l2 = sect2.getLecture();
		Lab lab1 = new Lab("01", sect2, false);
		sect2.addLab(lab1);
		c.addSection(sect2);
		Tutorial tut1 = new Tutorial("02", null, false);
		c.addOpenTut(tut1);
		courses.add(c);
		
		// valid
		Schedule schedule = new Schedule(ls, nls, courses);
		schedule.addAssignment(l2, lslot1);
		schedule.addAssignment(l1, lslot2);
		schedule.addAssignment(lab1, nlslot_good1);
		schedule.addAssignment(tut1, nlslot_good1);
		
		assertTrue(Constr.checkLabsDifferent(schedule));
		assertTrue(schedule.isValid());
		
		// valid		
		schedule.addAssignment(l2, lslot1);
		schedule.addAssignment(l1, lslot2);
		schedule.addAssignment(lab1, nlslot_good2);
		schedule.addAssignment(tut1, nlslot_good2);
		
		assertTrue(Constr.checkLabsDifferent(schedule));
		assertTrue(schedule.isValid());
		
		// invalid		
		schedule.addAssignment(l2, lslot1);
		schedule.addAssignment(l1, lslot2);
		schedule.addAssignment(lab1, nlslot_bad1);
		schedule.addAssignment(tut1, nlslot_good1);
		
		assertFalse(Constr.checkLabsDifferent(schedule));
		assertFalse(schedule.isValid());
		
		// invalid		
		schedule.addAssignment(l2, lslot1);
		schedule.addAssignment(l1, lslot2);
		schedule.addAssignment(lab1, nlslot_bad2);
		schedule.addAssignment(tut1, nlslot_good2);
		
		assertFalse(Constr.checkLabsDifferent(schedule));
		assertFalse(schedule.isValid());
		
		// invalid		
		schedule.addAssignment(l2, lslot1);
		schedule.addAssignment(l1, lslot2);
		schedule.addAssignment(lab1, nlslot_good2);
		schedule.addAssignment(tut1, nlslot_bad1);

		assertFalse(Constr.checkLabsDifferent(schedule));
		assertFalse(schedule.isValid());
		
		// invalid		
		schedule.addAssignment(l2, lslot1);
		schedule.addAssignment(l1, lslot2);
		schedule.addAssignment(lab1, nlslot_good1);
		schedule.addAssignment(tut1, nlslot_bad2);

		assertFalse(Constr.checkLabsDifferent(schedule));
		assertFalse(schedule.isValid());
	}

	/**
	 * 
	 */
	@Test
	public void testCheckNoncompatible() {
		clearLists();
		
		// make slots
		LectureSlot lslot1 = new LectureSlot("MO", 8, 0, 9, 0, 2, 0, false);
		ls.add(lslot1);
		LectureSlot lslot2 = new LectureSlot("MO", 18, 0, 19, 0, 2, 0, false);
		ls.add(lslot2);
		NonLectureSlot nlslot1 = new NonLectureSlot("TU", 8, 0, 9, 30, 2, 0, false);
		nls.add(nlslot1);
		NonLectureSlot nlslot2 = new NonLectureSlot("TU", 18, 0, 19, 30, 2, 0, false);
		nls.add(nlslot2);
		
		// make course
		Course c = new Course("ABCD", "01", "01", false);
		Section s1 = new Section(c, "01", false);
		Lecture l1 = s1.getLecture();
		Section s2 = new Section(c, "02", false);
		Lecture l2 = s2.getLecture();
		l1.addIncompatibility(l2);
		l2.addIncompatibility(l1);
		
		// make labs
		Tutorial b1 = new Tutorial("01", s1, false);
		s1.addTutorial(b1);
		Lab b2 = new Lab("02", null, false);
		c.addOpenLab(b2);
		b1.addIncompatibility(b2);
		b2.addIncompatibility(b1);
		courses.add(c);
		
		// valid
		Schedule schedule = new Schedule(ls, nls, courses);
		schedule.addAssignment(l1, lslot1);
		schedule.addAssignment(l2, lslot2);
		//schedule.addNoncompatible(l1, l2);
		//schedule.addNoncompatible(b1, b2);

		assertTrue(Constr.checkNoncompatible(schedule));
		assertTrue(schedule.isValid());
		
		// valid
		schedule = new Schedule(ls, nls, courses);
		schedule.addAssignment(b1, nlslot1);
		schedule.addAssignment(b2, nlslot2);
		//schedule.addNoncompatible(l1, l2);
		//schedule.addNoncompatible(b1, b2);
		
		assertTrue(Constr.checkNoncompatible(schedule));
		assertTrue(schedule.isValid());
		
		// invalid
		schedule = new Schedule(ls, nls, courses);
		schedule.addAssignment(l1, lslot1);
		schedule.addAssignment(l2, lslot1);
		//schedule.addNoncompatible(l1, l2);
		//schedule.addNoncompatible(b1, b2);
		
		assertFalse(Constr.checkNoncompatible(schedule));
		assertFalse(schedule.isValid());
		
		// invalid
		schedule = new Schedule(ls, nls, courses);
		schedule.addAssignment(b1, nlslot2);
		schedule.addAssignment(b2, nlslot2);
		//schedule.addNoncompatible(l1, l2);
		//schedule.addNoncompatible(b1, b2);
		
		assertFalse(Constr.checkNoncompatible(schedule));
		assertFalse(schedule.isValid());
	}

	/**
	 * 
	 */
	@Test
	public void testCheckPartassign() {
		clearLists();
		
		// make slots
		LectureSlot lslot1 = new LectureSlot("MO", 8, 0, 9, 0, 2, 0, false);
		ls.add(lslot1);
		LectureSlot lslot2 = new LectureSlot("MO", 18, 0, 19, 0, 2, 0, false);
		ls.add(lslot2);
		NonLectureSlot nlslot1 = new NonLectureSlot("TU", 8, 0, 9, 30, 2, 0, false);
		nls.add(nlslot1);
		NonLectureSlot nlslot2 = new NonLectureSlot("TU", 18, 0, 19, 30, 2, 0, false);
		nls.add(nlslot2);
		
		// make course
		Course c = new Course("ABCD", "01", "01", false);
		Section s = new Section(c, "01", false);
		Lecture l1 = s.getLecture();
		l1.setPartassign(lslot1);
		
		// make lab
		Lab b1 = new Lab("01", null, false);
		b1.setPartassign(nlslot1);
		s.addLab(b1);
		courses.add(c);
		
		// valid
		Schedule schedule = new Schedule(ls, nls, courses);
		schedule.addAssignment(l1, lslot1);
		schedule.addAssignment(b1, nlslot1);

		assertTrue(Constr.checkPartassign(schedule));
		assertTrue(schedule.isValid());
		
		// invalid
		schedule = new Schedule(ls, nls, courses);
		schedule.addAssignment(l1, lslot1);
		schedule.addAssignment(b1, nlslot2);

		assertFalse(Constr.checkPartassign(schedule));
		assertFalse(schedule.isValid());
		
		// invalid
		schedule = new Schedule(ls, nls, courses);
		schedule.addAssignment(l1, lslot2);
		schedule.addAssignment(b1, nlslot1);

		assertFalse(Constr.checkPartassign(schedule));
		assertFalse(schedule.isValid());
		
		// invalid
		schedule = new Schedule(ls, nls, courses);
		schedule.addAssignment(l1, nlslot2);
		schedule.addAssignment(b1, nlslot2);

		assertFalse(Constr.checkPartassign(schedule));
		assertFalse(schedule.isValid());
	}

	/**
	 * 
	 */
	@Test
	public void testCheckUnwanted() {
		clearLists();
		
		// make slots
		LectureSlot lslot1 = new LectureSlot("MO", 8, 0, 9, 0, 2, 0, false);
		ls.add(lslot1);
		LectureSlot lslot2 = new LectureSlot("MO", 18, 0, 19, 0, 2, 0, false);
		ls.add(lslot2);
		NonLectureSlot nlslot1 = new NonLectureSlot("TU", 8, 0, 9, 30, 2, 0, false);
		nls.add(nlslot1);
		NonLectureSlot nlslot2 = new NonLectureSlot("TU", 18, 0, 19, 30, 2, 0, false);
		nls.add(nlslot2);
		
		// make course
		Course c = new Course("ABCD", "01", "01", false);
		Section s = new Section(c, "01", false);
		Lecture l1 = s.getLecture();
		l1.addUnwanted(lslot2);
		courses.add(c);
		
		// make lab
		Lab b1 = new Lab("01", null, false);
		b1.addUnwanted(nlslot2);
		
		// valid
		Schedule schedule = new Schedule(ls, nls, courses);
		schedule.addAssignment(l1, lslot1);
		schedule.addAssignment(b1, lslot1);

		assertTrue(Constr.checkUnwanted(schedule));
		assertTrue(schedule.isValid());
		
		// invalid
		schedule = new Schedule(ls, nls, courses);
		schedule.addAssignment(l1, nlslot1);
		schedule.addAssignment(b1, nlslot2);

		assertFalse(Constr.checkUnwanted(schedule));
		assertFalse(schedule.isValid());
		
		// invalid
		schedule = new Schedule(ls, nls, courses);
		schedule.addAssignment(l1, lslot2);
		schedule.addAssignment(b1, lslot1);

		assertFalse(Constr.checkUnwanted(schedule));
		assertFalse(schedule.isValid());
		
		// invalid
		schedule = new Schedule(ls, nls, courses);
		schedule.addAssignment(l1, nlslot2);
		schedule.addAssignment(b1, nlslot2);

		assertFalse(Constr.checkUnwanted(schedule));
		assertFalse(schedule.isValid());
	}

	/**
	 * 
	 */
	@Test
	public void testCheckEveningClasses() {
		clearLists();
		
		// make slots
		LectureSlot lslot1 = new LectureSlot("MO", 8, 0, 9, 0, 2, 0, false);
		ls.add(lslot1);
		LectureSlot lslot2 = new LectureSlot("MO", 18, 0, 19, 0, 2, 0, false);
		ls.add(lslot2);
		NonLectureSlot nlslot1 = new NonLectureSlot("TU", 8, 0, 9, 30, 2, 0, false);
		nls.add(nlslot1);
		NonLectureSlot nlslot2 = new NonLectureSlot("TU", 18, 0, 19, 30, 2, 0, false);
		nls.add(nlslot2);
		
		// make courses
		Course c1 = new Course("ABCD", "01", "99", true);
		Section s1 = new Section(c1, "99", true);
		Lecture l1 = s1.getLecture();
		Lab b1 = new Lab("99", s1, true);
		s1.addLab(b1);
		courses.add(c1);
		
		Course c2 = new Course("ABCD", "01", "01", false);
		Section s2 = new Section(c2, "01", false);
		Lecture l2 = s2.getLecture();
		Lab b2 = new Lab("01", s2, false);
		s1.addLab(b2);
		courses.add(c2);
		
		// valid
		Schedule schedule = new Schedule(ls, nls, courses);
		schedule.addAssignment(l1, lslot2);
		schedule.addAssignment(b1, nlslot2);
		schedule.addAssignment(l2, lslot1);
		schedule.addAssignment(b2, nlslot1);

		assertTrue(Constr.checkEveningClasses(schedule));
		assertTrue(schedule.isValid());
		
		// valid
		schedule = new Schedule(ls, nls, courses);
		schedule.addAssignment(l1, lslot2);
		schedule.addAssignment(b1, nlslot2);
		schedule.addAssignment(l2, lslot2);
		schedule.addAssignment(b2, nlslot2);

		assertTrue(Constr.checkEveningClasses(schedule));
		assertTrue(schedule.isValid());
		
		// invalid
		schedule = new Schedule(ls, nls, courses);
		schedule.addAssignment(l1, lslot1);
		schedule.addAssignment(b1, nlslot1);
		schedule.addAssignment(l2, lslot1);
		schedule.addAssignment(b2, nlslot1);

		assertFalse(Constr.checkEveningClasses(schedule));
		assertFalse(schedule.isValid());
		
		// invalid
		schedule = new Schedule(ls, nls, courses);
		schedule.addAssignment(l1, lslot1);
		schedule.addAssignment(b1, nlslot1);
		schedule.addAssignment(l2, lslot2);
		schedule.addAssignment(b2, nlslot2);

		assertFalse(Constr.checkEveningClasses(schedule));
		assertFalse(schedule.isValid());
	}

	/**
	 * 
	 */
	@Test
	public void testCheckOver500Classes() {
		clearLists();
		
		// make slots
		LectureSlot lslot1 = new LectureSlot("MO", 8, 0, 9, 0, 5, 0, false);
		ls.add(lslot1);
		LectureSlot lslot2 = new LectureSlot("TU", 8, 0, 9, 30, 5, 0, false);
		ls.add(lslot2);

		// make courses
		Course c1 = new Course("CPSC", "500", "01", false);
		Section s1 = new Section(c1, "01", false);
		Lecture l1 = s1.getLecture();
		courses.add(c1);
		
		Course c2 = new Course("CPSC", "501", "01", false);
		Section s2 = new Section(c2, "01", false);
		Lecture l2 = s2.getLecture();
		courses.add(c2);
		
		// valid
		Schedule schedule = new Schedule(ls, nls, courses);
		schedule.addAssignment(l1, lslot1);
		schedule.addAssignment(l2, lslot2);

		assertTrue(Constr.checkOver500Classes(schedule));
		assertTrue(schedule.isValid());
		
		// valid
		schedule = new Schedule(ls, nls, courses);
		schedule.addAssignment(l1, lslot2);
		schedule.addAssignment(l2, lslot1);

		assertTrue(Constr.checkOver500Classes(schedule));
		assertTrue(schedule.isValid());
		
		// invalid
		schedule = new Schedule(ls, nls, courses);
		schedule.addAssignment(l1, lslot1);
		schedule.addAssignment(l2, lslot1);

		assertFalse(Constr.checkOver500Classes(schedule));
		assertFalse(schedule.isValid());
		
		// invalid
		schedule = new Schedule(ls, nls, courses);
		schedule.addAssignment(l1, lslot2);
		schedule.addAssignment(l2, lslot2);

		assertFalse(Constr.checkOver500Classes(schedule));
		assertFalse(schedule.isValid());
	}

	/**
	 * 
	 */
	@Test
	public void testCheckSpecificTimes() {
		clearLists();

		// make slots
		LectureSlot lslot1 = new LectureSlot("MO", 8, 0, 9, 0, 2, 0, false);
		ls.add(lslot1);
		LectureSlot lslot2 = new LectureSlot("TU", 11, 0, 12, 30, 2, 0, false);
		ls.add(lslot2);
		
		// make course
		Course c = new Course("ABCD", "01", "01", false);
		Section s = new Section(c, "01", false);
		Lecture l1 = s.getLecture();
		
		// valid
		Schedule schedule = new Schedule(ls, nls, courses);
		schedule.addAssignment(l1, lslot1);

		assertTrue(Constr.checkSpecificTimes(schedule));
		assertTrue(schedule.isValid());
		
		// invalid
		schedule = new Schedule(ls, nls, courses);
		schedule.addAssignment(l1, lslot2);

		assertFalse(Constr.checkSpecificTimes(schedule));
		assertFalse(schedule.isValid());
	}

	/**
	 * 
	 */
	@Test
	public void testCheckSpecialClasses() {
		clearLists();
		
		// make slots
		LectureSlot lslot1 = new LectureSlot("MO", 8, 0, 9, 0, 4, 0, false);
		ls.add(lslot1);
		LectureSlot lslot2 = new LectureSlot("TU", 18, 0, 19, 30, 4, 0, false);
		ls.add(lslot2);
		NonLectureSlot nlslot1 = new NonLectureSlot("TU", 8, 0, 9, 30, 4, 0, false);
		nls.add(nlslot1);
		NonLectureSlot nlslot2 = new NonLectureSlot("MO", 18, 0, 19, 0, 4, 0, false);
		nls.add(nlslot2);
		
		// make 313
		Course c313 = new Course("CPSC", "313", "01", false);
		Section s313 = new Section(c313, "01", false);
		Lecture l313 = s313.getLecture();
		Lab b313 = new Lab("01", s313, false);
		s313.addLab(b313);
		Tutorial t313 = new Tutorial("02", s313, false);
		s313.addTutorial(t313);
		courses.add(c313);
		
		// make 413
		Course c413 = new Course("CPSC", "413", "01", false);
		Section s413 = new Section(c413, "01", false);
		Lecture l413 = s413.getLecture();
		Lab b413 = new Lab("01", s413, false);
		s413.addLab(b413);
		Tutorial t413 = new Tutorial("02", s413, false);
		s413.addTutorial(t413);
		courses.add(c413);
		
		// make 813
		Course c813 = new Course("CPSC", "813", "01", false);
		Section s813 = new Section(c813, "01", false);
		Lecture l813 = s813.getLecture();
		Lab b813 = new Lab("01", s813, false);
		s813.addLab(b813);
		Tutorial t813 = new Tutorial("02", s813, false);
		s813.addTutorial(t813);
		courses.add(c813);
		
		// make 913
		Course c913 = new Course("CPSC", "913", "01", false);
		Section s913 = new Section(c913, "01", false);
		Lecture l913 = s913.getLecture();
		Lab b913 = new Lab("01", s913, false);
		s913.addLab(b913);
		Tutorial t913 = new Tutorial("02", s913, false);
		s913.addTutorial(t913);
		courses.add(c913);
		
		// valid
		Schedule schedule = new Schedule(ls, nls, courses);
		schedule.addAssignment(l313, lslot1);
		schedule.addAssignment(l413, lslot1);
		schedule.addAssignment(l813, lslot2);
		schedule.addAssignment(l913, lslot2);
		schedule.addAssignment(b313, nlslot1);
		schedule.addAssignment(b413, nlslot1);
		schedule.addAssignment(b813, nlslot2);
		schedule.addAssignment(b913, nlslot2);
		schedule.addAssignment(t313, nlslot1);
		schedule.addAssignment(t413, nlslot1);
		schedule.addAssignment(t813, nlslot2);
		schedule.addAssignment(t913, nlslot2);

		assertTrue(Constr.checkSpecialClasses(schedule));
		assertTrue(schedule.isValid());
		
		// invalid
		schedule = new Schedule(ls, nls, courses);
		schedule.addAssignment(l313, lslot1);
		schedule.addAssignment(l413, lslot2);
		schedule.addAssignment(l813, lslot1);
		schedule.addAssignment(l913, lslot2);
		schedule.addAssignment(b313, nlslot1);
		schedule.addAssignment(b413, nlslot1);
		schedule.addAssignment(b813, nlslot2);
		schedule.addAssignment(b913, nlslot2);
		schedule.addAssignment(t313, nlslot1);
		schedule.addAssignment(t413, nlslot1);
		schedule.addAssignment(t813, nlslot2);
		schedule.addAssignment(t913, nlslot2);

		assertFalse(Constr.checkSpecialClasses(schedule));
		assertFalse(schedule.isValid());
		
		// invalid
		schedule = new Schedule(ls, nls, courses);
		schedule.addAssignment(l313, lslot2);
		schedule.addAssignment(l413, lslot1);
		schedule.addAssignment(l813, lslot2);
		schedule.addAssignment(l913, lslot1);
		schedule.addAssignment(b313, nlslot1);
		schedule.addAssignment(b413, nlslot1);
		schedule.addAssignment(b813, nlslot2);
		schedule.addAssignment(b913, nlslot2);
		schedule.addAssignment(t313, nlslot1);
		schedule.addAssignment(t413, nlslot1);
		schedule.addAssignment(t813, nlslot2);
		schedule.addAssignment(t913, nlslot2);

		assertFalse(Constr.checkSpecialClasses(schedule));
		assertFalse(schedule.isValid());
		
		// invalid
		schedule = new Schedule(ls, nls, courses);
		schedule.addAssignment(l313, lslot2);
		schedule.addAssignment(l413, lslot2);
		schedule.addAssignment(l813, lslot1);
		schedule.addAssignment(l913, lslot1);
		schedule.addAssignment(b313, nlslot1);
		schedule.addAssignment(b413, nlslot1);
		schedule.addAssignment(b813, nlslot2);
		schedule.addAssignment(b913, nlslot2);
		schedule.addAssignment(t313, nlslot1);
		schedule.addAssignment(t413, nlslot1);
		schedule.addAssignment(t813, nlslot2);
		schedule.addAssignment(t913, nlslot2);

		assertFalse(Constr.checkSpecialClasses(schedule));
		assertFalse(schedule.isValid());
		
		// TODO more conditions
	}

	/**
	 * 
	 */
	private void clearLists() {
		ls.clear();
		nls.clear();
		courses.clear();
	}
}
