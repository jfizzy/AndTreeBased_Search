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
import Search.Eval;

/**
 * JUnit test class for Eval
 *
 */
public class EvalTests {
	
	ArrayList<LectureSlot> ls;
	ArrayList<NonLectureSlot> nls;
	ArrayList<Course> courses;
	ArrayList<Lecture> lecs;
	ArrayList<NonLecture> nonlecs;

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
		ls = new ArrayList<>();
		nls = new ArrayList<>();
		courses = new ArrayList<>();
		lecs = new ArrayList<>();
		nonlecs = new ArrayList<>();
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
	public void testGetCourseMinEval() {
		clearLists();
		
		// course min = 3
		// make slot
		LectureSlot slot = new LectureSlot("MO", 8, 0, 9, 0, 5, 3, false);
		ls.add(slot);
		
		// make course
		Course c = new Course("ABCD", "01", "01", false);
		Section s1 = c.getSections().get(0);
		Lecture l1 = s1.getLecture();
		Section s2 = new Section(c, "02", false);
		c.addSection(s2);
		Lecture l2 = s2.getLecture();
		Section s3 = new Section(c, "03", false);
		c.addSection(s3);
		Lecture l3 = s3.getLecture();
		Section s4 = new Section(c, "04", false);
		c.addSection(s4);
		Lecture l4 = s4.getLecture();
		courses.add(c);
		
		// 3 less than coursemin
		Schedule schedule = new Schedule(ls, nls, courses);
		
		Eval e = new Eval(schedule);
		assertEquals(e.getCourseMinEval(), 3);
		
		// 2 less than coursemin
		schedule = new Schedule(ls, nls, courses);
		schedule.updateAssignment(l1, slot);
		
		e = new Eval(schedule);
		assertEquals(2, e.getCourseMinEval());
		
		// 1 less than coursemin
		schedule = new Schedule(ls, nls, courses);
		schedule.updateAssignment(l1, slot);
		schedule.updateAssignment(l2, slot);
		
		e = new Eval(schedule);
		assertEquals(1, e.getCourseMinEval());
		
		// 0 less than coursemin
		schedule = new Schedule(ls, nls, courses);
		schedule.updateAssignment(l1, slot);
		schedule.updateAssignment(l2, slot);
		schedule.updateAssignment(l3, slot);
		
		e = new Eval(schedule);
		assertEquals(0, e.getCourseMinEval());
		
		// 1 more than coursemin
		schedule = new Schedule(ls, nls, courses);
		schedule.updateAssignment(l1, slot);
		schedule.updateAssignment(l2, slot);
		schedule.updateAssignment(l3, slot);
		schedule.updateAssignment(l4, slot);
		
		e = new Eval(schedule);
		assertEquals(0, e.getCourseMinEval());
	}

	/**
	 * 
	 */
	@Test
	public void testGetLabMinEval() {
		clearLists();
		
		// lab min = 3
		// make slot
		NonLectureSlot slot = new NonLectureSlot("MO", 8, 0, 9, 0, 5, 3, false);
		nls.add(slot);
		
		// make course
		Course c = new Course("ABCD", "01", "01", false);
		Section s1 = new Section(c, "01", false);
		c.addSection(s1);
		Lab b1 = new Lab("01", s1, false);
		s1.addLab(b1);
		Section s2 = new Section(c, "02", false);
		c.addSection(s2);
		Lab b2 = new Lab("02", s2, false);
		s1.addLab(b2);
		Section s3 = new Section(c, "03", false);
		c.addSection(s3);
		Lab b3 = new Lab("03", s3, false);
		s1.addLab(b3);
		Section s4 = new Section(c, "04", false);
		c.addSection(s4);
		Lab b4 = new Lab("04", s4, false);
		s1.addLab(b4);
		courses.add(c);
		
		// 3 less than labmin
		Schedule schedule = new Schedule(ls, nls, courses);
		
		Eval e = new Eval(schedule);
		assertEquals(3, e.getLabMinEval());
		
		// 2 less than labmin
		schedule = new Schedule(ls, nls, courses);
		schedule.updateAssignment(b1, slot);
		
		e = new Eval(schedule);
		assertEquals(2, e.getLabMinEval());
		
		// 1 less than labmin
		schedule = new Schedule(ls, nls, courses);
		schedule.updateAssignment(b1, slot);
		schedule.updateAssignment(b2, slot);
		
		e = new Eval(schedule);
		assertEquals(1, e.getLabMinEval());
		
		// 0 less than labmin
		schedule = new Schedule(ls, nls, courses);
		schedule.updateAssignment(b1, slot);
		schedule.updateAssignment(b2, slot);
		schedule.updateAssignment(b3, slot);
		
		e = new Eval(schedule);
		assertEquals(0, e.getLabMinEval());
		
		// 1 more than labmin
		schedule = new Schedule(ls, nls, courses);
		schedule.updateAssignment(b1, slot);
		schedule.updateAssignment(b2, slot);
		schedule.updateAssignment(b3, slot);
		schedule.updateAssignment(b4, slot);
		
		e = new Eval(schedule);
		assertEquals(0, e.getLabMinEval());
	}

	/**
	 * 
	 */
	@Test
	public void testGetPrefEval() {
		clearLists();
		
		// make slots
		LectureSlot lslot1 = new LectureSlot("MO", 8, 0, 9, 0, 5, 3, false);
		ls.add(lslot1);
		NonLectureSlot nlslot1 = new NonLectureSlot("MO", 8, 0, 9, 0, 5, 3, false);
		nls.add(nlslot1);
		LectureSlot lslot2 = new LectureSlot("MO", 18, 0, 19, 0, 5, 3, false);
		ls.add(lslot2);
		NonLectureSlot nlslot2 = new NonLectureSlot("MO", 18, 0, 19, 0, 5, 3, false);
		nls.add(nlslot2);
		
		// make course
		Course c = new Course("ABCD", "01", "01", false);
		Section s1 = new Section(c, "01", false);
		c.addSection(s1);
		Lecture l1 = s1.getLecture();
		l1.addPreference(lslot2, 100);
		Lab b1 = new Lab("01", s1, false);
		b1.addPreference(nlslot2, 11);
		s1.addLab(b1);
		courses.add(c);
		
		// penalty = 0
		Schedule schedule = new Schedule(ls, nls, courses);
		schedule.updateAssignment(l1, lslot2);
		schedule.updateAssignment(b1, nlslot2);
		
		Eval e = new Eval(schedule);
		assertEquals(0, e.getPrefEval());
		
		// penalty = 100
		schedule = new Schedule(ls, nls, courses);
		schedule.updateAssignment(l1, lslot1);
		schedule.updateAssignment(b1, nlslot2);
		
		e = new Eval(schedule);
		assertEquals(100, e.getPrefEval());
		
		// penalty = 11
		schedule = new Schedule(ls, nls, courses);
		schedule.updateAssignment(l1, lslot2);
		schedule.updateAssignment(b1, nlslot1);
		
		e = new Eval(schedule);
		assertEquals(11, e.getPrefEval());
		
		// penalty = 111
		schedule = new Schedule(ls, nls, courses);
		schedule.updateAssignment(l1, lslot1);
		schedule.updateAssignment(b1, nlslot1);
		
		e = new Eval(schedule);
		assertEquals(111, e.getPrefEval());
	}

	/**
	 * 
	 */
	@Test
	public void testGetPairEval() {
		clearLists();
		
		// make slots
		LectureSlot lslot1 = new LectureSlot("MO", 8, 0, 9, 0, 5, 3, false);
		ls.add(lslot1);
		NonLectureSlot nlslot1 = new NonLectureSlot("MO", 8, 0, 9, 0, 5, 3, false);
		nls.add(nlslot1);
		LectureSlot lslot2 = new LectureSlot("MO", 18, 0, 19, 0, 5, 3, false);
		ls.add(lslot2);
		NonLectureSlot nlslot2 = new NonLectureSlot("MO", 18, 0, 19, 0, 5, 3, false);
		nls.add(nlslot2);
		
		// make course
		Course c1 = new Course("ABCD", "01", "01", false);
		Section s1 = new Section(c1, "01", false);
		Lecture l1 = s1.getLecture();
		c1.addSection(s1);
		Course c2 = new Course("EFGH", "01", "01", false);
		Section s2 = new Section(c2, "01", false);
		Lecture l2 = s2.getLecture();
		c2.addSection(s2);
		l1.addPaired(l2);
		l2.addPaired(l1);
		Lab b1 = new Lab("01", s1, false);
		Lab b2 = new Lab("02", s2, false);
		b1.addPaired(b2);
		b2.addPaired(b1);
		s1.addLab(b1);
		s1.addLab(b2);
		courses.add(c1);
		courses.add(c2);
		
		// penalty = 0
		Schedule schedule = new Schedule(ls, nls, courses);
		schedule.updateAssignment(l1, lslot1);
		schedule.updateAssignment(l2, lslot1);
		schedule.updateAssignment(b1, nlslot1);
		schedule.updateAssignment(b2, nlslot1);
		schedule.addPair(l1, l2);
		schedule.addPair(b1, b2);
		
		Eval e = new Eval(schedule);
		assertEquals(0, e.getPairEval());
		
		// penalty = 1
		schedule = new Schedule(ls, nls, courses);
		schedule.updateAssignment(l1, lslot1);
		schedule.updateAssignment(l2, lslot2);
		schedule.updateAssignment(b1, nlslot1);
		schedule.updateAssignment(b2, nlslot1);
		schedule.addPair(l1, l2);
		schedule.addPair(b1, b2);
		
		e = new Eval(schedule);
		assertEquals(1, e.getPairEval());
		
		// penalty = 1
		schedule = new Schedule(ls, nls, courses);
		schedule.updateAssignment(l1, lslot2);
		schedule.updateAssignment(l2, lslot2);
		schedule.updateAssignment(b1, nlslot2);
		schedule.updateAssignment(b2, nlslot1);
		schedule.addPair(l1, l2);
		schedule.addPair(b1, b2);
		
		e = new Eval(schedule);
		assertEquals(1, e.getPairEval());
		
		// penalty = 2
		schedule = new Schedule(ls, nls, courses);
		schedule.updateAssignment(l1, lslot2);
		schedule.updateAssignment(l2, lslot1);
		schedule.updateAssignment(b1, nlslot2);
		schedule.updateAssignment(b2, nlslot1);
		schedule.addPair(l1, l2);
		schedule.addPair(b1, b2);
		
		e = new Eval(schedule);
		assertEquals(2, e.getPairEval());
	}

	/**
	 * 
	 */
	@Test
	public void testGetSecDiffEval() {
		clearLists();
		
		// make slots
		LectureSlot lslot1 = new LectureSlot("MO", 8, 0, 9, 0, 5, 3, false);
		ls.add(lslot1);
		LectureSlot lslot2 = new LectureSlot("TU", 8, 0, 9, 30, 5, 3, false);
		ls.add(lslot2);
		LectureSlot lslot3 = new LectureSlot("TU", 18, 0, 19, 30, 5, 3, false);
		ls.add(lslot3);
		
		// make course
		Course c1 = new Course("ABCD", "01", "01", false);
		Section s1 = new Section(c1, "01", false);
		Lecture l1 = s1.getLecture();
		c1.addSection(s1);
		Section s2 = new Section(c1, "02", false);
		Lecture l2 = s2.getLecture();
		c1.addSection(s2);
		Section s3 = new Section(c1, "03", false);
		Lecture l3 = s3.getLecture();
		c1.addSection(s3);
		courses.add(c1);
		
		// penalty = 0
		Schedule schedule = new Schedule(ls, nls, courses);
		schedule.updateAssignment(l1, lslot1);
		schedule.updateAssignment(l2, lslot2);
		schedule.updateAssignment(l3, lslot3);
		
		Eval e = new Eval(schedule);
		assertEquals(0, e.getSecDiffEval());
		
		// penalty = 1
		schedule = new Schedule(ls, nls, courses);
		schedule.updateAssignment(l1, lslot1);
		schedule.updateAssignment(l2, lslot1);
		schedule.updateAssignment(l3, lslot3);
		
		e = new Eval(schedule);
		assertEquals(1, e.getSecDiffEval());
		
		// penalty = 3
		schedule = new Schedule(ls, nls, courses);
		schedule.updateAssignment(l1, lslot2);
		schedule.updateAssignment(l2, lslot2);
		schedule.updateAssignment(l3, lslot2);
		
		e = new Eval(schedule);
		assertEquals(3, e.getSecDiffEval());
	}

	/**
	 * 
	 */
	private void clearLists() {
		ls.clear();
		nls.clear();
		courses.clear();
		lecs.clear();
		nonlecs.clear();
	}
}
