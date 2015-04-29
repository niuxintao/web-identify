package com.fc.tuple;

import interaction.CoveringManage;
import interaction.DataCenter;

import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;

public class DealTupleOfIndex {
	private DataCenter dataCenter;

	public DealTupleOfIndex(DataCenter dataCenter) {
		this.dataCenter = dataCenter;
	}

	public int getIndexOfTuple(Tuple tuple) {
		int result = 0;
		int[] values = tuple.getParamValue();
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

	public Tuple getTupleFromIndex(int index) {
		int[] indexOfTuple = this.getTupleIndex2(index);
//		 print(indexOfTuple);

		int[] valueOfTuple = new int[dataCenter.degree];

		TestCase testCaseForTuple2 = new TestCaseImplement(dataCenter.param_num);
		Tuple tuple2 = new Tuple(dataCenter.degree, testCaseForTuple2);
		tuple2.setParamIndex(indexOfTuple);

		int indexreMian = index - this.getIndexOfTuple(tuple2);

		for (int i = 0; i < dataCenter.degree; i++) {
			int needMode = 1;
			for (int j = i + 1; j < dataCenter.degree; j++) {
				needMode *= dataCenter.param[indexOfTuple[j]];
			}
			int value = indexreMian / needMode;
			indexreMian = indexreMian % needMode;
			valueOfTuple[i] = value;

		}

		TestCase testCaseForTuple = new TestCaseImplement(dataCenter.param_num);
		for (int i = 0; i < indexOfTuple.length; i++)
			testCaseForTuple.set(indexOfTuple[i], valueOfTuple[i]);
		Tuple tuple = new Tuple(indexOfTuple.length, testCaseForTuple);
		tuple.setParamIndex(indexOfTuple);

		return tuple;
	}

	
	public void print(int[] a){
		for(int i: a)
			System.out.print(i + " ");
		System.out.println();
	}
	public int[] getTupleIndex2(int index) {
		int[] indexs = dataCenter.index;

		int high = indexs.length - 1;
		int low = 0;

		int result = high;

		while (high >= low) {
			int middle = (int) (0.5 * (low + high));
			// System.out.println("middle : " + middle);

			if (middle == high) {
				result = middle;
				break;
			}

			int thisIndex = dataCenter.index[middle];
			int nextIndex = dataCenter.index[middle + 1];

			if (thisIndex <= index && nextIndex > index) {
				result = middle;
				break;
			} else if (index >= nextIndex) {
				low = middle + 1;

			} else if (index < thisIndex) {
				high = middle - 1;
			}
		}

		Tuple tuple = dataCenter.tupleIndex[result];
		return tuple.getParamIndex();

	}

	public int[] getTupleIndex(int index) {
		int[] indexes = new int[dataCenter.degree];
		int degree = dataCenter.degree;

		int[] indexFirst = new int[0];
		for (int i = 0; i < degree; i++) {
			int nextIndex = this.getNextIndex(indexFirst, index);
			// System.out.println(nextIndex);
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
		int high = dataCenter.param_num - dataCenter.degree;
		if (indexFirst != null && indexFirst.length > 0)
			high = dataCenter.param_num - (dataCenter.degree - indexFirst.length);

		// System.out.println("high : " + high + "low : " + low);

		int result = high;

		while (high >= low) {
			int middle = (int) (0.5 * (low + high));
			// System.out.println("middle : " + middle);

			if (middle == high) {
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

			Tuple tuple1 = this.getTuple(first, dataCenter.degree);
			int tuple1Index = this.getIndexOfTuple(tuple1);

			Tuple tuple2 = this.getTuple(first2, dataCenter.degree);
			int tuple2Index = this.getIndexOfTuple(tuple2);

			if (tuple1Index <= index && tuple2Index > index) {
				result = middle;
				break;
			} else if (index >= tuple2Index) {
				low = middle + 1;

			} else if (index < tuple1Index) {
				high = middle - 1;
			}
			// System.out.println("start : " + low + " tail : " + high);
		}

		return result;
	}

	public Tuple getTuple(int[] indexFirst, int degree) {
		TestCase testCaseForTuple = new TestCaseImplement(dataCenter.param_num);
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

}
