package com.fc.TRT;

import java.util.ArrayList;
import java.util.List;

import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.testObject.TestSuite;
import com.fc.tuple.Tuple;

public class CorpTupleWithTestCase {
	private int[] param;

	private TestCase wrongCase; // current wrong Case
	private List<Tuple> currentBugs; // new cases must not contain exsiting
										// found bug.

	public CorpTupleWithTestCase(TestCase wrongCase, 
			int[] param, List<Tuple> currentBugs) {
		this.wrongCase = wrongCase;
		this.param = param;
		this.currentBugs = currentBugs;
	}
	public CorpTupleWithTestCase(TestCase wrongCase, 
			int[] param) {
		this.wrongCase = wrongCase;
		this.param = param;
		this.currentBugs = new ArrayList<Tuple> ();
	}

	/**
	 * @param currentBugs the currentBugs to set
	 */
	public void setCurrentBugs(List<Tuple> currentBugs) {
		this.currentBugs = currentBugs;
	}

	public TestCase generateTestCaseContainTuple(Tuple tuple) {
		TestCase result = new TestCaseImplement(param.length);
		for (int i = 0; i < result.getLength(); i++) {
			result.set(i, -1);
		}
		for (int i = 0; i < tuple.getDegree(); i++) {
			result.set(tuple.getParamIndex()[i], tuple.getParamValue()[i]);
		}
		for (int i = 0; i < result.getLength(); i++) {
			if (result.getAt(i) == -1)
				result.set(i, this.getItheElement(i, wrongCase.getAt(i)));
		}

		removeFoundBug(result, tuple);

		return result;
	}

	//assume that we have many values in a paramter [1,2,2] [1,3,3] ... [1,n,n]
	public TestCase generateTestCaseContainTuple(Tuple tuple , TestSuite tempSuite) {
		TestCase result = new TestCaseImplement(param.length);
		for (int i = 0; i < result.getLength(); i++) {
			result.set(i, -1);
		}
		for (int i = 0; i < tuple.getDegree(); i++) {
			result.set(tuple.getParamIndex()[i], tuple.getParamValue()[i]);
		}
		

		 TestCase lastCase = null;
		if(tempSuite.getTestCaseNum() > 0){
		    lastCase = tempSuite.getAt(tempSuite.getTestCaseNum()-1);
		   
		}
		else 
			lastCase = wrongCase;
		for (int i = 0; i < result.getLength(); i++) {
			if (result.getAt(i) == -1)
				result.set(i, this.getItheElement(i, lastCase.getAt(i)));
		}

		removeFoundBug(result, tuple);

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
		while (isContainFoundBug(testCase)) {
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
	//	do {
			result = (result +  1) % this.param[index];
			//}
		//while(result == this.wrongCase.getAt(index));
		return result;
	}

	public int[] getParam() {
		return param;
	}

	public void setParam(int[] param) {
		this.param = param;
	}
}
