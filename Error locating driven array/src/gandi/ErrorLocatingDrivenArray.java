package gandi;

import interaction.CoveringManage;
import interaction.DataCenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import locatConstaint.SOFOT_Constriants;

import com.fc.caseRunner.CaseRunner;
import com.fc.caseRunner.CaseRunnerWithBugInject;
//import com.fc.coveringArray.CoveringManage;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

import ct.AETG_Constraints;
import experiment.SimilarityMFS;

public class ErrorLocatingDrivenArray implements CT_process {

	protected List<Tuple> actualMFS;

	protected CaseRunner caseRunner;

	protected HashSet<TestCase> overallTestCases;

	protected HashSet<TestCase> regularCTCases;

	protected HashSet<TestCase> identifyCases;

	protected HashSet<TestCase> failTestCase;

	protected HashSet<Tuple> MFS;

	protected DataCenter dataCenter;

	protected int[] coveredMark; // schemas covered condition

	private int[] t_tested_coveredMark; // t-covered mark

	protected long timeAll = 0;

	protected long timeIden = 0;

	protected long timeGen = 0;

	private int multipleMFS = 0;

	private double precise = 0;

	private double recall = 0;

	private double f_measure = 0;
	
	protected int identificationTimes = 0;
	
	public int getIdentificationTimes(){
		return identificationTimes;
	}

	// protected List<Tuple> UnsafeVaules;
	//
	// public void setUnSafeValues(List<Tuple> UnsafeValues){
	// this.UnsafeVaules = UnsafeValues;
	// }

	public double getMultip_precise() {
		return multip_precise;
	}

	public double getMultip_recall() {
		return multip_recall;
	}

	public double getMultip_f_measure() {
		return multip_f_measure;
	}

	public List<TestCase> getFormultipleCases() {
		return FormultipleCases;
	}

	public List<Tuple> getIdentifiedMFSForMultiple() {
		return identifiedMFSForMultiple;
	}

	public List<Tuple> getActualRealMFSInMultiple() {
		return actualRealMFSInMultiple;
	}

	private double multip_precise = 0;

	private double multip_recall = 0;

	private double multip_f_measure = 0;

	private double multipe_found = 0;

	private double multipe_found_percent = 0;

	private double helpedInTheNextRun = 0;

	private double helpedInTheNextRun_percen = 0;

	// if identified right, the covered is the same as those, otherwise, will
	// not do so.

	protected double encounterUnsafe = 0;

	protected double triggerDifferentUnsafe = 0;
	
	protected List<Tuple> containNow = null;

	public double getEncounterUnsafe() {
		return encounterUnsafe;
	}

	public double getTriggerDifferentUnsafe() {
		return triggerDifferentUnsafe;
	}

	public double getMultipe_found() {
		return multipe_found;
	}

	public double getMultipe_found_percent() {
		return multipe_found_percent;
	}

	public double getHelpedInTheNextRun() {
		return helpedInTheNextRun;
	}

	public double getHelpedInTheNextRun_percen() {
		return helpedInTheNextRun_percen;
	}

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

	protected CoveringManage cm;

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

	public List<TestCase> FormultipleCases;
	public List<Tuple> identifiedMFSForMultiple;
	public List<Tuple> actualRealMFSInMultiple;

	public ErrorLocatingDrivenArray(List<Tuple> actualMFS,
			DataCenter dataCenter, CaseRunner caseRunner) {
		this.caseRunner = caseRunner;
		overallTestCases = new HashSet<TestCase>();
		regularCTCases = new HashSet<TestCase>();
		identifyCases = new HashSet<TestCase>();
		failTestCase = new HashSet<TestCase>();
		cm = new CoveringManage(dataCenter);
		MFS = new HashSet<Tuple>();
		coveredNums = new HashMap<Integer, Integer>();
		realIdentify = new HashMap<Tuple, Integer>();
		this.dataCenter = dataCenter;

		identifiedMFSForMultiple = new ArrayList<Tuple>();
		actualRealMFSInMultiple = new ArrayList<Tuple>();
		FormultipleCases = new ArrayList<TestCase>();

		this.actualMFS = actualMFS;

	}

	public void setActualMFS(List<Tuple> actualMFS) {
		this.actualMFS = actualMFS;
	}

	public ErrorLocatingDrivenArray(DataCenter dataCenter, CaseRunner caseRunner) {
		this.caseRunner = caseRunner;
		overallTestCases = new HashSet<TestCase>();
		regularCTCases = new HashSet<TestCase>();
		identifyCases = new HashSet<TestCase>();
		failTestCase = new HashSet<TestCase>();
		cm = new CoveringManage(dataCenter);
		MFS = new HashSet<Tuple>();
		coveredNums = new HashMap<Integer, Integer>();
		realIdentify = new HashMap<Tuple, Integer>();
		this.dataCenter = dataCenter;

		identifiedMFSForMultiple = new ArrayList<Tuple>();
		actualRealMFSInMultiple = new ArrayList<Tuple>();
		FormultipleCases = new ArrayList<TestCase>();

	}

	protected boolean isIn(Tuple t, List<Tuple> tuples) {
		for (Tuple t1 : tuples) {
			if (t.equals(t1))
				return true;
		}
		return false;
	}

	/**
	 * -1, not contain other shcemas
	 * 
	 * 
	 * 0, contain other schemas, but also contain my schemas;
	 * 
	 * 
	 * 1, contain other schemas, but not contain this schema.
	 * 
	 * @return
	 */
	public int containDifferentNot(List<Tuple> t_nows, TestCase testCase) {

		for (Tuple t_now : t_nows) {
			if (testCase.containsOf(t_now)) {
				for (Tuple t : this.actualMFS) {
					if (!isIn(t, t_nows)) {
						if (testCase.containsOf(t)) {
							return 0;
						}
					}
				}
				return -1;
			}
		}
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gandi.CT_process#run()
	 */
	@Override
	public void run() {

		AETG_Constraints ac = new AETG_Constraints(dataCenter);

		// coverage is equal to 0 is ending
		while (ac.unCovered > 0) {
			long allTime = System.currentTimeMillis();
			long geTime = System.currentTimeMillis();

			int[] test = ac.getNextTestCase();

			geTime = System.currentTimeMillis() - geTime;
			this.timeGen += geTime;

			TestCase testCase = new TestCaseImplement(test);
			overallTestCases.add(testCase);
			regularCTCases.add(testCase);
			// System.out.println("aetg" + testCase.getStringOfTest() +
			// " "+ac.unCovered);

			if (caseRunner.runTestCase(testCase) == TestCase.PASSED) {
				ac.unCovered = cm.setCover(ac.unCovered, ac.coveredMark, test);
			} else {
				this.failTestCase.add(testCase);

				long ideTime = System.currentTimeMillis();

				int contain = 0;
				List<Tuple> templ = new ArrayList<Tuple>();
				for (Tuple acMFS : actualMFS) {
					if (testCase.containsOf(acMFS))
						contain++;
					if (contain > 1) {
						break;
					}
				}

				containNow = new ArrayList<Tuple>();
				for (Tuple acMFS : actualMFS) {
					if (testCase.containsOf(acMFS))
						containNow.add(acMFS);
				}

				if (contain > 1) {
					for (Tuple acMFS : actualMFS) {
						if (testCase.containsOf(acMFS))
							templ.add(acMFS);
					}
				}

				this.actualRealMFSInMultiple.addAll(templ);

				List<Tuple> mfs = getMFS(ac, testCase);

				if (contain > 1) {
					this.identifiedMFSForMultiple.addAll(mfs);
				}

				ideTime = System.currentTimeMillis() - ideTime;
				this.timeIden += ideTime;

				ac.addConstriants(mfs);
				this.MFS.addAll(mfs);
				// setCoverage(mfs);
			}

			allTime = System.currentTimeMillis() - allTime;
			this.timeAll += allTime;
		}

		this.coveredMark = ac.coveredMark;

		this.evaluate_multiple();
	}

	public List<Tuple> getMFS(AETG_Constraints ac, TestCase testCase) {
		
		identificationTimes ++;

		SOFOT_Constriants sc = new SOFOT_Constriants(dataCenter, testCase, ac);
		// sc.process(testCase, DataCenter.param, caseRunner);

		while (!sc.isEnd()) {
			TestCase nextTestCase = sc.generateNext();
			identifyCases.add(nextTestCase);

			overallTestCases.add(nextTestCase);
			// System.out.println("ofot" +
			// nextTestCase.getStringOfTest());

			int[] next = new int[nextTestCase.getLength()];
			for (int i = 0; i < next.length; i++) {
				next[i] = nextTestCase.getAt(i);
			}
			if (caseRunner.runTestCase(nextTestCase) == TestCase.PASSED) {
				ac.unCovered = cm.setCover(ac.unCovered, ac.coveredMark, next);
				nextTestCase.setTestState(TestCase.PASSED);
			} else {
				nextTestCase.setTestState(TestCase.FAILED);
				if (containNow != null) {
					int safeState = this.containDifferentNot(containNow,
							nextTestCase);
					if (safeState == 0 || safeState == 1) {
						this.encounterUnsafe++;
					}
					if (safeState == 1) {
						this.triggerDifferentUnsafe++;
					}
				}

			}
		}

		sc.analysis();

		List<Tuple> mfs = sc.getBugs();
		return mfs;
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

		for (TestCase testCase : this.failTestCase) {
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

	// �����޸�
	public void evaluate_multiple() {
		List<Tuple> identified = new ArrayList<Tuple>();
		identified.addAll(identifiedMFSForMultiple);
		List<Tuple> actualMFS = new ArrayList<Tuple>();
		actualMFS.addAll(actualRealMFSInMultiple);

		// computingF-meausre

		double[] pAndR = SimilarityMFS.getPreciseAndRecall(identified,
				actualMFS);

		// second precise
		// second recall
		// second f-measure
		this.multip_precise = pAndR[0];
		this.multip_recall = pAndR[1];
		this.multip_f_measure = SimilarityMFS.f_measue(pAndR[0], pAndR[1]);

		for (Tuple t : identifiedMFSForMultiple) {
			boolean existed = false;
			for (Tuple t2 : actualRealMFSInMultiple) {
				if (t.equals(t2)) {
					existed = true;
					break;
				}
			}
			if (existed) {
				this.multipe_found += 1;
			}
		}

		this.multipe_found_percent = multipe_found
				/ (double) actualRealMFSInMultiple.size();

		List<Tuple> notContained = new ArrayList<Tuple>();

		for (Tuple t2 : actualRealMFSInMultiple) {
			boolean existed = false;
			for (Tuple t : identifiedMFSForMultiple) {
				if (t.equals(t2)) {
					existed = true;
					break;
				}
			}
			if (!existed) {
				notContained.add(t2);
			}
		}

		for (Tuple t : notContained) {
			boolean existed = false;
			for (Tuple t2 : this.MFS) {
				if (t.equals(t2)) {
					existed = true;
					break;
				}
			}
			if (existed) {
				this.helpedInTheNextRun += 1;
			}
		}

		this.helpedInTheNextRun_percen = helpedInTheNextRun
				/ (double) actualRealMFSInMultiple.size();

		// besides, we should compute how many schemas are obtained in the
		// following schemas.

		// computing multiple

		// for (TestCase testCase : this.failTestCase) {
		// int contain = 0;
		// for (Tuple acMFS : actualMFS) {
		// if (testCase.containsOf(acMFS))
		// contain++;
		// if (contain > 1) {
		// this.multipleMFS++;
		// break;
		// }
		// }
		// }
		// computingTcove
		// computeT_cover(actualMFS);
		// computeRealIdentify(actualMFS);
		// computeCoveredNum();
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

		Tuple bugModel1 = new Tuple(1, wrongCase2);
		bugModel1.set(0, 0);
		// bugModel1.set(1, 5);

		Tuple bugModel2 = new Tuple(1, wrongCase2);
		bugModel2.set(0, 1);

		CaseRunner caseRunner = new CaseRunnerWithBugInject();
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel1);
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel2);

		List<Tuple> actualMFS = new ArrayList<Tuple>();
		actualMFS.add(bugModel1);
		actualMFS.add(bugModel2);

		CT_process elda = new ErrorLocatingDrivenArray(actualMFS, dataCenter,
				caseRunner);
		elda.run();

		System.out
				.println("testCase Num: " + elda.getOverallTestCases().size());
		for (TestCase testCase : elda.getOverallTestCases()) {
			System.out.println(testCase.getStringOfTest());
		}
		System.out.println("MFS");
		for (Tuple mfs : elda.getMFS())
			System.out.println(mfs.toString());

		// ((ErrorLocatingDrivenArray)elda).evaluate_multiple();

		System.out.println("multi "
				+ ((ErrorLocatingDrivenArray) elda).FormultipleCases.size());

		System.out.println("multip Num: " + elda.getMultipleMFS());

		System.out.println("multip found Num: "
				+ ((ErrorLocatingDrivenArray) elda).getMultipe_found());

	}

}
