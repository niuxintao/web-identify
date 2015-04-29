package ct;

import interaction.InputToClauses;
import interaction.SAT;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

import interaction.DataCenter;

public class AETG_Constraints extends AETG {

	protected InputToClauses ic;
	protected SAT sat;

	protected List<int[]> clauses;

	protected List<Tuple> MFS;

	public AETG_Constraints(DataCenter dataCenter) {
		super(dataCenter);

		ic = new InputToClauses(dataCenter.param);
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
		int index = DOI.getIndexOfTuple(tuple);
		if (this.coveredMark[index] == 0) {
			coveredMark[index] = 1;
			this.unCovered--;
		}
	}

	public void setCoverage(List<Tuple> newlyMFS) {
		// itself
		for (Tuple mfs : newlyMFS) {
			if (mfs.getDegree() == dataCenter.degree) {
				setCoverage(mfs);
			} else if (mfs.getDegree() < dataCenter.degree) {
				// the parent
				List<Tuple> parentT = mfs
						.getFatherTuplesByDegree(dataCenter.degree);
				for (Tuple parent : parentT) {
					setCoverage(parent);
				}

			}
		}
		// the implicit
		for (int i = 0; i < this.coveredMark.length; i++) {
			if (this.coveredMark[i] == 0) {
				// System.out.println(i);
				Tuple tuple = DOI.getTupleFromIndex(i);
				if (!this.isSatisifed(tuple)) {
					this.coveredMark[i] = 1;
					this.unCovered--;
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

	public boolean isInvoude(int[] index, int[] value) {
		for (int i = 0; i < index.length; i++) {
			if (isInvoude(index[i], value[i]))
				return true;
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

	public int[] getNextTestCase() {
		int[] best = new int[dataCenter.param_num];

		int bestUncovered = -1;

		// select the first parameter and value
		HashSet<Tuple> cannot = new HashSet<Tuple>();
		boolean isSat = false;
		Tuple first = null;
		Tuple tempFirst = null;

		while (!isSat) {
			if (tempFirst != null)
				cannot.add(tempFirst);

			first = gpv.selectFirst(cannot, coveredMark, dataCenterTminus1.coveringArrayNum, DOI, DOIminus1);

			tempFirst = first;

			// judege if it is satisified
			isSat = !this.isInvoude(first.getParamIndex(),
					first.getParamValue())
					|| this.isSatisifed(first);
		}

		for (int i = 0; i < M; i++) {
			int[] testCase = new int[dataCenter.param_num];
			for (int k = 0; k < testCase.length; k++)
				testCase[k] = -1;

			int[] index = first.getParamIndex();
			int[] value = first.getParamValue();
			for (int j = 0; j < index.length; j++)
				testCase[index[j]] = value[j];
			
			int[] firstSequnce = new int[dataCenter.param_num];
			for (int j : index) {
				firstSequnce[j] = 1;
			}
			// System.out.println("first" + first.parameter + " " +
			// first.value);

			// random the remaining parameters
			int[] remainingSequence = this.randomSequnce(firstSequnce);

			// ************************dit not add maxtries time
			// *************************/

			for (int rmI : remainingSequence) {
				// for each remaining parameter, select the best value
				isSat = false;
				int bvalue = -1;
				int tempValue = -1;
				HashSet<Integer> cannot2 = new HashSet<Integer>();
				while (!isSat) {
					if (tempValue != -1)
						cannot2.add(tempValue);
					bvalue = this.getBestValue(testCase, rmI, cannot2);
					tempValue = bvalue;

					// judege if it is satisified
					List<Integer> indexes = new ArrayList<Integer>();
					TestCase testCaseForTuple = new TestCaseImplement(
							dataCenter.param_num);
					for (int j = 0; j < testCase.length; j++) {
						if (j == rmI) {
							testCaseForTuple.set(j, bvalue);
							indexes.add(j);
						} else if (testCase[j] != -1) {
							testCaseForTuple.set(j, testCase[j]);
							indexes.add(j);
						}
					}
					Tuple tuple = new Tuple(indexes.size(), testCaseForTuple);
					tuple.setParamIndex(convertIntegers(indexes));
					;

					isSat = !this.isInvoude(rmI, bvalue)
							|| this.isSatisifed(tuple);
				}
				testCase[rmI] = bvalue;
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

	//

	public int getBestValue(int[] testCase, int rmI, HashSet<Integer> cannot) {
		int bestUnCover = -1;
		int bestV = -1;

		for (int v = 0; v < dataCenter.param[rmI]; v++) {

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

			TestCase testCaseForTuple = new TestCaseImplement(
					dataCenter.param_num);
			for (int i = 0; i < givenIndex.length; i++)
				testCaseForTuple.set(givenIndex[i], givenValue[i]);

			Tuple tuple = new Tuple(givenIndex.length, testCaseForTuple);

			tuple.setParamIndex(givenIndex);

			// print(tuple.getParamIndex());

			List<Tuple> child = tuple.getChildTuplesByDegree(dataCenter.degree);

			for (Tuple ch : child) {
				int ind = DOI.getIndexOfTuple(ch);
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

	public static void main(String[] args) {
		int[] param = new int[] { 2, 2, 2, 2, 2, 2, 2, 2, 2 };
		DataCenter dataCenter = new DataCenter(param, 2);
		AETG_Constraints aetg = new AETG_Constraints(dataCenter);

		// next implicat (- , -, 1, 1,- , -, -, - )
		TestCaseImplement testCaseForTuple = new TestCaseImplement(
				dataCenter.param_num);
		int[] test = new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 1 };
		testCaseForTuple.setTestCase(test);

		Tuple tuple = new Tuple(2, testCaseForTuple);
		tuple.set(0, 1);
		tuple.set(1, 2);

		TestCaseImplement testCaseForTupl2e = new TestCaseImplement(
				dataCenter.param_num);
		int[] test2 = new int[] { 1, 0, 1, 1, 1, 1, 1, 1, 1 };
		testCaseForTupl2e.setTestCase(test2);
		Tuple tuple2 = new Tuple(2, testCaseForTupl2e);
		tuple2.set(0, 1);
		tuple2.set(1, 3);

		// child
		Tuple tuple3 = new Tuple(1, testCaseForTuple);
		tuple3.set(0, 7);

		// tuple.set(2, 3);

		List<Tuple> MFS = new ArrayList<Tuple>();
		MFS.add(tuple);
		MFS.add(tuple2);
		MFS.add(tuple3);

		aetg.addConstriants(MFS);
		aetg.process();
		
		List<int[]> corrvery = aetg.coveringArray;
		for (int[] row : corrvery) {
			aetg.print(row);
		}
		// int index = aetg.getIndexOfTuple(tuple);
		// System.out.println(index);
		// Tuple tu = aetg.getTupleFromIndex(63);
		// System.out.println(tu.toString());

	}

}
