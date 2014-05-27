package com.fc.TRT;

import com.fc.tuple.Tuple;

public class CorpTupleWithNum {
	public static int getIndex(Tuple tuple) {
		int[] indexs = tuple.getParamIndex();
		int alllen = tuple.getCaseLen();
		int result = 0;
		for (int index : indexs) {
			int temp = 1;
			temp = temp << (alllen - index - 1);
			result += temp;
		}
		return result;
	}
}
