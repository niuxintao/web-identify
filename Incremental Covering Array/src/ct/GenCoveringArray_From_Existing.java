package ct;

import interaction.CoveringManage;
import interaction.DataCenter;

import java.util.ArrayList;
import java.util.List;

import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

public class GenCoveringArray_From_Existing {
	public static final int M = 50;

	public int[] coveredMark; //
	public Integer unCovered;//

	public List<int[]> coveringArray;
	public DataCenter dataCenter;
	public List<int[]> existing;

	public GenCoveringArray_From_Existing(DataCenter dataCenter, List<int[]> existing) {
		coveringArray = new ArrayList<int[]>();
		coveredMark = new int[dataCenter.coveringArrayNum];
		unCovered = this.coveredMark.length;
		this.dataCenter = dataCenter;
		this.existing = existing;
	}

	public void init() {

	}

	public int[] getNextTestCase() {
		int[] best = new int[dataCenter.n];

		int bestC = -1;
		for (int[] c : this.existing) {
			int uc = this.getUncovered(c);
			if (uc > bestC) {
				bestC = uc;
				for (int i = 0; i < best.length; i++)
					best[i] = c[i];
			}
		}

		coveringArray.add(best);
		return best;
	}

	public int getUncovered(int[] testCase) {
		int tempCover = 0;
		TestCase testCaseForTuple = new TestCaseImplement(dataCenter.n);
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

	public static int[] convertIntegers(List<Integer> integers) {
		int[] ret = new int[integers.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = integers.get(i).intValue();
		}
		return ret;
	}

	public static void main(String[] args) {
		int[] param = new int[] { 2, 2, 2, 2 };
		DataCenter dataCenter = new DataCenter(param, 2);
		GenCoveringArray_From_Existing aetg = new GenCoveringArray_From_Existing(dataCenter,
				new ArrayList<int[]> ());
		aetg.process();
	}
}
