package com.fc.caseRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fc.caseRunner.CaseRunner;
import com.fc.testObject.TestCase;
import com.fc.tuple.Tuple;

public class BasicRunner implements CaseRunner {

	// integer : current run fault code,  List<Integer> : the higher fault code which can mask the current integer
	private HashMap<Integer, List<Integer>> higherPriority;
	private HashMap<Integer, List<Tuple>> bugs;

	public BasicRunner(HashMap<Integer, List<Integer>> higherPriority,
			HashMap<Integer, List<Tuple>> bugs) {
		this.higherPriority = higherPriority;
		this.bugs = bugs;
	}

	// public BasicRunner() {
	//
	// }

	@Override
	public int runTestCase(TestCase testCase) {
		// TODO Auto-generated method stub
		List<Integer> candidate = new ArrayList<Integer>();
		for (Integer code : bugs.keySet()) {
			List<Tuple> subBugs = bugs.get(code);
			for (Tuple subBug : subBugs) {
				if (testCase.containsOf(subBug)) {
					candidate.add(code);
					break;
				}
			}
		}

		if (candidate.size() == 0) {
			testCase.setTestState(0);
			return 0;
		}

		for (Integer code : candidate) {
			List<Integer> hihger = this.higherPriority.get(code);
			boolean flag = true;
			for (Integer remain : candidate) {
				if (hihger.contains(remain)) {
					flag = false;
					break;
				}
			}
			if (flag) {
				testCase.setTestState(code);
				return code;
			}
		}

		return -1;
	}

	public static void main(String[] args) {

	}

}
