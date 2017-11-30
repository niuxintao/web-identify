package incremental;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cs.analyse.Analyze;
import com.cs.analyse.ReadInput;
import com.fc.DataCenter.DataCenter;
import com.fc.testObject.TestCase;

public class DecreaseHaiming_now {
	public HashMap<Integer, List<TestCase>> incrementalCoveringArrays;

	public HashMap<Integer, Long> neededTime;

	public HashMap<Integer, Long> getNeededTime() {
		return neededTime;
	}

	public HashMap<Integer, List<TestCase>> getIncrementalCoveringArrays() {
		return incrementalCoveringArrays;
	}

	public DecreaseHaiming_now(int model) {
		this.readInital(model);
	}

	public int[] param;

	public int maxDegree;

	public DataCenter currentDataCenter;

	public List<TestCase> currentTestCases;

	public void readInital(int model) {		
		incrementalCoveringArrays = new HashMap<Integer, List<TestCase>>();
		neededTime = new HashMap<Integer, Long>();

		Analyze analyse = new Analyze();

		ReadInput read = new ReadInput();

		// int[] models = new int[55];
		// for (int i = 0; i < models.length; i++) {
		// models[i] = i;
		// }

		// System.out.println("model: " + model);
		String pathParam = analyse.getPathOfParmater(model);
		read.readParam(pathParam);
		// System.out.print("[");

		
		int currentDegree = 0;
		String currentPath = "";
		String currentTimePath = "";
		
		if(model == 21){
			currentDegree = 4;
			currentPath = "./results/35model/result_top_down/benchmark_2.result";
			currentTimePath = "./results/35model/result_top_down/benchmark_2.result";
		}else if (model == 38){
			currentDegree = 3;
			currentPath = 	"./results/35model/result_top_down/benchmark_19.result";
			currentTimePath = 	"./results/35model/result_top_down/benchmark_19.result";
			
		}else if(model == 47){
			currentDegree = 3;
			currentPath = 	"./results/35model/result_top_down/benchmark_28.result";
			currentTimePath = 	"./results/35model/result_top_down/benchmark_28.result";
		}

	
		read.readTestCases(currentPath, 3);
		read.readTimeSpecial(currentTimePath);

		DataCenter dataCenter = new DataCenter();
		dataCenter.reset(read.getParam(), currentDegree - 1);

		this.param = dataCenter.getParam();
		this.currentDataCenter = dataCenter;

		currentTestCases = new ArrayList<TestCase>();
		for (TestCase testCase : read.getTestCases()) {
			currentTestCases.add(testCase);
		}

		incrementalCoveringArrays.put(currentDegree, currentTestCases);
		neededTime.put(currentDegree, read.getTime());

		// System.out.println("degree :" + currentDegree);
		// System.out.println("num :" + currentTestCases.size());
		// for (TestCase testCase : currentTestCases) {
		// System.out.println(testCase.getStringOfTest());
		// }
		// System.out.println("-------------------------------------");
	}

	public void process() {
		while (true) {

			if (currentDataCenter.getDegree() < 2) {
				break;
			}
			long before = System.currentTimeMillis();
			GetBestFromExistingHaiming getBestFromExisting = new GetBestFromExistingHaiming(
					currentDataCenter, currentTestCases);
			getBestFromExisting.process();
			long after = System.currentTimeMillis();
			long duration = after - before;

			incrementalCoveringArrays.put(currentDataCenter.getDegree(),
					getBestFromExisting.getSelected());
			neededTime.put(currentDataCenter.getDegree(), duration);

			// System.out.println("degree :" + currentDataCenter.getDegree());
			// System.out.println("num :" +
			// getBestFromExisting.getSelected().size());
			// for (TestCase testCase : getBestFromExisting.getSelected()) {
			// System.out.println(testCase.getStringOfTest());
			// }
			// System.out.println("-------------------------------------");

			DataCenter dataCenter = new DataCenter();
			dataCenter.reset(this.param, currentDataCenter.getDegree() - 1);
			this.currentDataCenter = dataCenter;
			this.currentTestCases = getBestFromExisting.getSelected();
		}

		// this.currentTestCases =
	}

	public static void main(String[] args) {
		DecreaseHaiming_now decrease = new DecreaseHaiming_now(4);
		decrease.process();
		// DataCenter dataCenter = new DataCenter(param, 3);
		// AETG_Seeds aetg_seeds = new AETG_Seeds(dataCenter);
		// aetg_seeds.process();
	}
}
