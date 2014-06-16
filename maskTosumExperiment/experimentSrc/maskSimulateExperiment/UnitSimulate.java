package maskSimulateExperiment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import newMaskAlgorithms.FIC_MASK_NEWLY;
import newMaskAlgorithms.OFOT_MASK_NEWLY;
import maskAlogrithms.CTA;
import maskAlogrithms.FIC;
import maskAlogrithms.SOFOT;

import com.fc.caseRunner.CaseRunner;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.testObject.TestSuite;
import com.fc.testObject.TestSuiteImplement;
import com.fc.tuple.Tuple;

public class UnitSimulate {

	// public CaseRunner getCaseRunner() {
	//
	// CaseRunner caseRuner = null;
	//
	// return null;
	// }

	public void testTraditional(int[] param, TestCase wrongCase,
			CaseRunner runner) throws Exception {
		FIC fic = new FIC(wrongCase, param, runner);
		fic.FicNOP();
		System.out.println("non Mask runner");
		for (Tuple tuple : fic.getBugs())
			System.out.println(tuple.toString());

		SOFOT ofot = new SOFOT();
		ofot.process(wrongCase, param, runner);
		System.out.println("no Mask ofot");
		for (Tuple tuple : ofot.getBugs())
			System.out.println(tuple.toString());

		// SOFOT ofot = new SOFOT();
		// ofot.process(wrongCase, param, runner);

		TestSuite suite = new TestSuiteImplement();
		for (TestCase testCase : ofot.getExecuted())
			suite.addTest(testCase);

		String[] classes = new String[] { "0", "1" };

		CTA cta = new CTA();

		String[] state = new String[suite.getTestCaseNum()];
		for (int i = 0; i < state.length; i++) {
			int runresult = runner.runTestCase(suite.getAt(i)) == TestCase.PASSED ? 0
					: 1;
			state[i] = "" + runresult;
		}
		cta.process(param, classes, suite, state);
		System.out.println("no Mask cta");
		if (cta.getBugs(1) != null)
			for (Tuple tuple : cta.getBugs(1))
				System.out.println(tuple.toString());
		// return cta.getBugs(1);
	}

	public void testAugment(int[] param, TestCase wrongCase,
			BasicRunner runner, int code) throws Exception {

		FIC_MASK_NEWLY ficmasknew = new FIC_MASK_NEWLY(wrongCase, param,
				runner, code);
		ficmasknew.FicNOP();
		System.out.println("Mask fic");
		for (Tuple tuple : ficmasknew.getBugs())
			System.out.println(tuple.toString());

		OFOT_MASK_NEWLY ofot = new OFOT_MASK_NEWLY(wrongCase, param, runner,
				code);
		ofot.process();

		System.out.println("Mask ofot:");
		for (Tuple tuple : ofot.getBugs()) {
			System.out.println(tuple.toString());
		}

		TestSuite suite = new TestSuiteImplement();
		for (TestCase testCase : ofot.getExecuted()) {
			if (testCase.testDescription() == 0
					|| testCase.testDescription() == code)
				suite.addTest(testCase);
		}
		String[] classes = new String[] { "0", "1" };
		CTA cta = new CTA();
		String[] state = new String[suite.getTestCaseNum()];
		for (int i = 0; i < state.length; i++) {

			state[i] = "" + (suite.getAt(i).testDescription() == 0 ? 0 : 1);
			// System.out.println(runresult);
		}
		cta.process(param, classes, suite, state);

		System.out.println("Mask cta:");
		if (cta.getBugs(1) != null)
			for (Tuple tuple : cta.getBugs(1))
				System.out.println(tuple.toString());

		// FIC_MASK ficmask = new FIC_MASK(wrongCase, param, runner, 1);
		// ficmask.FicNOP();
		// System.out.println(" Mask runner");
		// for (Tuple tuple : ficmask.getBugs())
		// System.out.println(tuple.toString());
		//
		// OFOT_MASK ofotM = new OFOT_MASK();
		// ofotM.process(wrongCase, param, runner);
		// System.out.println("Mask ofot");
		// for (Tuple tuple : ofotM.getBugs())
		// System.out.println(tuple.toString());
		//
		// TestSuite suite = new TestSuiteImplement();
		// for (TestCase testCase : ofotM.getExecuted())
		// suite.addTest(testCase);
		// String[] classes = new String[] { "0", "1" };
		// CTA cta = new CTA();
		// String[] state = new String[suite.getTestCaseNum()];
		// for (int i = 0; i < state.length; i++) {
		// int runresult = runner.runTestCase(suite.getAt(i)) == TestCase.FAILED
		// ? 1
		// : 0;
		// state[i] = "" + runresult;
		// // System.out.println(runresult);
		// }
		// cta.process(param, classes, suite, state);
		//
		// for (Tuple tuple : cta.getBugs(1))
		// System.out.println(tuple.toString());
	}

	public void testScenoria() throws Exception {
		int[] param = new int[] { 3, 3, 3, 3, 3 };

		int[] wrong = new int[] { 1, 1, 1, 1, 1 };

		TestCase wrongCase = new TestCaseImplement();
		wrongCase.setTestState(TestCase.FAILED);
		((TestCaseImplement) wrongCase).setTestCase(wrong);

		int[] wrong2 = new int[] { 2, 2, 2, 2, 2 };

		TestCase wrongCase2 = new TestCaseImplement();
		wrongCase2.setTestState(TestCase.FAILED);
		((TestCaseImplement) wrongCase2).setTestCase(wrong2);

		Tuple bug1 = new Tuple(2, wrongCase);
		bug1.set(0, 0);
		bug1.set(1, 1);

		List<Tuple> bugs1 = new ArrayList<Tuple>();
		bugs1.add(bug1);

		Tuple bug2 = new Tuple(2, wrongCase2);
		bug2.set(0, 3);
		bug2.set(1, 4);

		List<Tuple> bugs2 = new ArrayList<Tuple>();
		bugs2.add(bug2);

		List<Integer> priority1 = new ArrayList<Integer>();
		priority1.add(2);
		List<Integer> priority2 = new ArrayList<Integer>();

		HashMap<Integer, List<Tuple>> bugs = new HashMap<Integer, List<Tuple>>();
		bugs.put(1, bugs1);
		bugs.put(2, bugs2);

		HashMap<Integer, List<Integer>> priority = new HashMap<Integer, List<Integer>>();

		priority.put(1, priority1);
		priority.put(2, priority2);

		BasicRunner basicRunner = new BasicRunner(priority, bugs);

		ExperiementData exData = new ExperiementData();

		exData.setParam(param);
		exData.setHigherPriority(priority);
		exData.setBugs(bugs);

		for (Integer code : exData.getWrongCases().keySet()) {
			List<TestCase> wrongCases = exData.getWrongCases().get(code);
			for (TestCase testCase : wrongCases) {
				System.out.println("testCase: " + testCase.getStringOfTest());
				System.out.println("distinguish");
				this.testTraditional(param, testCase, new DistinguishRunner(
						basicRunner, code));
				System.out.println("ignore");
				this.testTraditional(param, testCase, new IgnoreRunner(
						basicRunner));

				System.out.println("mask");
				this.testAugment(param, testCase, basicRunner, code);
			}

		}

		// List<TestCase> wrongCases1 = exData.getWrongCases().get(1);
		//
		// this.testTraditional(param, wrongCases1.get(0), new
		// DistinguishRunner(
		// basicRunner, 1));

	}

	public static void main(String[] args) throws Exception {
		UnitSimulate uS = new UnitSimulate();
		uS.testScenoria();
	}
}
