package InputOutput;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

public class ReadInput {
	List<Tuple> bugs;
	List<HashMap<String, Integer>> paramterToString;
	int[] param;

	public List<Tuple> getBugs() {
		return bugs;
	}

	List<List<Integer>> coveredLines;

	public ReadInput() {
	}

	public int[] getParam() {
		return param;
	}

	public List<List<Integer>> getCoveredLines() {
		return coveredLines;
	}

	public List<HashMap<String, Integer>> getParamterToString() {
		return this.paramterToString;
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
							Integer va = paramterToString.get(i).get(num);
							value = va;
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

	public void readParam(String path) {
		paramterToString = new ArrayList<HashMap<String, Integer>>();
		boolean start = true;
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
					String[] tokensCase = tokensAll[0].split(" ");

					if (start == true) {
						// the first paramter value
						for (int i = 0; i < tokensCase.length; i++) {
							HashMap<String, Integer> factor = new HashMap<String, Integer>();
							factor.put(tokensCase[i], 0);
							paramterToString.add(factor);
						}
						start = false;
					}
					for (int i = 0; i < tokensCase.length; i++) {
						String num = tokensCase[i];
						if (!paramterToString.get(i).containsKey(num)) {
							// HashMap<String, Integer> factor = new
							// HashMap<String, Integer>();
							int max = -1;
							for (Integer v : paramterToString.get(i).values()) {
								if (max < v)
									max = v;
							}
							max += 1;
							paramterToString.get(i).put(tokensCase[i], max);
							// paramterToString.add(factor);
						}
					}
				}
			}
			this.param = new int[paramterToString.size()];
			for (int i = 0; i < param.length; i++) {
				int max = -1;
				for (Integer v : paramterToString.get(i).values()) {
					if (max < v)
						max = v;
				}
				max += 1;
				param[i] = max;
				// paramterToString.get(i).put(tokensCase[i], max);
			}
			// Close the input stream
			in.close();
		} catch (Exception e) {// Catch exception if any
			e.printStackTrace();
			System.err.println("Error: " + e.getMessage());
		}
	}

	public void readCoveredLines(String path) {
		coveredLines = new ArrayList<List<Integer>>();
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

					List<Integer> oneline = new ArrayList<Integer>();
					// test case
					String[] tokensCase = strLine.split(" ");

					for (int i = 0; i < tokensCase.length; i++) {
						String num = tokensCase[i];
						if (num.length() >= 1) {
							oneline.add(Integer.parseInt(num));
						}
					}
					coveredLines.add(oneline);
				}
			}
			System.out.println(coveredLines.size());

			// Close the input stream
			in.close();
		} catch (Exception e) {// Catch exception if any
			e.printStackTrace();
			System.err.println("Error: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		ReadInput test = new ReadInput();
		test.readParam("./result.txt");
	}
}
