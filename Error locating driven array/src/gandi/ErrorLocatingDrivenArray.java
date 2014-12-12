package gandi;

import java.util.HashSet;
import java.util.List;

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
			} else {

				SOFOT_Constriants sc = new SOFOT_Constriants();
				// sc.process(testCase, DataCenter.param, caseRunner);

				while (sc.isEnd()) {
					TestCase nextTestCase = sc.generateNext();
					int[] next = new int[nextTestCase.getLength()];
					for (int i = 0; i < next.length; i++) {
						next[i] = nextTestCase.getAt(i);
					}
					if (caseRunner.runTestCase(nextTestCase) == TestCase.PASSED) {
						cm.setCover(ac.unCovered, ac.coveredMark, next);
					}
				}

				sc.analysis();
				List<Tuple> mfs = sc.getBugs();
				this.MFS.addAll(mfs);
				setCoverage(mfs);

			}
			overallTestCases.add(testCase);
		}
	}

	public void setCoverage(List<Tuple> mfs) {
		// t-way itsself
		for (Tuple tuple : mfs) {
			if (tuple.getDegree() == DataCenter.degree) {
				// setCoverage
			} else if (tuple.getDegree() < DataCenter.degree) {
				List<Tuple> parents = tuple
						.getFatherTuplesByDegree(DataCenter.degree);
				for (Tuple parent : parents) {
					// set parent coverage
				}
			}
		}

		// the remaining coverage
		List<Tuple> remianing = null;

		for (Tuple remain : remianing) {
			// to test if it is implicit
			// and set it as forbidden.
		}
	}

}
