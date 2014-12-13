package ct;

import interaction.InputToClauses;
import interaction.SAT;

import java.util.ArrayList;
import java.util.HashSet;
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

	private InputToClauses ic;
	private SAT sat;

	private List<int[]> clauses;

	private List<Tuple> MFS;

	public AETG_Constraints() {
		coveringArray = new ArrayList<int[]>();
		coveredMark = new int[DataCenter.coveringArrayNum];
		unCovered = this.coveredMark.length;
		ic = new InputToClauses(DataCenter.param);
		clauses = new ArrayList<int[]>();
		clauses.addAll(ic.getClauses());
		MFS = new ArrayList<Tuple>();
		sat = new SAT();
	}

	public void addConstriants(List<Tuple> MFS) {
		this.MFS.addAll(MFS);

		for (Tuple mfs : MFS) {
			int[] clause = ic.combinationToClause(mfs.getParamIndex(),
					mfs.getParamValue());
			for (int i = 0; i < clause.length; i++)
				clause[i] = -clause[i];
			clauses.add(clause);
		}

		setCoverage(MFS);
	}

	public void setCoverage(Tuple tuple) {
		int index = this.getIndexOfTuple(tuple);
		if (this.coveredMark[index] == 0){
			coveredMark[index] = 1;
			this.unCovered --;
		}
	}

	public void setCoverage(List<Tuple> newlyMFS) {
		// itself
		for (Tuple mfs : newlyMFS) {
			if (mfs.getDegree() == DataCenter.degree) {
				setCoverage(mfs);
			} else if (mfs.getDegree() < DataCenter.degree) {
				// the parent
				List<Tuple> parentT = mfs
						.getFatherTuplesByDegree(DataCenter.degree);
				for (Tuple parent : parentT) {
					setCoverage(parent);
				}

			}
		}
		// the implicit
		for (int i = 0; i < this.coveredMark.length; i++) {
			if (this.coveredMark[i] == 0) {
//				System.out.println(i);
				Tuple tuple = this.getTupleFromIndex(i);
				if (!this.isSatisifed(tuple)){
					this.coveredMark[i] = 1;
					this.unCovered --;
				}
			}
		}
	}

	public boolean isInvoude(int index, int value) {
		for (Tuple tuple : this.MFS) {
			int[] indexes = tuple.getParamIndex();
			for (int i = 0; i < indexes.length; i++) {
				if (indexes[i] == index && tuple.getParamValue()[i] == value)
					return true;
			}
		}

		return false;

	}

	public boolean isSatisifed(Tuple tuple) {
		int[] clause = ic.combinationToClause(tuple.getParamIndex(),
				tuple.getParamValue());

		int[][] clauses = new int[this.clauses.size()][];
		for (int i = 0; i < clauses.length; i++) {
			clauses[i] = this.clauses.get(i);
		}

		return this.sat.issatisfied(ic.getAllValue(), clauses, clause);
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
			IJ first = null;
			HashSet<IJ> cannot = new HashSet<IJ>();
			boolean isSat = false;
			IJ tempFirst = null;

			while (!isSat) {
				if (tempFirst != null)
					cannot.add(tempFirst);

				first = selectFirst(cannot);

				tempFirst = first;

				// judege if it is satisified
				TestCase testCaseForTuple = new TestCaseImplement(DataCenter.n);
				for (int j = 0; j < testCase.length; j++) {
					if (j == first.parameter)
						testCaseForTuple.set(j, first.value);
				}
				Tuple tuple = new Tuple(1, testCaseForTuple);
				tuple.set(0, first.parameter);

				isSat = !this.isInvoude(first.parameter, first.value)
						|| this.isSatisifed(tuple);
			}

			testCase[first.parameter] = first.value;
			// System.out.println("first" + first.parameter + " " +
			// first.value);

			// random the remaining parameters
			int[] remainingSequence = this.randomSequnce(first.parameter);

			// ************************dit not add maxtries time
			// *************************/

			for (int rmI : remainingSequence) {
				// for each remaining parameter, select the best value
				isSat = false;
				int value = -1;
				int tempValue = -1;
				HashSet<Integer> cannot2 = new HashSet<Integer>();
				while (!isSat) {
					if (tempValue != -1)
						cannot2.add(tempValue);
					value = this.getBestValue(testCase, rmI, cannot2);
					tempValue = value;

					// judege if it is satisified
					List<Integer> indexes = new ArrayList<Integer>();
					TestCase testCaseForTuple = new TestCaseImplement(
							DataCenter.n);
					for (int j = 0; j < testCase.length; j++) {
						if (j == rmI) {
							testCaseForTuple.set(j, value);
							indexes.add(j);
						} else if (testCase[j] != -1) {
							testCaseForTuple.set(j, testCase[j]);
							indexes.add(j);
						}
					}
					Tuple tuple = new Tuple(indexes.size(), testCaseForTuple);
					tuple.setParamIndex(convertIntegers(indexes));
					;

					isSat = !this.isInvoude(rmI, value)
							|| this.isSatisifed(tuple);

				}
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

	public IJ selectFirst(HashSet<IJ> cannot) {
		IJ ij = new IJ();

		int bestI = -1;
		int bestJ = -1;
		int bestUncovered = -1;

		for (int i = 0; i < DataCenter.n; i++) {

			int tempBestJ = -1;
			int tempBestUncover = -1;

			for (int j = 0; j < DataCenter.param[i]; j++) {
				IJ tempij = new IJ();
				tempij.parameter = i;
				tempij.value = j;
				if (cannot.contains(ij))
					continue;

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

	public int getBestValue(int[] testCase, int rmI, HashSet<Integer> cannot) {
		int bestUnCover = -1;
		int bestV = -1;

		for (int v = 0; v < DataCenter.param[rmI]; v++) {

			if (cannot.contains(v))
				continue;

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
			int tempCover = 0;

			int[] givenIndex = convertIntegers(index);
			int[] givenValue = convertIntegers(value);

			if (index.size() >= DataCenter.degree) {
				TestCase testCaseForTuple = new TestCaseImplement(DataCenter.n);
				for (int i = 0; i < givenIndex.length; i++)
					testCaseForTuple.set(givenIndex[i], givenValue[i]);

				Tuple tuple = new Tuple(givenIndex.length, testCaseForTuple);

				tuple.setParamIndex(givenIndex);

				// print(tuple.getParamIndex());

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

	/**
	 * important function
	 * ***********************************************************************
	 **/
	public int getIndexOfTuple(Tuple tuple) {
		int result = 0;
		int[] values = tuple.getParamValue();
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

	/**
	 * important function
	 * ***********************************************************************
	 **/
	public Tuple getTupleFromIndex(int index) {
		int[] indexOfTuple = this.getTupleIndex(index);
//		print(indexOfTuple);

		int[] valueOfTuple = new int[DataCenter.degree];

		
		TestCase testCaseForTuple2 = new TestCaseImplement(DataCenter.n);
		Tuple tuple2 = new Tuple(DataCenter.degree, testCaseForTuple2);
		tuple2.setParamIndex(indexOfTuple);
		
		
		int indexreMian = index - this.getIndexOfTuple(tuple2);
		
		for (int i = 0; i < DataCenter.degree; i++) {
			int needMode = 1;
			for (int j = i + 1; j < DataCenter.degree; j++) {
				needMode *= DataCenter.param[indexOfTuple[j]];
			}
			int value = indexreMian / needMode;
			indexreMian = indexreMian % needMode;
			valueOfTuple[i] = value;

		}

		TestCase testCaseForTuple = new TestCaseImplement(DataCenter.n);
		for (int i = 0; i < indexOfTuple.length; i++)
			testCaseForTuple.set(indexOfTuple[i], valueOfTuple[i]);
		Tuple tuple = new Tuple(indexOfTuple.length, testCaseForTuple);
		tuple.setParamIndex(indexOfTuple);

		return tuple;
	}

	public int[] getTupleIndex(int index) {
		int[] indexes = new int[DataCenter.degree];
		int degree = DataCenter.degree;

		int[] indexFirst = new int[0];
		for (int i = 0; i < degree; i++) {
			int nextIndex = this.getNextIndex(indexFirst, index);
//			System.out.println(nextIndex);
			indexes[i] = nextIndex;
			int[] indexFirstTemp = new int[indexFirst.length + 1];
			for (int j = 0; j < indexFirst.length; j++) {
				indexFirstTemp[j] = indexFirst[j];
			}
			indexFirstTemp[indexFirst.length] = nextIndex;
			indexFirst = indexFirstTemp;
		}

		return indexes;
	}

	public int getNextIndex(int[] indexFirst, int index) {

		int low = 0;
		if (indexFirst != null && indexFirst.length > 0)
			low = indexFirst[indexFirst.length - 1] + 1;

		// add 1 or not
		int high = DataCenter.n - DataCenter.degree;
		if (indexFirst != null && indexFirst.length > 0)
			high = DataCenter.n - (DataCenter.degree - indexFirst.length);

		
//		 System.out.println("high : " + high  + "low : " + low);
		 
		int result = high;

		while (high >= low) {
			int middle = (int) (0.5 * (low + high));
//			 System.out.println("middle : " + middle);
			
			 if(middle == high){
				 result = middle;
				 break;
			 }
			int[] first = new int[indexFirst.length + 1];
			for (int i = 0; i < indexFirst.length; i++)
				first[i] = indexFirst[i];
			first[indexFirst.length] = middle;

			int[] first2 = new int[indexFirst.length + 1];
			for (int i = 0; i < indexFirst.length; i++)
				first2[i] = indexFirst[i];
			first2[indexFirst.length] = middle + 1;

			Tuple tuple1 = this.getTuple(first, DataCenter.degree);
			int tuple1Index = this.getIndexOfTuple(tuple1);

			Tuple tuple2 = this.getTuple(first2, DataCenter.degree);
			int tuple2Index = this.getIndexOfTuple(tuple2);

			if (tuple1Index <= index && tuple2Index > index) {
				result = middle;
				break;
			} else if (index >= tuple2Index) {
				low = middle + 1;

			} else if (index < tuple1Index) {
				high = middle - 1;
			}
//			 System.out.println("start : " + low + " tail : " + high);
		}


		return result;
	}

	public Tuple getTuple(int[] indexFirst, int degree) {
		TestCase testCaseForTuple = new TestCaseImplement(DataCenter.n);
		Tuple tuple = new Tuple(degree, testCaseForTuple);
		int last = -1;
		for (int i = 0; i < degree; i++) {
			if (i < indexFirst.length) {
				last = indexFirst[i];
				tuple.set(i, last);
			} else {
				last++;
				tuple.set(i, last);
			}
		}
		return tuple;
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
		int[] param = new int[] { 2, 2, 2, 2, 2, 2, 2, 2, 2 };
		DataCenter.init(param, 2);
		AETG_Constraints aetg = new AETG_Constraints();

		
		//next implicat (- , -, 1, 1,- , -, -, - )
		TestCaseImplement testCaseForTuple = new TestCaseImplement(DataCenter.n);
		int[] test = new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 1 };
		testCaseForTuple.setTestCase(test);

		Tuple tuple = new Tuple(2, testCaseForTuple);
		tuple.set(0, 1);
		tuple.set(1, 2);
		
		
		TestCaseImplement testCaseForTupl2e = new TestCaseImplement(DataCenter.n);
		int[] test2 = new int[] { 1, 0, 1, 1, 1, 1, 1, 1, 1 };
		testCaseForTupl2e.setTestCase(test2);
		Tuple tuple2 = new Tuple(2, testCaseForTupl2e);
		tuple2.set(0, 1);
		tuple2.set(1, 3);
		
		
		
		//child
		Tuple tuple3 = new Tuple(1, testCaseForTuple);
		tuple3.set(0, 7);
		
//		tuple.set(2, 3);

		List<Tuple> MFS = new ArrayList<Tuple>();
		MFS.add(tuple);
		MFS.add(tuple2);
		MFS.add(tuple3);

		aetg.addConstriants(MFS);
		aetg.process();
//		int index = aetg.getIndexOfTuple(tuple);
//		System.out.println(index);
//		Tuple tu = aetg.getTupleFromIndex(63);
//		System.out.println(tu.toString());
		
	}

}
