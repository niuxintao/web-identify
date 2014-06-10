package maskPracticalExperiment;

import java.util.ArrayList;
import java.util.List;

import maskSimulateExperiment.BasicRunnerInMask;

import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;

public class TableRunnerBasic extends BasicRunnerInMask {
	private List<Integer> result;
	private int[] index;

	// private int wrongCode;

	public TableRunnerBasic(List<Integer> result, int[] parameters) {
		this.result = result;
		// this.wrongCode = wrongCode;
		this.index = new int[parameters.length];
		this.index[parameters.length - 1] = 1;

		for (int i = parameters.length - 2; i >= 0; i--) {
			int value = parameters[i + 1];
			index[i] = value * index[i + 1];
		}
	}

	public TableRunnerBasic() {
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

		return res;
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

		TableRunnerBasic runner = new TableRunnerBasic(result, parameter);
		TestCaseImplement testCase = new TestCaseImplement();
		testCase.setTestCase(new int[] { 0, 0, 1 });
		System.out.println(runner.runTestCase(testCase));
	}

}
