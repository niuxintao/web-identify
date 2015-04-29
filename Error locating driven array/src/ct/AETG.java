package ct;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.DealTupleOfIndex;
import com.fc.tuple.Tuple;

import interaction.CoveringManage;
import interaction.DataCenter;

public class AETG {

	public static final int M = 10;

	public int[] coveredMark; //
	public Integer unCovered;//

	public List<int[]> coveringArray;

	public DataCenter dataCenter;

	public DataCenter dataCenterTminus1;

	protected DealTupleOfIndex DOI;

	protected DealTupleOfIndex DOIminus1;

	protected GetFirstParameterValue gpv;

	public AETG(DataCenter dataCenter) {
		coveringArray = new ArrayList<int[]>();
		coveredMark = new int[dataCenter.coveringArrayNum];
		unCovered = this.coveredMark.length;
		this.dataCenter = dataCenter;
		dataCenterTminus1 = new DataCenter(dataCenter.param,
				dataCenter.degree - 1);
		DOI = new DealTupleOfIndex(dataCenter);
		DOIminus1 = new DealTupleOfIndex(dataCenterTminus1);

		gpv = new GetFirstParameterValue();
	}

	public void init() {

	}

	public int[] getNextTestCase() {
		int[] best = new int[dataCenter.param_num];

		int bestUncovered = -1;

		// System.out.println("tFirst strat");
		Tuple first = gpv.selectFirstTmiunus1(coveredMark, dataCenterTminus1.coveringArrayNum, DOI, DOIminus1);
		// System.out.println("tFirst End");

		// System.out.println("rem strat");
		for (int i = 0; i < M; i++) {
			int[] testCase = new int[dataCenter.param_num];
			for (int k = 0; k < testCase.length; k++)
				testCase[k] = -1;

			// select the first parameter and value

			int[] index = first.getParamIndex();
			int[] value = first.getParamValue();
			for (int j = 0; j < index.length; j++)
				testCase[index[j]] = value[j];
			// System.out.println("first" + first.parameter + " " +
			// first.value);

			// random the remaining parameters
			int[] firstSequnce = new int[dataCenter.param_num];
			for (int j : index) {
				firstSequnce[j] = 1;
			}
			int[] remainingSequence = this.randomSequnce(firstSequnce);

			for (int rmI : remainingSequence) {
				// for each remaining parameter, select the best value
				int bvalue = this.getBestValue(testCase, rmI);
				testCase[rmI] = bvalue;
				// System.out.println("the " + rmI + " " + value);
			}

			int thisUncovered = this.getUncovered(testCase);
			// System.out.println("remaining selected, this uncovred: " +
			// thisUncovered);

			// repeat 50 times to get the best one.
			if (thisUncovered > bestUncovered) {
				best = testCase;
				bestUncovered = thisUncovered;
			}
		}

		// System.out.println("rem end");

		coveringArray.add(best);
		return best;
	}

	public int getUncovered(int[] testCase) {
		int tempCover = 0;
		TestCase testCaseForTuple = new TestCaseImplement(dataCenter.param_num);
		for (int i = 0; i < testCase.length; i++)
			testCaseForTuple.set(i, testCase[i]);

		Tuple tuple = new Tuple(testCase.length, testCaseForTuple);
		int[] indexset = new int[testCase.length];
		for (int i = 0; i < indexset.length; i++)
			indexset[i] = i;
		tuple.setParamIndex(indexset);

		// System.out.print(tuple.toString());;

		List<Tuple> child = tuple.getChildTuplesByDegree(dataCenter.degree);

		for (Tuple ch : child) {
			int ind = this.getIndexOfTuple(ch);
			// System.out.println(ind + " " +ch.toString());
			// System.out.println(coveredMark[ind]);
			if (coveredMark[ind] == 0)
				tempCover++;
		}

		return tempCover;

	}

	public void process() {
		while (unCovered > 0) {
			int[] testCase = this.getNextTestCase();
			// print(testCase);
			CoveringManage cm = new CoveringManage(dataCenter);
			unCovered = cm.setCover(unCovered, coveredMark, testCase);

			// print(this.coveredMark);
			// System.out.println(unCovered);

		}
	}

	public void print(int[] array) {
		for (int i : array)
			System.out.print(i + " ");
		System.out.println();
	}

	// the settled parameters if not reach to t, then we just

	// if reached to t, we just give the most Existed uncovered.

	public int getBestValue(int[] testCase, int rmI) {
		int bestUnCover = -1;
		int bestV = -1;

		for (int v = 0; v < dataCenter.param[rmI]; v++) {

			int[] tempTestCase = new int[testCase.length];
			System.arraycopy(testCase, 0, tempTestCase, 0, testCase.length);
			tempTestCase[rmI] = v;
			List<Integer> index = new ArrayList<Integer>();
			List<Integer> value = new ArrayList<Integer>();

			for (int i = 0; i < tempTestCase.length; i++) {
				if (tempTestCase[i] > -1) {
					index.add(i);
					value.add(tempTestCase[i]);
				}
			}

			// List<Integer> tempIndex = new ArrayList<Integer>();
			// tempIndex.addAll(index);
			// List<Integer> tempValue = new ArrayList<Integer>();
			// tempValue.addAll(value);
			//
			// tempIndex.add(rmI);
			// tempValue.add(v);

			int tempCover = 0;

			int[] givenIndex = convertIntegers(index);
			int[] givenValue = convertIntegers(value);

			TestCase testCaseForTuple = new TestCaseImplement(
					dataCenter.param_num);
			for (int i = 0; i < givenIndex.length; i++)
				testCaseForTuple.set(givenIndex[i], givenValue[i]);

			Tuple tuple = new Tuple(givenIndex.length, testCaseForTuple);

			tuple.setParamIndex(givenIndex);

			// print(tuple.getParamIndex());

			List<Tuple> child = tuple.getChildTuplesByDegree(dataCenter.degree);

			for (Tuple ch : child) {
				int ind = this.getIndexOfTuple(ch);
				if (coveredMark[ind] == 0)
					tempCover++;
			}

			if (tempCover > bestUnCover) {
				bestUnCover = tempCover;
				bestV = v;
			}
		}

		return bestV;
	}

	public int[] randomSequnce(int firstIndex) {
		int[] sequence = new int[dataCenter.param_num - 1];
		// sequence[0] = firstIndex;
		int cur = 0;
		for (int i = 0; i < dataCenter.param_num; i++) {
			if (i != firstIndex) {
				sequence[cur] = i;
				cur++;
			}
		}
		shuffleArray(sequence);
		return sequence;
	}

	public int[] randomSequnce(int[] firstIndex) {
		int[] sequence = new int[dataCenter.param_num - 1];
		// sequence[0] = firstIndex;
		int cur = 0;
		for (int i = 0; i < dataCenter.param_num; i++) {
			if (firstIndex[i] != 1) {
				sequence[cur] = i;
				cur++;
			}
		}
		shuffleArray(sequence);
		return sequence;
	}

	// Implementing Fisher¨CYates shuffle
	void shuffleArray(int[] ar) {
		Random rnd = new Random();
		for (int i = ar.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			// Simple swap
			int a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
		}
	}

	public int getIndexOfTuple(Tuple tuple) {
		int result = 0;
		int[] values = tuple.getParamValue();

		// System.out.println("tuple" + tuple.toString());
		// print(tuple.getParamIndex());
		// System.out.println(tuple.toString() + " " +
		// CoveringManage.getIndex(tuple));
		CoveringManage cm = new CoveringManage(dataCenter);

		int basicIndex = dataCenter.index[cm.getIndex(tuple)];

		for (int j = 0; j < dataCenter.degree; j++) {
			int k = j + 1;
			int temR = values[j];
			while (k < dataCenter.degree) {
				temR *= dataCenter.param[tuple.getParamIndex()[k]];
				k++;
			}
			basicIndex += temR;
		}
		result = basicIndex;

		return result;
	}

	public boolean isOverlapp(int[] ina, int[] inb) {
		for (int i : ina) {
			for (int j : inb) {
				if (i == j)
					return true;
			}
		}
		return false;
	}

	public int[][] getAllPossibleValues(int[] index) {
		int allValuesNumber = 1;
		for (int i : index)
			allValuesNumber *= dataCenter.param[i];

		int[][] result = new int[allValuesNumber][];

		MyStack stack = new MyStack(index.length);
		// int indexNum = 1;
		int currentPoint = 0;

		int i = 0;

		boolean state = false;

		while (true) {
			if (stack.isFull()) {
				int[] indextemp = new int[stack.size];
				System.arraycopy(stack.dataIndexs, 0, indextemp, 0, stack.size);
				result[i] = indextemp;
				i++;
				stack.pop();
				currentPoint++;
			} else if (currentPoint == dataCenter.param[index[stack.currentIndex]]) {
				if (stack.isEmpty())
					break;
				stack.pop();
				currentPoint = stack.dataIndexs[stack.currentIndex] + 1;

				state = true;
			} else {

				stack.push(currentPoint);

				if (state) {
					currentPoint = 0;
					state = false;
				}
			}
		}

		return result;
	}

	public int[][] getAllDgreeIndexs(int degree) {
		// get All the numnber of low degree indexes
		int allIndexesNum = 1;
		for (int i = 0; i < degree; i++) {
			allIndexesNum *= dataCenter.param_num - i;
		}
		for (int i = 0; i < degree; i++) {
			allIndexesNum /= i + 1;
		}

		int[][] tupleIndexs = new int[allIndexesNum][];
		MyStack stack = new MyStack(degree);
		// int indexNum = 1;
		int currentPoint = 0;
		// int allNum = 0;
		int i = 0;

		while (true) {
			if (stack.isFull()) {
				// DataCenter.index[i] = allNum;
				// allNum += stack.mutli();
				int[] indextemp = new int[stack.size];
				System.arraycopy(stack.dataIndexs, 0, indextemp, 0, stack.size);
				tupleIndexs[i] = indextemp;

				i++;
				stack.pop();
			} else if (currentPoint == dataCenter.param.length) {
				if (stack.isEmpty())
					break;
				stack.pop();
				currentPoint = stack.dataIndexs[stack.currentIndex] + 1;

			} else {
				stack.push(currentPoint);
				currentPoint++;
			}
		}

		return tupleIndexs;
	}

	public static int[] convertIntegers(List<Integer> integers) {
		int[] ret = new int[integers.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = integers.get(i).intValue();
		}
		return ret;
	}

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		int[] param = new int[30];
		for (int i = 0; i < 30; i++) {
			param[i] = 3;
		}
		DataCenter dataCenter = new DataCenter(param, 4);
		AETG aetg = new AETG(dataCenter);
		aetg.process();
		List<int[]> corrvery = aetg.coveringArray;
		for (int[] row : corrvery) {
			aetg.print(row);
		}
		long time = System.currentTimeMillis() - start;
		System.out.print(time / 1000);
	}

}
