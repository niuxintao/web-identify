package incremental;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cs.analyse.Analyze;
import com.cs.analyse.ReadInput;
import com.fc.DataCenter.DataCenter;
import com.fc.testObject.TestCase;

public class Decrease {
	public HashMap<Integer, List<TestCase>> incrementalCoveringArrays;
	
	
	public HashMap<Integer, Long> neededTime;
	

	public HashMap<Integer, Long> getNeededTime() {
		return neededTime;
	}

	public HashMap<Integer, List<TestCase>> getIncrementalCoveringArrays() {
		return incrementalCoveringArrays;
	}

	public Decrease(int model) {
		this.readInital(model);
	}

	public int[] param;

	public int maxDegree;
	

	public DataCenter currentDataCenter;

	public List<TestCase> currentTestCases;

	public void readInital(int model) {
		incrementalCoveringArrays = new HashMap<Integer, List<TestCase>>();
		neededTime = new  HashMap<Integer, Long>();
		
		Analyze analyse = new Analyze();

		ReadInput read = new ReadInput();

		// int[] models = new int[55];
		// for (int i = 0; i < models.length; i++) {
		// models[i] = i;
		// }

		int[] degrees = new int[] { 2, 3, 4, 5, 6 };

		// System.out.println("model: " + model);
		String pathParam = analyse.getPathOfParmater(model);
		read.readParam(pathParam);
		// System.out.print("[");

		int currentDegree = 0;
		String currentPath = "";
		String currentTimePath = "";
		
		for (int degree : degrees) {
			// System.out.println("degree: " + degree);
			String[] sutiePaths = analyse.getPathsOfSuite(Analyze.Mtop_down,
					model, degree);
			
			String[] timePaths = analyse.getPathsOfTime(Analyze.Mtop_down,
					model, degree);

			String timePath = timePaths[0];
			String suitePath = sutiePaths[0];
			suitePath= suitePath.replace("20model", "model20");
			suitePath= suitePath.replace("35model", "model35");
			suitePath= suitePath.replace("result_top_down", "result_haiming_top_down");
			suitePath= suitePath.replace("way", "");
//			System.out.println("suitePath : " + suitePath);
			timePath = suitePath;
			// System.out.println("path :" + suitePath);
			if (read.isFileExists(suitePath)) {
				currentDegree = degree;
				currentPath = suitePath;
				currentTimePath = timePath;
			} else {
				break;
			}
		}

		read.readTestCases(currentPath, 3);
		read.readTimeSpecial2(currentTimePath);
		
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
		
//		System.out.println("degree :" + currentDegree);
//		System.out.println("num :" + currentTestCases.size());
//		for (TestCase testCase : currentTestCases) {
//			System.out.println(testCase.getStringOfTest());
//		}
//		System.out.println("-------------------------------------");
	}

	public void process() {
		while (true) {
			
			if( currentDataCenter.getDegree() < 2){
				break;
			}
			long before = System.currentTimeMillis();
			GetBestFromExisting getBestFromExisting = new GetBestFromExisting(
					currentDataCenter, currentTestCases);
			getBestFromExisting.process();
			long after = System.currentTimeMillis();
			long duration = after - before;
			
			incrementalCoveringArrays.put(currentDataCenter.getDegree(), getBestFromExisting.getSelected());
			neededTime.put(currentDataCenter.getDegree(), duration);
			
//			System.out.println("degree :" + currentDataCenter.getDegree());
//			System.out.println("num :" + getBestFromExisting.getSelected().size());
//			for (TestCase testCase : getBestFromExisting.getSelected()) {
//				System.out.println(testCase.getStringOfTest());
//			}
//			System.out.println("-------------------------------------");
			
			DataCenter dataCenter = new DataCenter();
			dataCenter.reset(this.param, currentDataCenter.getDegree() - 1);
			this.currentDataCenter = dataCenter;
			this.currentTestCases = getBestFromExisting.getSelected();
		}

		// this.currentTestCases =
	}

	public static void main(String[] args) {
		Decrease decrease = new Decrease(4);
		decrease.process();
		// DataCenter dataCenter = new DataCenter(param, 3);
		// AETG_Seeds aetg_seeds = new AETG_Seeds(dataCenter);
		// aetg_seeds.process();
	}
}
