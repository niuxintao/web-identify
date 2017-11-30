package driver;

import incremental.Increase;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.fc.testObject.TestCase;

import output.OutPut;

public class Driver_bottomUp {

	public static void main(String[] args) {
		new File("results/model20/result_bo_up/").mkdirs();
		new File("results/model35/result_bo_up/").mkdirs();
		for (int i = 0; i < 55; i++) {
			System.out.println("model : " + i);
			Increase increase = new Increase(i);
			increase.process();
			HashMap<Integer, ArrayList<TestCase>> incrementalCoveringArrays = increase
					.getIncrementalCoveringArrays();
			HashMap<Integer, Long> times = increase.getNeededTime();
			for (Integer key : incrementalCoveringArrays.keySet()) {
				ArrayList<TestCase> testCases = incrementalCoveringArrays
						.get(key);
				
				OutPut output;
				if(i < 20){
					output= new OutPut(
						"results/model20/result_bo_up/benchmark_" + (i + 1) + "_"
								+ key + ".ca");
				}else{
					output= new OutPut(
							"results/model35/result_bo_up/benchmark_" + (i + 1 - 20) + "_"
									+ key + ".ca");
				}
				output.println("time:" + times.get(key));
				output.println("size:" + testCases.size());
				for(TestCase testCase : testCases){
					output.println(testCase.getStringOfTest());
				}
				output.close();
			}
		}
	}
}
