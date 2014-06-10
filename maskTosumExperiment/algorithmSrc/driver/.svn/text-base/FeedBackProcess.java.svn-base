package driver;

import java.util.List;

import com.fc.caseRunner.CaseRunner;
import com.fc.caseRunner.CaseRunnerWithBugInject;
import com.fc.model.Chain;
import com.fc.model.CharacterFeedBack;
import com.fc.model.TuplePool;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.testObject.TestSuite;
import com.fc.testObject.TestSuiteImplement;
import com.fc.tuple.CorpTupleWithTestCase;
import com.fc.tuple.Tuple;

public class FeedBackProcess {
	protected CaseRunner caseRunner;
	protected CorpTupleWithTestCase generate;
	protected TuplePool pool;
	protected CharacterFeedBack fb;

	public FeedBackProcess(TestCase wrongCase, CaseRunner caseRunner, int[] param,
			TestSuite rightSuite) {
		this.caseRunner = caseRunner;
		pool = new TuplePool(wrongCase, rightSuite);
		generate = new CorpTupleWithTestCase(wrongCase, param);
	}

	public void testWorkFlow() {

		Chain workMachine = new Chain(pool, generate);
		fb = new CharacterFeedBack();
		fb.process(workMachine, caseRunner, generate);
	//	outputResult(fb);
	}

	public CharacterFeedBack getFb() {
		return fb;
	}

	public void outputResult(CharacterFeedBack fb) {
		System.out.println("begin");
		List<Tuple> bugs = fb.getBugs();
		for (Tuple bug : bugs) {
			System.out.println(bug.toString());
		}
		System.out.println("done");

		List<TestCase> extra = fb.getTestCases();
		System.out.println("all : " + extra.size());
		for (TestCase testCase : extra) {
			System.out.print(testCase.getStringOfTest());
			System.out
					.println(testCase.testDescription() == TestCase.PASSED ? "pass"
							: "fail");
		}
	}

	public static void main(String[] args) {
		int[] wrong = new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

		int[] wrong2 = new int[] { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 };

		int[] pass = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		TestCase rightCase = new TestCaseImplement();
		((TestCaseImplement) rightCase).setTestCase(pass);
		TestCase wrongCase = new TestCaseImplement();
		((TestCaseImplement) wrongCase).setTestCase(wrong);

		TestCase wrongCase2 = new TestCaseImplement();
		((TestCaseImplement) wrongCase2).setTestCase(wrong2);

		TestSuite rightSuite = new TestSuiteImplement();
		rightSuite.addTest(rightCase);

		int[] param = new int[] { 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10,
				10, 10, 10, 10 };

		Tuple bugModel = new Tuple(3, wrongCase);
		bugModel.set(0, 1);
		bugModel.set(1, 2);
		bugModel.set(2, 4);

		Tuple bugModel2 = new Tuple(3, wrongCase2);
		bugModel2.set(0, 12);
		bugModel2.set(1, 13);
		bugModel2.set(2, 14);

		CaseRunner caseRunner = new CaseRunnerWithBugInject();
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel);
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel2);

		FeedBackProcess test = new FeedBackProcess(wrongCase, caseRunner, param, rightSuite);
		test.testWorkFlow();
	}
}
