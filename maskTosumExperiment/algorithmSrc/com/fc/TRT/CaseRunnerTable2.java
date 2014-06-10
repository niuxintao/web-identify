package com.fc.TRT;

import java.util.ArrayList;
import java.util.List;

import com.fc.testObject.TestCase;
import com.fc.testObject.TestSuite;

public class CaseRunnerTable2 implements CaseRunner {

	@Override
	public void runTestSuite(TestSuite suite) {
		// TODO Auto-generated method stub

	}

	@Override
	public TestSuite getRunnedTestSuite() {
		// TODO Auto-generated method stub
		return null;
	}

	private List<Integer> result;
	private int[] index;
	private List<Integer> lowerPriority;

	public CaseRunnerTable2(List<Integer> result, int[] parameters, List<Integer> lowerPriority) {
		this.result = result;
		this.lowerPriority = lowerPriority;
		this.index = new int[parameters.length];
		this.index[parameters.length - 1] = 1;

		for (int i = parameters.length - 2; i >= 0; i--) {
			int value = parameters[i + 1];
			index[i] = value * index[i + 1];
		}
	}

	public CaseRunnerTable2() {
		result = new ArrayList<Integer>();
	}

	@Override
	public void runTestCase(TestCase testCase) {
		// TODO Auto-generated method stub
		int index = 0;
		for (int i = 0; i < testCase.getLength(); i++) {
			index += testCase.getAt(i) * this.index[i];
		}
		int res = result.get(index);
		int result = TestCase.FAILED;

		if (res == 0)
			result =  TestCase.PASSED;

		for (Integer i : this.lowerPriority)
			if (res == i)
				result =  TestCase.PASSED;
		
		
		testCase.setTestState(result);
	}
}
