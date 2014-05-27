package maskPracticalExperiment;

import java.util.ArrayList;
import java.util.List;

import com.fc.caseRunner.CaseRunner;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;

public class TableRunnerMask implements CaseRunner {
	private List<Integer> result;
	private int[] index;
	private int wrongCode;

	private List<Integer> lowerpriority;

	public TableRunnerMask(List<Integer> result, int[] parameters,
			List<Integer> lowerpriority, int wrongCode) {
		this.result = result;
		this.wrongCode = wrongCode;
		this.lowerpriority = lowerpriority;
		this.index = new int[parameters.length];
		this.index[parameters.length - 1] = 1;

		for (int i = parameters.length - 2; i >= 0; i--) {
			int value = parameters[i + 1];
			index[i] = value * index[i + 1];
		}
	}

	public TableRunnerMask() {
		result = new ArrayList<Integer>();
	}

	@Override
	public int runTestCase(TestCase testCase) {
		// TODO Auto-generated method stub
		int index = 0;
		for (int i = 0; i < testCase.getLength(); i++) {
			index += testCase.getAt(i) * this.index[i];
		}
		int res = result.get(index);

		if (res == 0)
			return TestCase.PASSED;
		else if (res == wrongCode)
			return TestCase.FAILED;

		for (Integer i : this.lowerpriority)
			if (res == i)
				return TestCase.PASSED;

		return TestCase.UNTESTED;
	}

	public static void main(String[] args) {
		int[] parameter = { 2, 2, 3 };
		List<Integer> result = new ArrayList<Integer>();
		result.add(1);
		result.add(-1);
		result.add(1);
		result.add(-1);
		result.add(1);
		result.add(1);
		result.add(1);
		result.add(1);
		result.add(-1);
		result.add(1);
		result.add(1);
		result.add(1);

		TableRunnerMask runner = new TableRunnerMask(result, parameter,
				new ArrayList<Integer>(), 1);
		TestCaseImplement testCase = new TestCaseImplement();
		testCase.setTestCase(new int[] { 0, 0, 1 });
		System.out.println(runner.runTestCase(testCase));
	}

}
