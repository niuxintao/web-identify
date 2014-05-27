package caseStudy;

import maskAlogrithms.FIC;
//import maskAlogrithms.OFOT_MASK;
//import maskAlogrithms.SOFOT;
import maskSimulateExperiment.*;

import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

public class SimluateScenairo {

	public void testScenoria() {

		int[] param = new int[] { 4, 4, 4, 4, 4, 4, 4, 4 };

		int[] wrong = new int[] { 1, 1, 1, 1, 1, 1, 1, 1 };

		TestCase wrongCase = new TestCaseImplement();
		wrongCase.setTestState(TestCase.FAILED);
		((TestCaseImplement) wrongCase).setTestCase(wrong);

		int[] wrong2 = new int[] { 2, 2, 2, 2, 2, 2, 2, 2 };

		TestCase wrongCase2 = new TestCaseImplement();
		wrongCase2.setTestState(TestCase.FAILED);
		((TestCaseImplement) wrongCase2).setTestCase(wrong2);

		int[] wrong3 = new int[] { 3, 3, 3, 3, 3, 3, 3, 3 };

		TestCase wrongCase3 = new TestCaseImplement();
		wrongCase3.setTestState(TestCase.FAILED);
		((TestCaseImplement) wrongCase3).setTestCase(wrong3);

		Tuple bug1 = new Tuple(2, wrongCase);
		bug1.set(0, 0);
		bug1.set(1, 1);

		Tuple bug2 = new Tuple(1, wrongCase2);
		bug2.set(0, 3);

		Tuple bug4 = new Tuple(2, wrongCase2);
		bug4.set(0, 6);
		bug4.set(1, 7);

		Tuple bug3 = new Tuple(1, wrongCase3);
		bug3.set(0, 3);

		Tuple bug5 = new Tuple(2, wrongCase3);
		bug5.set(0, 5);
		bug5.set(1, 6);

		BasicRunnerInMask runner = new BasicRunnerInMask();
		runner.inject(bug1, 1);
		runner.inject(bug2, 2);
		runner.inject(bug3, 3);
		runner.inject(bug4, 3);
		runner.inject(bug5, 2);

		MaskRunner maskRunner = new MaskRunner(runner, 1);

		NonMaskRunner nonMaskRunner = new NonMaskRunner(runner, 1);
		FIC fic = new FIC(wrongCase, param, nonMaskRunner);
		fic.FicNOP();
		System.out.println("non Mask runner");
		for (Tuple tuple : fic.getBugs())
			System.out.println(tuple.toString());

		FIC_MASK ficmask = new FIC_MASK(wrongCase, param, maskRunner, 1);
		ficmask.FicNOP();
		System.out.println(" Mask runner");
		for (Tuple tuple : ficmask.getBugs())
			System.out.println(tuple.toString());

		// ((CaseRunnerWithBugInject) caseRunner).inject(bugModel2);
		// ((CaseRunnerWithBugInject) caseRunner).inject(bugModel3);

		FIC_MASK_NEWLY ficmasknew = new FIC_MASK_NEWLY(wrongCase, param,
				runner, 1, new int[] { 2, 3 });
		ficmasknew.FicNOP();
		System.out.println(" Mask runner");
		for (Tuple tuple : ficmasknew.getBugs())
			System.out.println(tuple.toString());

		// List<TestCase> executed = ficmask.getExecuted();
		// for(TestCase testCase : executed)
		// System.out.println(testCase.getStringOfTest());

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
		SimluateScenairo sc = new SimluateScenairo();
		sc.testScenoria();
	}

}
