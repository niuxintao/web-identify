package com.fc.action;

import com.fc.testObject.TestCase;
import com.fc.tuple.Tuple;

public class Transfer {
	static String testCaseFromInt(TestCase testCase, String[][] paramstr) {
		String result = "";

		for (int i = 0; i < testCase.getLength(); i++) {
			String temp = paramstr[i][testCase.getAt(i)];
			result += temp + " ";
		}

		return result;
	}

	static String tupleFromInt(TestCase testCase, Tuple tuple,
			String[][] paramstr) {
		String[] data = new String[testCase.getLength()];
		for (int i = 0; i < testCase.getLength(); i++) {
			data[i] = "-";
		}
		for (int i = 0; i < tuple.getDegree(); i++) {
			int index = tuple.getParamIndex()[i];
			data[index] = paramstr[index][testCase.getAt(index)];
		}

		String rs = "";
		rs += "[";
		for (int i = 0; i < testCase.getLength(); i++) {
			rs += " " + data[i] + " ";
			if (i != testCase.getLength() - 1)
				rs += ",";
		}
		rs += "]";
		return rs;
	}
}
