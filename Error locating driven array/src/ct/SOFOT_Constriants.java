package ct;

import interaction.CoveringManage;
import interaction.DataCenter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.fc.caseRunner.CaseRunner;
import com.fc.caseRunner.CaseRunnerWithBugInject;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

public class SOFOT_Constriants {
	public List<Tuple> getBugs() {
		return bugs;
	}

	public static int TRYNUMBER = 3;

	private List<TestCase> executed;

	// private List<Tuple> constraints;

	private TestCase wrongCase;

	private int currentIndex = 0;

	private AETG_Constraints ac;

	public List<TestCase> getExecuted() {
		return executed;
	}

	private List<Tuple> bugs;
	
	private DataCenter dataCenter;

	public SOFOT_Constriants(DataCenter dataCenter) {
		executed = new ArrayList<TestCase>();
		bugs = new ArrayList<Tuple>();
		this.dataCenter = dataCenter;
		// constraints = new ArrayList<Tuple>();
	}

	public SOFOT_Constriants(DataCenter dataCenter, TestCase wrongCase, AETG_Constraints ac) {
		this.dataCenter = dataCenter;
		executed = new ArrayList<TestCase>();
		bugs = new ArrayList<Tuple>();
		// this.constraints = constriants;
		this.wrongCase = wrongCase;
		this.ac = ac;
	}

	public void setAETG_Constraints(AETG_Constraints ac) {
		this.ac = ac;
	}

	public void setWrongCase(TestCase wrongCase) {
		this.wrongCase = wrongCase;
	}

	public boolean isEnd() {
//		System.out.println(currentIndex + " " + DataCenter.n);
		return currentIndex == dataCenter.n;
	}

	public TestCase generateNext() {
		TestCase testCase = generateTestCase(wrongCase, dataCenter.param,
				this.currentIndex);

		executed.add(testCase);

		currentIndex++;

		return testCase;
	}

	public TestCase generateTestCase(TestCase wrongCase, int[] param,
			int currentIndex) {
		boolean isSat = false;
		int rmI = currentIndex;

		int[] testCase = new int[wrongCase.getLength()];
		for (int i = 0; i < testCase.length; i++)
			testCase[i] = wrongCase.getAt(i);
		testCase[currentIndex] = -1;

		int value = -1;

		int tempValue = -1;
		HashSet<Integer> cannot2 = new HashSet<Integer>();
		cannot2.add(wrongCase.getAt(currentIndex));

		while (!isSat) {
			if (tempValue != -1)
				cannot2.add(tempValue);
			value = ac.getBestValue(testCase, rmI, cannot2);
			tempValue = value;

			// judege if it is satisified
			List<Integer> indexes = new ArrayList<Integer>();
			TestCase testCaseForTuple = new TestCaseImplement(dataCenter.n);
			for (int j = 0; j < testCase.length; j++) {
				if (j == rmI) {
					testCaseForTuple.set(j, value);
					indexes.add(j);
				} else if (testCase[j] != -1) {
					testCaseForTuple.set(j, testCase[j]);
					indexes.add(j);
				}
			}
			Tuple tuple = new Tuple(indexes.size(), testCaseForTuple);
			tuple.setParamIndex(AETG_Constraints.convertIntegers(indexes));
			;

			isSat = !ac.isInvoude(rmI, value) || ac.isSatisifed(tuple);

		}

		testCase[rmI] = value;

		TestCaseImplement casetemple = new TestCaseImplement(
				wrongCase.getLength());
		casetemple.setTestCase(testCase);

		return casetemple;
	}

	public void analysis() {
		analysis(executed, wrongCase, dataCenter.param);
	}

	public void process(TestCase wrongCase, int[] parameters, CaseRunner runner) {
		// executed.add(wrongCase);
		// List<TestCase> analysisT = new ArrayList<TestCase>();
		for (int i = 0; i < wrongCase.getLength(); i++) {
			TestCase lastCase = wrongCase;
			TestCase testCase = generateTestCase(wrongCase, parameters, i,
					lastCase);
			testCase.setTestState(runner.runTestCase(testCase));
			executed.add(testCase);
			// analysisT.add(testCase);
		}
		analysis(executed, wrongCase, parameters);
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

	public void analysis(List<TestCase> array, TestCase wrongCase,
			int[] parameters) {
		Tuple tuple = new Tuple(0, wrongCase);
		// System.out.println(array.size());
		for (int i = 0; i < array.size(); i++) {
			TestCase testCase = array.get(i);
			if (testCase.testDescription() == TestCase.PASSED) {
				Tuple tem = new Tuple(1, wrongCase);
				tem.set(0, i);
				tuple = tuple.cat(tuple, tem);
				// System.out.println(tuple.toString());
				// failure-inducing + 1
			} else if (testCase.testDescription() == TestCase.FAILED) {

			}
		}

		this.bugs.add(tuple);
	}

	public static void main(String[] args) {
		int[] wrong = new int[] { 1, 1, 1, 1, 1, 1, 1, 1 };
		TestCase wrongCase = new TestCaseImplement();
		((TestCaseImplement) wrongCase).setTestCase(wrong);

		int[] wrong2 = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		TestCase wrongCase2 = new TestCaseImplement();
		((TestCaseImplement) wrongCase2).setTestCase(wrong2);

		int[] param = new int[] { 3, 3, 3, 3, 3, 3, 3, 3};
		
		DataCenter dataCenter = new DataCenter(param, 2);

		Tuple bugModel1 = new Tuple(2, wrongCase);
		bugModel1.set(0, 2);
		bugModel1.set(1, 5);

		Tuple bugModel2 = new Tuple(1, wrongCase2);
		bugModel2.set(0, 1);

		CaseRunner caseRunner = new CaseRunnerWithBugInject();
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel1);
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel2);
		
		
		List<Tuple> MFS = new ArrayList<Tuple> ();
		MFS.add(bugModel1);
		AETG_Constraints ac = new AETG_Constraints(dataCenter);
		ac.addConstriants(MFS);

		SOFOT_Constriants sc = new SOFOT_Constriants(dataCenter, wrongCase, ac);
		CoveringManage cm = new CoveringManage(dataCenter);
		// sc.process(testCase, DataCenter.param, caseRunner);

		while (!sc.isEnd()) {
			TestCase nextTestCase = sc.generateNext();
			
			int[] next = new int[nextTestCase.getLength()];
			for (int i = 0; i < next.length; i++) {
				next[i] = nextTestCase.getAt(i);
			}
			if (caseRunner.runTestCase(nextTestCase) == TestCase.PASSED) {
				cm.setCover(ac.unCovered, ac.coveredMark, next);
				nextTestCase.setTestState(TestCase.PASSED);
			}else
				nextTestCase.setTestState(TestCase.FAILED);
			
		}

		sc.analysis();
		System.out.println("bugs:");
		for (Tuple tuple : sc.bugs) {
			System.out.println(tuple.toString());
		}
		System.out.println("cases:");

		for (TestCase cases : sc.executed) {
			System.out.print(cases.getStringOfTest());
			System.out.println(" " + cases.testDescription());
		}
	}
}
