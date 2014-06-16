package newMaskAlgorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import maskSimulateExperiment.BasicRunner;

import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.testObject.TestSuite;
import com.fc.testObject.TestSuiteImplement;
import com.fc.tuple.Tuple;

public class OFOT_MASK_NEWLY {
	public List<Tuple> getBugs() {
		return bugs;
	}

	public static int TRYNUMBER = 3;


	protected TestTupleSuspicious testTuple;

	protected int level;

	protected int[] param;

	protected TestSuite extraCases;

	protected TestCase testCase;

	public List<TestCase> getExecuted() {
		return this.testTuple.getExecuted();
	}

	private List<Tuple> bugs;

	// protected BasicRunner runner;

	// public OFOT_MASK_NEWLY() {
	// executed = new ArrayList<TestCase>();
	// bugs = new ArrayList<Tuple>();
	//
	// }

	public OFOT_MASK_NEWLY(TestCase testCase, int[] param,
			BasicRunner caseRunner, int level) {
		bugs = new ArrayList<Tuple>();
		this.testCase = testCase;
		this.level = level;
		testCase.setTestState(level);
		this.param = param;
		bugs = new ArrayList<Tuple>();
		// this.caseRunner = caseRunner;
		extraCases = new TestSuiteImplement();
		testTuple = new TestTupleSuspicious(caseRunner, param, testCase, level);
		// runner = caseRunner;
	}

	public void process() {
		int[] result = new int[testCase.getLength()];

		for (int i = 0; i < testCase.getLength(); i++) {

			Tuple tuple = new Tuple(testCase.getLength() - 1, testCase);
			int location = 0;
			for (int j = 0; j < testCase.getLength(); j++) {
				if (j != i) {
					tuple.set(location, j);
					location++;
				}
			}
			boolean out = testTuple.testTuple(tuple, level, testCase);

			result[i] = out ? TestCase.PASSED : TestCase.FAILED;

			// TestCase lastCase = wrongCase;
			// TestCase testCase = generateTestCase(wrongCase, parameters, i,
			// lastCase);
			// int state = runner.runTestCase(testCase);
			// int tryN = -1;
			// while (state != TestCase.FAILED && state != TestCase.PASSED) {
			// tryN++;
			// if (tryN >= TRYNUMBER)
			// break;
			// lastCase = testCase;
			// testCase = generateTestCase(wrongCase, parameters, i, lastCase);
			// state = runner.runTestCase(testCase);
			// }
			// if (tryN >= TRYNUMBER)
			// state = TestCase.PASSED;
			//
			// testCase.setTestState(state);
			// executed.add(testCase);
		}

		analysis(result, testCase, param);
	}

	public TestCase generateTestCase(TestCase wrongCase, int[] parameters,
			int mutantIndex, TestCase lastCase) {
		TestCase casetemple = new TestCaseImplement(wrongCase.getLength());

		for (int i = 0; i < lastCase.getLength(); i++)
			casetemple.set(i, lastCase.getAt(i));

		casetemple.set(mutantIndex, (casetemple.getAt(mutantIndex) + 1)
				% parameters[mutantIndex]);

		return casetemple;

	}

	public void analysis(int[] result, TestCase wrongCase, int[] parameters) {
		Tuple tuple = new Tuple(0, wrongCase);
		for (int i = 0; i < result.length; i++) {
			if (result[i] == TestCase.PASSED) {
				Tuple tem = new Tuple(1, wrongCase);
				tem.set(0, i);
				tuple = tuple.cat(tuple, tem);
				// failure-inducing + 1
			} else if (result[i] == TestCase.FAILED) {

			}
		}
		this.bugs.add(tuple);
	}

	public static void main(String[] args) {
		int[] wrong = new int[] { 1, 1, 1, 1, 1, 1, 1, 1 };
		TestCase wrongCase = new TestCaseImplement();
		((TestCaseImplement) wrongCase).setTestCase(wrong);

		int[] wrong2 = new int[] { 2, 2, 2, 2, 2, 2, 2, 2 };
		TestCase wrongCase2 = new TestCaseImplement();
		((TestCaseImplement) wrongCase2).setTestCase(wrong2);

		int[] param = new int[] { 10, 10, 10, 10, 10, 10, 10, 10 };

		Tuple bug1 = new Tuple(2, wrongCase);
		bug1.set(0, 0);
		bug1.set(1, 1);

		List<Tuple> bugs1 = new ArrayList<Tuple>();
		bugs1.add(bug1);

		Tuple bug2 = new Tuple(1, wrongCase2);
		bug2.set(0, 3);

		List<Tuple> bugs2 = new ArrayList<Tuple>();
		bugs2.add(bug2);

		List<Integer> priority1 = new ArrayList<Integer>();
		priority1.add(2);
		List<Integer> priority2 = new ArrayList<Integer>();

		HashMap<Integer, List<Tuple>> bugs = new HashMap<Integer, List<Tuple>>();
		bugs.put(1, bugs1);
		bugs.put(2, bugs2);

		HashMap<Integer, List<Integer>> priority = new HashMap<Integer, List<Integer>>();

		priority.put(1, priority1);
		priority.put(2, priority2);

		BasicRunner basicRunner = new BasicRunner(priority, bugs);

		// ExperiementData exData = new ExperiementData();

		OFOT_MASK_NEWLY ofot = new OFOT_MASK_NEWLY(wrongCase, param,
				basicRunner, 1);
		ofot.process();

		System.out.println("bugs:");
		for (Tuple tuple : ofot.bugs) {
			System.out.println(tuple.toString());
		}
		System.out.println("cases:");

		for (TestCase cases : ofot.testTuple.getExecuted()) {
			System.out.print(cases.getStringOfTest());
			System.out.println(" " + cases.testDescription());
		}

	}
}
