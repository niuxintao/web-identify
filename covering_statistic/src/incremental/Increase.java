package incremental;

import java.util.ArrayList;
import java.util.HashMap;

import com.cs.analyse.Analyze;
import com.cs.analyse.ReadInput;
import com.fc.DataCenter.DataCenter;
import com.fc.testObject.TestCase;

public class Increase {
	public HashMap<Integer, ArrayList<TestCase>> incrementalCoveringArrays;

	public HashMap<Integer, Long> neededTime;

	public int model;

	public HashMap<Integer, Long> getNeededTime() {
		return neededTime;
	}

	public HashMap<Integer, ArrayList<TestCase>> getIncrementalCoveringArrays() {
		return incrementalCoveringArrays;
	}

	public Increase(int model) {
		this.model = model;
		this.readInital(model);
	}

	public int[] param;

	public int maxDegree;

	public DataCenter currentDataCenter;

	public ArrayList<TestCase> currentTestCases;

	public void readInital(int model) {
		incrementalCoveringArrays = new HashMap<Integer, ArrayList<TestCase>>();
		neededTime = new HashMap<Integer, Long>();

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

		for (int degree : degrees) {
			// System.out.println("degree: " + degree);
			String[] sutiePaths = analyse.getPathsOfSuite(Analyze.Mtop_down,
					model, degree);

			String suitePath = sutiePaths[0];
			
			suitePath= suitePath.replace("20model", "model20");
			suitePath= suitePath.replace("35model", "model35");
			suitePath= suitePath.replace("result_top_down", "result_haiming_top_down");
			suitePath= suitePath.replace("way", "");
			
			// System.out.println("path :" + suitePath);
			if (read.isFileExists(suitePath)) {
				currentDegree = degree;
			} else {
				break;
			}
		}
		
		
		String[] sutiePaths = analyse.getPathsOfSuite(Analyze.Mbo_up,
				model, currentDegree);

		String[] timePaths = analyse.getPathsOfTime(Analyze.Mbo_up, model,
				currentDegree);

		String timePath = timePaths[0];
		String suitePath = sutiePaths[0];
		

		read.readTestCases(suitePath, 3);
		read.readTime(timePath,Analyze.Mbo_up);
		this.maxDegree = currentDegree;

		// DataCenter dataCenter = new DataCenter();
		// dataCenter.reset(read.getParam(), currentDegree - 1);

		// this.param = dataCenter.getParam();
		// this.currentDataCenter = dataCenter;

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
		int nowDegree = maxDegree;
		while (true) {
			if (nowDegree <= 2)
				break;
			nowDegree--;

			Analyze analyse = new Analyze();

			ReadInput read = new ReadInput();

			String pathParam = analyse.getPathOfParmater(model);
			read.readParam(pathParam);
			// System.out.print("[");

			String[] sutiePaths = analyse.getPathsOfSuite(Analyze.Mbo_up,
					model, nowDegree);

			String[] timePaths = analyse.getPathsOfTime(Analyze.Mbo_up, model,
					nowDegree);
			
			String timePath = timePaths[0];
			String suitePath = sutiePaths[0];

			read.readTestCases(suitePath, 3);
			read.readTime(timePath,Analyze.Mbo_up);
			
			currentTestCases = new ArrayList<TestCase>();
			for (TestCase testCase : read.getTestCases()) {
				currentTestCases.add(testCase);
			}

			incrementalCoveringArrays.put(nowDegree, currentTestCases);
			neededTime.put(nowDegree, read.getTime());

		}

		// this.currentTestCases =
	}

	public static void main(String[] args) {
		Increase decrease = new Increase(4);
		decrease.process();
		// DataCenter dataCenter = new DataCenter(param, 3);
		// AETG_Seeds aetg_seeds = new AETG_Seeds(dataCenter);
		// aetg_seeds.process();
	}
}
