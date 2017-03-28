package grandi2;

import java.util.ArrayList;
import java.util.List;

import locatConstaint.FIC_Constraints;
import gandi.CT_process;
import gandi.ErrorLocatingDrivenArray;
import interaction.DataCenter;

import com.fc.caseRunner.CaseRunner;
import com.fc.caseRunner.CaseRunnerWithBugInject;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

import ct.AETG_Constraints;

//import com.fc.coveringArray.CoveringManage;
public class ErrorLocatingDrivenArray_feedback_MUOFOT extends ErrorLocatingDrivenArray {

	public static final int CHANGENUM = 10;

	public ErrorLocatingDrivenArray_feedback_MUOFOT(DataCenter dataCenter, CaseRunner caseRunner) {
		super(dataCenter, caseRunner);
		// TODO Auto-generated constructor stub
	}

	public void run() {

		AETG_Constraints ac = new AETG_Constraints(dataCenter);

		// coverage is equal to 0 is ending
		int numChange = 0;
		int lastUnCovered = 0;

		while (ac.unCovered > 0) {
			long allTime = System.currentTimeMillis();
			long geTime = System.currentTimeMillis();

			// must jump out of the loop
			if (ac.unCovered.intValue() == lastUnCovered)
				numChange++;
			else {
				numChange = 0;
				lastUnCovered = ac.unCovered.intValue();
			}

			// System.out.println("get next ");

			int[] test = ac.getNextTestCase();

			geTime = System.currentTimeMillis() - geTime;
			this.timeGen += geTime;

			TestCase testCase = new TestCaseImplement(test);
			overallTestCases.add(testCase);
			regularCTCases.add(testCase);
			// System.out.println("aetg" + testCase.getStringOfTest() + " "
			// + ac.unCovered);

			if (caseRunner.runTestCase(testCase) == TestCase.PASSED) {
				ac.unCovered = cm.setCover(ac.unCovered, ac.coveredMark, test);
			} else {
				this.failTestCase.add(testCase);

				long ideTime = System.currentTimeMillis();

				// System.out.println("get MFS ");
				List<Tuple> mfs = getMFS(ac, testCase);

				if (mfs != null) {
					ideTime = System.currentTimeMillis() - ideTime;
					this.timeIden += ideTime;
					// System.out.println("add constriants");
					ac.addConstriants(mfs);
					this.MFS.addAll(mfs);
					// for(Tuple tuple : mfs)
					// System.out.println("mfs is: " + tuple.toString());
				} else {
					// System.out.println("multiple");
					Tuple tuple = new Tuple(testCase.getLength(), testCase);
					for (int i = 0; i < tuple.getDegree(); i++)
						tuple.set(i, i);
					List<Tuple> tuples = new ArrayList<Tuple>();
					tuples.add(tuple);
					ac.addConstriants(tuples);

					if (numChange > CHANGENUM) {
						numChange = 0;
						Tuple tuplet = new Tuple(1, testCase);
						tuplet.set(0, testCase.getLength() - 1);
						List<Tuple> tuplesr = new ArrayList<Tuple>();
						tuplesr.add(tuplet);
						ac.addConstriants(tuplesr);
						this.MFS.addAll(tuplesr);
					}
				}
				// setCoverage(mfs);
			}

			// System.out.println("identify is over");

			allTime = System.currentTimeMillis() - allTime;
			this.timeAll += allTime;
		}

		this.coveredMark = ac.coveredMark;
	}

	public List<Tuple> getMFS(AETG_Constraints ac, TestCase testCase) {

		FIC_Constraints sc = new FIC_Constraints(testCase, dataCenter.getParam(), caseRunner, ac);

		// sc.process(testCase, DataCenter.param, caseRunner);
		sc.FicSingleMuOFOT();
		List<Tuple> mfs = sc.getBugs();
		List<TestCase> executed = sc.getExecuted();

		for (TestCase nextTestCase : executed) {
			identifyCases.add(nextTestCase);
			overallTestCases.add(nextTestCase);
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

		// System.out.println("failing test Case : " +
		// testCase.getStringOfTest());
		for (Tuple tuple : mfs) {
			// System.out.println("obtained MFS : " + tuple.toString());
			if (tuple.getDegree() == 0) {
				// System.out.println("multiple");
				return null;
			} 
//			else if (isMFSWrong( ac, tuple, testCase, executed))
//				return null;
		}

		return mfs;
	}

	// public void anayisIterati(TestCase testCase){
	//
	// }

	/**
	 * to test whether the MFS is correct
	 * 
	 * @param MFS
	 * @param wrongCase
	 * @return
	 */
	boolean isMFSWrong(AETG_Constraints ac, Tuple MFS, TestCase wrongCase,List<TestCase> executed) {
		
		TestCase last = executed.get(executed.size() - 1);
		
		TestCaseImplement newCase = new TestCaseImplement();
		int[] newC = new int[wrongCase.getLength()];
		for (int i = 0; i < newC.length; i++)
			newC[i] =last.getAt(i);
//		for (int i = 0; i < MFS.getDegree(); i++)
//			newC[MFS.getParamIndex()[i]] = MFS.getParamValue()[i];
		newCase.setTestCase(newC);
		
		for(int i = 0; i < newC.length; i++){
			if(!MFScontainIndex(MFS, i)){
				int original = newCase.getAt(i);
				if(this.dataCenter.param[i] > 2){
					for(int j = 0; j < dataCenter.param[i]; j ++)
						if(j != wrongCase.getAt(i) && j != last.getAt(i)){
							newCase.set(i, j);
							break;
						}
//					newCase.set(i, dataCenter.param[i]);
				}
				if(containExistedMFS(newCase)){
					newCase.set(i, original);
					continue;
				}
			}
		}
			
		
		identifyCases.add(newCase);
		overallTestCases.add(newCase);
		
		if(this.caseRunner.runTestCase(newCase) == TestCase.PASSED){
			ac.unCovered = cm.setCover(ac.unCovered, ac.coveredMark, newCase.getTestCase());
			return true;
		}else
			return false;
//		return this.caseRunner.runTestCase(newCase) == TestCase.PASSED;
		
	}

	public boolean containExistedMFS(TestCase testCase) {
		for (Tuple tuple : MFS) {
			if (testCase.containsOf(tuple))
				return true;
		}
		return false;
	}

	public boolean MFScontainIndex(Tuple tuple, int index) {
		for (int i : tuple.getParamIndex())
			if (i == index)
				return true;
		return false;

	}
	
	public static void main(String[] args) {
		int[] wrong = new int[] { 1, 1, 0, 1 };
		TestCase wrongCase = new TestCaseImplement();
		((TestCaseImplement) wrongCase).setTestCase(wrong);

		int[] wrong2 = new int[] { 0, 0, 0, 0 };
		TestCase wrongCase2 = new TestCaseImplement();
		((TestCaseImplement) wrongCase2).setTestCase(wrong2);

		int[] param = new int[] { 3, 3, 2, 2};

		DataCenter dataCenter = new DataCenter(param, 2);

		Tuple bugModel1 = new Tuple(2, wrongCase);
		bugModel1.set(0, 2);
		bugModel1.set(1, 3);

		Tuple bugModel2 = new Tuple(1, wrongCase2);
		bugModel2.set(0, 1);

		CaseRunner caseRunner = new CaseRunnerWithBugInject();
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel1);
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel2);

		CT_process elda = new ErrorLocatingDrivenArray_feedback_MUOFOT(dataCenter, caseRunner);
		elda.run();

		System.out
				.println("testCase Num: " + elda.getOverallTestCases().size());
		for (TestCase testCase : elda.getOverallTestCases()) {
			System.out.println(testCase.getStringOfTest());
		}
		System.out.println("MFS");
		for (Tuple mfs : elda.getMFS())
			System.out.println(mfs.toString());

	}


}
