package maskSimulateExperiment;

import com.fc.caseRunner.CaseRunner;
import com.fc.testObject.TestCase;

public class MaskEffectRunner implements CaseRunner {

	private int code;
	private BasicRunner runner;

	public MaskEffectRunner(BasicRunner runner, int code) {
		this.runner = runner;
		this.code = code;
	}

	@Override
	public int runTestCase(TestCase testCase) {
		// TODO Auto-generated method stub
		int code = runner.runTestCase(testCase);
		if (this.code == code)
			return TestCase.FAILED;
		else if (code == 0)
			return TestCase.PASSED;
		else
			return TestCase.UNTESTED;
	}

}
