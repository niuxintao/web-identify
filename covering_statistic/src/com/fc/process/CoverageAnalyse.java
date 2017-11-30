package com.fc.process;

import com.fc.DataCenter.DataCenter;
import com.fc.process.BasicAnalyse;
import com.fc.testObject.TestSuite;

public class CoverageAnalyse {
	private BasicAnalyse basicAnalyse = new BasicAnalyse();

	public double getCoverage(int start, int end, TestSuite suite,
			DataCenter dataCenter) {
		double result = 0.0D;
		int covered = this.basicAnalyse.getCovered(start, end, suite,
				dataCenter);
		result = (double) covered / (double) dataCenter.getCoveringArrayNum();
		System.out.println( "all+ : " + dataCenter.getCoveringArrayNum());
		return result;
	}
}