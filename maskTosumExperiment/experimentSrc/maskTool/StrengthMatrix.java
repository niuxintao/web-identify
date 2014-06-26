package maskTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import com.fc.testObject.TestCase;

public class StrengthMatrix {
	private HashMap<Integer, double[]> matrix;
	private HashMap<Integer, List<TestCase>> executed;
	private int[] param;
	private int[] Vindex;
	private int allNum;

	private HashMap<Integer, List<TestCase>> temp;

	public HashMap<Integer, List<TestCase>> getExecuted() {
		return executed;
	}

	public void merge() {
		for (Integer key : executed.keySet()) {
			HashSet<TestCase> testCases = new HashSet<TestCase>();
			testCases.addAll(executed.get(key));
			testCases.addAll(temp.get(key));
			List<TestCase> newLy = new ArrayList<TestCase>(testCases);
			executed.put(key, newLy);
		}
	}

	public void addTestCase(Integer fault, TestCase testCase) {
		if (!this.executed.containsKey(fault)) {
			List<TestCase> values = new ArrayList<TestCase>();
			this.executed.put(fault, values);
			List<TestCase> values2 = new ArrayList<TestCase>();
			this.temp.put(fault, values2);

			matrix.put(fault, new double[allNum]);
		}
		this.temp.get(fault).add(testCase);
	}

	public StrengthMatrix(HashMap<Integer, List<TestCase>> executed,
			int[] param, int[] Vindex) {
		this.executed = executed;
		this.param = param;
		this.Vindex = Vindex;
		this.allNum = Vindex[this.param.length - 1]
				+ this.param[this.param.length - 1];

		matrix = new HashMap<Integer, double[]>();
		for (Integer key : executed.keySet()) {
			if (!matrix.containsKey(key)) {
				matrix.put(key, new double[allNum]);
			}
		}

		// matrix = new
		// double[executed.keySet().size()][Vindex[this.param.length - 1]
		// + this.param[this.param.length - 1]];
		temp = new HashMap<Integer, List<TestCase>>();
		for (Integer key : executed.keySet()) {
			List<TestCase> testCase = new ArrayList<TestCase>();
			temp.put(key, testCase);
		}
		this.updateMaxtrix();
	}

	public HashMap<Integer, double[]> getMaxtrix() {
		return matrix;
	}

	public void updateMaxtrix() {
		for (int i = 0; i < allNum; i++)
			this.updateMaxtrixS(i);
	}

	public void updateMaxtrixS(int maxtrixIndex) {
		for (int i : executed.keySet())
			this.updateMaxtrixS_S(i, maxtrixIndex);
	}

	public void updateMaxtrixS(int index, int value) {
		for (int i : executed.keySet())
			this.updateMaxtrixS_S(i, index, value);
	}

	public void updateMaxtrixS_S(int fault, int maxtrixIndex) {

		int index = this.getIndex(maxtrixIndex);
		int value = maxtrixIndex - Vindex[index];

		double result = (float) this.MO(index, value, fault)
				/ (float) (this.ALLO(index, value));

		matrix.get(fault)[maxtrixIndex] = result;
	}

	public int getIndex(int maxtrixIndex) {
		int head = 0;
		int tail = Vindex.length - 1;
		int middle = (int) (0.5 * head + 0.5 * tail);

		while (tail - head > 1) {
			if (Vindex[middle] > maxtrixIndex) {
				tail = middle;
			} else if (Vindex[middle] < maxtrixIndex) {
				head = middle;
			} else {
				head = tail = middle;
			}
			middle = (int) (0.5 * head + 0.5 * tail);
		}

		if (Vindex[tail] > maxtrixIndex)
			return head;
		else
			return tail;
	}

	public void updateMaxtrixS_S(int fault, int index, int value) {
		int maxtrixIndex = Vindex[index] + value;

		double result = (float) this.MO(index, value, fault)
				/ (float) (this.ALLO(index, value));

		matrix.get(fault)[maxtrixIndex] = result;

	}

	public int ALLO(int index, int value) {
		int result = 1;
		for (Entry<Integer, List<TestCase>> list : executed.entrySet()) {
			List<TestCase> testCases = list.getValue();
			for (TestCase testCase : testCases) {
				if (testCase.getAt(index) == value)
					result++;
			}
		}

		for (Entry<Integer, List<TestCase>> list : temp.entrySet()) {
			List<TestCase> testCases = list.getValue();
			for (TestCase testCase : testCases) {
				if (testCase.getAt(index) == value)
					result++;
			}
		}
		// System.out.println("all + " + result);
		return result;
	}

	public int MO(int index, int value, int level) {
		// System.out.print(index + " : " + value + " = ");
		int result = 0;
		List<TestCase> testCases = executed.get(level);
		if (testCases != null)
			for (TestCase testCase : testCases) {
				if (testCase.getAt(index) == value) {
					result++;
				}
			}

		List<TestCase> testCases2 = temp.get(level);
		if (testCases2 != null)
			for (TestCase testCase : testCases2) {
				if (testCase.getAt(index) == value) {
					result++;
				}
			}
		// System.out.println(result);
		return result;
	}
}
