package maskSimulateExperiment;

import com.fc.caseRunner.CaseRunner;
import com.fc.testObject.TestCase;

public class NonMaskRunner implements CaseRunner {
	
	private int level;
	private BasicRunnerInMask runner ;
	
	public NonMaskRunner(BasicRunnerInMask runner, int level){
		this.runner = runner;
		this.level = level;
	}
	
	@Override
	public int runTestCase(TestCase testCase) {
		// TODO Auto-generated method stub
		int level = runner.runTestCase(testCase);
		if(this.level == level)
			return TestCase.FAILED;
		else
			return TestCase.PASSED;
	}

}
