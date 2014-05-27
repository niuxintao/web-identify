package com.fc.TRT;

import java.util.ArrayList;
import java.util.List;

import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.testObject.TestSuite;
import com.fc.testObject.TestSuiteImplement;
import com.fc.tuple.Tuple;

public class PathProcess {
	protected CaseRunner caseRunner;
	protected CorpTupleWithTestCase generate;
	protected TreeStruct tree;
	protected TestSuite additionalSuite;
	protected List<Tuple> bugs;

	public void testWorkFlow(TestCase wrongCase, List<Tuple> bugs, int[] param,
			TestSuite rightSuite) {
		// init();

		caseRunner = new CaseRunnerWithBugInject();
		for (Tuple bug : bugs)
			((CaseRunnerWithBugInject) caseRunner).inject(bug);

		tree = new TreeStruct(wrongCase, rightSuite);
		tree.constructTree();
		tree.init();

		generate = new CorpTupleWithTestCase(wrongCase, param);

		Path path = new Path(tree, caseRunner, generate);
		path.process();
		this.bugs = tree.getBugModes();
		additionalSuite = path.getExtraCases();
		/*
		 * List<Tuple> bugs = tree.getBugModes(); for(Tuple bug : bugs){
		 * System.out.println(bug.toString()); }
		 */
	}

	public void testWorkFlow(TestCase wrongCase, List<Tuple> bugs,
			List<Integer> result, int[] param, TestSuite rightSuite,
			List<Integer> lowerPriority) {
		// init();
		caseRunner = new CaseRunnerTable(result, param, lowerPriority);

		tree = new TreeStruct(wrongCase, rightSuite);
		tree.constructTree();
		tree.init();

		generate = new CorpTupleWithTestCase(wrongCase, param);

		Path path = new Path(tree, caseRunner, generate);
		path.process();
		this.bugs = tree.getBugModes();
		additionalSuite = path.getExtraCases();
		/*
		 * List<Tuple> bugs = tree.getBugModes(); for(Tuple bug : bugs){
		 * System.out.println(bug.toString()); }
		 */
	}

	public TestSuite getAdditionalSuite() {
		return additionalSuite;
	}

	public List<Tuple> getBugs() {
		return bugs;
	}

	public static void main(String[] args) {
		PathProcess na = new PathProcess();
		int[] wrong = new int[8];
		int[] pass = new int[8];
		int[] param = new int[8];
		for (int i = 0; i < 8; i++) {
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
		List<Tuple> bugs = new ArrayList<Tuple>();
		bugs.add(bugModel);

		na.testWorkFlow(wrongCase, bugs, param, rightSuite);

		for (Tuple bug : na.bugs)
			System.out.println(bug.toString());

		System.out.println("all:" + na.additionalSuite.getTestCaseNum());
		for (int i = 0; i < na.additionalSuite.getTestCaseNum(); i++) {
			System.out.println(na.additionalSuite.getAt(i).getStringOfTest());
		}
	}
}
