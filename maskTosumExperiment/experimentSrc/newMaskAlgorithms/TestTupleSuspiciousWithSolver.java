package newMaskAlgorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import maskSimulateExperiment.BasicRunner;
import maskTool.StrengthMatrix;

import com.fc.testObject.TestCase;
import com.fc.tuple.Tuple;

//it first indentify a fault, and then omit it in the later test cases

//exactly, we cann't easily judge which one was masked by others, so in this paper,

//we just think they are masked each other defaultly. 

//the no masked fault will not be masked.

//the masked will be find.

//the only ineffectively is that if we find two many faults, and no pass information, that way, 

//this system is too weak and 

public class TestTupleSuspiciousWithSolver {

	public static final int TRYNUMBER = 10; // if too many faults, we should
											// redesign this program instead of
											// repair it.

	public static final int CANDIDATENUM = 3;

	private BasicRunner runner;

	private int[] param;

	private int[] Vindex;

	// private List<TestCase> executed;

	private HashMap<Integer, List<TestCase>> executedHash;

	public TestTupleSuspiciousWithSolver(BasicRunner runner, int[] param,
			TestCase wrongCase, int fault) {
		this.runner = runner;

		this.param = param;

		Vindex = new int[param.length];
		int curr = 0;
		for (int i = 0; i < Vindex.length; i++) {
			Vindex[i] = curr;
			curr += param[i];
		}

		// executed = new ArrayList<TestCase>();
		executedHash = new HashMap<Integer, List<TestCase>>();
//		for (Integer key : faults) {
//			List<TestCase> testCase = new ArrayList<TestCase>();
//			executedHash.put(key, testCase);
//		}
		List<TestCase> existed = new ArrayList<TestCase>();
		existed.add(wrongCase);
		executedHash.put(fault, existed);
//		executedHash.get(fault).addAll(existed);
		// executed.add(wrongCase);
	}

	public boolean testTuple(Tuple tuple, int fault, TestCase wrongCase) {
		StrengthMatrix stengthMatrix = new StrengthMatrix(executedHash, Vindex,
				Vindex);

		int[] fixed = getFixedPart(tuple);

		TransILP ilp = new TransILP(param, Vindex, stengthMatrix.getMaxtrix(),
				fault, fixed);
		// ilp.run();
		// System.out.println(tuple.toString());
		// int time = 0;
		boolean result = true;
		// GenMaskTestCaseNewly generate = new GenMaskTestCaseNewly(wrongCase,
		// param, tuple);

		while (true) {
			// time++;
			// if (time > TRYNUMBER) {
			// result = true;
			// break;
			// }
			// if (generate.isStop()) {
			// result = true;
			// break;
			// }
			TestCase testCase = ilp.run();
			if (testCase == null) {
				result = true;
				break;
			}

			// System.out.println("start");

			// System.out.print(testCase.getStringOfTest() + " : ");

			// executed.add(testCase);
			//
			// if (testCase == null) {// generate all the test cases that it
			// could
			// // be
			// result = true;
			// break;
			// }
			// System.out.println("executed");
			// System.out.print(testCase.getStringOfTest() + " : ");
			int runresult = runner.runTestCase(testCase);
			stengthMatrix.addTestCase(runresult, testCase);
			for (int i = 0; i < testCase.getLength(); i++) {
				int value = testCase.getAt(i);
				stengthMatrix.updateMaxtrixS(i, value);
			}

			// System.out.println(runresult);

			// if (!this.executedHash.containsKey(runresult)) {
			// List<TestCase> values = new ArrayList<TestCase>();
			// this.executedHash.put(runresult, values);
			// }

			// this.executedHash.get(runresult).add(testCase);

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

		stengthMatrix.merge();
		this.executedHash = stengthMatrix.getExecuted();
		return result;
	}

	private int[] getFixedPart(Tuple tuple) {
		int[] fixed = new int[tuple.getDegree()];
		for (int i = 0; i < fixed.length; i++) {
			int index = tuple.getParamIndex()[i];
			int value = tuple.getParamValue()[i];
			int matrxiIndex = Vindex[index] + value;
			fixed[i] = matrxiIndex;
		}
		return fixed;
	}

	public List<TestCase> getExecuted() {
		List<TestCase> executed = new ArrayList<TestCase>();
		for (Integer key : this.executedHash.keySet())
			executed.addAll(this.executedHash.get(key));

		return executed;
	}

	public static void main(String[] args) {
	}
}
