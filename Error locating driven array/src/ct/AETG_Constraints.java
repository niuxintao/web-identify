package ct;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.fc.coveringArray.DataCenter;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

public class AETG_Constraints {

	public static final int M = 50;

	public int[] coveredMark; //
	public Integer unCovered;//

	public List<int[]> coveringArray;

	public AETG_Constraints() {
		coveringArray = new ArrayList<int[]>();
		coveredMark = new int[DataCenter.coveringArrayNum];
		unCovered = this.coveredMark.length;
	}

	public void init() {

	}

	public int[] getNextTestCase() {
		int[] best = new int[DataCenter.n];

		int bestUncovered = -1;

		for (int i = 0; i < M; i++) {
			int[] testCase = new int[DataCenter.n];
			for (int k = 0; k < testCase.length; k++)
				testCase[k] = -1;

			// select the first parameter and value
			IJ first = selectFirst();
			testCase[first.parameter] = first.value;
//			System.out.println("first" + first.parameter + " " + first.value);

			// random the remaining parameters
			int[] remainingSequence = this.randomSequnce(first.parameter);
			for (int rmI : remainingSequence) {
				// for each remaining parameter, select the best value
				int value = this.getBestValue(testCase, rmI);
				testCase[rmI] = value;
			}

			int thisUncovered = this.getUncovered(testCase);
			// System.out.println(thisUncovered);

			// repeat 50 times to get the best one.
			if (thisUncovered > bestUncovered) {
				best = testCase;
				bestUncovered = thisUncovered;
			}
		}

		coveringArray.add(best);
		return best;
	}

	public int getUncovered(int[] testCase) {
		int tempCover = 0;
		TestCase testCaseForTuple = new TestCaseImplement(DataCenter.n);
		for (int i = 0; i < testCase.length; i++)
			testCaseForTuple.set(i, testCase[i]);

		Tuple tuple = new Tuple(testCase.length, testCaseForTuple);
		tuple.setParamIndex(testCase);

		// System.out.print(tuple.toString());;

		List<Tuple> child = tuple.getChildTuplesByDegree(DataCenter.degree);

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
			print(testCase);
			CoveringManage cm = new CoveringManage();
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

	public IJ selectFirst() {
		IJ ij = new IJ();

		int bestI = -1;
		int bestJ = -1;
		int bestUncovered = -1;

		for (int i = 0; i < DataCenter.n; i++) {

			int tempBestJ = -1;
			int tempBestUncover = -1;

			for (int j = 0; j < DataCenter.param[i]; j++) {
				int uncoverThis = getUncoveredNumber(i, j);

				if (uncoverThis > tempBestUncover) {
					tempBestUncover = uncoverThis;
					tempBestJ = j;
				}
			}

			if (tempBestUncover > bestUncovered) {
				bestUncovered = tempBestUncover;
				bestI = i;
				bestJ = tempBestJ;
			}

			ij.parameter = bestI;
			ij.value = bestJ;

		}

		return ij;
	}

	public int getUncoveredNumber(int i, int j) {

		int[] giveindex = new int[1];
		int[] givevalue = new int[1];
		giveindex[0] = i;
		givevalue[0] = j;

		return this.getNumberOfCovered(giveindex, givevalue);
	}

	// the settled parameters if not reach to t, then we just

	// if reached to t, we just give the most Existed uncovered.

	public int getBestValue(int[] testCase, int rmI) {
		int bestUnCover = -1;
		int bestV = -1;

		

		for (int v = 0; v < DataCenter.param[rmI]; v++) {
			
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
			

//			List<Integer> tempIndex = new ArrayList<Integer>();
//			tempIndex.addAll(index);
//			List<Integer> tempValue = new ArrayList<Integer>();
//			tempValue.addAll(value);
//
//			tempIndex.add(rmI);
//			tempValue.add(v);

			int tempCover = 0;

			int[] givenIndex = convertIntegers(index);
			int[] givenValue = convertIntegers(value);

			if (index.size() >= DataCenter.degree) {
				TestCase testCaseForTuple = new TestCaseImplement(DataCenter.n);
				for (int i = 0; i < givenIndex.length; i++)
					testCaseForTuple.set(givenIndex[i], givenValue[i]);

				Tuple tuple = new Tuple(givenIndex.length, testCaseForTuple);

				tuple.setParamIndex(givenIndex);
				
				

//				print(tuple.getParamIndex());

				List<Tuple> child = tuple
						.getChildTuplesByDegree(DataCenter.degree);

				for (Tuple ch : child) {
					int ind = this.getIndexOfTuple(ch);
					if (coveredMark[ind] == 0)
						tempCover++;
				}

			} else
				tempCover = this.getNumberOfCovered(givenIndex, givenValue);

			if (tempCover > bestUnCover) {
				bestUnCover = tempCover;
				bestV = v;
			}
		}

		return bestV;
	}

	public int[] randomSequnce(int firstIndex) {
		int[] sequence = new int[DataCenter.n - 1];
		// sequence[0] = firstIndex;
		int cur = 0;
		for (int i = 0; i < DataCenter.n; i++) {
			if (i != firstIndex) {
				sequence[cur] = i;
				cur++;
			}
		}
		shuffleArray(sequence);
		return sequence;
	}

	// Implementing FisherĘCYates shuffle
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

	public int getNumberOfCovered(int[] givenIndex, int[] givenValue) {
		int result = 0;

		int[][] lowIndexes = this.getAllDgreeIndexs(DataCenter.degree
				- givenIndex.length);

		for (int[] lowIndex : lowIndexes) {
			if (this.isOverlapp(givenIndex, lowIndex))
				continue;

			int[][] allValues = this.getAllPossibleValues(lowIndex);

			// loop each possible value
			for (int[] lowValue : allValues) {

				// firstSet the given
				TestCase testCase = new TestCaseImplement(DataCenter.n);
				for (int i = 0; i < givenIndex.length; i++)
					testCase.set(givenIndex[i], givenValue[i]);

				// Then set the low part
				for (int i = 0; i < lowIndex.length; i++)
					testCase.set(lowIndex[i], lowValue[i]);

				Tuple existed = new Tuple(givenIndex.length, testCase);
				existed.setParamIndex(givenIndex);
				Tuple Low = new Tuple(lowIndex.length, testCase);
				Low.setParamIndex(lowIndex);

				Tuple newT = existed.cat(existed, Low);

				int index = getIndexOfTuple(newT);
				if (coveredMark[index] == 0)
					result++;
			}

		}
		return result;
	}

	public int getIndexOfTuple(Tuple tuple) {
		int result = 0;
		int[] values = tuple.getParamValue();

		// System.out.println("tuple" + tuple.toString());
		// print(tuple.getParamIndex());
		// System.out.println(tuple.toString() + " " +
		// CoveringManage.getIndex(tuple));

		int basicIndex = DataCenter.index[CoveringManage.getIndex(tuple)];

		for (int j = 0; j < DataCenter.degree; j++) {
			int k = j + 1;
			int temR = values[j];
			while (k < DataCenter.degree) {
				temR *= DataCenter.param[tuple.getParamIndex()[k]];
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
			allValuesNumber *= DataCenter.param[i];

		int[][] result = new int[allValuesNumber][];

		myStack stack = new myStack(index.length);
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
			} else if (currentPoint == DataCenter.param[index[stack.currentIndex]]) {
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
			allIndexesNum *= DataCenter.n - i;
		}
		for (int i = 0; i < degree; i++) {
			allIndexesNum /= i + 1;
		}

		int[][] tupleIndexs = new int[allIndexesNum][];
		myStack stack = new myStack(degree);
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
			} else if (currentPoint == DataCenter.param.length) {
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
		int[] param = new int[] { 2, 2, 2, 2 };
		DataCenter.init(param, 2);
		AETG_Constraints aetg = new AETG_Constraints();
		aetg.process();
	}

}
