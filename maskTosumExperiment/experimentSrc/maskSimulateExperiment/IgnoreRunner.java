package maskSimulateExperiment;

import com.fc.caseRunner.CaseRunner;
import com.fc.testObject.TestCase;

public class IgnoreRunner implements CaseRunner {

	private BasicRunner runner;

	public IgnoreRunner(BasicRunner runner) {
		this.runner = runner;
	}

	@Override
	public int runTestCase(TestCase testCase) {
		// TODO Auto-generated method stub
		int result = runner.runTestCase(testCase);
		if (result == 0)
			return TestCase.PASSED;
		else
			return TestCase.FAILED;
	}
}
