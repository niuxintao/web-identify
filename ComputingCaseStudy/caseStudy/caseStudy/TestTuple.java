package caseStudy;

import java.util.ArrayList;
import java.util.List;


import com.fc.caseRunner.CaseRunner;
import com.fc.testObject.TestCase;
import com.fc.tuple.Tuple;

//it first indentify a fault, and then omit it in the later test cases

//exactly, we cann't easily judge which one was masked by others, so in this paper,

//we just think they are masked each other defaultly. 

//the no masked fault will not be masked.

//the masked will be find.

//the only ineffectively is that if we find two many faults, and no pass information, that way, 

//this system is too weak and 

public class TestTuple {

	public static final int TRYNUMBER = 10; // if too many faults, we should
											// redesign this program instead of
											// repair it.

	private CaseRunner runner;

	private int[] param;

	private List<TestCase> executed;

	public TestTuple(CaseRunner runner, int[] param, TestCase wrongCase) {
		this.runner = runner;

		this.param = param;

		executed = new ArrayList<TestCase>();
	}

	public List<TestCase> getExecuted() {
		return this.executed;
	}

	public boolean testTuple(Tuple tuple, int fault, TestCase wrongCase) {

		// int time = 0;
		boolean result = true;
		GenMaskTestCaseNewly generate = new GenMaskTestCaseNewly(wrongCase, param, tuple);

		while (true) {
			// time++;
			// if (time > TRYNUMBER) {
			// result = true;
			// break;
			// }

			if (generate.isStop()) {
				result = true;
				break;
			}
			TestCase testCase = generate.generateTestCaseContainTuple(tuple);
			executed.add(testCase);
			//
			// if (testCase == null) {// generate all the test cases that it
			// could
			// // be
			// result = true;
			// break;
			// }

		     System.out.println(testCase.getStringOfTest());

			int runresult = runner.runTestCase(testCase);
			
//			System.out.println(runresult);

			if (runresult == TestCase.PASSED) {
				result = true;
				break;
			} else if (runresult == TestCase.FAILED) {
				result = false;
				break;
			} else {
				// result = true;
				// break;
			}
		}

		return result;
	}

	public static void main(String[] args) {
	}
}
