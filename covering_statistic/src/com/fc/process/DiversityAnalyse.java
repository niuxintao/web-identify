package com.fc.process;

import com.fc.DataCenter.DataCenter;
import com.fc.testObject.TestSuite;
import com.fc.process.BasicAnalyse;

public class DiversityAnalyse {
	private BasicAnalyse basicAnalyse = new BasicAnalyse();

	public double getDiversity(int start, int end, TestSuite suite,
			DataCenter dataCenter) {
		double result = 0.0D;
		int covered = this.basicAnalyse.getCovered(start, end, suite,
				dataCenter);
		int k = end - start;
		int n = dataCenter.getIndex().length;
		result = (double) covered / (double) (k * n);
		return result;
	}
}