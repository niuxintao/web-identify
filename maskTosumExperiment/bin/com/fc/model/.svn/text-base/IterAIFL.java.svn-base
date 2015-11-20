package com.fc.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.fc.caseRunner.CaseRunner;
import com.fc.caseRunner.CaseRunnerWithBugInject;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.testObject.TestSuite;
import com.fc.testObject.TestSuiteImplement;
import com.fc.tuple.CorpTupleWithTestCase;
import com.fc.tuple.Tuple;

public class IterAIFL {
	private CorpTupleWithTestCase generate;
	private List<Tuple> bugs;
	private HashMap<TestCase, Integer> tested;
	private TestSuite suite;
	private CaseRunner caseRunner;

	public static int SUSSET = 2;
	public static int ITER = 2;

	public IterAIFL(CorpTupleWithTestCase generate, CaseRunner caseRunner) {
		this.generate = generate;
		this.caseRunner = caseRunner;
		bugs = new ArrayList<Tuple>();
		tested = new HashMap<TestCase, Integer>();
		suite = new TestSuiteImplement();

	}

	public TestSuite getSuite() {
		return suite;
	}

	public void process(TestCase wrongCase) {
		Tuple tuple = new Tuple(0, wrongCase);
		Tuple root = tuple.getReverseTuple();
		List<Tuple> scheSet = root.getAllChilds();
		iterFl(wrongCase, scheSet, 1);
		this.reduce();
	}

	public void iterFl(TestCase wrongCase, List<Tuple> scheSet, int degree) {
		boolean rmflag = false;
		List<TestCase> testCases = this.generateTestCases(wrongCase, degree);
		executeTestSuite(testCases);

		for (TestCase testCase : testCases) {
			if (testCase.testDescription() == TestCase.PASSED) {
				Iterator<Tuple> iter = scheSet.iterator();
				while (iter.hasNext()) {
					Tuple tuple = iter.next();
					if (testCase.containsOf(tuple)) {
						rmflag = true;
						iter.remove();
					}
				}
			}
		}
		degree++;

		if ((degree > ITER && !rmflag) || degree == wrongCase.getLength()
				|| scheSet.size() <= SUSSET) {
			bugs = scheSet;
			return;
		}

		iterFl(wrongCase, scheSet, degree);
	}

	public void executeTestSuite(List<TestCase> testSuite) {
		for (TestCase testCase : testSuite) {
			if (this.tested.containsKey(testCase)) {
				testCase.setTestState(tested.get(testCase));
			} else {
				testCase.setTestState(this.caseRunner.runTestCase(testCase));
				this.tested.put(testCase, testCase.testDescription());
				suite.addTest(testCase);
			}
		}

	}

	public List<Tuple> getBugs() {
		return bugs;
	}

	List<TestCase> generateTestCases(TestCase testCase, int degree) {
		List<TestCase> tests = new ArrayList<TestCase>();
		Tuple tuple = new Tuple(0, testCase);
		Tuple root = tuple.getReverseTuple();
		List<Tuple> childs = root.getChildTuplesByDegree(testCase.getLength()
				- degree);
		for (Tuple child : childs) {
			tests.add(generate.generateTestCaseContainTuple(child));
		}
		return tests;
	}

	public void reduce() {
		List<Tuple> bugs = new ArrayList<Tuple>();
		for (Tuple tuple : this.bugs) {
			boolean flag = true;
			for (Tuple tem : this.bugs) {
				if (tuple.equals(tem))
					continue;
				if (tuple.contains(tem)) {
					flag = false;
					break;
				}
			}
			if (flag)
				bugs.add(tuple);
		}
		this.bugs = bugs;
	}

	public static void main(String[] args) {
		int[] wrong = new int[] { 1, 1, 1, 1, 1, 1, 1, 1 };
		TestCase wrongCase = new TestCaseImplement();
		((TestCaseImplement) wrongCase).setTestCase(wrong);

		int[] param = new int[] { 10, 10, 10, 10, 10, 10, 10, 10 };

		Tuple bugModel1 = new Tuple(2, wrongCase);
		bugModel1.set(0, 2);
		bugModel1.set(1, 5);

		Tuple bugModel2 = new Tuple(2, wrongCase);
		bugModel2.set(0, 3);
		bugModel2.set(1, 5);

		CaseRunner caseRunner = new CaseRunnerWithBugInject();
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel1);
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel2);
		CorpTupleWithTestCase generate = new CorpTupleWithTestCase(wrongCase,
				param);

		IterAIFL ifl = new IterAIFL(generate, caseRunner);
		ifl.process(wrongCase);
		for (Tuple tuple : ifl.getBugs()) {
			System.out.println(tuple.toString());
		}
		for (int i = 0; i < ifl.getSuite().getTestCaseNum(); i++)
			System.out.println(ifl.getSuite().getAt(i).getStringOfTest());
	}
}
