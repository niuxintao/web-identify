package gandi;

import interaction.CoveringManage;
import interaction.DataCenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import location.CTA;

import com.fc.caseRunner.CaseRunner;
import com.fc.caseRunner.CaseRunnerWithBugInject;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.testObject.TestSuite;
import com.fc.testObject.TestSuiteImplement;
import com.fc.tuple.Tuple;

import ct.AETG_Constraints;
import ct.AETG_Constraints_Seeds;
import experiment.SimilarityMFS;

public class FD_CIT implements CT_process {

	private CaseRunner caseRunner;

	private HashSet<TestCase> overallTestCases;

	private HashSet<TestCase> regularCTCases;

	private HashSet<TestCase> identifyCases;

//	private HashSet<TestCase> failTestCase;

	private HashSet<Tuple> MFS;

	private DataCenter dataCenter;

	private int[] coveredMark; // schemas covered condition

	private int[] t_tested_coveredMark; // t-covered mark

	private long timeAll = 0;

	private long timeIden = 0;

	private long timeGen = 0;

	private int multipleMFS = 0;

	private double precise = 0;

	private double recall = 0;

	private double f_measure = 0;

	// if identified right, the covered is the same as those, otherwise, will
	// not do so.

	private int t_tested_covered = 0;

	private HashMap<Integer, Integer> coveredNums;
	
	private HashMap<Tuple, Integer> realIdentify;

	@Override
	public HashMap<Tuple, Integer> getRealIdentify() {
		return realIdentify;
	}

	@Override
	public HashMap<Integer, Integer> getCoveredNums() {
		return coveredNums;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gandi.CT_process#getCoveredMark()
	 */
	@Override
	public int[] getCoveredMark() {
		return coveredMark;
	}

	private CoveringManage cm;

	/*
	 * (non-Javadoc)
	 * 
	 * @see gandi.CT_process#getOverallTestCases()
	 */
	@Override
	public HashSet<TestCase> getOverallTestCases() {
		return overallTestCases;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gandi.CT_process#getMFS()
	 */
	@Override
	public HashSet<Tuple> getMFS() {
		return MFS;
	}

	public FD_CIT(DataCenter dataCenter, CaseRunner caseRunner) {
		this.caseRunner = caseRunner;
		overallTestCases = new HashSet<TestCase>();
		regularCTCases = new HashSet<TestCase>();
		identifyCases = new HashSet<TestCase>();
//		failTestCase = new HashSet<TestCase>();
		cm = new CoveringManage(dataCenter);
		MFS = new HashSet<Tuple>();
		coveredNums = new HashMap<Integer, Integer>();
		realIdentify = new HashMap<Tuple, Integer>();
		this.dataCenter = dataCenter;
	}

	public List<int[]> transform(HashSet<TestCase> overallTestCases2) {
		List<int[]> result = new ArrayList<int[]>();
		for (TestCase testCase : overallTestCases2) {
			result.add(((TestCaseImplement) testCase).getTestCase());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gandi.CT_process#run()
	 */
	@Override
	public void run() {
		AETG_Constraints aetg_all = new AETG_Constraints(dataCenter);
		long allTime = System.currentTimeMillis();

		while (aetg_all.unCovered > 0) {

			long geTime = System.currentTimeMillis();

			List<Tuple> mfss = new ArrayList<Tuple>();
			mfss.addAll(MFS);

			AETG_Constraints_Seeds aetg = new AETG_Constraints_Seeds(dataCenter);
			aetg.addSeeds(aetg_all.coveredMark, aetg_all.unCovered);
			aetg.addConstriants(mfss);
			aetg.process();

			geTime = System.currentTimeMillis() - geTime;
			this.timeGen += geTime;

			for (int[] test : aetg.coveringArray) {
				TestCase testCase = new TestCaseImplement(test);
				overallTestCases.add(testCase);
				regularCTCases.add(testCase);
//				System.out.println(testCase.getStringOfTest());
			}

			long ideTime = System.currentTimeMillis();

			// idenitfy

			String[] state = new String[this.overallTestCases.size()];
			TestSuite suite = new TestSuiteImplement();
			int cur = 0;
			for (TestCase testCase : overallTestCases) {
				int runresult = caseRunner.runTestCase(testCase);
				testCase.setTestState(runresult);
				suite.addTest(testCase);
				state[cur] = runresult == TestCase.PASSED ? "pass" : "fail";
				cur++;
			}

			CTA cta = new CTA();
			String[] classes = { "pass", "fail" };

			try {
				cta.process(dataCenter.param, classes, suite, state);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<Tuple> bugs = cta.getBugs();

			for (Tuple bug : bugs) {
				this.MFS.add(bug);
//				System.out.println(bug.toString());
			}

			ideTime = System.currentTimeMillis() - ideTime;
			this.timeIden += ideTime;

			for (int[] test : aetg.coveringArray) {
				TestCase testCase = new TestCaseImplement(test);
				if (caseRunner.runTestCase(testCase) == TestCase.PASSED) {
					aetg_all.unCovered = cm.setCover(aetg_all.unCovered,
							aetg_all.coveredMark, test);
				}
			}

			List<Tuple> mfssthen = new ArrayList<Tuple>();
			mfssthen.addAll(MFS);
			aetg_all.addConstriants(mfssthen);
		}

		this.coveredMark = aetg_all.coveredMark;

		allTime = System.currentTimeMillis() - allTime;
		this.timeAll += allTime;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gandi.CT_process#getT_tested_coveredMark()
	 */
	@Override
	public int[] getT_tested_coveredMark() {
		return t_tested_coveredMark;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gandi.CT_process#evaluate(java.util.List)
	 */
	@Override
	public void evaluate(List<Tuple> actualMFS) {
		List<Tuple> identified = new ArrayList<Tuple>();
		identified.addAll(MFS);

		// computingF-meausre

		double[] pAndR = SimilarityMFS.getPreciseAndRecall(identified,
				actualMFS);
		this.precise = pAndR[0];
		this.recall = pAndR[1];
		this.f_measure = SimilarityMFS.f_measue(pAndR[0], pAndR[1]);

		// computing multiple

		for (TestCase testCase : this.overallTestCases) {
			int contain = 0;
			for (Tuple acMFS : actualMFS) {
				if (testCase.containsOf(acMFS))
					contain++;
				if (contain > 1) {
					this.multipleMFS++;
					break;
				}
			}
		}

		// computingTcove
		computeT_cover(actualMFS);
		
		computeRealIdentify(actualMFS);

		computeCoveredNum();

	}

	public void computeRealIdentify(List<Tuple> actualMFS) {
		for (Tuple t : actualMFS) {
			if (this.MFS.contains(t)) {
				realIdentify.put(t, 1);
			}
		}
	}

	public void computeT_cover(List<Tuple> actualMFS) {
		t_tested_coveredMark = new int[dataCenter.coveringArrayNum];
		AETG_Constraints ac = new AETG_Constraints(dataCenter);
		//

		for (TestCase testCase : this.overallTestCases) {
			if (caseRunner.runTestCase(testCase) == TestCase.PASSED) {
				int[] test = new int[testCase.getLength()];
				for (int i = 0; i < test.length; i++) {
					test[i] = testCase.getAt(i);
				}
				ac.unCovered = cm.setCover(ac.unCovered, ac.coveredMark, test);
			}
		}

		List<Tuple> mfss = new ArrayList<Tuple>();
		for (Tuple mfs : MFS) {
			if (actualMFS.contains(mfs)) {
				mfss.add(mfs);
			}
		}
		ac.addConstriants(mfss);

		this.t_tested_covered = dataCenter.coveringArrayNum - ac.unCovered;
		this.t_tested_coveredMark = ac.coveredMark;
	}

	public void computeCoveredNum() {

		for (int i : coveredMark) {
			Integer I = new Integer(i);
			if (!this.coveredNums.containsKey(I)) {
				this.coveredNums.put(I, 1);
			} else {
				this.coveredNums.put(I,
						(this.coveredNums.get(I).intValue() + 1));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gandi.CT_process#getTimeAll()
	 */
	@Override
	public long getTimeAll() {
		return timeAll;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gandi.CT_process#getTimeIden()
	 */
	@Override
	public long getTimeIden() {
		return timeIden;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gandi.CT_process#getTimeGen()
	 */
	@Override
	public long getTimeGen() {
		return timeGen;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gandi.CT_process#getMultipleMFS()
	 */
	@Override
	public int getMultipleMFS() {
		return multipleMFS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gandi.CT_process#getPrecise()
	 */
	@Override
	public double getPrecise() {
		return precise;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gandi.CT_process#getRecall()
	 */
	@Override
	public double getRecall() {
		return recall;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gandi.CT_process#getF_measure()
	 */
	@Override
	public double getF_measure() {
		return f_measure;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gandi.CT_process#getT_tested_covered()
	 */
	@Override
	public int getT_tested_covered() {
		return t_tested_covered;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gandi.CT_process#getRegularCTCases()
	 */
	@Override
	public HashSet<TestCase> getRegularCTCases() {
		return regularCTCases;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gandi.CT_process#getIdentifyCases()
	 */
	@Override
	public HashSet<TestCase> getIdentifyCases() {
		return identifyCases;
	}

	public static void main(String[] args) {
		int[] wrong = new int[] { 1, 1, 1, 1, 1, 1, 1, 1 };
		TestCase wrongCase = new TestCaseImplement();
		((TestCaseImplement) wrongCase).setTestCase(wrong);

		int[] wrong2 = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		TestCase wrongCase2 = new TestCaseImplement();
		((TestCaseImplement) wrongCase2).setTestCase(wrong2);

		int[] param = new int[] { 3, 3, 3, 3, 3, 3, 3, 3 };

		DataCenter dataCenter = new DataCenter(param, 2);

		Tuple bugModel1 = new Tuple(2, wrongCase);
		bugModel1.set(0, 2);
		bugModel1.set(1, 5);

		Tuple bugModel2 = new Tuple(1, wrongCase2);
		bugModel2.set(0, 1);

		List<Tuple> acutal = new ArrayList<Tuple>();
		acutal.add(bugModel1);
		acutal.add(bugModel2);

		CaseRunner caseRunner = new CaseRunnerWithBugInject();
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel1);
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel2);

		CT_process elda = new FD_CIT(dataCenter, caseRunner);
		elda.run();
		elda.evaluate(acutal);

		System.out
				.println("testCase Num: " + elda.getOverallTestCases().size());
		for (TestCase testCase : elda.getOverallTestCases()) {
			System.out.println(testCase.getStringOfTest());
		}
		System.out.println("MFS");
		for (Tuple mfs : elda.getMFS())
			System.out.println(mfs.toString());

	}

}
