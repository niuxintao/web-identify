package driver;

import incremental.IsstaIncrease;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fc.testObject.TestCase;

import output.OutPut;

public class Driver_IsstaIncrease {

	public static void main(String[] args) {
		new File("results/model20/result_issta_bo_up/").mkdirs();
		new File("results/model35/result_issta_bo_up/").mkdirs();
		for (int i = 0; i < 55; i++) {
			System.out.println("model : " + i);
			IsstaIncrease increase = new IsstaIncrease(i);
			increase.process();
			HashMap<Integer, List<ArrayList<TestCase>>> incrementalCoveringArrays = increase
					.getIncrementalCoveringArrays();
			HashMap<Integer, List<Long>> times = increase.getNeededTime();
			for (Integer key : incrementalCoveringArrays.keySet()) {
				List<ArrayList<TestCase>> testCasesSet = incrementalCoveringArrays
						.get(key);
				List<Long> neededTimeSet = times.get(key);
				for (int j = 0; j < testCasesSet.size(); j++) {
					ArrayList<TestCase> testCases = testCasesSet.get(j);
					Long neededTime = neededTimeSet.get(j);
					OutPut output;
					if (i < 20) {
						output = new OutPut(
								"results/model20/result_issta_bo_up/benchmark_"
										+ (i + 1) + "_" + key + "_" + j +  ".ca");
					} else {
						output = new OutPut(
								"results/model35/result_issta_bo_up/benchmark_"
										+ (i + 1 - 20) + "_" + key +  "_"+ j + ".ca");
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
}
