package maskPracticalExperiment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import maskAlogrithms.CTA;
import maskSimulateExperiment.MaskRunner;
import maskTool.MaskANProcess;

import com.fc.coveringArray.DataCenter;
import com.fc.coveringArray.Process;
import com.fc.testObject.TestCaseImplement;
import com.fc.testObject.TestSuite;
import com.fc.testObject.TestSuiteImplement;
import com.fc.tuple.Tuple;

public class CTAbasic {
	private TableRunnerBasic caseRunner;

	public HashMap<Integer, List<Tuple>> expCTAIngore(Statistics statistic)
			throws Exception {
		// System.out.println("Classified tree analysis");

		int[] param = statistic.getParam();
		List<Integer> result = statistic.getResult();

		TableRunnerBasic caseRunner = getCaseRunner(result, param);
		DataCenter.init(param, 2);
		Process t = new Process(2, 0.9998);
		t.process();
		TestSuite suite = new TestSuiteImplement();
		String[] state = new String[t.rsTable.length];
		int i = 0;
		for (int[] row : t.rsTable) {
			TestCaseImplement testCase = new TestCaseImplement();
			testCase.setTestCase(row);
			int runresult = caseRunner.runTestCase(testCase);
			suite.addTest(testCase);
			state[i] = "" + runresult;
			// System.out.println(state[i]);
			i++;
		}

		String[] classes = new String[statistic.getBugCode().size() + 1];
		classes[0] = "0";
		for (int j = 1; j < classes.length; j++)
			classes[j] = statistic.getBugCode().get(j - 1).intValue() + "";

		CTA cta = new CTA();

		cta.process(param, classes, suite, state);

		return cta.getBugs();

	}

	public HashMap<Integer, List<Tuple>> expCTAOne(Statistics statistic)
			throws Exception {
		// System.out.println("Classified tree analysis");

		int[] param = statistic.getParam();
		List<Integer> temp = statistic.getResult();
		List<Integer> result = new ArrayList<Integer>();
		for (Integer it : temp) {
			if (it.intValue() == 0)
				result.add(0);
			else
				result.add(1);
		}

		TableRunnerBasic caseRunner = getCaseRunner(result, param);
		DataCenter.init(param, 2);
		Process t = new Process(2, 0.9998);
		t.process();
		TestSuite suite = new TestSuiteImplement();
		String[] state = new String[t.rsTable.length];
		int i = 0;
		for (int[] row : t.rsTable) {
			TestCaseImplement testCase = new TestCaseImplement();
			testCase.setTestCase(row);
			int runresult = caseRunner.runTestCase(testCase);
			suite.addTest(testCase);
			state[i] = "" + runresult;
			// System.out.println(state[i]);
			i++;
		}

		String[] classes = { "0", "1" };

		CTA cta = new CTA();

		cta.process(param, classes, suite, state);

		return cta.getBugs();
	}

	private TableRunnerBasic getCaseRunner(List<Integer> result,
			int[] parameters) {
		// if (caseRunner == null) {
		caseRunner = new TableRunnerBasic(result, parameters);
		// }
		return caseRunner;
	}

	public HashMap<Integer, List<Tuple>> expCTAOur(Statistics statistic)
			throws Exception {
		// System.out.println("Classified tree analysis");
		HashMap<Integer, List<Tuple>> bugs = new HashMap<Integer, List<Tuple>>();
		List<Integer> result = statistic.getResult();
		int[] param = statistic.getParam();
		List<Integer> wrongCodes = statistic.getBugCode();
		for (int wrongCode : wrongCodes) {
			MaskRunner caseRunner = getCaseRunnerMask(result, param, wrongCode);
			DataCenter.init(param, 2);
			MaskANProcess t = new MaskANProcess(2, 0.9998, caseRunner);
			t.process();
			TestSuite suite = new TestSuiteImplement();
			String[] state = new String[t.rsTable.length];

			int i = 0;
			for (int[] row : t.rsTable) {
				TestCaseImplement testCase = new TestCaseImplement();
				testCase.setTestCase(row);
				int runresult = caseRunner.runTestCase(testCase);

				suite.addTest(testCase);
				if (runresult == -1)
					state[i] = "" + 0;
				else
					state[i] = "" + 1;
				i++;
			}

			CTA cta = new CTA();
			String[] classes = { "0", "1" };

			cta.process(param, classes, suite, state);
			if (cta.getBugs(1) != null)
				bugs.put(wrongCode, cta.getBugs(1));
		}

		return bugs;
	}

	private MaskRunner getCaseRunnerMask(List<Integer> result,
			int[] parameters, int wrongCode) {
		TableRunnerBasic runner = new TableRunnerBasic(result, parameters);
		MaskRunner caseRunner = new MaskRunner(runner, wrongCode);
		return caseRunner;
	}

	public void test() throws Exception {

		// ReadInput in = new ReadInput();

		// int[] wrongCodes = new int[] { 1, 2, 3 };

		// in.readTestCases("./result.txt");

		Statistics statistic = new Statistics();
		statistic.readTestCases("./resultNew.txt");
		statistic.readBugCodeAndLowePriority("./FaultLevel.txt");

		// ReadInput in = new ReadInput();
		// in.readBugs("./bug_ot" + wrongCode + ".txt");

		// List<Tuple> bugs = in.getBugs();

		expCTAIngore(statistic);
		expCTAOne(statistic);
		expCTAOur(statistic);
	}

	public static void main(String[] args) throws Exception {
		CTAbasic tc = new CTAbasic();
		tc.test();
	}

}
