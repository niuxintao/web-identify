package incremental;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cs.analyse.Analyze;
import com.cs.analyse.ReadInput;
import com.fc.DataCenter.DataCenter;
import com.fc.testObject.TestCase;

public class IsstaIncrease {
	public HashMap<Integer, List<ArrayList<TestCase>>> incrementalCoveringArrays;

	public HashMap<Integer, List<Long>> neededTime;

	public int model;

	public HashMap<Integer, List<Long>> getNeededTime() {
		return neededTime;
	}

	public HashMap<Integer, List<ArrayList<TestCase>>> getIncrementalCoveringArrays() {
		return incrementalCoveringArrays;
	}

	public IsstaIncrease(int model) {
		this.model = model;
		this.readInital(model);
	}

	public int[] param;

	public int maxDegree;

	public DataCenter currentDataCenter;

	public ArrayList<TestCase> currentTestCases;

	public void readInital(int model) {
		incrementalCoveringArrays = new HashMap<Integer, List<ArrayList<TestCase>>>();
		neededTime = new HashMap<Integer, List<Long>>();

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

		for (int degree : degrees) {
			// System.out.println("degree: " + degree);
			String[] sutiePathsTop_down = analyse.getPathsOfSuite(
					Analyze.Mtop_down, model, degree);

			String suitePathTop_down = sutiePathsTop_down[0];
			
			suitePathTop_down= suitePathTop_down.replace("20model", "model20");
			suitePathTop_down= suitePathTop_down.replace("35model", "model35");
			suitePathTop_down= suitePathTop_down.replace("result_top_down", "result_haiming_top_down");
			suitePathTop_down= suitePathTop_down.replace("way", "");
			
			// System.out.println("path :" + suitePath);
			if (read.isFileExists(suitePathTop_down)) {

				List<ArrayList<TestCase>> temp = new ArrayList<ArrayList<TestCase>>();
				List<Long> tempT = new ArrayList<Long>();

				incrementalCoveringArrays.put(degree, temp);
				neededTime.put(degree, tempT);

				String[] sutiePaths = analyse.getPathsOfSuite(Analyze.Missta,
						model, degree);

				String[] timePaths = analyse.getPathsOfTime(Analyze.Missta,
						model, degree);

				for (int i = 0; i < timePaths.length; i++) {
					String timePath = timePaths[i];
					String suitePath = sutiePaths[i];

					if (read.isFileExists(suitePath)) {
						read.readTestCases(suitePath, 8);
						read.readTime(timePath,Analyze.Missta);
						currentTestCases = new ArrayList<TestCase>();
						for (TestCase testCase : read.getTestCases()) {
							currentTestCases.add(testCase);
						}

						incrementalCoveringArrays.get(degree).add(
								currentTestCases);
						neededTime.get(degree).add(read.getTime());
					}
				}
			} else {
				break;
			}
		}

		// DataCenter dataCenter = new DataCenter();
		// dataCenter.reset(read.getParam(), currentDegree - 1);

		// this.param = dataCenter.getParam();
		// this.currentDataCenter = dataCenter;

		// System.out.println("degree :" + currentDegree);
		// System.out.println("num :" + currentTestCases.size());
		// for (TestCase testCase : currentTestCases) {
		// System.out.println(testCase.getStringOfTest());
		// }
		// System.out.println("-------------------------------------");
	}

	public void process() {

		// this.currentTestCases =
	}

	public static void main(String[] args) {
		IsstaIncrease decrease = new IsstaIncrease(4);
		decrease.process();
		// DataCenter dataCenter = new DataCenter(param, 3);
		// AETG_Seeds aetg_seeds = new AETG_Seeds(dataCenter);
		// aetg_seeds.process();
	}
}
