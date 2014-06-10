package com.fc.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fc.caseRunner.CaseRunner;
import com.fc.caseRunner.CaseRunnerWithBugInject;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.testObject.TestSuite;
import com.fc.testObject.TestSuiteImplement;
import com.fc.tuple.CorpTupleWithTestCase;
import com.fc.tuple.Tuple;

public class RI {
	private CorpTupleWithTestCase generate;
	private CaseRunner caseRunner;
	private TestSuite addtionalTestSuite;

	private HashMap<Tuple, Boolean> tested;

	public RI(CorpTupleWithTestCase generate, CaseRunner caseRunner) {
		this.generate = generate;
		this.caseRunner = caseRunner;
		addtionalTestSuite = new TestSuiteImplement();
		tested = new HashMap<Tuple, Boolean>();
	}

	public Tuple getRelatedIndex(TestCase failCase, Tuple tuple,
			Tuple unrelated, Tuple related) {
		// System.out.println(tuple.toString());
		if (tuple.getDegree() == 1)
			return tuple;
		else {
			Tuple[] next = getSplit(tuple, failCase);
			Tuple doreplace = next[0].cat(next[0], unrelated);
			Tuple suspicious = doreplace.getReverseTuple();
			suspicious.cat(suspicious, related);

			if (test(suspicious)) {
				// pass
				Tuple nexTuple = next[0];
				return getRelatedIndex(failCase, nexTuple, unrelated, related);

			} else {
				// fail
				Tuple nexTuple = next[1];
				unrelated = unrelated.cat(unrelated, next[0]);
				return getRelatedIndex(failCase, nexTuple, unrelated, related);
			}
		}
	}

	private boolean test(Tuple suspicious) {
		if (this.tested.containsKey(suspicious)) {
			return this.tested.get(suspicious);
		}

		TestCase ite = this.generate.generateTestCaseContainTuple(suspicious);
		addtionalTestSuite.addTest(ite);
		Boolean result = runTest(ite);
		this.tested.put(suspicious, result);
		return result;
	}

	public Tuple getRelated(TestCase failCase, Tuple identified,
			Tuple suspicious) {
		Tuple related = new Tuple(0, failCase);

		while (true) {
			Tuple unrelated = identified.copy();
			Tuple rr = this.getRelatedIndex(failCase, suspicious, unrelated,
					related);
			// System.out.println(rr.toString());
			related = related.cat(related, rr);

			if (test(related)) {
				Tuple replace = related.cat(related, identified);
				suspicious = replace.getReverseTuple();
			} else {
				break;
			}
		}

		return related;
	}

	public List<Tuple> process(TestCase failCase) {
		List<Tuple> result = new ArrayList<Tuple>();
		Tuple rela = new Tuple(0, failCase);
		Tuple identified = new Tuple(0, failCase);
		Tuple suspicious = identified.getReverseTuple();
		while (true) {
			Tuple bug = this.getRelated(failCase, identified, suspicious);
			rela = rela.cat(rela, bug);
			result.add(bug);

			if (rela.getReverseTuple().getDegree() == 0)
				break;
			else {
				if (!test(rela.getReverseTuple())) {
					identified = rela;
					suspicious = identified.getReverseTuple();
				} else
					break;
			}
		}
		return result;
	}

	protected boolean runTest(TestCase testCase) {
		testCase.setTestState(caseRunner.runTestCase(testCase));
		return testCase.testDescription() == TestCase.PASSED;
	}

	public Tuple[] getSplit(Tuple tuple, TestCase testCase) {
		int degree = tuple.getDegree();
		int bdegree = degree / 2;
		int adegree = degree - bdegree;
		Tuple A = new Tuple(adegree, testCase);
		Tuple B = new Tuple(bdegree, testCase);
		for (int i = 0; i < A.getDegree(); i++) {
			A.set(i, tuple.getParamIndex()[i]);
		}
		for (int i = 0; i < B.getDegree(); i++) {
			B.set(i, tuple.getParamIndex()[i + A.getDegree()]);
		}

		Tuple[] result = new Tuple[2];
		result[0] = A;
		result[1] = B;

		return result;
	}

	public static void main(String[] args) {
		int[] wrong = new int[] { 1, 1, 1, 1, 1, 1, 1, 1 };
		TestCase wrongCase = new TestCaseImplement();
		((TestCaseImplement) wrongCase).setTestCase(wrong);

		int[] param = new int[] { 10, 10, 10, 10, 10, 10, 10, 10 };

		Tuple bugModel1 = new Tuple(2, wrongCase);
		bugModel1.set(0, 4);
		bugModel1.set(1, 7);

		Tuple bugModel2 = new Tuple(1, wrongCase);
		bugModel2.set(0, 3);

		CaseRunner caseRunner = new CaseRunnerWithBugInject();
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel1);
		// ((CaseRunnerWithBugInject) caseRunner).inject(bugModel2);
		CorpTupleWithTestCase generate = new CorpTupleWithTestCase(wrongCase,
				param);

		RI ri = new RI(generate, caseRunner);
		List<Tuple> tupleg = ri.process(wrongCase);
		for (Tuple tuple : tupleg)
			System.out.println(tuple.toString());
		for (int i = 0; i < ri.addtionalTestSuite.getTestCaseNum(); i++)
			System.out
					.println(ri.addtionalTestSuite.getAt(i).getStringOfTest());
	}

	public TestSuite getAddtionalTestSuite() {
		return addtionalTestSuite;
	}

}
