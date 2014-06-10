package maskPracticalExperiment;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fc.caseRunner.CaseRunner;
import com.fc.testObject.TestCase;
import com.fc.tuple.Tuple;

import exhaustiveMethod.ChainAugProcess;

public class Statistics {
	private ReadInput in;

	private int[] parameter;
	private List<TestCase> allCases;
	private List<Integer> result;

	private List<Integer> BugCode;
	private HashMap<Integer, List<Integer>> lowerPriority;

	private List<TestCase> rightCases;
	private List<TestCase> wrongCases;

	private HashMap<Integer, List<Tuple>> bugsTable;

	// private List<Tuple> bugs;

	// private int wrongCode;

	// private List<Integer> lowerpriority;

	public List<TestCase> getAllCases() {
		return allCases;
	}

	public HashMap<Integer, List<Tuple>> getBugsTable() {
		return this.bugsTable;
	}

	public List<Integer> getBugCode() {
		return this.BugCode;
	}

	public HashMap<Integer, List<Integer>> getLowerPriority() {
		return this.lowerPriority;
	}

	public List<TestCase> getRightCases() {
		return rightCases;
	}

	public List<TestCase> getWrongCases() {
		return wrongCases;
	}

	// public List<Tuple> getBugs() {
	// return bugs;
	// }

	public List<Integer> getResult() {
		return result;
	}

	public int[] getParam() {
		return this.parameter;
	}

	public Statistics() {
		in = new ReadInput();
		allCases = new ArrayList<TestCase>();
		BugCode = new ArrayList<Integer>();
		lowerPriority = new HashMap<Integer, List<Integer>>();
		bugsTable = new HashMap<Integer, List<Tuple>>();

		rightCases = new ArrayList<TestCase>();
		wrongCases = new ArrayList<TestCase>();
		// bugs = new ArrayList<Tuple>();
		result = new ArrayList<Integer>();

	}

	public void readTestCases(String casePath) {
		in.readTestCases(casePath);
		parameter = in.getParam();
		allCases = in.getTestCases();
		result = in.getResult();

		// rightCases = new ArrayList<TestCase>();
		for (int i = 0; i < allCases.size(); i++) {
			TestCase testCase = allCases.get(i);
			Integer res = result.get(i);
			if (res == 0)
				this.rightCases.add(testCase);
		}

	}

	public void readBugCodeAndLowePriority(String levelPath) {
		in.readBugCodeAndLowerPriority(levelPath);
		this.BugCode = in.getBugCode();
		this.lowerPriority = in.getLowerPriority();

		for (Integer wrongCode : BugCode) {
			List<Tuple> bugs = new ArrayList<Tuple>();
			bugsTable.put(wrongCode, bugs);
		}
	}

	public void readResults(int wrongCode) {
		// in.readResult(Result);
		// result = in.getResult();

		// rightCases = new ArrayList<TestCase>();
		wrongCases = new ArrayList<TestCase>();

		for (int i = 0; i < allCases.size(); i++) {
			TestCase testCase = allCases.get(i);
			Integer res = result.get(i);
			if (res == wrongCode)
				wrongCases.add(testCase);
			// else
			// rightCases.add(testCase);
		}

		// this.wrongCode = wrongCode;
		// this.lowerpriority = lowerpriority;
	}

	public void readResults() {
		// in.readResult(Result);
		// result = in.getResult();

		// rightCases = new ArrayList<TestCase>();
		wrongCases = new ArrayList<TestCase>();

		// if (!bugsTable.containsKey(wrongCode)) {
		// List<Tuple> bugs = new ArrayList<Tuple>();
		// bugsTable.put(wrongCode, bugs);
		// }

		for (int i = 0; i < allCases.size(); i++) {
			TestCase testCase = allCases.get(i);
			Integer res = result.get(i);
			if (res != 0)
				wrongCases.add(testCase);
			// else
			// rightCases.add(testCase);
		}

		// this.wrongCode = wrongCode;
		// this.lowerpriority = lowerpriority;
	}

	public int countMasking() {
		int masking = 0;
		for (int i = 0; i < allCases.size(); i++) {
			TestCase testCase = allCases.get(i);
			Integer res = result.get(i);
			if (res != 0) {
				// find the lower bug tuples to see if it masking any one
				List<Integer> lower = this.lowerPriority.get(res);
				for (Integer bugCode : lower) {
					List<Tuple> modes = this.bugsTable.get(bugCode);
					boolean find = false;
					for (Tuple mode : modes) {
						if (testCase.containsOf(mode)) {
							find = true;
							break;
						}
					}
					if (find == true)
						masking++;
				}
			}
		}
		return masking;
	}

	public void analysisBugs(int wrongCode, List<Integer> lowerpriority,
			BufferedWriter outBug, BufferedWriter outSta) {
		int mutilple = 0;
		int ovlp = 0;

		// identify all the wrong cases
		CaseRunner caseRunner = new TableRunner(result, parameter,
				lowerpriority);

		ChainAugProcess cp = new ChainAugProcess(parameter, caseRunner);

		for (TestCase testCase : this.wrongCases) {

			System.out.println("one test case");

			List<Tuple> tuples = cp.analysis(testCase);

			if (tuples.size() > 1)
				mutilple += 1;

			if (this.existOverlap(tuples))
				ovlp += 1;
		}

		this.bugsTable.get(wrongCode).addAll(cp.getExistedMFS());

		// this.bugs.addAll(cp.getExistedMFS());

		for (Tuple tuple : cp.getExistedMFS()) {
			try {
				outBug.write(this.toStringTuple(tuple));
				outBug.newLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		try {
			outSta.write("mutilple:");
			outSta.newLine();
			outSta.write(mutilple + " / " + this.wrongCases.size());
			outSta.newLine();
			outSta.write("overlapped:");
			outSta.newLine();
			outSta.write(ovlp + " / " + mutilple);
			outSta.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String toStringTuple(Tuple tuple) {
		int len = tuple.getCaseLen();
		int[] data = new int[len];
		for (int i = 0; i < len; i++) {
			data[i] = -1;
		}
		for (int i = 0; i < tuple.getDegree(); i++) {
			data[tuple.getParamIndex()[i]] = tuple.getParamValue()[i];
		}

		String rs = "";
		for (int i = 0; i < len; i++) {
			if (data[i] == -1)
				rs += "-";
			else
				rs += data[i];
			if (i != len - 1)
				rs += " ";
		}
		return rs;
	}

	public boolean isOverlap(Tuple a, Tuple b) {
		boolean result = false;
		int[] index_a = a.getParamIndex();
		int[] index_b = b.getParamIndex();
		int taga = 0, tagb = 0;
		boolean flag = true;
		while (taga < index_a.length && tagb < index_b.length) {
			if (index_a[taga] == index_b[tagb]) {
				result = true;
				break;
			}
			if (flag) {
				// count in index_a
				if (index_a[taga] < index_b[tagb]) {
					taga++;
					continue;
				} else {
					flag = false;
					tagb++;
					continue;
				}
			} else {
				// count in index_b
				if (index_b[tagb] < index_a[taga]) {
					tagb++;
					continue;
				} else {
					flag = true;
					taga++;
					continue;
				}
			}
		}
		return result;
	}

	public boolean existOverlap(List<Tuple> tuples) {
		for (int i = 0; i < tuples.size(); i++) {
			Tuple a = tuples.get(i);
			for (int j = i + 1; j < tuples.size(); j++) {
				Tuple b = tuples.get(j);
				if (isOverlap(a, b))
					return true;
			}
		}
		return false;
	}

	// public void getNumOfLargeThan2Degree(BufferedWriter outSta) {
	// int all = this.bugs.size();
	// int bigger = 0;
	// for (Tuple tuple : bugs)
	// if (tuple.getDegree() > 2)
	// bigger++;
	//
	// try {
	// outSta.write("largeThan2:");
	// outSta.newLine();
	// outSta.write(bigger + " / " + all);
	// outSta.newLine();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }

	// distance is one are all accept , if any one is bigger than 1 than reject
	public int testCaseRelatedBugs(TestCase testCase, Tuple tuple) {
		int result = 1;
		for (int i = 0; i < tuple.getDegree(); i++) {
			int index = tuple.getParamIndex()[i];
			int value = tuple.getParamValue()[i];

			int testValue = testCase.getAt(index);
			if (testValue != value && ((testValue + 1) % parameter[i]) != value) {
				result = 0;
				break;
			}
		}

		return result;
	}

	public int distanceBetweenTuples(Tuple A, Tuple B) {
		int[] param = parameter;
		int Aindex = 0;
		int Bindex = 0;
		int result = 0;

		// A change to B
		while (Aindex < A.getDegree() || Bindex < B.getDegree()) {
			if (Aindex == A.getDegree()) {
				// A not have , B have, A must add something to be B, a half
				result += param[B.getParamIndex()[Bindex]] * (1 / 2);
				Bindex++;
			} else if (Bindex == B.getDegree()) {
				// B not have, A have , add 1, A just need to reject something
				// to be B.
				result += 1;
				Aindex++;
			} else if (A.getParamIndex()[Aindex] > B.getParamIndex()[Bindex]) {
				// A large than B , so now is A want to add some thing to be B
				result += param[B.getParamIndex()[Bindex]] * (1 / 2);
				Bindex++;
			} else if (A.getParamIndex()[Aindex] < B.getParamIndex()[Bindex]) {
				// A smaller than B, so now is A want to reduce something to be
				// B
				result += 1;
				Aindex++;
			} else if (A.getParamIndex()[Aindex] == B.getParamIndex()[Bindex]) {
				// A index is equal to B, so just need to minus
				if (A.getParamValue()[Aindex] >= B.getParamValue()[Bindex])
					result += (A.getParamValue()[Aindex] - B.getParamValue()[Bindex]);
				else
					result += (param[A.getParamIndex()[Aindex]]
							+ A.getParamValue()[Aindex] - B.getParamValue()[Bindex]);
				Aindex++;
				Bindex++;
			}
		}

		return result;

	}

	// // pair wise compute, to dived the ibiggest distance
	// public void getNumOfImported(BufferedWriter outSta) {
	// // statistic the test case and the similar model (the distance must be
	// // 1)
	//
	// int all = 0;
	// int distance = 0;
	// for (Tuple A : this.bugs) {
	// for (Tuple B : this.bugs) {
	// if (!A.equals(B)) {
	// distance += this.distanceBetweenTuples(A, B);
	// all++;
	// }
	// }
	// }
	// double avgDistance = (double) distance / ((double) all * (all - 1));
	//
	// // double allMaxDis = 0;
	// // for (int i : this.parameter)
	// // allMaxDis += i;
	//
	// // double stdAvgDistance = avgDistance / allMaxDis;
	//
	// // int num = 0;
	// // int all = this.wrongCases.size();
	// //
	// // for (TestCase testCase : wrongCases) {
	// // for (Tuple tuple : bugs) {
	// // if (!testCase.containsOf(tuple)) {
	// // if (testCaseRelatedBugs(testCase, tuple) == 1) {
	// // num++;
	// // break;
	// // }
	// // }
	// // }
	// // }
	//
	// try {
	// outSta.write("imported rate:");
	// outSta.newLine();
	// outSta.write("" + avgDistance);
	// outSta.newLine();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	public static void main(String[] args) {
		Statistics statistic = new Statistics();
		statistic.readTestCases("./resultNew.txt");
		statistic.readBugCodeAndLowePriority("./FaultLevel.txt");

		for (int i = 0; i < statistic.getBugCode().size(); i++) {
			int wrongCode = statistic.getBugCode().get(i);
			List<Integer> lowerpriority = statistic.getLowerPriority().get(
					wrongCode);
			testAndRecord(statistic, wrongCode, lowerpriority);
		}

		int masking = statistic.countMasking();
		int allTestCase = statistic.allCases.size();
		int wrongCases = allTestCase - statistic.rightCases.size();
		String file = "./masking_count" + ".txt";

		try {
			BufferedWriter outSta = null;

			outSta = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file)));
			outSta.write("all : ");
			outSta.write(allTestCase + "");
			outSta.newLine();
			outSta.write("wrong : ");
			outSta.write(wrongCases + "");
			outSta.newLine();
			outSta.write("masking : ");
			outSta.write(masking + "");
			outSta.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//
		// int wrongCode = 3;
		// List<Integer> lowerpriority = new ArrayList<Integer>();
		// // lowerpriority.add(2);
		// // lowerpriority.add(3);
		// testAndRecord(statistic, wrongCode, lowerpriority);
	}

	private static void testAndRecord(Statistics statistic, int wrongCode,
			List<Integer> lowerpriority) {
		BufferedWriter outBug = null;
		BufferedWriter outSta = null;

		// List<Integer> lowerpriority = new ArrayList<Integer>();
		// lowerpriority.add(2);
		statistic.readResults(wrongCode);

		String fileBug = "./bug_ot" + wrongCode + ".txt";
		String fileSta = "./statis_ot" + wrongCode + ".txt";

		try {
			outBug = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileBug)));
			outSta = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileSta)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		statistic.analysisBugs(wrongCode, lowerpriority, outBug, outSta);
		// statistic.getNumOfLargeThan2Degree(outSta);
		// statistic.getNumOfImported(outSta);

		try {
			outBug.close();
			outSta.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
