package maskPracticalExperiment;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

public class ReadInput {
	List<TestCase> testCases;
	List<Integer> result;
	List<Tuple> bugs;
	int[] param;

	List<Integer> bugCode;
	HashMap<Integer, List<Integer>> lowerpriority;

	public ReadInput() {
	}

	public int[] getParam() {
		return param;
	}

	public List<Integer> getResult() {
		return result;
	}

	public List<TestCase> getTestCases() {
		return testCases;
	}

	public List<Tuple> getBugs() {
		return bugs;
	}

	public List<Integer> getBugCode() {
		return this.bugCode;
	}

	public HashMap<Integer, List<Integer>> getLowerPriority() {
		return this.lowerpriority;
	}

	public void readBugs(String path) {
		bugs = new ArrayList<Tuple>();
		try {
			// Open the file that is the first
			// command line parameter
			FileInputStream fstream = new FileInputStream(path);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				// Print the content on the console
				if (strLine.length() > 0) {
					String[] tokens = strLine.split(" ");
					TestCase testCase = new TestCaseImplement(tokens.length);
					int[] tu = new int[tokens.length];

					int degree = 0;
					for (int i = 0; i < tokens.length; i++) {
						String num = tokens[i];
						Integer value = 0;
						if (!num.equals("-")) {
							value = Integer.parseInt(num);
							tu[degree] = i;
							degree++;
						}
						testCase.set(i, value);
					}
					Tuple tuple = new Tuple(degree, testCase);
					for (int i = 0; i < tuple.getDegree(); i++)
						tuple.set(i, tu[i]);
					this.bugs.add(tuple);
				}
			}
			// Close the input stream
			in.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	public void readTestCases(String path) {
		testCases = new ArrayList<TestCase>();
		result = new ArrayList<Integer>();
		try {
			// Open the file that is the first
			// command line parameter
			FileInputStream fstream = new FileInputStream(path);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			int[] last = null;
			while ((strLine = br.readLine()) != null) {
				// Print the content on the console
				if (strLine.length() > 0) {
					String[] tokensAll = strLine.split(":");
					// test case
					String[] tokensCase = tokensAll[0].split(" ");
					TestCase testCase = new TestCaseImplement(tokensCase.length);
					for (int i = 0; i < tokensCase.length; i++) {
						String num = tokensCase[i];
						Integer value = Integer.parseInt(num);
						testCase.set(i, value);
					}
					this.testCases.add(testCase);
					last = ((TestCaseImplement) testCase).getTestCase();
					// result
					String[] tokensResult = tokensAll[1].split(" ");
					Integer result = Integer.parseInt(tokensResult[0]);
					this.result.add(result);
				}
			}
			this.param = new int[last.length];
			for (int i = 0; i < last.length; i++)
				this.param[i] = last[i] + 1;
			// Close the input stream
			in.close();
		} catch (Exception e) {// Catch exception if any
			e.printStackTrace();
			System.err.println("Error: " + e.getMessage());
		}
	}

	public void readBugCodeAndLowerPriority(String path) {
		lowerpriority = new HashMap<Integer, List<Integer>>();
		bugCode = new ArrayList<Integer>();
		try {
			// Open the file that is the first
			// command line parameter
			FileInputStream fstream = new FileInputStream(path);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				// Print the content on the console
				if (strLine.length() > 0) {
					String[] tokensAll = strLine.split(":");
					// test case
					List<Integer> lower = new ArrayList<Integer>();
					if (tokensAll.length > 1) {
						String[] tokensCase = tokensAll[1].split(" ");

						for (int i = 0; i < tokensCase.length; i++) {
							String num = tokensCase[i];
							if (num.equals(""))
								continue;
							Integer value = Integer.parseInt(num);
							lower.add(value);
						}
					}

					String[] tokensResult = tokensAll[0].split(" ");
					Integer result = Integer.parseInt(tokensResult[0]);
					this.bugCode.add(result);
					this.lowerpriority.put(result, lower);
				}
			}
			// Close the input stream
			in.close();
		} catch (Exception e) {// Catch exception if any
			e.printStackTrace();
			System.err.println("Error: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		ReadInput test = new ReadInput();
		test.readTestCases("./result.txt");
		for (TestCase testCase : test.testCases)
			System.out.println(testCase.getStringOfTest());
	}
}
