package com.fc.caseRunner;

import com.fc.testObject.TestCase;

public class CaseRunnerBoolean implements CaseRunner {
	boolean[] passFail;
	int current = 0;

	public CaseRunnerBoolean(boolean[] passFail) {
		this.passFail = passFail;
		current = 0;
	}

	@Override
	public int runTestCase(TestCase testCase) {
		// TODO Auto-generated method stub
		if (current < passFail.length) {
			int temp = current;
			current++;
			return passFail[temp] ? 1 : 0;
		} else
			return 0;
	}

}
