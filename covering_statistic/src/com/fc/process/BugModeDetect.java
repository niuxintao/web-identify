package com.fc.process;

import com.fc.testObject.TestSuite;
import com.fc.tuple.Tuple;

public class BugModeDetect {
	public int getTestCaseIndexContainBubMode(Tuple tuple, TestSuite suite) {
		int index = -1;

		for (int i = 0; i < suite.getTestCaseNum(); ++i) {
			if (suite.getAt(i).containsOf(tuple)) {
				index = i;
				break;
			}
		}

		return index;
	}
}