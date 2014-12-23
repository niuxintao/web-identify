package com.fc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.fc.caseRunner.CaseRunner;
import com.fc.caseRunner.CaseRunnerWithBugInject;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.testObject.TestSuite;
import com.fc.testObject.TestSuiteImplement;
import com.fc.tuple.Tuple;

public class SpectrumBased {

	public int RESULTNUMBER = 5;
	public int TOPNUMBER = 50;
	public int ITERATENUMBER = 200;

	private List<Tuple> failure_inducing;
	private CaseRunner caseRunner;
	private TestSuite addtionalSuite;

	public SpectrumBased(CaseRunner caseRunner) {
		this.caseRunner = caseRunner;
		addtionalSuite = new TestSuiteImplement();
		failure_inducing = new ArrayList<Tuple>();
	}

	public void process(TestSuite suite, int[] parameters, int degree) {
		TestSuite iteratSuite = new TestSuiteImplement();
		for (int i = 0; i < suite.getTestCaseNum(); i++) {
			// System.out.println(suite.getAt(i).getStringOfTest());
			iteratSuite.addTest(suite.getAt(i));
		}
		// for(int i = 0 ; i < iteratSuite.getTestCaseNum() ; i ++)
		// System.out.println(iteratSuite.getAt(i).getStringOfTest());
		List<Tuple> pi = new ArrayList<Tuple>();
		List<Tuple> tway = getTwayTuplesInTestSuite(iteratSuite, degree);
		tway = this.removeInPassed(tway, iteratSuite);
		tway = this.rank(tway, iteratSuite);

		// for (Tuple tuple : tway) {
		// System.out.println(tuple.toString());
		// }

		while (true) {
			if (tway.size() != 0 && (tway.size() < pi.size() || pi.size() == 0)) {
				pi = tway;
				TestSuite iterator = this.generateSuite(parameters,
						iteratSuite, pi);
				if (iterator == null) // marked find failure-inducing;
					return;
				else {
					execute(iterator);
					tway = this.removeInPassed(tway, iterator);
					for (int i = 0; i < iterator.getTestCaseNum(); i++) {
						iteratSuite.addTest(iterator.getAt(i));
					}
					tway = this.rank(tway, iteratSuite);
				}
			} else if (tway.size() == 0) {
				return;
			}
			if (tway.size() == pi.size()) {
				for (int i = 0; i < tway.size(); i++) {
					failure_inducing.add(tway.get(i));
				}
				break;
			}
		}
		failure_inducing = this.reduce(failure_inducing, iteratSuite, degree);
	}

	public void execute(TestSuite suite) {
		for (int i = 0; i < suite.getTestCaseNum(); i++) {
			TestCase testCase = suite.getAt(i);
			testCase.setTestState(caseRunner.runTestCase(testCase));
		}
	}

	public float suspiciousO(int parameter, int value, TestSuite suite,
			List<Tuple> pi) {
		float failAndIn = 0;
		float failNumber = 0;
		float inNumber = 0;
		float inPi = 0;
		for (int i = 0; i < suite.getTestCaseNum(); i++) {
			TestCase testCase = suite.getAt(i);
			if (testCase.testDescription() == TestCase.FAILED) {
				failNumber++;
				if (testCase.getAt(parameter) == value) {
					failAndIn++;
					inNumber++;
				}
			} else if (testCase.getAt(parameter) == value) {
				inNumber++;
			}
		}
		for (Tuple tuple : pi) {
			if (isTupleContainParameterValue(tuple, parameter, value)) {
				inPi++;
			}
		}
		float u = failAndIn / failNumber;
		float v = failAndIn / inNumber;
		float w = inPi / pi.size();

		return (float) ((1.0 / 3.0) * (u + v + w));
	}

	public float suspiciousC(Tuple tuple, TestSuite suite, List<Tuple> pi) {
		float result = 0;
		for (int i = 0; i < tuple.getDegree(); i++) {
			int parameter = tuple.getParamIndex()[i];
			int value = tuple.getParamValue()[i];
			result += suspiciousO(parameter, value, suite, pi);
		}
		result /= tuple.getDegree();
		return result;
	}

	public float suspiciousE(Tuple tuple, TestSuite suite, List<Tuple> pi) {
		float result = -1;
		for (int i = 0; i < suite.getTestCaseNum(); i++) {
			TestCase testCase = suite.getAt(i);
			if (testCase.containsOf(tuple)) {
				float tempSpisious = 0;
				for (int j = 0; j < testCase.getLength(); j++) {
					if (!isTupleContainParameterValue(tuple, j,
							testCase.getAt(j))) {
						tempSpisious += suspiciousO(j, testCase.getAt(j),
								suite, pi);
					}
				}
				tempSpisious /= (testCase.getLength() - tuple.getDegree());
				if (tempSpisious < result || result == -1) {
					result = tempSpisious;
				}
			}
		}

		return result;
	}

	public List<Tuple> rank(List<Tuple> pi, TestSuite suite) {
		List<TempStruct> lin = new ArrayList<TempStruct>();
		for (Tuple tuple : pi) {
			float c = this.suspiciousC(tuple, suite, pi);
			float e = this.suspiciousE(tuple, suite, pi);
			TempStruct te = new TempStruct();
			te.tuple = tuple;
			te.c = c;
			te.e = e;
			lin.add(te);
		}

		List<TempStruct> linc = new ArrayList<TempStruct>();
		linc.addAll(lin);

		Collections.sort(lin, new TempStructC());

		Collections.sort(linc, new TempStructE());

		List<TempStruct> newLin = new ArrayList<TempStruct>();

		int ineed = 0;
		for (int i = 0; i < lin.size(); i++) {
			TempStruct te = new TempStruct();
			te.tuple = lin.get(i).tuple;
			if (i != 0 && !te.tuple.equals(lin.get(i - 1))) {
				ineed++;
			}
			te.c = ineed;
			int jneed = 0;
			for (int j = 0; j < linc.size(); j++) {
				if (j != 0 && !linc.get(j).tuple.equals(linc.get(j - 1).tuple)) {
					jneed++;
				}
				if (te.tuple.equals(linc.get(j).tuple)) {
					te.c += jneed;
					break;
				}
			}
			newLin.add(te);
		}
		Collections.sort(newLin, Collections.reverseOrder(new TempStructC()));
		List<Tuple> newPi = new ArrayList<Tuple>();
		for (TempStruct te : newLin) {
			newPi.add(te.tuple);
		}
		return newPi;
	}

	public List<Tuple> reduce(List<Tuple> pi, TestSuite suite, int degree) {
		if (degree <= 0)
			return pi;
		List<Tuple> next = new ArrayList<Tuple>();
		List<Tuple> tminusway = this
				.getTwayTuplesInTestSuite(suite, degree - 1);
		for (Tuple tuple : tminusway) {
			boolean flag = true;
			List<Tuple> tway = tuple.getFatherTuplesByDegree(degree);
			for (Tuple tuf : tway) {
				if (!pi.contains(tuf)) {
					flag = false;
					break;
				}
			}
			if (flag) {
				next.add(tuple);
				// Iterator<Tuple> iter = pi.iterator();
				// while (iter.hasNext()) {
				// Tuple tuplef = iter.next();
				// if (tuplef.contains(tuple))
				// iter.remove();
				// }
			}
		}
		Iterator<Tuple> iter = pi.iterator();
		while (iter.hasNext()) {
			Tuple tuplef = iter.next();
			for (Tuple tuple : next) {
				if (tuplef.contains(tuple)) {
					iter.remove();
					break;
				}
			}
		}
		List<Tuple> nextIter = reduce(next, suite, degree - 1);
		List<Tuple> result = new ArrayList<Tuple>();
		for (Tuple tuple : pi) {
			result.add(tuple);
		}
		for (Tuple tuple : nextIter) {
			result.add(tuple);
		}
		return result;
	}

	public TestSuite generateSuite(int[] parameter, TestSuite suite,
			List<Tuple> pi) {
		TestSuite result = new TestSuiteImplement();
		Iterator<Tuple> iter = pi.iterator();
		int n = 0;
		while (iter.hasNext()) {
			if (n >= TOPNUMBER)
				break;
			Tuple tuple = iter.next();
			TestCase testCase = generateCase(tuple, parameter, suite, pi);
			if (testCase == null) {
				failure_inducing.add(tuple);
				return null;
			} else {
				result.addTest(testCase);
				// System.out.println(testCase.getStringOfTest());
				this.addtionalSuite.addTest(testCase);
			}
			n++;
		}
		return result;
	}

	public boolean contain(TestSuite suite, TestCase testCase) {
		for (int i = 0; i < suite.getTestCaseNum(); i++) {
			if (suite.getAt(i).equals(testCase))
				return true;
		}
		return false;
	}

	public TestCase generateCase(Tuple tuple, int[] parameter, TestSuite suite,
			List<Tuple> pi) {
		/* generate a part test case just contain the tuple */
		TestCase result = new TestCaseImplement(parameter.length);
		for (int i = 0; i < result.getLength(); i++) {
			result.set(i, -1);
		}
		for (int i = 0; i < tuple.getDegree(); i++) {
			result.set(tuple.getParamIndex()[i], tuple.getParamValue()[i]);
		}
		/* to complete the remain part */
		for (int i = 0; i < result.getLength(); i++) {
			if (result.getAt(i) == -1) {
				float min = -1;
				int value = -1;
				for (int j = 0; j < parameter[i]; j++) {
					float cur = suspiciousO(i, j, suite, pi);
					if (cur < min || min == -1) {
						min = cur;
						value = j;
					}
				}
				result.set(i, value);
			}
		}
		/**
		 * check whether the result is executed
		 */

		while (contain(suite, result)) {
			ITERATENUMBER--;
			if (ITERATENUMBER < 0)
				break;
			int i = new Random().nextInt(parameter.length);
			int k = 0;
			while (isTupleContainParameterValue(tuple, i, result.getAt(i))) {
				// System.out.println(tuple.toString());
				// System.out.println(i);
				// System.out.println(parameter.length);
				i = (i + 1) % parameter.length;
				k++;
				if (k == parameter.length) {
					ITERATENUMBER = -1;
					break;
				}
			}
			if (k == parameter.length)
				break;

			int value = result.getAt(i);
			float min = -1;
			// float cur = suspiciousO()
			for (int j = 0; j < parameter[i]; j++) {
				result.set(i, j);
				if (contain(suite, result))
					continue;
				float cur = suspiciousO(i, j, suite, pi);
				if (cur < min || min == -1) {
					min = cur;
					value = j;
				}
			}
			result.set(i, value);
		}

		if (ITERATENUMBER < 0)
			result = null;
		/**
		 * return NUll means the tuple is the failure-inducing combinations
		 */
		return result;
	}

	public boolean isTupleContainParameterValue(Tuple tuple, int parameter,
			int value) {
		for (int i = 0; i < tuple.getDegree(); i++) {
			int index = tuple.getParamIndex()[i];
			if (index < parameter)
				continue;
			if (index > parameter)
				return false;
			if (tuple.getParamValue()[i] == value)
				return true;
			else
				return false;
		}
		return false;
	}

	public TestSuite getAddtionalSuite() {
		return addtionalSuite;
	}

	public List<Tuple> getFailure_inducing() {
		return failure_inducing;
	}

	public List<Tuple> getTwayTuplesInTestCase(TestCase testCase, int degree) {
		Tuple root = new Tuple(testCase.getLength(), testCase);
		for (int i = 0; i < root.getDegree(); i++) {
			root.set(i, i);
		}
		List<Tuple> tuples = root.getChildTuplesByDegree(degree);
		return tuples;
	}

	public List<Tuple> getTwayTuplesInTestSuite(TestSuite suite, int degree) {
		HashSet<Tuple> tuples = new HashSet<Tuple>();
		for (int i = 0; i < suite.getTestCaseNum(); i++) {
			TestCase testCase = suite.getAt(i);
			if (testCase.testDescription() == TestCase.FAILED) {
				List<Tuple> temp = this.getTwayTuplesInTestCase(testCase,
						degree);
				for (Tuple tuple : temp) {
					tuples.add(tuple);
				}
			}
		}
		List<Tuple> result = new ArrayList<Tuple>();
		for (Tuple tuple : tuples) {
			result.add(tuple);
		}
		return result;
	}

	public List<Tuple> removeInPassed(List<Tuple> tuples, TestSuite suite) {
		List<Tuple> result = new ArrayList<Tuple>();
		for (Tuple tuple : tuples) {
			boolean addflag = true;
			for (int i = 0; i < suite.getTestCaseNum(); i++) {
				TestCase testCase = suite.getAt(i);
				if (testCase.testDescription() == TestCase.PASSED
						&& testCase.containsOf(tuple)) {
					addflag = false;
					break;
				}
			}
			if (addflag)
				result.add(tuple);
		}
		return result;
	}

	public List<Tuple> getFailreIndcuing() {
		return this.failure_inducing;
	}

	public static void main(String[] args) {
		int[][] cases = new int[][] { { 1, 1, 1, 1, 1, 1, 1, 1 } };
		TestSuite suite = new TestSuiteImplement();
		int[] parameters = new int[] { 3, 3, 3, 3, 3, 3, 3, 3 };

		for (int j = 0; j < cases.length; j++) {
			int[] testCase = cases[j];
			TestCase Case = new TestCaseImplement();
			((TestCaseImplement) Case).setTestCase(testCase);
			if (j == 0) {
				Case.setTestState(TestCase.FAILED);
				suite.addTest(Case);
			} else
				Case.setTestState(TestCase.PASSED);
			// suite.addTest(Case);
		}
		//
		// Tuple bugModel1 = new Tuple(2, suite.getAt(0));
		// bugModel1.set(0, 2);
		// bugModel1.set(1, 4);
		for (int i = 0; i < 8; i++) {

			Tuple bugModel2 = new Tuple(1, suite.getAt(0));
			bugModel2.set(0, i);
			// System.out.println(bugModel2.toString());

			CaseRunner caseRunner = new CaseRunnerWithBugInject();
			// ((CaseRunnerWithBugInject) caseRunner).inject(bugModel1);
			((CaseRunnerWithBugInject) caseRunner).inject(bugModel2);

			SpectrumBased sp = new SpectrumBased(caseRunner);

			sp.process(suite, parameters, 4);

			if (sp.failure_inducing == null)
				System.out.println("no t way failure inducing tuples:");
			else {
				System.out.println("failure inducing tuples:");
				for (Tuple tuple : sp.failure_inducing) {
					System.out.println(tuple.toString());
				}
			}
		}
		// System.out.println("addtional:");
		// for (int i = 0; i < sp.addtionalSuite.getTestCaseNum(); i++) {
		// System.out.println(sp.addtionalSuite.getAt(i).getStringOfTest());
		// }
	}

	public void test1() {
		int[][] cases = new int[][] { { 0, 0, 0, 0, }, { 1, 1, 1, 0 },
				{ 0, 1, 2, 0 }, { 1, 0, 0, 1 }, { 0, 0, 1, 1 }, { 1, 1, 2, 1 },
				{ 0, 1, 0, 2 }, { 1, 0, 1, 2 }, { 0, 0, 2, 2 }, { 0, 1, 0, 3 },
				{ 1, 0, 1, 3 }, { 1, 0, 2, 3 } };
		TestSuite suite = new TestSuiteImplement();
		int[] parameters = new int[] { 3, 3, 4, 5 };

		for (int i = 0; i < cases.length; i++) {
			int[] testCase = cases[i];
			TestCase Case = new TestCaseImplement();
			((TestCaseImplement) Case).setTestCase(testCase);
			if (i == 0 || i == 6 || i == 9)
				Case.setTestState(TestCase.FAILED);
			else
				Case.setTestState(TestCase.PASSED);
			suite.addTest(Case);
		}

		Tuple bugModel1 = new Tuple(2, suite.getAt(9));
		bugModel1.set(0, 0);
		bugModel1.set(1, 2);

		Tuple bugModel2 = new Tuple(2, suite.getAt(9));
		bugModel2.set(0, 0);
		bugModel2.set(1, 3);

		CaseRunner caseRunner = new CaseRunnerWithBugInject();
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel1);
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel2);

		SpectrumBased sp = new SpectrumBased(caseRunner);

		sp.process(suite, parameters, 2);
		if (sp.failure_inducing == null)
			System.out.println("no t way failure inducing tuples:");
		else {
			System.out.println("failure inducing tuples:");
			for (Tuple tuple : sp.failure_inducing) {
				System.out.println(tuple.toString());
			}
		}
		System.out.println("addtional:");
		for (int i = 0; i < sp.addtionalSuite.getTestCaseNum(); i++) {
			System.out.println(sp.addtionalSuite.getAt(i).getStringOfTest());
		}
	}
}

class TempStruct {
	public Tuple tuple;
	public float c;
	public float e;
}

class TempStructC implements Comparator<TempStruct> {

	public int compare(TempStruct o1, TempStruct o2) {
		if (o1.c > o2.c)
			return -1;
		else if (o1.c == o2.c)
			return 0;
		else
			return 1;
	}
}

class TempStructE implements Comparator<TempStruct> {

	public int compare(TempStruct o1, TempStruct o2) {
		if (o1.e > o2.e)
			return 1;
		else if (o1.e == o2.e)
			return 0;
		else
			return -1;
	}
}
