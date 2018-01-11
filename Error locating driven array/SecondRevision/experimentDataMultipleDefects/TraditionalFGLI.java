package experimentDataMultipleDefects;

import gandi.CT_process;
import interaction.CoveringManage;
import interaction.DataCenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import location.FIC;

import com.fc.caseRunner.BasicRunner;
import com.fc.caseRunner.CaseRunner;
import com.fc.caseRunner.CaseRunnerWithBugInject;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

import ct.AETG;
import ct.AETG_Constraints;
import experiment.SimilarityMFS;

public class TraditionalFGLI implements CT_process {

	private CaseRunner caseRunner;

	private HashSet<TestCase> overallTestCases;

	private HashSet<TestCase> regularCTCases;

	private HashSet<TestCase> identifyCases;

	private HashSet<TestCase> failTestCase;

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

	private int t_tested_covered = 0;
	
	private HashMap<Integer, Integer> coveredNums;
	
	private HashMap<Tuple, Integer> realIdentify;
	
	private List<Tuple> actualMFS;
	
	public List<TestCase> FormultipleCases;
	public List<Tuple> identifiedMFSForMultiple;
	public List<Tuple> actualRealMFSInMultiple;

	
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
	
	public double getMultipe_found(){
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

	

	@Override
	public HashMap<Tuple, Integer> getRealIdentify() {
		return realIdentify;
	}
	

	@Override
	public HashMap<Integer, Integer> getCoveredNums() {
		return coveredNums;
	}


	public HashSet<TestCase> getOverallTestCases() {
		return overallTestCases;
	}

	public HashSet<Tuple> getMFS() {
		return MFS;
	}

	public TraditionalFGLI(DataCenter dataCenter, CaseRunner caseRunner) {
		this.caseRunner = caseRunner;
		this.dataCenter = dataCenter;
		overallTestCases = new HashSet<TestCase>();
		regularCTCases = new HashSet<TestCase>();
		identifyCases = new HashSet<TestCase>();
		failTestCase = new HashSet<TestCase>();
		MFS = new HashSet<Tuple>();
		coveredNums = new HashMap<Integer, Integer>();
		realIdentify = new HashMap<Tuple, Integer>();
		actualRealMFSInMultiple = new ArrayList<Tuple> ();
		identifiedMFSForMultiple = new ArrayList<Tuple> ();
		FormultipleCases = new ArrayList<TestCase> ();
	}
	
	
	public TraditionalFGLI(List<Tuple> actualMFS, DataCenter dataCenter, CaseRunner caseRunner) {
		this.caseRunner = caseRunner;
		this.dataCenter = dataCenter;
		overallTestCases = new HashSet<TestCase>();
		regularCTCases = new HashSet<TestCase>();
		identifyCases = new HashSet<TestCase>();
		failTestCase = new HashSet<TestCase>();
		MFS = new HashSet<Tuple>();
		coveredNums = new HashMap<Integer, Integer>();
		realIdentify = new HashMap<Tuple, Integer>();
		this.actualMFS = actualMFS;
		actualRealMFSInMultiple = new ArrayList<Tuple> ();
		identifiedMFSForMultiple = new ArrayList<Tuple> ();
		FormultipleCases = new ArrayList<TestCase> ();
	}
	
	public void setActualMFS(List<Tuple> actualMFS){
		this.actualMFS = actualMFS;
	}

	public void run() {
		// generate covering array
		long allTime = System.currentTimeMillis();
		long geTime = System.currentTimeMillis();

		AETG aetg = new AETG(dataCenter);
		aetg.process();

		geTime = System.currentTimeMillis() - geTime;
		this.timeGen += geTime;

		for (int[] test : aetg.coveringArray) {
			TestCase testCase = new TestCaseImplement(test);
			overallTestCases.add(testCase);
			regularCTCases.add(testCase);
		}

		long ideTime = System.currentTimeMillis();
		// idenitfy
		HashSet<TestCase> additional = new HashSet<TestCase>();
		for (TestCase testCase : overallTestCases) {
			int failurecode = caseRunner.runTestCase(testCase);
			if (failurecode != 0) {
				int contain = 0;
				List<Tuple> templ = new ArrayList<Tuple> ();
				for (Tuple acMFS : actualMFS) {
					if (testCase.containsOf(acMFS))
						contain++;
					if (contain > 1) {
						break;
					}
				}
				if (contain > 1) {
					for (Tuple acMFS : actualMFS) {
						if (testCase.containsOf(acMFS))
							templ.add(acMFS);
					}
				}
				
				this.actualRealMFSInMultiple.addAll(templ);

				
				

				failTestCase.add(testCase);
				
				List<Tuple> currentMFS = new ArrayList<Tuple> ();
				currentMFS.addAll(MFS);
				CaseRunner caseRunner = new DistinguishRunner(((BasicRunner)this.caseRunner), failurecode);
				
				FIC fic_feedback = new FIC(testCase, dataCenter.param, caseRunner, currentMFS);
				fic_feedback.FicNOP_withMFS();
				additional.addAll(fic_feedback.getExecuted());
				
				
				if(contain > 1){
					this.identifiedMFSForMultiple.addAll(fic_feedback.getBugs());
				}
				
				MFS.addAll(fic_feedback.getBugs());
				identifyCases.addAll(fic_feedback.getExecuted());
				// MFS.add(ofot.)

			}
		}

		ideTime = System.currentTimeMillis() - ideTime;
		this.timeIden += ideTime;

		allTime = System.currentTimeMillis() - allTime;
		this.timeAll += allTime;

		
		CoveringManage cm = new CoveringManage(dataCenter);
		for (TestCase testCase : additional) {
			int[] test = new int[testCase.getLength()];
			for (int i = 0; i < test.length; i++) {
				test[i] = testCase.getAt(i);
			}
			cm.setCover(aetg.unCovered, aetg.coveredMark, test);
		}
		
		this.coveredMark = aetg.coveredMark;

		// merge them
		overallTestCases.addAll(additional);
		
		
		this.evaluate_multiple();

	}

	public HashSet<TestCase> getRegularCTCases() {
		return regularCTCases;
	}

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

		CaseRunner caseRunner = new CaseRunnerWithBugInject();
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel1);
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel2);

		TraditionalFGLI fglt = new TraditionalFGLI(dataCenter, caseRunner);
		fglt.run();

		System.out
				.println("testCase Num: " + fglt.getOverallTestCases().size());
		for (TestCase testCase : fglt.getOverallTestCases()) {
			System.out.println(testCase.getStringOfTest());
		}
		System.out.println("MFS");
		for (Tuple mfs : fglt.getMFS())
			System.out.println(mfs.toString());
	}

	@Override
	public int[] getCoveredMark() {
		// TODO Auto-generated method stub
		return this.coveredMark;
	}

	@Override
	public int[] getT_tested_coveredMark() {
		// TODO Auto-generated method stub
		return this.t_tested_coveredMark;
	}

	@Override
	public void evaluate(List<Tuple> actualMFS) {
		// TODO Auto-generated method stub

		CoveringManage cm = new CoveringManage(dataCenter);

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

		computeT_cover(actualMFS, cm);
		computeRealIdentify(actualMFS);

		computeCoveredNum();

	}
	
	
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
		
		
		for(Tuple t :	identifiedMFSForMultiple ){
			boolean existed = false;
			for(Tuple t2 : actualRealMFSInMultiple ){
				if(t.equals(t2)){
					existed = true;
					break;
				}
			}
			if(existed){
				this.multipe_found  += 1;
			}
		}
		
		this.multipe_found_percent = multipe_found/(double)actualRealMFSInMultiple.size();
		
		List<Tuple> notContained = new ArrayList<Tuple> ();
		
		for(Tuple t2 : actualRealMFSInMultiple){
			boolean existed = false;
			for(Tuple t : identifiedMFSForMultiple ){
				if(t.equals(t2)){
					existed = true;
					break;
				}
			}
			if(!existed){
				notContained.add(t2);
			}
		}
		
		for(Tuple t : notContained){
			boolean existed = false;
			for(Tuple t2 : this.MFS ){
				if(t.equals(t2)){
					existed = true;
					break;
				}
			}
			if(existed){
				this.helpedInTheNextRun  += 1;
			}
		}
		
		this.helpedInTheNextRun_percen = helpedInTheNextRun/(double)actualRealMFSInMultiple.size();
		
		//besides, we should compute how many schemas are obtained in the following schemas.

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

	public void computeT_cover(List<Tuple> actualMFS, CoveringManage cm) {
		// computingTcove
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

	@Override
	public long getTimeAll() {
		// TODO Auto-generated method stub
		return this.timeAll;
	}

	@Override
	public long getTimeIden() {
		// TODO Auto-generated method stub
		return this.timeIden;
	}

	@Override
	public long getTimeGen() {
		// TODO Auto-generated method stub
		return this.timeGen;
	}

	@Override
	public int getMultipleMFS() {
		// TODO Auto-generated method stub
		return this.multipleMFS;
	}

	@Override
	public double getPrecise() {
		// TODO Auto-generated method stub
		return this.precise;
	}

	@Override
	public double getRecall() {
		// TODO Auto-generated method stub
		return this.recall;
	}

	@Override
	public double getF_measure() {
		// TODO Auto-generated method stub
		return this.f_measure;
	}

	@Override
	public int getT_tested_covered() {
		// TODO Auto-generated method stub
		return this.t_tested_covered;
	}

}
