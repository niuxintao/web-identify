package com.fc.process;

import com.fc.DataCenter.DataCenter;
import com.fc.testObject.TestSuite;
import com.fc.tuple.Tuple;
import com.fc.process.BugModeDetect;
import com.fc.process.CoverageAnalyse;
import com.fc.process.DetectionRate;
import com.fc.process.DiversityAnalyse;


public class DriverForAnalyse {
	private DataCenter dataCenter = new DataCenter();
	private TestSuite suite;
	private Tuple bugMode;

	public DriverForAnalyse() {
	}

	public void set(int[] param, int degree, TestSuite suite, Tuple bugMode) {
		this.suite = suite;
		this.dataCenter.reset(param, degree);
		this.bugMode = bugMode;
	}

	public double analyseCoverage() {
		double result = 0.0D;
		CoverageAnalyse coverageAnalyse = new CoverageAnalyse();
		result = coverageAnalyse.getCoverage(0, this.suite.getTestCaseNum(),
				this.suite, this.dataCenter);
		return result;
	}

	public double analyseDiversity() {
		double result = 0.0D;
		DiversityAnalyse diversityAnalyse = new DiversityAnalyse();
		result = diversityAnalyse.getDiversity(0, this.suite.getTestCaseNum(),
				this.suite, this.dataCenter);
		return result;
	}

	public double analyseDetectionRate() {
		double result = 0.0D;
		DetectionRate detectionRate = new DetectionRate();
		result = (double) detectionRate.getDetectionRate(0,
				this.suite.getTestCaseNum(), this.suite, this.dataCenter);
		return result;
	}
	
//	public double analyseDetectionRate_Test() {
//		double result = 0.0D;
//		DetectionRate detectionRate = new DetectionRate();
//		result = (double) detectionRate.getDetectionRate(0,
//				1, this.suite, this.dataCenter);
//		return result;
//	}

	public int analyseBugModeDetect() {
//		boolean result = false;
		BugModeDetect bugModeDetect = new BugModeDetect();
		int result1 = bugModeDetect.getTestCaseIndexContainBubMode(
				this.bugMode, this.suite);
		return result1;
	}

	public void setDataCenter(DataCenter dataCenter) {
		this.dataCenter = dataCenter;
	}

	public DataCenter getDataCenter() {
		return this.dataCenter;
	}
}