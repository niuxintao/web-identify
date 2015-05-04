package gandi;

import java.util.List;

import locatConstaint.SOFOT_Constriants;
import interaction.DataCenter;

import com.fc.caseRunner.CaseRunner;
import com.fc.testObject.TestCase;
import com.fc.tuple.Tuple;

import ct.AETG_Constraints;
//import com.fc.coveringArray.CoveringManage;
public class ErrorLocatingDrivenArray_CB extends ErrorLocatingDrivenArray {

	public ErrorLocatingDrivenArray_CB(DataCenter dataCenter,
			CaseRunner caseRunner) {
		super(dataCenter, caseRunner);
		// TODO Auto-generated constructor stub
	}
	
	public List<Tuple> getMFS(AETG_Constraints ac, TestCase testCase) {
		

		SOFOT_Constriants sc = new SOFOT_Constriants(dataCenter,
				testCase, ac);
		// sc.process(testCase, DataCenter.param, caseRunner);

		while (!sc.isEnd()) {
			TestCase nextTestCase = sc.generateNext();
			identifyCases.add(nextTestCase);
			overallTestCases.add(nextTestCase);
			// System.out.println("ofot" +
			// nextTestCase.getStringOfTest());

			int[] next = new int[nextTestCase.getLength()];
			for (int i = 0; i < next.length; i++) {
				next[i] = nextTestCase.getAt(i);
			}
			if (caseRunner.runTestCase(nextTestCase) == TestCase.PASSED) {
				ac.unCovered = cm.setCover(ac.unCovered,
						ac.coveredMark, next);
				nextTestCase.setTestState(TestCase.PASSED);
			} else
				nextTestCase.setTestState(TestCase.FAILED);
		}

		sc.analysis();
		
		List<Tuple> mfs = sc.getBugs();
		return mfs;
	}

}
