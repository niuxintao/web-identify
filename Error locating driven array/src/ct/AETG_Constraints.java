package ct;

import interaction.InputToClauses;
import interaction.SAT;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import interaction.CoveringManage;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

import interaction.DataCenter;

public class AETG_Constraints extends AETG {

	protected InputToClauses ic;
	protected SAT sat;

	protected List<int[]> clauses;

	protected HashSet<Tuple> MFS;

	public AETG_Constraints(DataCenter dataCenter) {
		super(dataCenter);

		ic = new InputToClauses(dataCenter.param);
		clauses = new ArrayList<int[]>();
		clauses.addAll(ic.getClauses());
		MFS = new HashSet<Tuple>();
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

	public void addConstriants(HashSet<Tuple> MFS) {
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
	
//	public void addCoverage(int[] testCase){
//			CoveringManage cm = new CoveringManage(this.dataCenter);
//		unCovered= cm.setCover(unCovered, coveredMark, testCase);
//	}
	
	public void addCoverage(HashSet<TestCase> testcases){
		CoveringManage cm = new CoveringManage(this.dataCenter);
		for(TestCase testCase : testcases){
		unCovered= cm.setCover(unCovered, coveredMark, ((TestCaseImplement)testCase).getTestCase());
		}
	}
	
//	public void removeCoverage(int[] testCase){
//			CoveringManage cm = new CoveringManage(this.dataCenter);
//		unCovered= cm.rmCover(unCovered, coveredMark, testCase);
////		unCovered=cm.setCover(unCovered, coveringArray, oldRow);
//	}
	
	public void removeCoverage(HashSet<TestCase> testcases ){
		CoveringManage cm = new CoveringManage(this.dataCenter);
		for(TestCase testCase : testcases){
		unCovered= cm.rmCover(unCovered, coveredMark, ((TestCaseImplement)testCase).getTestCase());
		}
	}

	public void setCoverage(HashSet<Tuple> newlyMFS) {
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

			first = gpv.selectFirst(cannot, coveredMark,
					dataCenterTminus1.coveringArrayNum, DOI, DOIminus1);

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

					// judege if it is satisfied
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

	/**
	 * 
	 * part must be kept
	 * 
	 * 
	 * the remaining should be different than the original.
	 * 
	 * 
	 * the best one is computed for the most covered (AETG)
	 * 
	 * 
	 * */
	public int[] getBestTestCase(Tuple part, TestCase original) {
		// System.out.println("part :" + part.toString() + " original :" +
		// original.getStringOfTest());
		int[] best = new int[dataCenter.param_num];

		int bestUncovered = -1;

		// select the first parameter and value
		HashSet<Tuple> cannot = new HashSet<Tuple>();
		boolean isSat = false;

		Tuple first = null;
		Tuple tempFirst = null;

		// get the first part
		if (part.getDegree() >= dataCenter.degree - 1) {
			first = part;
		} else {
			while (!isSat) {
				if (tempFirst != null)
					cannot.add(tempFirst);

				// System.out.println("first paramter start selection " );
				if (part.getDegree() == 0)
					first = gpv.selectFirst(cannot, coveredMark,
							dataCenterTminus1.coveringArrayNum, DOI, DOIminus1);
				first = gpv.selectFirst(part, cannot, coveredMark,
						dataCenterTminus1.coveringArrayNum, DOI, DOIminus1,
						dataCenter.param);

				tempFirst = first;
				if (tempFirst == null)
					continue;
				// System.out.println("first paramter:" + first.toString());
				//
				// //should not contain it
				boolean tempSat = true;
				for (int i = 0; i < first.getDegree(); i++) {

					TestCaseImplement testForTuple = new TestCaseImplement(
							original.getLength());
					for (int k = 0; k < testForTuple.getLength(); k++) {
						testForTuple.set(k, 0);
					}

					for (int k = 0; k < first.getParamIndex().length; k++) {
						testForTuple.set(first.getParamIndex()[k],
								first.getParamValue()[k]);
					}

					Tuple tOne = new Tuple(1, testForTuple);
					tOne.set(0, first.getParamIndex()[i]);
					// System.out.println("tOne" + tOne.toString());
					if (part.contains(tOne))
						continue;
					if (original.containsOf(tOne)) {
						tempSat = false;
						break;
					}
				}
				if (!tempSat)
					continue;

				// judege if it is satisified
				isSat = !this.isInvoude(first.getParamIndex(),
						first.getParamValue())
						|| this.isSatisifed(first);
				// System.out.println(isSat);
			}
		}

		// System.out.println("first final : " + first.toString());

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

			// for(int j = 0; j < remainingSequence.length; j++ )
			// System.out.print(remainingSequence[j] +" ");
			//
			// System.out.println();

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

					if (original.getAt(rmI) == tempValue)
						continue;

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

			// test cases can be set be -1 (cannot set the different value as it
			// is mfs), needs to reset to value the same as original
			/**
			 * this is the most important bug
			 */
			for (int k = 0; k < testCase.length; k++) {
				if (testCase[k] == -1) {
					testCase[k] = (original.getAt(k))
							% this.dataCenter.param[k];
				}
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
	
	/**
	 * 
	 * part must be kept
	 * 
	 * 
	 * the remaining should be different than the original.
	 * 
	 * 
	 * the best one is computed for the most covered (AETG)
	 * 
	 * 
	 * It should not contain the existed test cases (Use Constraints)
	 * 
	 * 
	 * */
	public int[] getBestTestCase(Tuple part, TestCase original, HashSet<TestCase> testcases) {
//		System.out.println("part :" + part.toString() + " original :" +
//		 original.getStringOfTest());
		if(testcases != null){
		List<Tuple> tuples = new ArrayList<Tuple> ();
		for(TestCase testCase : testcases){
			Tuple t = new Tuple(testCase.getLength(), testCase);
			for(int i = 0; i < t.getDegree(); i++)
				t.set(i, i);
			tuples.add(t);
		}
		
		this.addCoverage(testcases);
		this.addConstriants(tuples);
		}
		
		int[] best = new int[dataCenter.param_num];

		int bestUncovered = -1;

		// select the first parameter and value
		HashSet<Tuple> cannot = new HashSet<Tuple>();
		boolean isSat = false;

		Tuple first = null;
		Tuple tempFirst = null;

		// get the first part
		if (part.getDegree() >= dataCenter.degree - 1) {
			first = part;
		} else {
			while (!isSat) {
				if (tempFirst != null)
					cannot.add(tempFirst);

				// System.out.println("first paramter start selection " );
				if (part.getDegree() == 0)
					first = gpv.selectFirst(cannot, coveredMark,
							dataCenterTminus1.coveringArrayNum, DOI, DOIminus1);
				first = gpv.selectFirst(part, cannot, coveredMark,
						dataCenterTminus1.coveringArrayNum, DOI, DOIminus1,
						dataCenter.param);

				tempFirst = first;
				if (tempFirst == null)
					continue;
				// System.out.println("first paramter:" + first.toString());
				//
				// //should not contain it
				boolean tempSat = true;
				for (int i = 0; i < first.getDegree(); i++) {

					TestCaseImplement testForTuple = new TestCaseImplement(
							original.getLength());
					for (int k = 0; k < testForTuple.getLength(); k++) {
						testForTuple.set(k, 0);
					}

					for (int k = 0; k < first.getParamIndex().length; k++) {
						testForTuple.set(first.getParamIndex()[k],
								first.getParamValue()[k]);
					}

					Tuple tOne = new Tuple(1, testForTuple);
					tOne.set(0, first.getParamIndex()[i]);
					// System.out.println("tOne" + tOne.toString());
					if (part.contains(tOne))
						continue;
					if (original.containsOf(tOne)) {
						tempSat = false;
						break;
					}
				}
				if (!tempSat)
					continue;

				// judege if it is satisified
				isSat = !this.isInvoude(first.getParamIndex(),
						first.getParamValue())
						|| this.isSatisifed(first);
				// System.out.println(isSat);
			}
		}

		// System.out.println("first final : " + first.toString());

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

			// for(int j = 0; j < remainingSequence.length; j++ )
			// System.out.print(remainingSequence[j] +" ");
			//
			// System.out.println();

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

					if (original.getAt(rmI) == tempValue)
						continue;

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

			// test cases can be set be -1 (cannot set the different value as it
			// is mfs), needs to reset to value the same as original
			/**
			 * this is the most important bug
			 */
			for (int k = 0; k < testCase.length; k++) {
				if (testCase[k] == -1) {
					testCase[k] = (original.getAt(k))
							% this.dataCenter.param[k];
				}
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
		
		if(testcases != null)
		this.removeCoverage(testcases);
		
		
		return best;
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
