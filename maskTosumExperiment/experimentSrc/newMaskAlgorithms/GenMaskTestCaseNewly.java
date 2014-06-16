package newMaskAlgorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

public class GenMaskTestCaseNewly {
	private int[] param;

	private TestCase wrongCase; // current wrong Case

	// private int[] addedIndex;

	private int[] addedParam;

	// private int nowStep;

	private int allStep;

	private Tuple fixTuple;

	private List<Integer> array;

	public TestCase getWrongCase() {
		return wrongCase;
	}

	private void init(TestCase wrongCase, int[] param, Tuple tuple) {
		this.wrongCase = wrongCase;
		this.param = param;
		this.fixTuple = tuple;

		// addedIndex = new int[param.length - fixTuple.getDegree()];
		addedParam = new int[param.length - fixTuple.getDegree()];

		// for (int i = 0; i < addedIndex.length; i++)
		// addedIndex[i] = 0;

		int j = 0;
		for (int i = 0; i < param.length; i++) {
			if (!this.isTheIndexInTuple(i, tuple)) {
				addedParam[j] = param[i] - 1;
				j++;
			}
		}

		// nowStep = 0;

		allStep = 1;

		for (int i : addedParam)
			allStep *= i;

		array = new ArrayList<Integer>(allStep);

		for (int i = 0; i < allStep; i++)
			array.add(i);
	}

	public int[] takeKaccrodingtoStep(int step) {
		int[] result = new int[addedParam.length];
		int num = 1;
		while (num <= addedParam.length) {
			int divedfactor = 1;
			for (int i = num; i < addedParam.length; i++) {
				divedfactor *= addedParam[i];
			}
			// System.out.println(step);
			// System.out.println(divedfactor);
			int current = step / divedfactor;
			result[num - 1] = current;
			step = step % divedfactor;
			num++;
		}

	//	result[num - 1] = step;

		return result;
	}

	public boolean isStop() {
		return this.array.isEmpty();
	}

	// public void step() {
	// int i = addedIndex.length - 1;
	// addedIndex[i] += 1;
	//
	// while (addedIndex[i] >= addedParam[i]) {
	// addedIndex[i] = 0;
	// i--;
	// if (i < 0)
	// break;
	// addedIndex[i] += 1;
	// }
	// nowStep++;
	// }

	public TestCase genenteTestCase(int[] added, Tuple tuple, TestCase wrongCase) {

		int[] t = ((TestCaseImplement) wrongCase).getTestCase();
		int[] n = new int[t.length];

		int j = 0;

		for (int i = 0; i < t.length; i++) {
			if (this.isTheIndexInTuple(i, tuple))
				n[i] = t[i];
			else {
				n[i] = (t[i] + 1 + added[j]) % param[i];
				j++;
			}
		}

		TestCaseImplement gen = new TestCaseImplement();
		gen.setTestCase(n);

		return gen;
	}

	public GenMaskTestCaseNewly(TestCase wrongCase, int[] param, Tuple tuple) {
		init(wrongCase, param, tuple);
	}

	public TestCase generateTestCaseContainTuple(int givenNum, Tuple tuple) {
		/* generate a part test case just contain the tuple */

		int step = this.array.get(givenNum);

		// System.out.println(step);

		int[] index = this.takeKaccrodingtoStep(step);
		TestCase result = this.genenteTestCase(index, tuple, wrongCase);
		// this.step();
		return result;
	}

	public void deleteGenerated(int givenNum) {
		this.array.remove(givenNum);
	}

	public TestCase generateTestCaseContainTuple(Tuple tuple) {
		/* generate a part test case just contain the tuple */
		int givenNum = new Random().nextInt(array.size());

		int step = this.array.get(givenNum);

		// System.out.println(step);

		int[] index = this.takeKaccrodingtoStep(step);

//		System.out.print(tuple.toString() + " : ( ");
//
//		for (int i : index)
//			System.out.print(i + " ");
//
//		System.out.println(" )");

		TestCase result = this.genenteTestCase(index, tuple, wrongCase);

		this.deleteGenerated(givenNum);

		// nowStep++;

		// System.out.println(tuple.toString());

		// System.out.println(allStep + " " + nowStep);
		// this.step();
		return result;
	}

	public int[] tryTestCaseContainTuple(Tuple tuple, int tryNum) {
		/* generate a part test case just contain the tuple */
		Random random = new Random();
		int[] givenNums = new int[tryNum];
		for (int i = 0; i < tryNum; i++) {
			givenNums[i] = random.nextInt(array.size());
			if (i < array.size())
				for (int j = 0; j < i; j++) {
					if (givenNums[i] == givenNums[j]) {
						i--;
						break;
					}
				}
		}
		return givenNums;

		// this.deleteGenerated(givenNum);
		// this.step();
	}

	public boolean isTheIndexInTuple(int index, Tuple tuple) {
		int[] indexes = tuple.getParamIndex();
		for (int i : indexes) {
			if (index > i) {
				continue;
			} else if (index == i) {
				return true;
			} else if (index < i) {
				break;
			}
		}
		return false;
	}

	public int[] getParam() {
		return param;
	}

	public void setParam(int[] param) {
		this.param = param;
	}

	public List<Integer> getArray() {
		return array;
	}
}
