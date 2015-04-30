package gandi;

import interaction.DataCenter;

import java.util.HashSet;
import java.util.List;

import com.fc.caseRunner.CaseRunner;
import com.fc.caseRunner.CaseRunnerWithBugInject;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

import ct.AETG;
import ct.SOFOT;

public class TraditionalFGLI implements CT_process {

	private CaseRunner caseRunner;

	private HashSet<TestCase> overallTestCases;
	
	
	private HashSet<TestCase> regularCTCases;
	
	private HashSet<TestCase> identifyCases;

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
		MFS = new HashSet<Tuple>();
	}

	public void run() {
		// generate covering array
		AETG aetg = new AETG(dataCenter);
		aetg.process();
		for (int[] test : aetg.coveringArray) {
			TestCase testCase = new TestCaseImplement(test);
			overallTestCases.add(testCase);
			regularCTCases.add(testCase);
		}

		// idenitfy
		HashSet<TestCase> additional = new HashSet<TestCase>();
		for (TestCase testCase : overallTestCases) {
			if (caseRunner.runTestCase(testCase) == TestCase.FAILED) {
				SOFOT ofot = new SOFOT();
				ofot.process(testCase, dataCenter.param, caseRunner);
				additional.addAll(ofot.getExecuted());
				identifyCases.addAll(ofot.getExecuted());
				MFS.addAll(ofot.getBugs());
				// MFS.add(ofot.)

			}
		}

		// merge them
		overallTestCases.addAll(additional);
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
