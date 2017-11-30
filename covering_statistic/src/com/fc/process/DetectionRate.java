package com.fc.process;

//import incremental.GetCovered;

import com.fc.DataCenter.DataCenter;
import com.fc.testObject.TestSuite;
import com.fc.process.BasicAnalyse;

public class DetectionRate {
	private BasicAnalyse basicAnalyse = new BasicAnalyse();
//	private GetCovered getCovered  = new GetCovered();

	public double getDetectionRate(int start, int end, TestSuite suite,
			DataCenter dataCenter) {
		long result = 0;
		int[] detection = this.basicAnalyse.dealDetection(start, end, suite,
				dataCenter);

		for (int i = 0; i < detection.length; ++i) {
			//System.out.println("i: " +  detection[i]);
			result += detection[i];
		}

		return result;
	}
}