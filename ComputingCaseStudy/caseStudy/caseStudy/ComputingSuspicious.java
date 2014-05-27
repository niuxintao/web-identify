package caseStudy;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.fc.testObject.TestCase;

public class ComputingSuspicious {
	private HashMap<Integer, List<TestCase>> executed;

	public ComputingSuspicious(HashMap<Integer, List<TestCase>> executed) {
		this.executed = executed;
	}

	public double computingStrength(TestCase testCase, int faultLevel) {
		double strength = 0;
		for (int i = 0; i < testCase.getLength(); i++) {
			int index = i;
			int value = testCase.getAt(i);
			double strengO = (float) this.MO(index, value, faultLevel)
					/ (float) (this.ALLO(index, value) + 1);
			strength += strengO;
		}

		strength /= testCase.getLength();
		return strength;
	}

	public double maxStrength(TestCase testCase, int[] levels) {
		double max = -1;
		for (int level : levels) {
			double strength = this.computingStrength(testCase, level);
			if (strength > max)
				max = strength;
		}

		return max;
	}

	public int getTestCases(List<TestCase> testCases, int[] levels) {
		int result = -1;
		double min = 0;

		for (int i = 0; i < testCases.size(); i++) {
			TestCase testCase = testCases.get(i);
			double curmin = this.maxStrength(testCase, levels);
			if (result == -1 || curmin < min) {
				min = curmin;
				result = i;
			}
		}

		return result;
	}

	public int ALLO(int index, int value) {
		int result = 1;
		for (Entry<Integer, List<TestCase>> list : executed.entrySet()) {
			List<TestCase> testCases = list.getValue();
			for (TestCase testCase : testCases) {
				if (testCase.getAt(index) == value)
					result++;
			}
		}
		return result;
	}

	public int MO(int index, int value, int level) {
		int result = 0;
		List<TestCase> testCases = executed.get(level);
		if (testCases != null)
			for (TestCase testCase : testCases) {
				if (testCase.getAt(index) == value) {
					result++;
				}
			}
		return result;
	}
}
