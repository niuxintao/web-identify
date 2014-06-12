package com.fc.testObject;

import com.fc.tuple.Tuple;

/**
 * the class implements the TestCase interface
 * 
 * @author Xintao Niu
 * 
 */
public class TestCaseImplement implements TestCase {
	private int[] testCase;
	private int testInfo;

	public TestCaseImplement(int degree) {
		this.testInfo = TestCase.UNTESTED;
		testCase = new int[degree];
	}

	public TestCaseImplement(int[] testCase) {
		this.testInfo = TestCase.UNTESTED;
		this.testCase = testCase;
	}

	public TestCaseImplement() {
		this.testInfo = TestCase.UNTESTED;
	}

	@Override
	public boolean equals(Object b) {
		if (!(b instanceof TestCase))
			return false;
		TestCase bt = (TestCase) b;
		if (this.getLength() != bt.getLength())
			return false;
		for (int i = 0; i < this.getLength(); i++)
			if (this.getAt(i) != bt.getAt(i))
				return false;
		return true;
	}

	@Override
	public int getAt(int index) {
		// TODO Auto-generated method stub
		return testCase[index];
	}

	@Override
	public void set(int index, int value) {
		// TODO Auto-generated method stub
		testCase[index] = value;
	}

	@Override
	public int hashCode() {
		String string = "";
		for (int i : this.testCase) {
			string += i;
		}
		return string.hashCode();
	}

	@Override
	public int testDescription() {
		// TODO Auto-generated method stub
		return testInfo;
	}

	@Override
	public void setTestState(int state) {
		// TODO Auto-generated method stub
		this.testInfo = state;
	}

	public int[] getTestCase() {
		return testCase;
	}

	public void setTestCase(int[] testCase) {
		this.testCase = testCase;
	}

	@Override
	public int getLength() {
		// TODO Auto-generated method stub
		return this.testCase.length;
	}

	@Override
	public String getStringOfTest() {
		// TODO Auto-generated method stub
		String str = "";
		for (int i = 0; i < this.getLength(); i++) {
			str += this.getAt(i) + " ";
		}
		return str;
	}

	@Override
	public boolean containsOf(Tuple tuple) {
		// TODO Auto-generated method stub
		int[] index = tuple.getParamIndex();
		int[] value = tuple.getParamValue();

		for (int i = 0; i < tuple.getDegree(); i++) {
			if (index[i] >= this.getLength())
				return false;
			if (this.getAt(index[i]) != value[i])
				return false;
		}
		return true;
	}

	@Override
	public TestCase copy() {
		// TODO Auto-generated method stub
		TestCase result = new TestCaseImplement(this.getLength());
		for (int i = 0; i < this.getLength(); i++) {
			result.set(i, this.getAt(i));
		}
		return result;
	}

}
