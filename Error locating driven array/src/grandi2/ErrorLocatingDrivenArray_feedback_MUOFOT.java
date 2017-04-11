package grandi2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import locatConstaint.FIC_Constraints;
import experiment_for_assumption.DataForSafeValueAssumption;
//import gandi.CT_process;
import gandi.ErrorLocatingDrivenArray;
import gandi.TraditionalFGLI;
import interaction.DataCenter;

import com.fc.caseRunner.CaseRunner;
import com.fc.caseRunner.CaseRunnerWithBugInject;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;




import ct.AETG_Constraints;

//import com.fc.coveringArray.CoveringManage;
//random get new tests
//find the non-mfs re-do 
public class ErrorLocatingDrivenArray_feedback_MUOFOT extends
		ErrorLocatingDrivenArray {

	public static final int CHANGENUM = 10;

	public ErrorLocatingDrivenArray_feedback_MUOFOT(DataCenter dataCenter,
			CaseRunner caseRunner) {
		super(dataCenter, caseRunner);
		// TODO Auto-generated constructor stub
	}

	public void run() {

		AETG_Constraints ac = new AETG_Constraints(dataCenter);

		// coverage is equal to 0 is ending
//		int numChange = 0;
//		int lastUnCovered = 0;

		while (ac.unCovered > 0) {
			long allTime = System.currentTimeMillis();
			long geTime = System.currentTimeMillis();

			// must jump out of the loop
//			if (ac.unCovered.intValue() == lastUnCovered)
//				numChange++;
//			else {
//				numChange = 0;
//				lastUnCovered = ac.unCovered.intValue();
//			}

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

				List<Tuple> mfs = getMFS(ac, testCase);
				// System.out.println("get MFS's size " + mfs.size());

				if (mfs != null && mfs.size() != 0) {
					ideTime = System.currentTimeMillis() - ideTime;
					this.timeIden += ideTime;
					// System.out.println("add constriants");
					ac.addConstriants(mfs);
					this.MFS.addAll(mfs);
					// for(Tuple tuple : mfs)
					// System.out.println("mfs is: " + tuple.toString());
				} else {
					// System.out.println("multiple");
//					Tuple tuple = new Tuple(testCase.getLength(), testCase);
//					for (int i = 0; i < tuple.getDegree(); i++)
//						tuple.set(i, i);
//					List<Tuple> tuples = new ArrayList<Tuple>();
//					tuples.add(tuple);
//					ac.addConstriants(tuples);
//
//					if (numChange > CHANGENUM) {
//						numChange = 0;
//						Tuple tuplet = new Tuple(1, testCase);
//						tuplet.set(0, testCase.getLength() - 1);
//						List<Tuple> tuplesr = new ArrayList<Tuple>();
//						tuplesr.add(tuplet);
//						ac.addConstriants(tuplesr);
//						this.MFS.addAll(tuplesr);
//					}
				}
				// setCoverage(mfs);
			}

			// System.out.println("identify is over");

			allTime = System.currentTimeMillis() - allTime;
			this.timeAll += allTime;
		}

		this.coveredMark = ac.coveredMark;
	}

	public List<List<Tuple>> checkMFS(List<Tuple> mfs, AETG_Constraints ac,
			TestCase testCase, HashMap<Tuple, HashSet<TestCase>>  executed) {
		List<List<Tuple>> result = new ArrayList<List<Tuple>>();
		List<Tuple> right = new ArrayList<Tuple>();
		List<Tuple> wrong = new ArrayList<Tuple>();

		result.add(right);
		result.add(wrong);

		for (Tuple tuple : mfs) {
			Tuple revser = tuple.getReverseTuple();
			if (isMFSWrong(ac, tuple, testCase, executed.get(revser))) {
				wrong.add(tuple);
			} else {
				right.add(tuple);
			}
		}
		return result;
	}

	public List<Tuple> getMFS(AETG_Constraints ac, TestCase testCase) {

		List<Tuple> result = new ArrayList<Tuple>();

		FIC_Constraints sc = new FIC_Constraints(testCase,
				dataCenter.getParam(), caseRunner, ac);

		List<Tuple> temp_mfs = reLocate(ac, sc, new ArrayList<Tuple>());

		List<List<Tuple>> checkedNonMFS = this.checkMFS(temp_mfs, ac, testCase,
				sc.getTestedTupleTestCases());

		List<Tuple> right = checkedNonMFS.get(0);
		List<Tuple> wrong = checkedNonMFS.get(1);

		result.addAll(right);

		while (right.size() == 0) { // right is large than 0 , or wrong is 0,
									// i.e., right.size() == 0 or wrong.size() != 0
			temp_mfs = reLocate(ac, sc, right);
			checkedNonMFS = this.checkMFS(temp_mfs, ac, testCase,
					sc.getTestedTupleTestCases());
			right = checkedNonMFS.get(0);
			wrong = checkedNonMFS.get(1);
			result.addAll(right);
		}

		return result;
	}

	public List<Tuple> reLocate(AETG_Constraints ac, FIC_Constraints sc,
			List<Tuple> addedMFS) {
		sc.addMFS(addedMFS);
		sc.FicSingleMuOFOT();
		List<Tuple> mfs = sc.getBugs();
		List<TestCase> executed = sc.getExecuted();
//		System.out.println("size" + );

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
	boolean isMFSWrong(AETG_Constraints ac, Tuple MFS, TestCase wrongCase,
			HashSet<TestCase> executed) {
		
		int[] test = ac.getBestTestCase(MFS, wrongCase, executed);


		TestCaseImplement newCase = new TestCaseImplement();

		newCase.setTestCase(test);

		identifyCases.add(newCase);
		overallTestCases.add(newCase);

		if (this.caseRunner.runTestCase(newCase) == TestCase.PASSED) {
			ac.unCovered = cm.setCover(ac.unCovered, ac.coveredMark,
					newCase.getTestCase());
			return true;
		} else{
			
			HashSet<TestCase> executed2 = new HashSet<TestCase> ();
			executed2.addAll(executed);
			executed2.add(newCase);
			int[] test2 = ac.getBestTestCase(MFS, wrongCase, executed2);


			TestCaseImplement newCase2 = new TestCaseImplement();

			newCase2.setTestCase(test2);

			identifyCases.add(newCase2);
			overallTestCases.add(newCase2);
			
			if (this.caseRunner.runTestCase(newCase2) == TestCase.PASSED) {
				ac.unCovered = cm.setCover(ac.unCovered, ac.coveredMark,
						newCase2.getTestCase());
				return true;
			}
				else
			return false;
		}
		// return this.caseRunner.runTestCase(newCase) == TestCase.PASSED;

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

		DataForSafeValueAssumption data = new DataForSafeValueAssumption(20, 4);
		data.setDegree(2);

		showData(data.getDataCenter(), data.getCaseRunner());
		
		
		System.out.println("second");
		int[] wrong = new int[] { 1, 1, 1, 1, 1, 1, 1, 1 };
		TestCase wrongCase = new TestCaseImplement();
		((TestCaseImplement) wrongCase).setTestCase(wrong);

		int[] wrong2 = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		TestCase wrongCase2 = new TestCaseImplement();
		((TestCaseImplement) wrongCase2).setTestCase(wrong2);

		int[] param = new int[] { 3, 3, 3, 3, 3, 3, 3, 3 };

		DataCenter dataCenter = new DataCenter(param, 2);

		Tuple bugModel1 = new Tuple(2, wrongCase);
		bugModel1.set(0, 2);
		bugModel1.set(1, 5);

		Tuple bugModel2 = new Tuple(1, wrongCase2);
		bugModel2.set(0, 1);

		CaseRunner caseRunner = new CaseRunnerWithBugInject();
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel1);
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel2);
		
		showData(dataCenter, caseRunner);

	}

	private static void showData(DataCenter dataCenter, CaseRunner caseRunner) {
		// TODO Auto-generated method stub
		System.out.println("ours");

		ErrorLocatingDrivenArray_feedback_MUOFOT elda = new ErrorLocatingDrivenArray_feedback_MUOFOT(
				dataCenter,caseRunner);
		elda.run();

		System.out
				.println("testCase Num: " + elda.getOverallTestCases().size());
		
		System.out
		.println("identiy Num: " + elda.getIdentifyCases().size());
		
		System.out
		.println("reg Num: " + elda.getRegularCTCases().size());
		
		
		for (TestCase testCase : elda.getOverallTestCases()) {
			System.out.println(testCase.getStringOfTest());
		}
		System.out.println("MFS");
		for (Tuple mfs : elda.getMFS())
			System.out.println(mfs.toString());

		System.out.println("fglt");

		TraditionalFGLI fglt = new TraditionalFGLI(	dataCenter,caseRunner);
		fglt.run();

		System.out
				.println("testCase Num: " + fglt.getOverallTestCases().size());
		
		System.out
		.println("iden Num: " + fglt.getIdentifyCases().size());
		
		System.out
		.println("regu Num: " + fglt.getRegularCTCases().size());
		
		
		for (TestCase testCase : fglt.getOverallTestCases()) {
			System.out.println(testCase.getStringOfTest());
		}
		

		System.out.println("MFS");
		for (Tuple mfs : fglt.getMFS())
			System.out.println(mfs.toString());
		
	}

}
