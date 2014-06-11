package maskSimulateExperiment;

import maskAlogrithms.CTA;
import maskAlogrithms.FIC;
import maskAlogrithms.FIC_MASK;
import maskAlogrithms.OFOT_MASK;
import maskAlogrithms.SOFOT;

import com.fc.caseRunner.CaseRunner;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestSuite;
import com.fc.testObject.TestSuiteImplement;
import com.fc.tuple.Tuple;

public class UnitSimulate {

//	public CaseRunner getCaseRunner() {
//
//		CaseRunner caseRuner = null;
//
//		return null;
//	}

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

		for (Tuple tuple : cta.getBugs(1))
			System.out.println(tuple.toString());
		// return cta.getBugs(1);
	}

	public void testAugment(int[] param, TestCase wrongCase, CaseRunner runner)
			throws Exception {
		FIC_MASK ficmask = new FIC_MASK(wrongCase, param, runner, 1);
		ficmask.FicNOP();
		System.out.println(" Mask runner");
		for (Tuple tuple : ficmask.getBugs())
			System.out.println(tuple.toString());

		OFOT_MASK ofotM = new OFOT_MASK();
		ofotM.process(wrongCase, param, runner);
		System.out.println("Mask ofot");
		for (Tuple tuple : ofotM.getBugs())
			System.out.println(tuple.toString());

		TestSuite suite = new TestSuiteImplement();
		for (TestCase testCase : ofotM.getExecuted())
			suite.addTest(testCase);
		String[] classes = new String[] { "0", "1" };
		CTA cta = new CTA();
		String[] state = new String[suite.getTestCaseNum()];
		for (int i = 0; i < state.length; i++) {
			int runresult = runner.runTestCase(suite.getAt(i)) == TestCase.FAILED ? 1
					: 0;
			state[i] = "" + runresult;
			// System.out.println(runresult);
		}
		cta.process(param, classes, suite, state);

		for (Tuple tuple : cta.getBugs(1))
			System.out.println(tuple.toString());
	}

	public void testScenoria(int[] param, TestCase wrongCase, CaseRunner runner) {

		// int[] param = new int[] { 3, 3, 3, 3, 3, 3, 3, 3 };

		// int[] wrong = new int[] { 1, 1, 1, 1, 1, 1, 1, 1 };

		// TestCase wrongCase = new TestCaseImplement();
		// wrongCase.setTestState(TestCase.FAILED);
		// ((TestCaseImplement) wrongCase).setTestCase(wrong);
		//
		// int[] wrong2 = new int[] { 2, 2, 2, 2, 2, 2, 2, 2 };
		//
		// TestCase wrongCase2 = new TestCaseImplement();
		// wrongCase2.setTestState(TestCase.FAILED);
		// ((TestCaseImplement) wrongCase2).setTestCase(wrong2);
		//
		// Tuple bug1 = new Tuple(2, wrongCase);
		// bug1.set(0, 0);
		// bug1.set(1, 1);
		//
		// Tuple bug2 = new Tuple(1, wrongCase2);
		// bug2.set(0, 3);

		// BasicRunnerInMask runner = new BasicRunnerInMask();
		// runner.inject(bug1, 1);
		// runner.inject(bug2, 2);

		// MaskRunner maskRunner = new MaskRunner(runner, 1);
		//
		// NonMaskRunner nonMaskRunner = new NonMaskRunner(runner, 1);
		// FIC fic = new FIC(wrongCase, param, nonMaskRunner);
		// fic.FicNOP();
		// System.out.println("non Mask runner");
		// for (Tuple tuple : fic.getBugs())
		// System.out.println(tuple.toString());
		//
		// FIC_MASK ficmask = new FIC_MASK(wrongCase, param, maskRunner, 1);
		// ficmask.FicNOP();
		// System.out.println(" Mask runner");
		// for (Tuple tuple : ficmask.getBugs())
		// System.out.println(tuple.toString());
		//
		// OFOT_MASK ofotM = new OFOT_MASK();
		// ofotM.process(wrongCase, param, maskRunner);
		// System.out.println("Mask ofot");
		// for (Tuple tuple : ofotM.getBugs())
		// System.out.println(tuple.toString());
		//
		// SOFOT ofot = new SOFOT();
		// ofot.process(wrongCase, param, nonMaskRunner);
		// System.out.println("no Mask ofot");
		// for (Tuple tuple : ofot.getBugs())
		// System.out.println(tuple.toString());

	}

	public static void main(String[] args) {

	}
}
