package com.fc.model;

import java.util.ArrayList;
import java.util.List;

import com.fc.caseRunner.CaseRunner;
import com.fc.caseRunner.CaseRunnerWithBugInject;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

public class SOFOT {
	public List<Tuple> getBugs() {
		return bugs;
	}

	private List<TestCase> executed;
	private List<Tuple> bugs;

	private TestCase lastCase;
	private int currentNum = 0;
	private int[] param;
	private TestCase wrongCase;

	public List<TestCase> getExecuted() {
		return executed;
	}

	public SOFOT(int[] param, TestCase wrongCase) {
		executed = new ArrayList<TestCase>();
		bugs = new ArrayList<Tuple>();
		this.param = param;
		this.wrongCase = wrongCase;
		lastCase = wrongCase;
	}

	public SOFOT() {
		executed = new ArrayList<TestCase>();
		bugs = new ArrayList<Tuple>();
	}

	public TestCase generateNextTestCase() {
		TestCase result = null;
		if (currentNum < param.length) {
			result = generateTestCase(wrongCase, param, currentNum);
			currentNum++;
			lastCase = result;
			this.executed.add(lastCase);
		} else {
			this.analysis(executed, wrongCase, param);
		}

		return result;
	}

	public void setLastTestCase(boolean state) {
		lastCase.setTestState(state == true ? TestCase.PASSED : TestCase.FAILED);
	}

	public void process(TestCase wrongCase, int[] parameters, CaseRunner runner) {
		for (int i = 0; i < wrongCase.getLength(); i++) {
			TestCase testCase = generateTestCase(wrongCase, parameters, i);
			testCase.setTestState(runner.runTestCase(testCase));
			executed.add(testCase);
		}
		analysis(executed, wrongCase, parameters, runner);
	}

	public TestCase generateTestCase(TestCase wrongCase, int[] parameters,
			int mutantIndex) {
		TestCase casetemple = new TestCaseImplement(wrongCase.getLength());

		for (int i = 0; i < wrongCase.getLength(); i++)
			casetemple.set(i, wrongCase.getAt(i));

		casetemple.set(mutantIndex, (casetemple.getAt(mutantIndex) + 1)
				% parameters[mutantIndex]);

		return casetemple;

	}

	public void analysis(List<TestCase> array, TestCase wrongCase,
			int[] parameters, CaseRunner runner) {
		Tuple tuple = new Tuple(0, wrongCase);
		for (int i = 0; i < array.size(); i++) {
			TestCase testCase = array.get(i);
			if (testCase.testDescription() == TestCase.PASSED) {
				Tuple tem = new Tuple(1, wrongCase);
				tem.set(0, i);
				tuple = tuple.cat(tuple, tem);
				// failure-inducing + 1
			} else if (testCase.testDescription() == TestCase.FAILED) {

			}
		}
		this.bugs.add(tuple);
	}

	public void analysis(List<TestCase> array, TestCase wrongCase,
			int[] parameters) {
		Tuple tuple = new Tuple(0, wrongCase);
		for (int i = 0; i < array.size(); i++) {
			TestCase testCase = array.get(i);
			if (testCase.testDescription() == TestCase.PASSED) {
				Tuple tem = new Tuple(1, wrongCase);
				tem.set(0, i);
				tuple = tuple.cat(tuple, tem);
				// failure-inducing + 1
			} else if (testCase.testDescription() == TestCase.FAILED) {

			}
		}
		this.bugs.add(tuple);
	}

	public static void main(String[] args) {
		int[] wrong = new int[] { 1, 1, 1, 1, 1, 1, 1, 1 };
		TestCase wrongCase = new TestCaseImplement();
		((TestCaseImplement) wrongCase).setTestCase(wrong);

		int[] wrong2 = new int[] { 2, 2, 2, 2, 2, 2, 2, 2 };
		TestCase wrongCase2 = new TestCaseImplement();
		((TestCaseImplement) wrongCase2).setTestCase(wrong2);

		int[] param = new int[] { 10, 10, 10, 10, 10, 10, 10, 10 };

		Tuple bugModel1 = new Tuple(2, wrongCase);
		bugModel1.set(0, 2);
		bugModel1.set(1, 5);

		Tuple bugModel2 = new Tuple(1, wrongCase2);
		bugModel2.set(0, 2);

		CaseRunner caseRunner = new CaseRunnerWithBugInject();
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel1);
		// ((CaseRunnerWithBugInject) caseRunner).inject(bugModel2);

		SOFOT ofot = new SOFOT();
		ofot.process(wrongCase, param, caseRunner);

		System.out.println("bugs:");
		for (Tuple tuple : ofot.bugs) {
			System.out.println(tuple.toString());
		}
		System.out.println("cases:");

		for (TestCase cases : ofot.executed) {
			System.out.print(cases.getStringOfTest());
			System.out.println(" " + cases.testDescription());
		}

	}
}
