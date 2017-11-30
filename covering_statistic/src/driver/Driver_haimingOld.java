package driver;

import incremental.DecreaseHaiming;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import com.fc.testObject.TestCase;

import output.OutPut;

public class Driver_haimingOld {

	public static void main(String[] args) {
		new File("results/model20/result_haiming_top_down/").mkdirs();
		new File("results/model35/result_haiming_top_down/").mkdirs();
		for (int i = 0; i < 55; i++) {
			System.out.println("model : " + i);
			DecreaseHaiming increase = new DecreaseHaiming(i);
			increase.process();
			HashMap<Integer, List<TestCase>> incrementalCoveringArrays = increase
					.getIncrementalCoveringArrays();
			HashMap<Integer, Long> times = increase.getNeededTime();
			for (Integer key : incrementalCoveringArrays.keySet()) {

				List<TestCase> testCases = incrementalCoveringArrays
						.get(key);
				Long neededTime = times.get(key);
				OutPut output;
				if (i < 20) {
					output = new OutPut(
							"results/model20/result_haiming_top_down/benchmark_"
									+ (i + 1) + "_" + key + ".ca");
				} else {
					output = new OutPut(
							"results/model35/result_haiming_top_down/benchmark_"
									+ (i + 1 - 20) + "_" + key + ".ca");
				}
				output.println("time:" + neededTime);
				output.println("size:" + testCases.size());
				for (TestCase testCase : testCases) {
					output.println(testCase.getStringOfTest());
				}
				output.close();
			}
		}
	}
}
