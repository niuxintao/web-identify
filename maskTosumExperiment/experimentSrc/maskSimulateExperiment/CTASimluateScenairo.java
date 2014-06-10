package maskSimulateExperiment;

import java.util.List;

import maskTool.MaskANProcess;

//import com.fc.caseRunner.CaseRunner;
import com.fc.coveringArray.DataCenter;
import com.fc.coveringArray.Process;
import com.fc.model.CTA;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.testObject.TestSuite;
import com.fc.testObject.TestSuiteImplement;
import com.fc.tuple.Tuple;

public class CTASimluateScenairo {

	public void testCTA(int[] param, int degree, BasicRunnerInMask runner)
			throws Exception {
		DataCenter.init(param, degree);
		Process t = new Process(2, 0.9998);
		t.process();
		TestSuite suite = new TestSuiteImplement();
		String[] state = new String[t.rsTable.length];

		int i = 0;
		for (int[] row : t.rsTable) {
			TestCaseImplement testCase = new TestCaseImplement();
			testCase.setTestCase(row);
			int runresult = runner.runTestCase(testCase);
			suite.addTest(testCase);
			state[i] = "" + runresult;
			// System.out.println(state[i]);
			i++;
		}

		CTA cta = new CTA();
		String[] classes = { "0", "1", "2" };

		cta.process(param, classes, suite, state);
		List<Tuple> bugs = cta.getBugs();
		for (Tuple tuple : bugs)
			System.out.println(tuple.toString());

		String tree = cta.getTree();
		System.out.println(tree);

	}

	public void testCTAMask(int[] param, int degree, BasicRunnerInMask runner,
			int wrongCode) throws Exception {
		DataCenter.init(param, degree);
		MaskRunner maskRunner = new MaskRunner(runner, wrongCode);
		MaskANProcess t = new MaskANProcess(2, 0.9998, maskRunner);
		t.process();
		TestSuite suite = new TestSuiteImplement();
		String[] state = new String[t.rsTable.length];

		int i = 0;
		for (int[] row : t.rsTable) {
			TestCaseImplement testCase = new TestCaseImplement();
			testCase.setTestCase(row);
			int runresult = maskRunner.runTestCase(testCase);
			suite.addTest(testCase);
			state[i] = "" + runresult;
			i++;
		}

		CTA cta = new CTA();
		String[] classes = { "0", "1" };

		cta.process(param, classes, suite, state);
		List<Tuple> bugs = cta.getBugs();
		for (Tuple tuple : bugs)
			System.out.println(tuple.toString());

		String tree = cta.getTree();
		System.out.println(tree);

	}

	public void testScenoria() throws Exception {

		int[] param = new int[] { 3, 3, 3, 3, 3, 3, 3, 3 };

		int[] wrong = new int[] { 1, 1, 1, 1, 1, 1, 1, 1 };

		TestCase wrongCase = new TestCaseImplement();
		wrongCase.setTestState(TestCase.FAILED);
		((TestCaseImplement) wrongCase).setTestCase(wrong);

		int[] wrong2 = new int[] { 2, 2, 2, 2, 2, 2, 2, 2 };

		TestCase wrongCase2 = new TestCaseImplement();
		wrongCase2.setTestState(TestCase.FAILED);
		((TestCaseImplement) wrongCase2).setTestCase(wrong2);

		Tuple bug1 = new Tuple(2, wrongCase);
		bug1.set(0, 0);
		bug1.set(1, 1);

		Tuple bug2 = new Tuple(1, wrongCase2);
		bug2.set(0, 3);

		BasicRunnerInMask runner = new BasicRunnerInMask();
		runner.inject(bug1, 1);
		runner.inject(bug2, 2);

		// MaskRunner maskRunner = new MaskRunner(runner, 1);

		// NonMaskRunner nonMaskRunner = new NonMaskRunner(runner, 1);
		// FIC fic = new FIC(wrongCase, param, nonMaskRunner);
		// fic.FicNOP();
		// System.out.println("non Mask runner");
		// for (Tuple tuple : fic.getBugs())
		// System.out.println(tuple.toString());

		testCTA(param, 2, runner);

		testCTAMask(param, 2, runner, 1);

		// FIC_MASK ficmask = new FIC_MASK(wrongCase, param, maskRunner, 1);
		// ficmask.FicNOP();
		// System.out.println(" Mask runner");
		// for (Tuple tuple : ficmask.getBugs())
		// System.out.println(tuple.toString());

	}

	public static void main(String[] args) throws Exception {
		CTASimluateScenairo sc = new CTASimluateScenairo();
		sc.testScenoria();
	}

}
