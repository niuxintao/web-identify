package com.fc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.fc.caseRunner.CaseRunner;
import com.fc.caseRunner.CaseRunnerWithBugInject;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

public class LocateGraphBinary {
	private LocateGraph lg;
	private List<Tuple> bugs;

	public LocateGraphBinary(CaseRunner caseRunner) {
		lg = new LocateGraph(caseRunner);
		bugs = new ArrayList<Tuple>();
	}

	public void discoverEdges(TestCase passCase, TestCase wrongCase) {
		Tuple A = this.discoverA(wrongCase);
		Tuple[] tuples = this.splitA(wrongCase, A);
		Tuple B = this.dicoverB(A, wrongCase);
		this.determineE(A, B, tuples[1], wrongCase);
		this.findInAs(tuples[1], wrongCase, A, B);
	}

	public Tuple searchEndPoint(TestCase testCase, Tuple factors) {
		if (factors.getDegree() == 1)
			return factors;
		else {
			Tuple v1 = new Tuple(0, testCase);
			Tuple v2 = new Tuple(0, testCase);
			Tuple[] tuples = lg.getSplit(factors, testCase);
			TestCase case1 = this.generateTestCase(testCase, tuples[1]);
			if (!lg.test(case1))
				v1 = this.searchEndPoint(case1, tuples[0]);
			TestCase case2 = this.generateTestCase(testCase, tuples[0]);
			if (!lg.test(case2))
				v2 = this.searchEndPoint(case2, tuples[1]);

			Tuple result = v1.catComm(v1, v2);
			return result;
		}
	}

	public Tuple discoverA(TestCase testCase) {
		Tuple tuple = new Tuple(0, testCase);
		for (int i = 0; i < testCase.getLength(); i++) {
			if (testCase.getAt(i) == 1) {
				Tuple tem = new Tuple(1, testCase);
				tem.set(0, i);
				tuple = tuple.catComm(tuple, tem);
			}
		}
		Tuple tupleA = this.searchEndPoint(testCase, tuple);
		return tupleA;
	}

	public Tuple[] splitA(TestCase wrongCase, Tuple tupleA) {
		List<Tuple> edges = new ArrayList<Tuple>();
		Tuple[] tuples = new Tuple[2];
		Tuple AP = new Tuple(0, wrongCase);
		for (int i = 0; i < tupleA.getDegree(); i++) {
			for (int j = i + 1; j < tupleA.getDegree(); j++) {
				int iindex = tupleA.getParamIndex()[i];
				int jindex = tupleA.getParamIndex()[j];
				TestCase testCase = new TestCaseImplement(wrongCase.getLength());
				for (int k = 0; k < wrongCase.getLength(); k++)
					testCase.set(i, 0);
				testCase.set(iindex, 1);
				testCase.set(jindex, 1);
				if (lg.test(testCase)) {
					Tuple tuple = new Tuple(2, wrongCase);
					tuple.set(0, iindex);
					tuple.set(1, jindex);
					edges.add(tuple);
					AP = AP.catComm(AP, tuple);
				}
			}
		}

		Tuple AS = new Tuple(tupleA.getDegree() - AP.getDegree(), wrongCase);
		int apindex = 0;
		int asindex = 0;
		for (int i = 0; i < tupleA.getDegree(); i++) {
			if (apindex <= AP.getDegree() - 1) {
				if (tupleA.getParamIndex()[i] < AP.getParamIndex()[apindex]) {
					AS.set(asindex, tupleA.getParamIndex()[i]);
					asindex++;
				} else if (tupleA.getParamIndex()[i] == AP.getParamIndex()[apindex]) {
					if (apindex < AP.getDegree() - 1)
						apindex++;
				} else {
					AS.set(asindex, tupleA.getParamIndex()[i]);
					asindex++;
				}
			} else {
				AS.set(asindex, tupleA.getParamIndex()[i]);
				asindex++;
			}

		}

		bugs.addAll(edges);

		tuples[0] = AP;
		tuples[1] = AS;
		return tuples;
	}

	public Tuple dicoverB(Tuple tupleA, TestCase wrongCase) {
		List<Tuple> edges = new ArrayList<Tuple>();

		TestCase safeValues = new TestCaseImplement(wrongCase.getLength());
		for (int i = 0; i < safeValues.getLength(); i++) {
			safeValues.set(i, 0);
		}

		TestCase testCase = new TestCaseImplement(wrongCase.getLength());
		for (int i = 0; i < safeValues.getLength(); i++) {
			testCase.set(i, 1);
		}
		for (int i = 0; i < tupleA.getDegree(); i++)
			testCase.set(tupleA.getParamIndex()[i], 0);

		if (!lg.test(testCase)) {
			edges = lg.locateErrorsInTest(safeValues, testCase,
					tupleA.getReverseTuple());
		}

		bugs.addAll(edges);

		Tuple B = new Tuple(0, wrongCase);
		for (Tuple tu : edges)
			B = B.catComm(B, tu);
		return B;
	}

	public void determineE(Tuple A, Tuple B, Tuple AS, TestCase wrongCase) {
		List<Tuple> edges = new ArrayList<Tuple>();
		Tuple cr = A.catComm(A, B);
		Tuple C = cr.getReverseTuple();

		for (int i : AS.getParamIndex()) {
			Tuple partA = new Tuple(1, wrongCase);
			partA.set(0, i);

			TestCase testCase = new TestCaseImplement(wrongCase.getLength());
			for (int j = 0; j < testCase.getLength(); j++)
				testCase.set(j, 0);
			testCase.set(i, 1);
			while (true) {
				for (int j : C.getParamIndex())
					testCase.set(j, new Random().nextInt(2));
				if (lg.test(testCase))
					break;
			}
			for (int j : B.getParamIndex()) {
				TestCase testCaseB = testCase.copy();
				testCaseB.set(j, 1);
				if (!lg.test(testCaseB)) {
					Tuple partB = new Tuple(1, wrongCase);
					partB.set(0, j);
					Tuple edge = partA.catComm(partA, partB);
					edges.add(edge);
				}
			}

			TestCase testCaseC = testCase.copy();
			for (int j : C.getParamIndex()) {
				testCaseC.set(j, (testCaseC.getAt(j) + 1) % 2);
			}
			if (!lg.test(testCaseC)) {
				Tuple endpoint = this.searchEndPoint(testCaseC, C);
				for (int k : endpoint.getParamIndex()) {
					Tuple partC = new Tuple(1, wrongCase);
					partC.set(0, k);
					Tuple edge = partA.catComm(partA, partC);
					edges.add(edge);
				}
			}
		}
		bugs.addAll(edges);
	}

	public void findInAs(Tuple AS, TestCase wrongCase, Tuple A , Tuple B) {
		Tuple cr = A.catComm(A, B);
		Tuple C = cr.getReverseTuple();
		
		List<Tuple> edges = new ArrayList<Tuple>();
		for (int i = 0; i < AS.getDegree(); i++) {
			int iindex = AS.getParamIndex()[i];
			Tuple partA = new Tuple(1, wrongCase);
			partA.set(0, iindex);
			for (int j = i + 1; j < AS.getDegree(); j++) {
				int jindex = AS.getParamIndex()[j];
				TestCase testCase = new TestCaseImplement(wrongCase.getLength());
				for (int k = 0; k < testCase.getLength(); k++)
					testCase.set(k, 0);
				for(int k : C.getParamIndex())
					testCase.set(k, 1);
				testCase.set(iindex, 1);
				testCase.set(jindex, 1);
				if (!lg.test(testCase)) {
					Tuple partB = new Tuple(1, wrongCase);
					partB.set(0, jindex);
					Tuple tuple = partA.catComm(partA, partB);
					edges.add(tuple);
				}
			}
		}
		bugs.addAll(edges);
	}

	public List<Tuple> getBugs() {
		return bugs;
	}

	public TestCase generateTestCase(TestCase wrongCase, Tuple tuple) {
		TestCase result = new TestCaseImplement(wrongCase.getLength());
		for (int i = 0; i < wrongCase.getLength(); i++)
			result.set(i, wrongCase.getAt(i));
		for (int i = 0; i < tuple.getDegree(); i++)
			result.set(tuple.getParamIndex()[i],
					(wrongCase.getAt(tuple.getParamIndex()[i]) + 1) % 2);
		return result;
	}

	public static void main(String[] args) {
		int[] wrong = new int[] { 1, 1, 1, 1, 1, 1, 1, 0 };
		TestCase wrongCase = new TestCaseImplement();
		((TestCaseImplement) wrongCase).setTestCase(wrong);

		int[] pass = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		TestCase rightCase = new TestCaseImplement();
		((TestCaseImplement) rightCase).setTestCase(pass);

		Tuple bugModel1 = new Tuple(2, wrongCase);
		bugModel1.set(0, 2);
		bugModel1.set(1, 7);

		Tuple bugModel2 = new Tuple(2, wrongCase);
		bugModel2.set(0, 3);
		bugModel2.set(1, 7);

		CaseRunner caseRunner = new CaseRunnerWithBugInject();
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel1);
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel2);

		LocateGraphBinary lg = new LocateGraphBinary(caseRunner);

		Tuple tuple = new Tuple(0, wrongCase);
		tuple = tuple.getReverseTuple();

		lg.discoverEdges(rightCase, wrongCase);

		List<Tuple> bugs = lg.getBugs();
		System.out.println("bugs:");
		for (Tuple tu : bugs)
			System.out.println(tu.toString());

		System.out.println("testCases:");
		for (int i = 0; i < lg.getLg().getAddtionalTestSuite().getTestCaseNum(); i++)
			System.out.println(lg.getLg().getAddtionalTestSuite().getAt(i)
					.getStringOfTest());

	}

	public LocateGraph getLg() {
		return lg;
	}
}
