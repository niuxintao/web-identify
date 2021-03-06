package caseStudy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import maskSimulateExperiment.BasicRunnerInMask;

import com.fc.testObject.TestCase;
import com.fc.tuple.Tuple;

//it first indentify a fault, and then omit it in the later test cases

//exactly, we cann't easily judge which one was masked by others, so in this paper,

//we just think they are masked each other defaultly. 

//the no masked fault will not be masked.

//the masked will be find.

//the only ineffectively is that if we find two many faults, and no pass information, that way, 

//this system is too weak and 

public class TestTupleSuspicious {

	public static final int TRYNUMBER = 10; // if too many faults, we should
											// redesign this program instead of
											// repair it.

	public static final int CANDIDATENUM = 3;

	private BasicRunnerInMask runner;

	private int[] param;

	private List<TestCase> executed;

	private HashMap<Integer, List<TestCase>> executedHash;

	public TestTupleSuspicious(BasicRunnerInMask runner, int[] param,
			TestCase wrongCase, int fault) {
		this.runner = runner;

		this.param = param;

		executed = new ArrayList<TestCase>();
		executedHash = new HashMap<Integer, List<TestCase>>();
		List<TestCase> existed = new ArrayList<TestCase>();
		existed.add(wrongCase);
		executedHash.put(fault, existed);
		executed.add(wrongCase);
	}

	public List<TestCase> getExecuted() {
		return this.executed;
	}

	public boolean testTuple(Tuple tuple, int fault, TestCase wrongCase,
			int[] otherFaults) {

		// System.out.println(tuple.toString());
		// int time = 0;
		boolean result = true;
		GenMaskTestCaseNewly generate = new GenMaskTestCaseNewly(wrongCase,
				param, tuple);

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

			// System.out.println("start");

			int[] givenNums = generate.tryTestCaseContainTuple(tuple,
					CANDIDATENUM);

			// System.out.println("end");

			List<TestCase> testCases = new ArrayList<TestCase>();
			for (int i = 0; i < CANDIDATENUM; i++) {
				// int givenNum = new Random().nextInt(array.size());
				int step = generate.getArray().get(givenNums[i]);

				int[] index = generate.takeKaccrodingtoStep(step);
				TestCase candidate = generate.genenteTestCase(index, tuple,
						wrongCase);

				testCases.add(candidate);
			}

			ComputingSuspicious cs = new ComputingSuspicious(executedHash);

			int indexSuspicous = cs.getTestCases(testCases, otherFaults);

			// cs.computingStrength(testCase, faultLevel);
			// cs

			// generate.
			TestCase testCase = testCases.get(indexSuspicous);

			// System.out.print(testCase.getStringOfTest() + " : ");

			generate.deleteGenerated(indexSuspicous);

			executed.add(testCase);
			//
			// if (testCase == null) {// generate all the test cases that it
			// could
			// // be
			// result = true;
			// break;
			// }
			System.out.println("executed");
			System.out.print(testCase.getStringOfTest() + " : ");
			int runresult = runner.runTestCase(testCase);

			System.out.println(runresult);

			if (!this.executedHash.containsKey(runresult)) {
				List<TestCase> values = new ArrayList<TestCase>();
				this.executedHash.put(runresult, values);
			}

			this.executedHash.get(runresult).add(testCase);

			if (runresult == 0) {
				result = true;
				break;
			} else if (runresult == fault) {
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
