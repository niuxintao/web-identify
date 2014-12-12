package gandi;

import java.util.HashSet;

import com.fc.caseRunner.CaseRunner;
import com.fc.coveringArray.CoveringManage;
import com.fc.coveringArray.DataCenter;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

import ct.AETG_Constraints;
import ct.SOFOT_Constriants;

public class ErrorLocatingDrivenArray {

	private CaseRunner caseRunner;

	private HashSet<TestCase> overallTestCases;

	private HashSet<Tuple> MFS;

	private CoveringManage cm;

	public ErrorLocatingDrivenArray(CaseRunner caseRunner) {
		this.caseRunner = caseRunner;
		overallTestCases = new HashSet<TestCase>();
		cm = new CoveringManage();
	}

	public void run() {

		AETG_Constraints ac = new AETG_Constraints();

		// coverage is equal to 0 is ending
		while (ac.unCovered > 0) {
			int[] test = ac.getNextTestCase();
			TestCase testCase = new TestCaseImplement(test);

			if (caseRunner.runTestCase(testCase) == TestCase.PASSED) {
				cm.setCover(ac.unCovered, ac.coveredMark, test);
			}else{
				
				SOFOT_Constriants sc = new SOFOT_Constriants();
				sc.process(testCase, DataCenter.param, caseRunner);
				//if()
				
				
			}

			overallTestCases.add(testCase);

		}

	}

}
