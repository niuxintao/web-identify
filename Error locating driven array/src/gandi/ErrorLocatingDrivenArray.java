package gandi;

import interaction.CoveringManage;
import interaction.DataCenter;

import java.util.HashSet;
import java.util.List;

import com.fc.caseRunner.CaseRunner;
import com.fc.caseRunner.CaseRunnerWithBugInject;
//import com.fc.coveringArray.CoveringManage;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

import ct.AETG_Constraints;
import ct.SOFOT_Constriants;

public class ErrorLocatingDrivenArray {

	private CaseRunner caseRunner;

	private HashSet<TestCase> overallTestCases;

	private HashSet<Tuple> MFS;
	
	private DataCenter dataCenter;

	private CoveringManage cm;

	public HashSet<TestCase> getOverallTestCases() {
		return overallTestCases;
	}

	public HashSet<Tuple> getMFS() {
		return MFS;
	}

	public ErrorLocatingDrivenArray(DataCenter dataCenter, CaseRunner caseRunner) {
		this.caseRunner = caseRunner;
		overallTestCases = new HashSet<TestCase>();
		cm = new CoveringManage(dataCenter);
		MFS = new HashSet<Tuple>();
		this.dataCenter = dataCenter;
	}

	public void run() {

		AETG_Constraints ac = new AETG_Constraints(dataCenter);

		// coverage is equal to 0 is ending
		while (ac.unCovered > 0) {
			int[] test = ac.getNextTestCase();
			TestCase testCase = new TestCaseImplement(test);
			overallTestCases.add(testCase);
			// System.out.println("aetg" + testCase.getStringOfTest() +
			// " "+ac.unCovered);

			if (caseRunner.runTestCase(testCase) == TestCase.PASSED) {
				ac.unCovered = cm.setCover(ac.unCovered, ac.coveredMark, test);
			} else {

				SOFOT_Constriants sc = new SOFOT_Constriants(dataCenter, testCase, ac);
				// sc.process(testCase, DataCenter.param, caseRunner);

				while (!sc.isEnd()) {
					TestCase nextTestCase = sc.generateNext();
					overallTestCases.add(nextTestCase);
					// System.out.println("ofot" +
					// nextTestCase.getStringOfTest());

					int[] next = new int[nextTestCase.getLength()];
					for (int i = 0; i < next.length; i++) {
						next[i] = nextTestCase.getAt(i);
					}
					if (caseRunner.runTestCase(nextTestCase) == TestCase.PASSED) {
						ac.unCovered = cm.setCover(ac.unCovered,
								ac.coveredMark, next);
						nextTestCase.setTestState(TestCase.PASSED);
					} else
						nextTestCase.setTestState(TestCase.FAILED);
				}

				sc.analysis();
				List<Tuple> mfs = sc.getBugs();
				ac.addConstriants(mfs);
				this.MFS.addAll(mfs);
				// setCoverage(mfs);
			}

		}
	}

	public static void main(String[] args) {
		int[] wrong = new int[] { 1, 1, 1, 1, 1, 1, 1, 1 };
		TestCase wrongCase = new TestCaseImplement();
		((TestCaseImplement) wrongCase).setTestCase(wrong);

		int[] wrong2 = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		TestCase wrongCase2 = new TestCaseImplement();
		((TestCaseImplement) wrongCase2).setTestCase(wrong2);

		int[] param = new int[] { 3, 3, 3, 3, 3, 3, 3, 3 };

		DataCenter dataCenter = new DataCenter(param, 2);

		Tuple bugModel1 = new Tuple(2, wrongCase);
		bugModel1.set(0, 2);
		bugModel1.set(1, 5);

		Tuple bugModel2 = new Tuple(1, wrongCase2);
		bugModel2.set(0, 1);

		CaseRunner caseRunner = new CaseRunnerWithBugInject();
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel1);
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel2);

		ErrorLocatingDrivenArray elda = new ErrorLocatingDrivenArray(
				dataCenter, caseRunner);
		elda.run();

		System.out
				.println("testCase Num: " + elda.getOverallTestCases().size());
		for (TestCase testCase : elda.getOverallTestCases()) {
			System.out.println(testCase.getStringOfTest());
		}
		System.out.println("MFS");
		for (Tuple mfs : elda.getMFS())
			System.out.println(mfs.toString());

	}

}
