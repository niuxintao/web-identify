package com.fc.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fc.caseRunner.CaseRunner;
import com.fc.caseRunner.CaseRunnerWithBugInject;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

public class OFOT {
	public List<Tuple> getBugs() {
		return bugs;
	}

	public static int ITERATNUMBER = 2;

	private HashMap<TestCase, Integer> executed;

	public HashMap<TestCase, Integer> getExecuted() {
		return executed;
	}

	private List<Tuple> bugs;

	public OFOT() {
		executed = new HashMap<TestCase, Integer>();
		bugs = new ArrayList<Tuple>();
	}

	public void process(TestCase wrongCase, int[] parameters, CaseRunner runner) {
		List<List<TestCase>> array = this.generateSuiteArray(wrongCase,
				parameters);
		boolean generatedNew = false;
		for (List<TestCase> list : array) {
			for (TestCase testCase : list) {
				if (executed.containsKey(testCase))
					testCase.setTestState(executed.get(testCase));
				else {
					generatedNew = true;
					testCase.setTestState(runner.runTestCase(testCase));
					executed.put(testCase, testCase.testDescription());
				}
			}
		}

		if (generatedNew)
			analysis(array, wrongCase, parameters, runner);
	}

	public void analysis(List<List<TestCase>> array, TestCase wrongCase,
			int[] parameters, CaseRunner runner) {
		Tuple tuple = new Tuple(0, wrongCase);
		for (int i = 0; i < array.size(); i++) {
			List<TestCase> list = array.get(i);

			int state = 0;
			for (TestCase testCase : list)
				state += testCase.testDescription();

			if (state == TestCase.PASSED * list.size()) {
				// all passed
				Tuple tem = new Tuple(1, wrongCase);
				tem.set(0, i);
				tuple = tuple.cat(tuple, tem);
				// failure-inducing + 1
			} else if (state == TestCase.FAILED * list.size()) {
				// all wrong

			} else {
				// failure-inducing + 1
				Tuple tem = new Tuple(1, wrongCase);
				tem.set(0, i);
				tuple = tuple.cat(tuple, tem);
				for (TestCase testCase : list) {
					if (testCase.testDescription() == TestCase.FAILED)
						process(testCase, parameters, runner);
				}
			}
		}
		this.bugs.add(tuple);
	}

	public List<List<TestCase>> generateSuiteArray(TestCase wrongCase,
			int[] parameters) {
		List<List<TestCase>> suite = new ArrayList<List<TestCase>>();
		// TestSuite suite = new TestSuiteImplement();
		for (int i = 0; i < wrongCase.getLength(); i++) {
			List<TestCase> temp = new ArrayList<TestCase>();
			TestCase lastCase = wrongCase;
			for (int k = 0; k < (parameters[i] - 1 > ITERATNUMBER ? ITERATNUMBER
					: parameters[i] - 1); k++) {
				TestCase casetemple = new TestCaseImplement(
						wrongCase.getLength());
				for (int j = 0; j < lastCase.getLength(); j++)
					casetemple.set(j, lastCase.getAt(j));
				casetemple.set(i, (casetemple.getAt(i) + 1) % parameters[i]);
				temp.add(casetemple);
				lastCase = casetemple;
			}
			suite.add(temp);
		}
		return suite;
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
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel2);

		OFOT ofot = new OFOT();
		ofot.process(wrongCase, param, caseRunner);

		System.out.println("bugs:");
		for (Tuple tuple : ofot.bugs) {
			System.out.println(tuple.toString());
		}
		System.out.println("cases:");

		for (TestCase cases : ofot.executed.keySet()) {
			System.out.print(cases.getStringOfTest());
			System.out.println(" " + cases.testDescription());
		}

	}
}
