package maskSimulateExperiment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import newMaskAlgorithms.FIC_MASK_NEWLY;
import newMaskAlgorithms.FIC_MASK_SOVLER;
//import newMaskAlgorithms.OFOT_MASK_NEWLY;
//import newMaskAlgorithms.OFOT_MASK_SOVLER;
//import maskAlogrithms.CTA;
import maskAlogrithms.FIC;
//import maskAlogrithms.SOFOT;
import maskTool.EvaluateTuples;

import com.fc.caseRunner.CaseRunner;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
//import com.fc.testObject.TestSuite;
//import com.fc.testObject.TestSuiteImplement;
import com.fc.tuple.Tuple;

public class UnitSimulate {

	// public CaseRunner getCaseRunner() {
	//
	// CaseRunner caseRuner = null;
	//
	// return null;
	// }

	public final static int IGNORE_FIC = 0;
	public final static int IGNORE_OFOT = 1;
	public final static int IGNORE_CTA = 2;

	public HashMap<Integer, HashSet<Tuple>> getTuples() {
		return tuples;
	}

	public HashMap<Integer, List<TestCase>> getAdditionalTestCases() {
		return additionalTestCases;
	}

	public final static int DISTIN_FIC = 3;
	public final static int DISTIN_OFOT = 4;
	public final static int DISTIN_CTA = 5;

	public final static int MASK_FIC = 6;
	public final static int MASK_OFOT = 7;
	public final static int MASK_CTA = 8;

	public final static int NUM = 9;

	public final static int MASK_FIC_OLD = 9;
	public final static int MASK_OFOT_OLD = 10;
	public final static int MASK_CTA_OLD = 11;

	private HashMap<Integer, HashSet<Tuple>> tuples;
	private HashMap<Integer, List<TestCase>> additionalTestCases;

	private HashMap<Integer, List<EvaluateTuples>> evaluates;

	public HashMap<Integer, List<EvaluateTuples>> getEvaluates() {
		return evaluates;
	}

//	private List<Tuple> bugs;

//	public UnitSimulate(List<Tuple> bugs) {
//		tuples = new HashMap<Integer, HashSet<Tuple>>();
//		additionalTestCases = new HashMap<Integer, List<TestCase>>();
//		evaluates = new HashMap<Integer, List<EvaluateTuples>>();
//		this.bugs = bugs;
//
//		for (int i = 0; i < NUM; i++) {
//			HashSet<Tuple> tuple = new HashSet<Tuple>();
//			List<TestCase> testCase = new ArrayList<TestCase>();
//			List<EvaluateTuples> evas = new ArrayList<EvaluateTuples>();
//			tuples.put(i, tuple);
//			additionalTestCases.put(i, testCase);
//			evaluates.put(i, evas);
//		}
//	}

	public UnitSimulate() {
		tuples = new HashMap<Integer, HashSet<Tuple>>();
		additionalTestCases = new HashMap<Integer, List<TestCase>>();
		evaluates = new HashMap<Integer, List<EvaluateTuples>>();
		// this.bugs = bugs;

		for (int i = 0; i < NUM; i++) {
			HashSet<Tuple> tuple = new HashSet<Tuple>();
			List<TestCase> testCase = new ArrayList<TestCase>();
			List<EvaluateTuples> evas = new ArrayList<EvaluateTuples>();
			tuples.put(i, tuple);
			additionalTestCases.put(i, testCase);
			evaluates.put(i, evas);
		}
	}

//	public void setBugs(List<Tuple> bugs) {
//		this.bugs = bugs;
//	}

	public void testTraditional(int[] param, TestCase wrongCase,
			CaseRunner runner) {
		int added = runner instanceof DistinguishRunner ? 3 : 0;

		FIC fic = new FIC(wrongCase, param, runner);
		fic.FicNOP();

		this.additionalTestCases.get(IGNORE_FIC + added).addAll(
				fic.getExecuted());
		// for (int i = 0; i < fic.getExtraCases().getTestCaseNum(); i++)
		// this.additionalTestCases.get(IGNORE_FIC + added).add(
		// fic.getExtraCases().getAt(i));
		this.tuples.get(IGNORE_FIC + added).addAll(fic.getBugs());

//		EvaluateTuples eva = new EvaluateTuples();
//		eva.evaluate(bugs, fic.getBugs());
//		this.evaluates.get(IGNORE_FIC + added).add(eva);

		// System.out.println("non Mask runner");
		// for (Tuple tuple : fic.getBugs())
		// System.out.println(tuple.toString());

		// SOFOT ofot = new SOFOT();
		// ofot.process(wrongCase, param, runner);
		//
		// this.additionalTestCases.get(IGNORE_OFOT + added).addAll(
		// ofot.getExecuted());
		// this.tuples.get(IGNORE_OFOT + added).addAll(ofot.getBugs());
		//
		// // System.out.println("no Mask ofot");
		// // for (Tuple tuple : ofot.getBugs())
		// // System.out.println(tuple.toString());
		//
		// TestSuite suite = new TestSuiteImplement();
		// suite.addTest(wrongCase);
		// for (TestCase testCase : ofot.getExecuted())
		// suite.addTest(testCase);
		//
		// String[] classes = new String[] { "0", "1" };
		//
		// CTA cta = new CTA();
		//
		// String[] state = new String[suite.getTestCaseNum()];
		// for (int i = 0; i < state.length; i++) {
		// int runresult = runner.runTestCase(suite.getAt(i)) == TestCase.PASSED
		// ? 0
		// : 1;
		// state[i] = "" + runresult;
		// }
		// cta.process(param, classes, suite, state);
		// // System.out.println("no Mask cta");
		// if (cta.getBugs(1) != null)
		// this.tuples.get(IGNORE_CTA + added).addAll(cta.getBugs(1));
		// for (Tuple tuple : cta.getBugs(1))
		// System.out.println(tuple.toString());
		// return cta.getBugs(1);
	}

	public void testSovler(int[] param, TestCase wrongCase, BasicRunner runner,
			int code) {

		FIC_MASK_SOVLER ficmasknew = new FIC_MASK_SOVLER(wrongCase, param,
				runner, code);
		ficmasknew.FicNOP();

		this.additionalTestCases.get(MASK_FIC).addAll(ficmasknew.getExecuted());
		this.tuples.get(MASK_FIC).addAll(ficmasknew.getBugs());
//		EvaluateTuples eva = new EvaluateTuples();
//		eva.evaluate(bugs, ficmasknew.getBugs());
//		this.evaluates.get(MASK_FIC).add(eva);

		// System.out.println("Mask fic");
		// for (Tuple tuple : ficmasknew.getBugs())
		// System.out.println(tuple.toString());

		// OFOT_MASK_SOVLER ofot = new OFOT_MASK_SOVLER(wrongCase, param,
		// runner,
		// code);
		// ofot.process();
		//
		// this.additionalTestCases.get(MASK_OFOT).addAll(ofot.getExecuted());
		// this.tuples.get(MASK_OFOT).addAll(ofot.getBugs());
		//
		// // System.out.println("Mask ofot:");
		// // for (Tuple tuple : ofot.getBugs()) {
		// // System.out.println(tuple.toString());
		// // }
		//
		// TestSuite suite = new TestSuiteImplement();
		// suite.addTest(wrongCase);
		// for (TestCase testCase : ofot.getExecuted()) {
		// if (testCase.testDescription() == 0
		// || testCase.testDescription() == code)
		// suite.addTest(testCase);
		// }
		// String[] classes = new String[] { "0", "1" };
		// CTA cta = new CTA();
		// String[] state = new String[suite.getTestCaseNum()];
		// for (int i = 0; i < state.length; i++) {
		//
		// state[i] = "" + (suite.getAt(i).testDescription() == 0 ? 0 : 1);
		// // System.out.println(runresult);
		// }
		// cta.process(param, classes, suite, state);
		//
		// // System.out.println("Mask cta:");
		// if (cta.getBugs(1) != null)
		// // for (Tuple tuple : cta.getBugs(1))
		// this.tuples.get(MASK_CTA).addAll(cta.getBugs(1));
		// // System.out.println(tuple.toString());
	}

	public void testAugment(int[] param, TestCase wrongCase,
			BasicRunner runner, int code) {

		FIC_MASK_NEWLY ficmasknew = new FIC_MASK_NEWLY(wrongCase, param,
				runner, code);
		ficmasknew.FicNOP();

		this.additionalTestCases.get(MASK_FIC_OLD).addAll(
				ficmasknew.getExecuted());
		this.tuples.get(MASK_FIC_OLD).addAll(ficmasknew.getBugs());

//		EvaluateTuples eva = new EvaluateTuples();
//		eva.evaluate(bugs, ficmasknew.getBugs());
//		this.evaluates.get(MASK_FIC_OLD).add(eva);

		// System.out.println("Mask fic");
		// for (Tuple tuple : ficmasknew.getBugs())
		// System.out.println(tuple.toString());

		// OFOT_MASK_NEWLY ofot = new OFOT_MASK_NEWLY(wrongCase, param, runner,
		// code);
		// ofot.process();
		//
		// this.additionalTestCases.get(MASK_OFOT_OLD).addAll(ofot.getExecuted());
		// this.tuples.get(MASK_OFOT_OLD).addAll(ofot.getBugs());
		//
		// // System.out.println("Mask ofot:");
		// // for (Tuple tuple : ofot.getBugs()) {
		// // System.out.println(tuple.toString());
		// // }
		//
		// TestSuite suite = new TestSuiteImplement();
		// suite.addTest(wrongCase);
		// for (TestCase testCase : ofot.getExecuted()) {
		// if (testCase.testDescription() == 0
		// || testCase.testDescription() == code)
		// suite.addTest(testCase);
		// }
		// String[] classes = new String[] { "0", "1" };
		// CTA cta = new CTA();
		// String[] state = new String[suite.getTestCaseNum()];
		// for (int i = 0; i < state.length; i++) {
		//
		// state[i] = "" + (suite.getAt(i).testDescription() == 0 ? 0 : 1);
		// // System.out.println(runresult);
		// }
		// cta.process(param, classes, suite, state);
		//
		// // System.out.println("Mask cta:");
		// if (cta.getBugs(1) != null)
		// // for (Tuple tuple : cta.getBugs(1))
		// this.tuples.get(MASK_CTA_OLD).addAll(cta.getBugs(1));
		// // System.out.println(tuple.toString());
	}

	public void testScenoria() throws Exception {
		int[] param = new int[] { 3, 3, 3, 3, 3, 3, 3 };

		int[] wrong = new int[] { 1, 1, 1, 1, 1, 1, 1 };

		TestCase wrongCase = new TestCaseImplement();
		wrongCase.setTestState(TestCase.FAILED);
		((TestCaseImplement) wrongCase).setTestCase(wrong);

		int[] wrong2 = new int[] { 2, 2, 2, 2, 2, 2, 2 };

		TestCase wrongCase2 = new TestCaseImplement();
		wrongCase2.setTestState(TestCase.FAILED);
		((TestCaseImplement) wrongCase2).setTestCase(wrong2);

		Tuple bug1 = new Tuple(2, wrongCase);
		bug1.set(0, 0);
		bug1.set(1, 1);

		List<Tuple> bugs1 = new ArrayList<Tuple>();
		bugs1.add(bug1);

		Tuple bug2 = new Tuple(2, wrongCase2);
		bug2.set(0, 3);
		bug2.set(1, 4);

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

		ExperiementData exData = new ExperiementData();

		exData.setParam(param);
		exData.setHigherPriority(priority);
		exData.setBugs(bugs);

		List<Tuple> bench = new ArrayList<Tuple>();
		for (Integer key : bugs.keySet())
			bench.addAll(bugs.get(key));

//		this.setBugs(bench);

		int allNum = 0;
		for (Integer code : exData.getWrongCases().keySet()) {
			List<TestCase> wrongCases = exData.getWrongCases().get(code);
			for (TestCase testCase : wrongCases) {
				// System.out.println("testCase: " +
				// testCase.getStringOfTest());
				// System.out.println("distinguish");
				this.testTraditional(param, testCase, new DistinguishRunner(
						basicRunner, code));
				// System.out.println("ignore");
				this.testTraditional(param, testCase, new IgnoreRunner(
						basicRunner));

				// System.out.println("mask");
				this.testSovler(param, testCase, basicRunner, code);
				// break;
				allNum++;
			}
			// break;

		}

		for (int i : new int[] { 0, 3, 6 }) {
			System.out.println(i);
			// for (TestCase testCase : this.additionalTestCases.get(i))
			if (allNum > 0)
				System.out.println(this.additionalTestCases.get(i).size()
						/ (double) allNum);

		}

//		for (int i : new int[] { 0, 3, 6 }) {
//			System.out.println(i);
//			// for (TestCase testCase : this.additionalTestCases.get(i))
//			double metric = 0;
//			for (EvaluateTuples eva : this.evaluates.get(i)) {
//				metric += eva.getMetric();
//			}
//			if (allNum > 0)
//				System.out.println(metric / (double) allNum);
//
//		}

		// for(Tuple tuple : bench)
		// System.out.println(tuple.toString());

		for (int i : new int[] { 0, 3, 6 }) {
			System.out.println(i);
			HashSet<Tuple> tupl = this.tuples.get(i);
			List<Tuple> tuples = new ArrayList<Tuple>();
			tuples.addAll(tupl);
			EvaluateTuples eva = new EvaluateTuples();

			// for(Tuple tuple : tuples){
			// System.out.println("degree : " + tuple.getDegree());
			// System.out.println(tuple.toString());
			// }

			eva.evaluate(bench, tuples);
			System.out.println(eva.getMetric());
		}

	}

	public static void main(String[] args) throws Exception {
		UnitSimulate uS = new UnitSimulate();
		uS.testScenoria();
	}
}
