package com.fc.forbiddnen;

import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

public class TransferArrayToTuple {

	public Tuple getTuple(int[] index, int[] value, int[] param) {
		int[] temp = new int[param.length];
		for (int i = 0; i < param.length; i++)
			temp[i] = 0;
		for (int i = 0; i < index.length; i++)
			temp[index[i]] = value[i];
		TestCase testCase = new TestCaseImplement(temp);
		Tuple tuple = new Tuple(index.length, testCase);
		
		return tuple;
	}

}
