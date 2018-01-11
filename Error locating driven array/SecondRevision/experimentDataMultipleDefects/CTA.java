package experimentDataMultipleDefects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
//import weka.classifiers.trees.SimpleCart;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

import com.fc.caseRunner.CaseRunner;
//import com.fc.model.OFOT;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.testObject.TestSuite;
import com.fc.testObject.TestSuiteImplement;
import com.fc.tuple.Tuple;

public class CTA {
	private HashMap<TestCase, Integer> executed;
	private String tree;
	private HashMap<Integer, List<Tuple>> bugs;

	public String getTree() {
		return tree;
	}

	public HashMap<TestCase, Integer> getExecuted() {
		return executed;
	}

	public HashMap<Integer, List<Tuple>> getBugs() {
		return bugs;
	}

	public List<Tuple> getBugs(int wrongCode) {
		return bugs.get(wrongCode);
	}

	public CTA() {
		executed = new HashMap<TestCase, Integer>();
		bugs = new HashMap<Integer, List<Tuple>>();
	}

	public void process(int[] paramters, String[] classes, TestSuite suite,
			String[] state) throws Exception {
		FastVector attr = this.constructAttributes(paramters, classes);
		Instances data = this.constructData(suite, attr, state);
		// System.out.println(data.numInstances());
		tree = this.constructClassifier(data);
		bugs = this.getBugs(tree, paramters.length);
	}

	public FastVector constructAttributes(int[] paramters, String[] classes) {

		FastVector attributes = new FastVector(paramters.length + 1);

		for (int i = 0; i < paramters.length; i++) {
			FastVector labels = new FastVector(paramters[i]);
			for (int j = 0; j < paramters[i]; j++)
				labels.addElement("" + j);
			String c = "c_" + i;
			Attribute nominal = new Attribute(c, labels);
			attributes.addElement(nominal);
		}

		final FastVector classValues = new FastVector(classes.length);
		for (String str : classes)
			classValues.addElement(str);

		attributes.addElement(new Attribute("Class", classValues));

		return attributes;
		// Instances data = new Instances();
	}

	public Instances constructData(TestSuite suite, FastVector attributes,
			String[] state) {
		Instances data = new Instances("Data1", attributes,
				suite.getTestCaseNum());
		// set the index of the col of the class in the data
		data.setClassIndex(data.numAttributes() - 1);

		for (int i = 0; i < suite.getTestCaseNum(); i++) {
			TestCase testCase = suite.getAt(i);
			Instance ins = new Instance(testCase.getLength() + 1);
			for (int j = 0; j < testCase.getLength(); j++) {
				ins.setValue((Attribute) attributes.elementAt(j),
						testCase.getAt(j));
			}
			ins.setValue(
					(Attribute) attributes.elementAt(testCase.getLength()),
					state[i]);
			// testCase.testDescription() == TestCase.PASSED ? "pass": "fail");
			data.add(ins);
		}
		return data;
	}

	public String constructClassifier(Instances data) throws Exception {
		J48 classifier = new J48();
		String[] options = new String[3];
		options[0] = "-U";

		options[1] = "-M";
		options[2] = "1";
		classifier.setOptions(options);
		classifier.setConfidenceFactor((float) 0.25);
	

		Evaluation eval = new Evaluation(data);
		Random rand = new Random(1); // using seed = 1
		int folds = 10;
		eval.crossValidateModel(classifier, data, folds, rand);
//		System.out.println(eval.toSummaryString());
		// CrossValidate.get(data);

		// System.out.println(classifier.toString());
		classifier.buildClassifier(data);
		return classifier.toString();
	}

	// // <simple cart need>
	// public String constructClassifierNew(Instances data) throws Exception {
	// SimpleCart classifier = new SimpleCart();
	// String[] options = new String[3];
	// options[0] = "-U";
	//
	// options[1] = "-M";
	// options[2] = "1";
	// classifier.setOptions(options);
	// classifier.buildClassifier(data);
	// System.out.println(classifier.toString());
	// return classifier.toString();
	// }

	// </simple cart need>
	// public void process(TestSuite suite, int[] parameters, CaseRunner runner)
	// throws Exception {
	// for (int i = 0; i < suite.getTestCaseNum(); i++) {
	// this.executed.put(suite.getAt(i), suite.getAt(i).testDescription());
	// }
	// for (int i = 0; i < suite.getTestCaseNum(); i++) {
	// if (suite.getAt(i).testDescription() == TestCase.FAILED)
	// this.processOneTestCase(suite.getAt(i), parameters, runner);
	// }
	// TestSuite su = new TestSuiteImplement();
	// String[] state = new String[this.executed.size()];
	// int cur = 0;
	// for (TestCase testCase : this.executed.keySet()) {
	// su.addTest(testCase);
	// state[cur] = testCase.testDescription() == TestCase.PASSED ? "pass"
	// : "fail";
	// // System.out.print(testCase.getStringOfTest());
	// // System.out.println(" "+state[cur]);
	// cur++;
	// }
	// String[] classes = { "pass", "fail" };
	// this.process(parameters, classes, su, state);
	// }

	// public void processOneTestCase(TestCase wrongCase, int[] parameters,
	// CaseRunner runner) {
	// List<List<TestCase>> array = this.generateSuiteArray(wrongCase,
	// parameters);
	// for (List<TestCase> list : array) {
	// for (TestCase testCase : list) {
	// if (executed.containsKey(testCase))
	// testCase.setTestState(executed.get(testCase));
	// else {
	// testCase.setTestState(runner.runTestCase(testCase));
	// executed.put(testCase, testCase.testDescription());
	// }
	// }
	// }
	// }

	// public List<List<TestCase>> generateSuiteArray(TestCase wrongCase,
	// int[] parameters) {
	// List<List<TestCase>> suite = new ArrayList<List<TestCase>>();
	// // TestSuite suite = new TestSuiteImplement();
	// for (int i = 0; i < wrongCase.getLength(); i++) {
	// List<TestCase> temp = new ArrayList<TestCase>();
	// TestCase lastCase = wrongCase;
	// for (int k = 0; k < (parameters[i] - 1 > OFOT.ITERATNUMBER ?
	// OFOT.ITERATNUMBER
	// : parameters[i] - 1); k++) {
	// TestCase casetemple = new TestCaseImplement(
	// wrongCase.getLength());
	// for (int j = 0; j < lastCase.getLength(); j++)
	// casetemple.set(j, lastCase.getAt(j));
	// casetemple.set(i, (casetemple.getAt(i) + 1) % parameters[i]);
	// temp.add(casetemple);
	// lastCase = casetemple;
	// }
	// suite.add(temp);
	// }
	// return suite;
	// }

	public HashMap<Integer, List<Tuple>> getBugs(String tree, int length) {
		HashMap<Integer, List<Tuple>> bugs = new HashMap<Integer, List<Tuple>>();
		String[] strs = tree.split("\n");
		List<int[]> part = new ArrayList<int[]>();
		for (String str : strs) {
			if (str.indexOf("=") != -1)
				if (str.indexOf(":") == -1) { // not leaf
					int dep = depth(str);
					String[] va = str.split(" = ");
					int[] com = new int[2];
					com[0] = findNum(va[0]);
					com[1] = findNum(va[1]);
					part = part.subList(0, dep - 1);
					part.add(com);
				} else {// leaf
					String[] ss = str.split(": ");
					String[] wrongCodeString = ss[1].split(" ");
					int wrongCode = findNum(wrongCodeString[0]);
					if (wrongCode != 0) {
						int dep = depth(str);
						List<int[]> tu = new ArrayList<int[]>();
						tu.addAll(part.subList(0, dep - 1));
						String[] va = str.split(" = ");
						int[] com = new int[2];
						com[0] = findNum(va[0]);
						String[] vap = va[1].split(":");
						com[1] = findNum(vap[0]);
						tu.add(com);
						if (bugs.get(wrongCode) == null) {
							List<Tuple> bug_code = new ArrayList<Tuple>();
							bugs.put(wrongCode, bug_code);
						}
						bugs.get(wrongCode).add(this.getBug(tu, length));
					}
				}
		}
		return bugs;
	}

	// <simpleCart Need>
	public List<Tuple> getBugsNew(String tree, int length) {
		List<Tuple> bugs = new ArrayList<Tuple>();
		String[] strs = tree.split("\n");
		// List<int[]> part = new ArrayList<int[]>();
		for (String str : strs) {
			if (str.indexOf("=") != -1) {

			}
		}
		return bugs;
	}

	// </simpleCart need>

	public Tuple getBug(List<int[]> part, int length) {
		TestCase testCase = new TestCaseImplement(length);
		for (int i = 0; i < testCase.getLength(); i++)
			testCase.set(i, 0);

		int[] index = new int[part.size()];

		int k = 0;
		for (int[] com : part) {
			testCase.set(com[0], com[1]);
			index[k] = com[0];
			k++;
		}

		Tuple tuple = new Tuple(part.size(), testCase);

		Arrays.sort(index);
		for (int i = 0; i < part.size(); i++)
			tuple.set(i, index[i]);

		return tuple;
	}

	public int depth(String str) {
		int depth = 1;
		for (int i = 0; i < str.length(); i++)
			if (str.charAt(i) == '|')
				depth++;
		return depth;
	}

	public int findNum(String str) {
		boolean find = false;
		int num = 0;
		int dig = 1;
		for (int i = str.length() - 1; i >= 0; i--) {
			if (str.charAt(i) >= '0' && str.charAt(i) <= '9') {
				find = true;
				num += (str.charAt(i) - '0') * dig;
				dig *= 10;
			} else {
				if (find)
					break;
			}
		}
		if (!find)
			return -1;
		return num;
	}

	// <test Mutilple>
	public void process(int[] parameters, CaseRunner runner) throws Exception {
		List<TestCase> testCases = generateTestSuiteM(0, parameters,
				new ArrayList<Integer>());

		for (TestCase testCase : testCases) {
			testCase.setTestState(runner.runTestCase(testCase));
			System.out.println(testCase.getStringOfTest() + ": "
					+ testCase.testDescription());
		}

		TestSuite su = new TestSuiteImplement();
		String[] state = new String[testCases.size()];
		int cur = 0;
		for (TestCase testCase : testCases) {
			su.addTest(testCase);
			state[cur] = testCase.testDescription() == TestCase.PASSED ? "pass"
					: "fail";
			// System.out.print(testCase.getStringOfTest());
			// System.out.println(" "+state[cur]);
			cur++;
		}
		String[] classes = { "pass", "fail" };
		this.process(parameters, classes, su, state);
	}

	public List<TestCase> generateTestSuiteM(int index, int[] parameters,
			List<Integer> part) {
		int nextIndex = index + 1;
		List<TestCase> testCases = new ArrayList<TestCase>();
		for (int i = 0; i < parameters[index]; i++) {
			List<Integer> partCur = new ArrayList<Integer>(
					Arrays.asList(new Integer[part.size()]));
			Collections.copy(partCur, part);
			partCur.add(i);
			if (nextIndex == parameters.length) {
				int[] testCase = new int[parameters.length];
				for (int j = 0; j < testCase.length; j++)
					testCase[j] = partCur.get(j);
				TestCaseImplement testCaseReturn = new TestCaseImplement();
				testCaseReturn.setTestCase(testCase);

				testCases.add(testCaseReturn);
				// return testCases;
			} else
				testCases.addAll(this.generateTestSuiteM(nextIndex, parameters,
						partCur));
		}
		return testCases;
	}

	// </test Multiple>

	public static void main(String[] args) throws Exception {
		CTA cta = new CTA();
		int[] param = { 3, 3, 3 };
		String[] classes = { "pass", "err1", "err2", "err3" };

		int[][] suites = { { 0, 0, 0 }, { 0, 0, 1 }, { 0, 0, 2 },
				{ 0, 1, 0 }, { 0, 1, 1 }, { 0, 1, 2 },
				{ 0, 2, 0 }, { 0, 2, 1 }, { 0, 2, 2 }, 
				{ 1, 0, 0 }, { 1, 0, 1 }, { 1, 0, 2 },
				{ 1, 1, 0 }, { 1, 1, 1 }, { 1, 1, 2 }, 
				{ 1, 2, 0 }, { 1, 2, 1 }, { 1, 2, 2 }, 
				{ 2, 0, 0 }, { 2, 0, 1 }, { 2, 0, 2 }, 
				{ 2, 1, 0 }, { 2, 1, 1 }, { 2, 1, 2 },
				{ 2, 2, 0 }, { 2, 2, 1 }, { 2, 2, 2 } };
		TestSuite suite = new TestSuiteImplement();

		for (int[] test : suites) {
			TestCaseImplement testCase = new TestCaseImplement(test.length);
			testCase.setTestCase(test);
			suite.addTest(testCase);
		}

		String[] state = { "pass", "pass", "err3", 
				           "pass", "pass", "pass",
			 	           "pass", "pass", "pass", 
			 	           "err1", "err1", "err1", 
			 	           "err3", "err1", "err1", 
			 	           "err1", "err1", "err1", 
			 	           "err2", "err2", "err2", 
			 	           "err2", "err2", "err2", 
			 	           "err3", "err2", "err2" };

		cta.process(param, classes, suite, state);
		System.out.println(cta.tree);
	}
}
