package driver;

import incremental.Decrease;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import com.fc.testObject.TestCase;

import output.OutPut;

public class Driver_topDown {

	public static void main(String[] args) {
		new File("results/model20/result_top_down/").mkdirs();
		new File("results/model35/result_top_down/").mkdirs();
		for (int i = 20; i < 55; i++) {
			if (i != 24 && i != 35 & i != 39 && i != 41 && i != 52 && i != 54)
				continue;
			System.out.println("model : " + i);
			Decrease decrease = new Decrease(i);
			decrease.process();
			HashMap<Integer, List<TestCase>> incrementalCoveringArrays = decrease
					.getIncrementalCoveringArrays();
			HashMap<Integer, Long> times = decrease.getNeededTime();
			for (Integer key : incrementalCoveringArrays.keySet()) {
				List<TestCase> testCases = incrementalCoveringArrays.get(key);

				OutPut output;
				if (i < 20) {
					output = new OutPut(
							"results/model20/result_top_down/benchmark_"
									+ (i + 1) + "_" + key + ".ca");
				} else {
					output = new OutPut(
							"results/model35/result_top_down/benchmark_"
									+ (i + 1 - 20) + "_" + key + ".ca");
				}
				output.println("time:" + times.get(key));
				output.println("size:" + testCases.size());
				for (TestCase testCase : testCases) {
					output.println(testCase.getStringOfTest());
				}
				output.close();
			}
		}
	}
}
