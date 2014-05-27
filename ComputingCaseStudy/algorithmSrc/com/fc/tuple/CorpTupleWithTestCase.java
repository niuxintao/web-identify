package com.fc.tuple;

import java.util.ArrayList;
import java.util.List;

import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.testObject.TestSuite;
import com.fc.testObject.TestSuiteImplement;

public class CorpTupleWithTestCase {
	private int[] param;

	public static int RN = 100;

	private TestCase wrongCase; // current wrong Case
	private List<Tuple> currentBugs; // new cases must not contain exsiting
										// found bug.

	// every tuple is responding to a testSuite
	private List<Tuple> tuples;

	private int iter;

	public void addIter() {
		iter++;
	}

	public List<Tuple> getTuples() {
		return tuples;
	}

	private List<TestSuite> generatedTestCases;

	public List<TestSuite> getGeneratedTestCases() {
		return generatedTestCases;
	}

	private void init(TestCase wrongCase, int[] param) {
		this.wrongCase = wrongCase;
		this.param = param;
		tuples = new ArrayList<Tuple>();
		generatedTestCases = new ArrayList<TestSuite>();
		iter = 0;
	}

	public CorpTupleWithTestCase(TestCase wrongCase, int[] param,
			List<Tuple> currentBugs) {
		init(wrongCase, param);
		this.currentBugs = currentBugs;
	}

	public CorpTupleWithTestCase(TestCase wrongCase, int[] param) {
		init(wrongCase, param);
		this.currentBugs = new ArrayList<Tuple>();
	}

	/**
	 * @param currentBugs
	 *            the currentBugs to set
	 */
	public void setCurrentBugs(List<Tuple> currentBugs) {
		this.currentBugs = currentBugs;
	}

	/**
	 * return the tested sequnce number, if not tested , return 0
	 */
	public int isTupleTested(Tuple tuple) {
		for (int i = 0; i < tuples.size(); i++) {
			if (tuples.get(i).equals(tuple))
				return i;
		}
		return -1;
	}

	public TestCase generateTestCaseContainTuple(Tuple tuple) {

		/* generate a part test case just contain the tuple */
		TestCase result = new TestCaseImplement(param.length);
		for (int i = 0; i < result.getLength(); i++) {
			result.set(i, -1);
		}
		for (int i = 0; i < tuple.getDegree(); i++) {
			result.set(tuple.getParamIndex()[i], tuple.getParamValue()[i]);
		}
		/* is it tested */
		int tesI = isTupleTested(tuple);
		TestCase lastCase;
		TestSuite suite;
		if (tesI == -1) {
			lastCase = wrongCase;
			suite = new TestSuiteImplement();
			generatedTestCases.add(suite);
			tuples.add(tuple);
			/* to complete the remain part */
			for (int i = 0; i < result.getLength(); i++) {
				if (result.getAt(i) == -1)
					result.set(i,
							(this.getItheElement(i, lastCase.getAt(i)) + iter)
									% param[i]);
			}
		} else {
			suite = this.generatedTestCases.get(tesI);
			lastCase = suite.getAt(suite.getTestCaseNum() - 1);
			/* to complete the remain part */
			for (int i = 0; i < result.getLength(); i++) {
				if (result.getAt(i) == -1)
					result.set(i, this.getItheElement(i, lastCase.getAt(i)));
			}
		}

		// /* to complete the remain part */
		// for (int i = 0; i < result.getLength(); i++) {
		// if (result.getAt(i) == -1)
		// result.set(i, this.getItheElement(i, lastCase.getAt(i)));
		// }

		/* remove the found bug to avoid it is failing */
		// System.out.println("rS");
		removeFoundBug(result, tuple);
		// System.out.println("rE");

		suite.addTest(result);

		return result;
	}

	public boolean isTheIndexInTuple(int index, Tuple tuple) {
		int[] indexes = tuple.getParamIndex();
		for (int i : indexes) {
			if (index > i) {
				continue;
			} else if (index == i) {
				return true;
			} else if (index < i) {
				break;
			}
		}
		return false;
	}

	public void removeFoundBug(TestCase testCase, Tuple containTuple) {
		int num = 0;
		while (isContainFoundBug(testCase)) {
			if (num > RN){
				testCase = null;
				break;
			}
			num++;
			for (Tuple bug : currentBugs) {
				if (testCase.containsOf(bug)) {
					int index = -1;
					for (int i : bug.getParamIndex()) {
						if (!isTheIndexInTuple(i, containTuple)) {
							index = i;
						}
					}
					int orignalValue = testCase.getAt(index);
					int newValue = getItheElement(index, orignalValue);
					testCase.set(index, newValue);
				}
			}
		}
	}

	public boolean isContainFoundBug(TestCase testCase) {
		boolean result = false;
		if (currentBugs != null)
			for (Tuple bug : currentBugs) {
				if (testCase.containsOf(bug)) {
					result = true;
					break;
				}
			}
		return result;
	}

	// not equal to the orignal one
	public int getItheElement(int index, int originalValue) {
		int result = originalValue;
		// do {
		result = (result + 1) % this.param[index];
		// }
		// while(result == this.wrongCase.getAt(index));
		return result;
	}

	public int[] getParam() {
		return param;
	}

	public void setParam(int[] param) {
		this.param = param;
	}
}
