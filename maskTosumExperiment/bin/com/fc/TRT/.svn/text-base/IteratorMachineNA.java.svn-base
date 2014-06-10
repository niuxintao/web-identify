package com.fc.TRT;

import java.util.ArrayList;
import java.util.List;

import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.testObject.TestSuite;
import com.fc.testObject.TestSuiteImplement;
import com.fc.tuple.Tuple;

public class IteratorMachineNA {
	protected CaseRunner caseRunner;
	protected int[] param;
	protected TestSuite rightSuite;
	protected List<Tuple> bugs;
	/**
	 * @return the bugs
	 */
	public List<Tuple> getBugs() {
		return bugs;
	}

	protected TestSuite extraTestSuite;

	public IteratorMachineNA(int[] param, CaseRunner caseRunner,
			TestSuite rightSuite) {
		this.param = param;
		this.caseRunner = caseRunner;
		this.rightSuite = new TestSuiteImplement();
		if (rightSuite != null)
			for (int i = 0; i < rightSuite.getTestCaseNum(); i++) {
				this.rightSuite.addTest(rightSuite.getAt(i).copy());
			}
		this.bugs = new ArrayList<Tuple>();
		this.extraTestSuite = new TestSuiteImplement();
	}

	public void process(TestCase wrongCase) {
		TreeStruct tree = new TreeStruct(wrongCase, rightSuite);// set exsit bug
		tree.constructTree();
		tree.init();
		tree.removeFoundBug(bugs);
		CorpTupleWithTestCase generate = new CorpTupleWithTestCase(wrongCase,
				param, bugs);

		PathNA workMachine = new PathNA(tree, caseRunner, generate);
		workMachine.process();

		List<Tuple> cbugs = tree.getBugModes();
		this.bugs.addAll(cbugs);

		TestSuite extra = workMachine.getExtraCases();
		for (int i = 0; i < extra.getTestCaseNum(); i++) {
			TestCase test = extra.getAt(i);
			if (test.testDescription() == TestCase.PASSED) {
				rightSuite.addTest(test); // add new generated right case
			}
			extraTestSuite.addTest(test);
		}

		TestSuite newImportSuite = workMachine.getSuiteContainNewBug();
		for (int i = 0; i < newImportSuite.getTestCaseNum(); i++) {
		//	System.out.println("news: "+newImportSuite.getAt(i).getStringOfTest());
			process(newImportSuite.getAt(i));
		}
	}

	/**
	 * @return the extraTestSuite
	 */
	public TestSuite getExtraTestSuite() {
		return extraTestSuite;
	}

	public static void main(String[] args) {
		int[] wrong = new int[] { 1, 1, 1 };
		TestCase wrongCase = new TestCaseImplement();
		((TestCaseImplement) wrongCase).setTestCase(wrong);
		TestSuite rightSuite = new TestSuiteImplement();

		int[] param = new int[] { 10, 10, 10 };
		Tuple bugModel = new Tuple(2, wrongCase);
		bugModel.set(0, 0);
		bugModel.set(1, 1);

		int[] wrong1 = new int[] { 1, 2, 1 };
		TestCase wrongCase1 = new TestCaseImplement();
		((TestCaseImplement) wrongCase1).setTestCase(wrong1);
		Tuple bugModel1 = new Tuple(2, wrongCase1);
		bugModel1.set(0, 1);
		bugModel1.set(1, 2);

		int[] wrong2 = new int[] { 2, 1, 1 };
		TestCase wrongCase2 = new TestCaseImplement();
		((TestCaseImplement) wrongCase2).setTestCase(wrong2);
		Tuple bugModel2 = new Tuple(2, wrongCase2);
		bugModel2.set(0, 0);
		bugModel2.set(1, 1);

		int[] wrong3 = new int[] { 3, 1, 1 };
		TestCase wrongCase3 = new TestCaseImplement();
		((TestCaseImplement) wrongCase3).setTestCase(wrong3);
		Tuple bugModel3 = new Tuple(2, wrongCase3);
		bugModel3.set(0, 0);
		bugModel3.set(1, 1);

		CaseRunner caseRunner = new CaseRunnerWithBugInject();
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel);
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel1);
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel2);
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel3);

		IteratorMachineNA ina = new IteratorMachineNA(param, caseRunner,
				rightSuite);
		ina.process(wrongCase);
	}

}
