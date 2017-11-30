package incremental;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cs.analyse.Analyze;
import com.cs.analyse.ReadInput;
import com.fc.DataCenter.DataCenter;
import com.fc.testObject.TestCase;

public class DecreaseHaiming {
	public HashMap<Integer, List<TestCase>> incrementalCoveringArrays;

	public HashMap<Integer, Long> neededTime;

	public HashMap<Integer, Long> getNeededTime() {
		return neededTime;
	}

	public HashMap<Integer, List<TestCase>> getIncrementalCoveringArrays() {
		return incrementalCoveringArrays;
	}

	public DecreaseHaiming(int model) {
		this.readInital(model);
	}

	public int[] param;

	public int maxDegree;

	public DataCenter currentDataCenter;

	public ArrayList<TestCase> currentTestCases;

	public void readInital(int model) {
		incrementalCoveringArrays = new HashMap<Integer, List<TestCase>>();
		neededTime = new HashMap<Integer, Long>();

		Analyze analyse = new Analyze();
		ReadInput read = new ReadInput();

		int[] degrees = new int[] { 2, 3, 4, 5, 6 };

		int maxDegree = 0;

		// System.out.println("model: " + model);
		String pathParam = analyse.getPathOfParmater(model);
		read.readParam(pathParam);
		// System.out.print("[");

		for (int degree : degrees) {
			// System.out.println("degree: " + degree);
			String[] sutiePathsTop_down = analyse.getPathsOfSuite(
					Analyze.Mtop_down, model, degree);

			String suitePathTop_down = sutiePathsTop_down[0];
			// System.out.println("path :" + suitePath);
			if (read.isFileExists(suitePathTop_down)) {

				String[] sutiePaths = analyse.getPathsOfSuite(
						Analyze.Mtop_down, model, degree);

				String[] timePaths = analyse.getPathsOfTime(Analyze.Mtop_down,
						model, degree);

				String timePath = timePaths[0];
				String suitePath = sutiePaths[0];
				// System.out.println("path :" + suitePath);
				if (read.isFileExists(suitePath)) {
					maxDegree = degree;
					read.readTestCases(suitePath, 3);
					read.readTime(timePath, Analyze.Mtop_down);
					currentTestCases = new ArrayList<TestCase>();
					for (TestCase testCase : read.getTestCases()) {
						currentTestCases.add(testCase);
					}

					incrementalCoveringArrays.put(degree, currentTestCases);
					neededTime.put(degree, read.getTime());
				} else {
					break;
				}

			} else {
				break;
			}
		}

		
		
		String[] sutiePaths = analyse.getPathsOfSuite(Analyze.Mtop_down, model,
				maxDegree);


		String suitePath = sutiePaths[0];
		
		
		suitePath = suitePath.replace("20model", "model20");
		suitePath = suitePath.replace("35model", "model35");
		suitePath = suitePath.replace("way", "");
		
		String timePath = suitePath;
		
		
		read.readTestCases(suitePath, 3);
		read.readTime(timePath);
		
		
		currentTestCases = new ArrayList<TestCase>();
		for (TestCase testCase : read.getTestCases()) {
			currentTestCases.add(testCase);
		}

		incrementalCoveringArrays.put(maxDegree, currentTestCases);
		neededTime.put(maxDegree, read.getTime());

	}

	public void process() {

	}

	public static void main(String[] args) {
		DecreaseHaiming decrease = new DecreaseHaiming(4);
		decrease.process();
		// DataCenter dataCenter = new DataCenter(param, 3);
		// AETG_Seeds aetg_seeds = new AETG_Seeds(dataCenter);
		// aetg_seeds.process();
	}
}
