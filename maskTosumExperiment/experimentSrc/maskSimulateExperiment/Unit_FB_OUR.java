package maskSimulateExperiment;

import java.util.ArrayList;
import java.util.HashMap;
//import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.fc.coveringArray.DataCenter;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.testObject.TestSuite;
import com.fc.testObject.TestSuiteImplement;
import com.fc.tuple.Tuple;

import maskAlogrithms.CTA;
import maskTool.CoveringArrayGenFeedBack;
import maskTool.EvaluateTuples;
import newMaskAlgorithms.FIC_MASK_SOVLER;

public class Unit_FB_OUR {
	// first generate a covering array, Using the feedback the first time!
	// directly execute the feedback
	// for the first covering array, identify the error test case using our
	// compare the result ()
	// 30 times and get average
	// the third one gives the same test cases to the cta
	private HashSet<TestCase> additionalFIC;
	private HashSet<Tuple> bugsFIC;
	// private HashMap<Integer, HashSet<Tuple>> bugsFIC_record;
	private EvaluateTuples evaluateFIC;

	public HashSet<TestCase> getAdditionalFIC() {
		return additionalFIC;
	}

	public HashSet<Tuple> getBugsFIC() {
		return bugsFIC;
	}

	public EvaluateTuples getEvaluateFIC() {
		return evaluateFIC;
	}

	public List<TestCase> getAdditionalFB() {
		return additionalFB;
	}

	public HashSet<Tuple> getBugsFB() {
		return bugsFB;
	}

	public EvaluateTuples getEvaluateFB() {
		return evaluateFB;
	}

	private List<TestCase> additionalFB;
	private HashSet<Tuple> bugsFB;
	private EvaluateTuples evaluateFB;

	private EvaluateTuples evaluateFB_addtional;

	public Unit_FB_OUR() {
		this.additionalFB = new ArrayList<TestCase>();
		this.additionalFIC = new HashSet<TestCase>();
		this.bugsFB = new HashSet<Tuple>();
		this.bugsFIC = new HashSet<Tuple>();
		this.evaluateFB = new EvaluateTuples();
		this.evaluateFIC = new EvaluateTuples();
		this.evaluateFB_addtional = new EvaluateTuples();

	}

	public EvaluateTuples getEvaluateFB_addtional() {
		return evaluateFB_addtional;
	}

	public void testFB_OUR(List<Tuple> bugs, BasicRunner basicRunner,
			int[] param, int degree) throws Exception {
		DataCenter.init(param, degree);
		CoveringArrayGenFeedBack t = new CoveringArrayGenFeedBack(2, 0.9998);
		t.get(basicRunner);

		List<int[]> table = t.rsTable;
		List<int[]> firstTable = t.getFirstCoveringArray();

		bugsFB = t.getCurrentNewlyMFS();

		for (int[] test : table) {
			TestCaseImplement testCase = new TestCaseImplement(test);
			this.additionalFB.add(testCase);
		}

		List<Tuple> tuples = new ArrayList<Tuple>();
		tuples.addAll(bugsFB);
		evaluateFB.evaluate(bugs, tuples);

		List<TestCase> firstTestCases = new ArrayList<TestCase>();
		for (int[] test : firstTable) {
			TestCaseImplement testCase = new TestCaseImplement(test);
			firstTestCases.add(testCase);
		}

		this.testMASKFIC(bugs, firstTestCases, param, basicRunner);

		this.testAddtionalFB(bugs, basicRunner);

	}

	public void testMASKFIC(List<Tuple> bugs, List<TestCase> testCases,
			int[] param, BasicRunner runner) {

		for (TestCase wrongCase : testCases) {
			int code = runner.runTestCase(wrongCase);
			if (code != 0) {
				FIC_MASK_SOVLER ficmasknew = new FIC_MASK_SOVLER(wrongCase,
						param, runner, code);
				ficmasknew.FicNOP();

				this.additionalFIC.addAll(ficmasknew.getExecuted());
				this.bugsFIC.addAll(ficmasknew.getBugs());
			} else
				additionalFIC.add(wrongCase); // add the passed test case
		}

		List<Tuple> tuples = new ArrayList<Tuple>();
		tuples.addAll(bugsFIC);
		evaluateFIC.evaluate(bugs, tuples);

	}

	public void testAddtionalFB(List<Tuple> bench, BasicRunner basicRunner)
			throws Exception {
		HashSet<Integer> result = new HashSet<Integer>();
		TestSuite suite = new TestSuiteImplement();
		String[] state = new String[this.additionalFIC.size()];
		int i = 0;
		for (TestCase add : this.additionalFIC) {
			suite.addTest(add);
			int runresult = basicRunner.runTestCase(add);
			state[i] = "" + runresult;
			result.add(runresult);
			i++;
		}

		String[] classes = new String[result.size()];
		// classes[0] = "0";
		int j = 0;
		for (Integer r : result) {
			classes[j] = r + "";
			j++;
		}
		// for (int j = 0; j < classes.length; j++)
		// classes[j] = result + "";

		CTA cta = new CTA();

		cta.process(DataCenter.param, classes, suite, state);

		HashMap<Integer, List<Tuple>> bugs = cta.getBugs();

		HashSet<Tuple> mfs = new HashSet<Tuple>();
		for (Integer key : bugs.keySet()) {
			if (key != 0) {
				List<Tuple> curBugs = bugs.get(key);
				for (Tuple bug : curBugs) {
					mfs.add(bug);
				}
			}
		}

		List<Tuple> tuples = new ArrayList<Tuple>();
		tuples.addAll(mfs);
		evaluateFB_addtional.evaluate(bench, tuples);

	}

}
