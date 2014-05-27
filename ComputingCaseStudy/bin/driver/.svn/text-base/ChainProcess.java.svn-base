package driver;

import java.util.List;

import com.fc.caseRunner.CaseRunner;
import com.fc.caseRunner.CaseRunnerWithBugInject;
import com.fc.model.Chain;
import com.fc.model.TuplePool;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.testObject.TestSuite;
import com.fc.testObject.TestSuiteImplement;
import com.fc.tuple.CorpTupleWithTestCase;
import com.fc.tuple.Tuple;

public class ChainProcess {
	protected CaseRunner caseRunner;
	protected CorpTupleWithTestCase generate;
	protected TuplePool pool;
	protected Chain workMachine;

	public Chain getWorkMachine() {
		return workMachine;
	}

	public ChainProcess(TestCase wrongCase, CaseRunner caseRunner, int[] param,
			TestSuite rightSuite) {
		this.caseRunner = caseRunner;
		pool = new TuplePool(wrongCase, rightSuite);
		generate = new CorpTupleWithTestCase(wrongCase, param);
	}

	public void testWorkFlow() {
		workMachine = new Chain(pool, generate);
		character(workMachine);
		// outputResult(workMachine);
	}

	private void character(Chain workMachine) {
		while (true) {
			TestCase testcase = workMachine.genNextTest();
			if (testcase == null)
				break;
			workMachine
					.setLastTestCase(caseRunner.runTestCase(testcase) == TestCase.PASSED);
		}
	}

	public void outputResult(Chain workMachine) {
		System.out.println("begin");
		List<Tuple> bugs = workMachine.getPool().getExistedBugTuples();
		for (Tuple bug : bugs) {
			System.out.println(bug.toString());
		}
		System.out.println("done");

		TestSuite extra = workMachine.getExtraCases();
		System.out.println("all : " + extra.getTestCaseNum());
		for (int i = 0; i < extra.getTestCaseNum(); i++) {
			System.out.print(extra.getAt(i).getStringOfTest());
			System.out
					.println(extra.getAt(i).testDescription() == TestCase.PASSED ? "pass"
							: "fail");
		}
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

		int[] param = new int[] { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
				3, 3, 3, 3, 3, 3, 3, 3 };

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

		pool = new TuplePool(wrongCase, rightSuite);

		generate = new CorpTupleWithTestCase(wrongCase, param);
	}

	public static void main(String[] args) {
		int[] wrong = new int[8];
		int[] pass = new int[8];
		int[] param = new int [8];
		for(int i = 0; i < 8 ; i++){
			wrong[i] = 1;
			pass[i] = 0;
			param[i] = 3;
		}

		TestCase rightCase = new TestCaseImplement();
		((TestCaseImplement) rightCase).setTestCase(pass);
		TestCase wrongCase = new TestCaseImplement();
		((TestCaseImplement) wrongCase).setTestCase(wrong);

		TestSuite rightSuite = new TestSuiteImplement();
		rightSuite.addTest(rightCase);

		Tuple bugModel = new Tuple(2, wrongCase);
		bugModel.set(0, 2);
		bugModel.set(1, 3);

		Tuple bugModel2 = new Tuple(1, wrongCase);
		bugModel2.set(0, 4);

		CaseRunner caseRunner = new CaseRunnerWithBugInject();
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel);
		//((CaseRunnerWithBugInject) caseRunner).inject(bugModel2);
		ChainProcess test = new ChainProcess(wrongCase, caseRunner, param,
				rightSuite);
		test.testWorkFlow();
		test.outputResult(test.getWorkMachine());
	}
}
