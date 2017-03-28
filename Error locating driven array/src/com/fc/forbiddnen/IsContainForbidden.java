package com.fc.forbiddnen;

import java.util.ArrayList;
import java.util.List;

import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

public class IsContainForbidden {

	public boolean isContain(Tuple tuple, int index, int value) {
		for (int i = 0; i < tuple.getParamIndex().length; i++) {
			if (tuple.getParamIndex()[i] == index) {
				if (tuple.getParamValue()[i] == value)
					return true;
			}
		}
		return false;
	}

	// the mutated index and value
	public boolean isContain(Tuple tuplePart, int index, int value,
			List<Tuple> tupleForbiddenList) {

		for (Tuple tupleForbidden : tupleForbiddenList) {
			if (isContain(tupleForbidden, index, value)) {
				if (tuplePart.contains(tupleForbidden))
					return true;
			}
		}
		return false;
	}

	public boolean isContainLast(Tuple tuple, int index, int value) {
		if (tuple.getParamIndex()[tuple.getDegree() - 1] == index) {
			if (tuple.getParamValue()[tuple.getDegree() - 1] == value)
				return true;
		}
		return false;
	}

	// only need to compare the before
	// if the before contain non -forbidden, then it must can find a satisfied
	// test case, otherwise, there must exits a forbidden
	public boolean isContainByLastElement(Tuple tuplePart,
			List<Tuple> tupleForbiddenList) {
//		System.out.println(tuplePart);
		int index = tuplePart.getParamIndex()[tuplePart.getDegree() - 1];
		int value = tuplePart.getParamValue()[tuplePart.getDegree() - 1];

		for (Tuple tupleForbidden : tupleForbiddenList) {
			if (isContainLast(tupleForbidden, index, value)) {
				if (tuplePart.contains(tupleForbidden))
					return true;
			}
		}
		return false;
	}

	/**
	 * @param part
	 *            the part test case that has already been generated
	 * @param next_value
	 *            the next_value that has been selected for checked
	 * @param param
	 *            the param of the SUT
	 * @param tupleForbiddenList
	 *            forbidden list
	 * @return
	 */
	public boolean isPossibleForTheCurrentValueContainForbidden(int[] part,
			int next_value, int[] param, List<Tuple> tupleForbiddenList) {

		int[] temp = new int[param.length];
		for (int i = 0; i < part.length; i++)
			temp[i] = part[i];
		if (part.length < temp.length)
			temp[part.length] = next_value;
		for (int i = part.length + 1; i < temp.length; i++)
			temp[i] = 0;

		TestCase testCaseTemp = new TestCaseImplement(temp);
		Tuple partComp = new Tuple(part.length + 1, testCaseTemp);
		int[] partindex = new int[part.length + 1];
		for(int i = 0; i < partindex.length; i++)
			partindex[i] = i;
		partComp.setParamIndex(partindex);
//		System.out.println(partComp.getDegree() + partComp.toString());

		return isContainByLastElement(partComp, tupleForbiddenList);
	}
	
	

	/**
	 *    temp is the current test case,  index and value is that should be mutated
	 *     index and value has not be assigned to temp yet
	 * @return
	 */
	public boolean isPossibleForTheCurrentMutationContainForbidden(int[] temp,
			int index, int value, int[] param, List<Tuple> tupleForbiddenList) {

		int[] ttm = new int[param.length];
		for(int i = 0; i < temp.length; i++)
			ttm[i] = temp[i];
		ttm[index] = value;
		
		
		TestCase testCaseTemp = new TestCaseImplement(ttm);
		Tuple partComp = new Tuple(testCaseTemp.getLength(), testCaseTemp);
		
		int[] partindex = new int[param.length];
		for(int i = 0; i < partindex.length; i++)
			partindex[i] = i;
		partComp.setParamIndex(partindex);
		
		
		return isContain(partComp, index, value, tupleForbiddenList);
	}
	
	public static void main(String[] args){
		int[] param = new int[4];
		for (int i = 0; i < param.length; i++)
			param[i] = 3;

		int[] test = new int[4];
		for (int i = 0; i < 4; i++)
			test[i] = 0;
		TestCase te = new TestCaseImplement(test);
		Tuple tuple1 = new Tuple(2, te);
		tuple1.set(0, 0);
		tuple1.set(1, 2); // (0, -, 0, -)

		test = new int[4];
		for (int i = 0; i < 4; i++)
			test[i] = 1;

		te = new TestCaseImplement(test);
		Tuple tuple2 = new Tuple(2, te);
		tuple2.set(0, 0);
		tuple2.set(1, 2); // (1, -, 1, -)

		test = new int[4];
		for (int i = 0; i < 4; i++)
			test[i] = 1;

		test[2] = 0;
		test[3] = 0; // (1, 1, 0, 0)
		te = new TestCaseImplement(test);
		Tuple tuple3 = new Tuple(2, te);
		tuple3.set(0, 0);
		tuple3.set(1, 1); // (1, 1, -, -)

		test = new int[4];
		for (int i = 0; i < 4; i++)
			test[i] = 0;

		te = new TestCaseImplement(test);
		Tuple tuple4 = new Tuple(2, te);
		tuple4.set(0, 0);
		tuple4.set(1, 3); // (0, -, -, 0)

		test = new int[4];
		for (int i = 0; i < 4; i++)
			test[i] = 2;

		te = new TestCaseImplement(test);
		Tuple tuple5 = new Tuple(2, te);
		tuple5.set(0, 0);
		tuple5.set(1, 3); // (2, -, -, 2)

		List<Tuple> tuples = new ArrayList<Tuple>();
		tuples.add(tuple1);
		tuples.add(tuple2);
		tuples.add(tuple3);
		tuples.add(tuple4);
		tuples.add(tuple5);
		IsContainForbidden isfor = new IsContainForbidden();
		System.out.println(isfor
		.isPossibleForTheCurrentValueContainForbidden(new int[] { 0,
				0, 1 }, 0, param, tuples));
		
		System.out.println(isfor
		.isPossibleForTheCurrentMutationContainForbidden(new int[] { 0,
				0, 1, 0 }, 2, 0, param, tuples));

	}

}
