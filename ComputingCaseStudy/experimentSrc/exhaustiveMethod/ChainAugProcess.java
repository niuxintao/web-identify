package exhaustiveMethod;

import java.util.HashSet;
import java.util.List;

import com.fc.caseRunner.CaseRunner;
import com.fc.caseRunner.CaseRunnerWithBugInject;
import com.fc.model.TuplePool;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.testObject.TestSuite;
import com.fc.testObject.TestSuiteImplement;
import com.fc.tuple.Tuple;

public class ChainAugProcess {
	protected CaseRunner caseRunner;
	// protected CorpTupleWithTestCase generate;
	// protected TuplePool pool;
	// protected ChainAug workMachine;

	protected HashSet<Tuple> existedMFS;
	protected int[] param;

	public ChainAugProcess(int[] param, CaseRunner caseRunner) {
		this.caseRunner = caseRunner;
		this.existedMFS = new HashSet<Tuple>();
		this.param = param;
	}

	public List<Tuple> analysis(TestCase wrongCase) {
		//System.out.println("start");
		TuplePool pool = new TuplePool(wrongCase, existedMFS);
		CorpTupleWithTestCase generate = new CorpTupleWithTestCase(wrongCase,
				param);
		ChainAug workMachine = new ChainAug(pool, generate);
		character(workMachine);
		this.addBugs(pool.getExistedBugTuples());
		return pool.getExistedBugTuples();
	}

	public void addBugs(List<Tuple> tuples) {
		for (Tuple tuple : tuples)
			this.existedMFS.add(tuple);
	}

	public void anlysisAllTheWrongCases(List<TestCase> wrongCases) {
		for(TestCase testCase : wrongCases)
			this.analysis(testCase);

	}

	private void character(ChainAug workMachine) {
		while (true) {
			TestCase testcase = workMachine.genNextTest();
			if (testcase == null)
				break;

			//System.out.println(testcase.getStringOfTest());
			int state = caseRunner.runTestCase(testcase);
			if (state == TestCase.PASSED)
				workMachine.setLastTestCase(true);
			else
				workMachine.extraDealAfterFail();
			// workMachine
			// .setLastTestCase(caseRunner.runTestCase(testcase) ==
			// TestCase.PASSED);
		}
	}

	public void outputResult(List<Tuple> bugs) {
		System.out.println("begin");
		for (Tuple bug : bugs) {
			System.out.println(bug.toString());
		}
		System.out.println("done");

		// TestSuite extra = workMachine.getExtraCases();
		// System.out.println("all : " + extra.getTestCaseNum());
		// for (int i = 0; i < extra.getTestCaseNum(); i++) {
		// System.out.print(extra.getAt(i).getStringOfTest());
		// System.out
		// .println(extra.getAt(i).testDescription() == TestCase.PASSED ? "pass"
		// : "fail");
		// }
	}

	protected void init2() {
		int[] wrong = new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 1, 1 };

		int[] wrong2 = new int[] { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
				2, 2, 2, 2, 2, 2, 2, 2 };

		int[] pass = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0 };
		TestCase rightCase = new TestCaseImplement();
		((TestCaseImplement) rightCase).setTestCase(pass);
		TestCase wrongCase = new TestCaseImplement();
		((TestCaseImplement) wrongCase).setTestCase(wrong);

		TestCase wrongCase2 = new TestCaseImplement();
		((TestCaseImplement) wrongCase2).setTestCase(wrong2);

		TestSuite rightSuite = new TestSuiteImplement();
		rightSuite.addTest(rightCase);

		// int[] param = new int[] { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
		// 3,
		// 3, 3, 3, 3, 3, 3, 3, 3 };

		Tuple bugModel = new Tuple(3, wrongCase);
		bugModel.set(0, 1);
		bugModel.set(1, 2);
		bugModel.set(2, 4);

		Tuple bugModel2 = new Tuple(3, wrongCase2);
		bugModel2.set(0, 20);
		bugModel2.set(1, 21);
		bugModel2.set(2, 22);

		caseRunner = new CaseRunnerWithBugInject();
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel);
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel2);

		// pool = new TuplePool(wrongCase, rightSuite);

		// generate = new CorpTupleWithTestCase(wrongCase, param);
	}

	public static void main(String[] args) {
		int[] wrong = new int[12];
		int[] pass = new int[12];
		int[] param = new int[12];
		for (int i = 0; i < 12; i++) {
			wrong[i] = 1;
			pass[i] = 0;
			param[i] = 3;
		}

		TestCase wrongCase = new TestCaseImplement();
		((TestCaseImplement) wrongCase).setTestCase(wrong);

		Tuple bugModel = new Tuple(2, wrongCase);
		bugModel.set(0, 4);
		bugModel.set(1, 5);

		Tuple bugModel2 = new Tuple(2, wrongCase);
		bugModel2.set(0, 2);
		bugModel2.set(1, 3);

		CaseRunner caseRunner = new CaseRunnerWithBugInject();
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel);
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel2);
		ChainAugProcess test = new ChainAugProcess(param, caseRunner);
		test.outputResult(test.analysis(wrongCase));

		int[] wrong2 = new int[12];
		for (int i = 0; i < 12; i++) {
			wrong2[i] = 1;
		}
		wrong2[3] = 0;

		TestCase wrongCase2 = new TestCaseImplement();
		((TestCaseImplement) wrongCase2).setTestCase(wrong2);
		
		test.outputResult(test.analysis(wrongCase2));
	}

	public HashSet<Tuple> getExistedMFS() {
		return existedMFS;
	}

	public int[] getParam() {
		return param;
	}
}
