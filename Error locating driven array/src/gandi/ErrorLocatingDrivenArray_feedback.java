package gandi;

import java.util.ArrayList;
import java.util.List;

import locatConstaint.SOFOT_Constriants;
import interaction.DataCenter;

import com.fc.caseRunner.CaseRunner;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

import ct.AETG_Constraints;

//import com.fc.coveringArray.CoveringManage;
public class ErrorLocatingDrivenArray_feedback extends ErrorLocatingDrivenArray {

	public ErrorLocatingDrivenArray_feedback(DataCenter dataCenter,
			CaseRunner caseRunner) {
		super(dataCenter, caseRunner);
		// TODO Auto-generated constructor stub
	}

	public void run() {

		AETG_Constraints ac = new AETG_Constraints(dataCenter);

		// coverage is equal to 0 is ending
		while (ac.unCovered > 0) {
			long allTime = System.currentTimeMillis();
			long geTime = System.currentTimeMillis();

			int[] test = ac.getNextTestCase();

			geTime = System.currentTimeMillis() - geTime;
			this.timeGen += geTime;

			TestCase testCase = new TestCaseImplement(test);
			overallTestCases.add(testCase);
			regularCTCases.add(testCase);
//			System.out.println("aetg" + testCase.getStringOfTest() + " "
//					+ ac.unCovered);

			if (caseRunner.runTestCase(testCase) == TestCase.PASSED) {
				ac.unCovered = cm.setCover(ac.unCovered, ac.coveredMark, test);
			} else {
				this.failTestCase.add(testCase);

				long ideTime = System.currentTimeMillis();

				List<Tuple> mfs = getMFS(ac, testCase, 0);

				if (mfs != null) {
					ideTime = System.currentTimeMillis() - ideTime;
					this.timeIden += ideTime;

					ac.addConstriants(mfs);
					this.MFS.addAll(mfs);
				} else {
					Tuple tuple = new Tuple(testCase.getLength(), testCase);
					for (int i = 0; i < tuple.getDegree(); i++)
						tuple.set(i, i);
					List<Tuple> tuples = new ArrayList<Tuple>();
					tuples.add(tuple);
					ac.addConstriants(tuples);
				}
				// setCoverage(mfs);
			}

			allTime = System.currentTimeMillis() - allTime;
			this.timeAll += allTime;
		}

		this.coveredMark = ac.coveredMark;
	}

	public List<Tuple> getMFS(AETG_Constraints ac, TestCase testCase, int index) {

		SOFOT_Constriants sc = new SOFOT_Constriants(dataCenter, testCase, ac);
		// sc.process(testCase, DataCenter.param, caseRunner);
		// System.out.println("start : "+ testCase.getStringOfTest());
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
				ac.unCovered = cm.setCover(ac.unCovered, ac.coveredMark, next);
				nextTestCase.setTestState(TestCase.PASSED);
			} else
				nextTestCase.setTestState(TestCase.FAILED);
		}

		sc.analysis();

		List<Tuple> mfs = sc.getBugs();

		// System.out.println("end : "+ testCase.getStringOfTest());

		for (Tuple tuple : mfs)
			if (tuple.getDegree() == 0) {
				if (index == sc.getExecuted().size())
					return null;

				return getMFS(ac, sc.getExecuted().get(index), index + 1);
			} else if (isMFSWrong(tuple, testCase))
				return null;

		return mfs;
	}

	boolean isMFSWrong(Tuple MFS, TestCase wrongCase) {
		TestCaseImplement newCase = new TestCaseImplement();
		int[] newC = new int[wrongCase.getLength()];
		for (int i = 0; i < newC.length; i++)
			newC[i] = (wrongCase.getAt(i) + 1) % dataCenter.param[i];
		for (int i = 0; i < MFS.getDegree(); i++)
			newC[MFS.getParamIndex()[i]] = MFS.getParamValue()[i];
		newCase.setTestCase(newC);
		return this.caseRunner.runTestCase(newCase) == TestCase.PASSED;
	}

}
