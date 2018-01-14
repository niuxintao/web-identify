package grandi2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import locatConstaint.FIC_Constraints;
import experimentData.TomcatData;
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
	
	public int wrongNumberChecked = 0;
	
	public int rightChecked = 0;
	
	public int revised = 0;
	
	
	//错误的， 改正确了
	public int negtiveImporved = 0;
	
	//正确的， 确改错了
	public int postiveImporved = 0;
	
	
	//错误的的 检查到 错误
	public int postiveChecked = 0;
	
	//正确的的 检查到 错误
	public int negativeChecked = 0;
	
	
	//启动的次数
	public int feedBackStartTimes = 0;
	

	public static final int CHANGENUM = 10;

	public ErrorLocatingDrivenArray_feedback_MUOFOT(DataCenter dataCenter,
			CaseRunner caseRunner) {
		super(dataCenter, caseRunner);
		// TODO Auto-generated constructor stub
	}
	
	
	public ErrorLocatingDrivenArray_feedback_MUOFOT(List<Tuple> actualMFS, DataCenter dataCenter,
			CaseRunner caseRunner) {
		super(actualMFS, dataCenter, caseRunner);
		// TODO Auto-generated constructor stub
	}
	
//	public List<Tuple>
	
	// 1. 单独统计多个测试用例的
	// 2. 统计得到的mfs
	// 3. 计算f-measure
	// 4. 计算测试用例个数
	// 5. 测试改正次数
	

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
				
				containNow = new ArrayList<Tuple>();
				for (Tuple acMFS : actualMFS) {
					if (testCase.containsOf(acMFS))
						containNow.add(acMFS);
				}
				
				int contain = 0;
				List<Tuple> templ = new ArrayList<Tuple> ();
				for (Tuple acMFS : actualMFS) {
					if (testCase.containsOf(acMFS))
						contain++;
					if (contain > 1) {
						break;
					}
				}
				if (contain > 1) {
					for (Tuple acMFS : actualMFS) {
						if (testCase.containsOf(acMFS))
							templ.add(acMFS);
					}
				}
				
				this.actualRealMFSInMultiple.addAll(templ);

				

				long ideTime = System.currentTimeMillis();

				List<Tuple> mfs = getMFS(ac, testCase);
				// System.out.println("get MFS's size " + mfs.size());
				
				if(contain > 1){
					this.identifiedMFSForMultiple.addAll(mfs);
				}


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
		
		this.evaluate_multiple();
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
				this.wrongNumberChecked ++;
				wrong.add(tuple);
			} else {
				this.rightChecked ++;
				right.add(tuple);
			}
		}
		return result;
	}

//	public boolean isIn(Tuple t, List<Tuple> tuples){
//		 for(Tuple t1 : tuples){
//			 if(t.equals(t1))
//				 return true;
//		 }
//		 return false;
//	}
	public List<Tuple> getMFS(AETG_Constraints ac, TestCase testCase) {

		List<Tuple> result = new ArrayList<Tuple>();

		FIC_Constraints sc = new FIC_Constraints(testCase,
				dataCenter.getParam(), caseRunner, ac);

		List<Tuple> temp_mfs = reLocate(ac, sc, new ArrayList<Tuple>());
		
		boolean correctIdentified = false;
		
		// this is may be a problem. because we only test one
		for(Tuple t : temp_mfs){
			if(isIn(t, this.actualMFS)){
				// correct Identified
				correctIdentified = true;
			}else{
				correctIdentified = false;
			}
			break;
		}

		List<List<Tuple>> checkedNonMFS = this.checkMFS(temp_mfs, ac, testCase,
				sc.getTestedTupleTestCases());
		
		this.feedBackStartTimes ++;

		List<Tuple> right = checkedNonMFS.get(0);
		List<Tuple> wrong = checkedNonMFS.get(1);
		
		boolean correctChecked = false; 
		for(Tuple t : right){
			if(isIn(t, this.actualMFS)){
				// correct Identified
				correctChecked = true;
			}else{
				correctChecked = false;
			}
			break;
		}
		
		if(correctIdentified == true && correctChecked == false)
			this.negtiveImporved ++;
		
		if(correctIdentified == false && correctChecked == true)
			this.postiveImporved ++;
		
		if(correctIdentified == false && wrong.size() > 0)
			this.postiveChecked ++;
		
		if(correctIdentified == true && wrong.size() > 0)
			this.negativeChecked ++;
		

		result.addAll(right);

		while (right.size() == 0) { // right is large than 0 , or wrong is 0,
									// i.e., right.size() == 0 or wrong.size() != 0
			temp_mfs = reLocate(ac, sc, right);
			
			for(Tuple t : temp_mfs){
				if(isIn(t, this.actualMFS)){
					// correct Identified
					correctIdentified = true;
				}else{
					correctIdentified = false;
				}
				break;
			}
			
			checkedNonMFS = this.checkMFS(temp_mfs, ac, testCase,
					sc.getTestedTupleTestCases());
			this.feedBackStartTimes ++;
			
		
			right = checkedNonMFS.get(0);
			wrong = checkedNonMFS.get(1);
			
			for(Tuple t : right){
				if(isIn(t, this.actualMFS)){
					// correct Identified
					correctChecked = true;
				}else{
					correctChecked = false;
				}
				break;
			}
			
			
			if(correctIdentified == true && correctChecked == false)
				this.negtiveImporved ++;
			
			if(correctIdentified == false && correctChecked == true)
				this.postiveImporved ++;
	
			
			if(correctIdentified == false && wrong.size() > 0)
				this.postiveChecked ++;
			
			if(correctIdentified == true && wrong.size() > 0)
				this.negativeChecked ++;
			
			result.addAll(right);
		}

		return result;
	}

	public List<Tuple> reLocate(AETG_Constraints ac, FIC_Constraints sc,
			List<Tuple> addedMFS) {
		identificationTimes++;
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
			} else{
				nextTestCase.setTestState(TestCase.FAILED);
				if (containNow != null) {
					int safeState = this.containDifferentNot(containNow,
							nextTestCase);
					if (safeState == 0 || safeState == 1) {
						this.encounterUnsafe++;
					}
					if (safeState == 1) {
						this.triggerDifferentUnsafe++;
					}
				}
			}
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
			if(executed != null)
			executed2.addAll(executed);
			executed2.add(newCase);
			
			
			if (containNow != null) {
				int safeState = this.containDifferentNot(containNow,
						newCase);
				if (safeState == 0 || safeState == 1) {
					this.encounterUnsafe++;
				}
				if (safeState == 1) {
					this.triggerDifferentUnsafe++;
				}
			}
			
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
				else{
					if (containNow != null) {
						int safeState = this.containDifferentNot(containNow,
								newCase2);
						if (safeState == 0 || safeState == 1) {
							this.encounterUnsafe++;
						}
						if (safeState == 1) {
							this.triggerDifferentUnsafe++;
						}
					}
					
			return false;
				}
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

		DataForSafeValueAssumption data = new DataForSafeValueAssumption(8, 2);
		data.setDegree(2);

		showData(data.getDataCenter(), data.getCaseRunner());
		
		TomcatData data2 = new TomcatData();
		data2.setDegree(2);
//		showData(data2.getDataCenter(), data2.getCaseRunner());
		
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
