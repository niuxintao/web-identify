package locatConstaint;

import interaction.DataCenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

//import location.FIC.Pa;

import com.fc.caseRunner.CaseRunner;
import com.fc.caseRunner.CaseRunnerWithBugInject;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

import ct.AETG_Constraints;

// record test cases for each tuple

// for the pass, do not generate

// for the fail, generate 
public class FIC_Constraints {
	protected CaseRunner caseRunner;
	protected TestCase testCase;
	protected int[] param;
	protected List<Tuple> bugs;
	protected List<TestCase> executed;
	private HashMap<TestCase, Boolean> tested;

	private HashMap<Tuple, HashSet<TestCase>> testedTupleTestCases;
	public HashMap<Tuple, HashSet<TestCase>> getTestedTupleTestCases() {
		return testedTupleTestCases;
	}

	private HashMap<Tuple, Boolean> testedTupleBool;

	protected List<Tuple> currentMFS;

	GetBestTestCaseAndConstraints gtc;

	class Pa {
		public List<Integer> CFree;
		public Tuple fixdOne;
	}

	/**
	 * @return the bugs
	 */
	public List<Tuple> getBugs() {
		return bugs;
	}

	public FIC_Constraints(TestCase testCase, int[] param,
			CaseRunner caseRunner, AETG_Constraints ac) {
		tested = new HashMap<TestCase, Boolean>();
		testedTupleTestCases = new HashMap<Tuple, HashSet<TestCase>>();
		testedTupleBool = new HashMap<Tuple, Boolean>();
		this.testCase = testCase;
		this.param = param;
		bugs = new ArrayList<Tuple>();
		this.caseRunner = caseRunner;
		executed = new ArrayList<TestCase>();
		gtc = new GetBestTestCaseAndConstraints(ac, testCase);
		this.currentMFS = new ArrayList<Tuple>();
		// executed.add(testCase);
	}

	public FIC_Constraints(TestCase testCase, int[] param,
			CaseRunner caseRunner, AETG_Constraints ac, List<Tuple> currentMFS) {
		tested = new HashMap<TestCase, Boolean>();
		testedTupleTestCases = new HashMap<Tuple, HashSet<TestCase>>();
		testedTupleBool = new HashMap<Tuple, Boolean>();
		this.testCase = testCase;
		this.param = param;
		bugs = new ArrayList<Tuple>();
		this.caseRunner = caseRunner;
		executed = new ArrayList<TestCase>();
		gtc = new GetBestTestCaseAndConstraints(ac, testCase);
		this.currentMFS = currentMFS;
		// executed.add(testCase);
	}

	public void addMFS(List<Tuple> addedMFS) {
		this.currentMFS.addAll(addedMFS);
	}

	public Pa LocateFixedParam(List<Integer> CFree, Tuple partBug) {

		// convert the List to a int[]
		int[] free = CovertTntegerToInt(CFree);

		// get a Candidate index
		Tuple freeTuple = new Tuple(free.length, testCase);

		freeTuple.setParamIndex(free);
		Tuple noCand = freeTuple.catComm(freeTuple, partBug);

		Tuple cand = noCand.getReverseTuple();
		int[] Ccand = cand.getParamIndex();
		List<Integer> U = new ArrayList<Integer>();
		U.addAll(CFree);

		for (int i = 0; i < Ccand.length; i++) {
			Integer para = new Integer(Ccand[i]);
			List<Integer> temp = new ArrayList<Integer>();
			temp.addAll(U);
			// accroding to sequnce

			if (temp.size() == 0) {
				temp.add(para);
			} else
				for (int j = temp.size() - 1; j >= 0; j--) {
					if (temp.get(j) < para) {
						temp.add(j + 1, para);
						break;
					} else if (temp.get(j) == para) {
						break;
					} else if (j == 0) {
						temp.add(j, para);
						break;
					}
				}
			// System.out.println("temp : " + temp.size());
			if (temp.size() == param.length) {
				break;
			}
			// if (temp.size() == param.length - 1) {
			// Tuple fixone = new Tuple(1, testCase);
			// fixone.setParamIndex(new int[] { para });
			// Pa pa = new Pa();
			// pa.CFree = U;
			// pa.fixdOne = fixone;
			// return pa;
			// }
			if (testTuple(temp)) {
				Tuple fixone = new Tuple(1, testCase);
				fixone.setParamIndex(new int[] { para });
				Pa pa = new Pa();
				pa.CFree = U;
				pa.fixdOne = fixone;
				return pa;
			}
			U = temp;
		}
		Tuple fixone = new Tuple(0, testCase);
		Pa pa = new Pa();
		pa.CFree = U;
		pa.fixdOne = fixone;
		return pa;
	}

	private boolean testTuple(List<Integer> temp) {

		int[] free = CovertTntegerToInt(temp);
		Tuple testTuple = new Tuple(free.length, testCase);
		testTuple.setParamIndex(free);
		
//		System.out.println(testTuple+ " under test");

		if (!this.testedTupleTestCases.containsKey(testTuple)) {

			HashSet<TestCase> testcases = new HashSet<TestCase>();
			this.testedTupleTestCases.put(testTuple, testcases);

			TestCase testCase = Motivate(temp);
			boolean result = this.runTest(testCase);

			testedTupleTestCases.get(testTuple).add(testCase);
			this.testedTupleBool.put(testTuple, result);

			return result;

		} else {
			boolean result = testedTupleBool.get(testTuple);
			if (result)
				return result; // pass do not need to test
			else {
				TestCase testCase = Motivate_different(temp);

				boolean newResult = this.runTest(testCase);

				testedTupleTestCases.get(testTuple).add(testCase);

				this.testedTupleBool.put(testTuple, newResult);

				return newResult;
			}
		}
	}

	public int[] CovertTntegerToInt(List<Integer> CFree) {
		int[] free = new int[CFree.size()];
		for (int i = 0; i < free.length; i++) {
			free[i] = CFree.get(i);
		}
		return free;
	}

	public List<Integer> CovertTntToTnteger(int[] CFree) {
		List<Integer> free = new ArrayList<Integer>();
		for (int i = 0; i < CFree.length; i++) {
			free.add(new Integer(CFree[i]));
		}
		return free;
	}

	protected boolean runTest(TestCase testCase) {
//		System.out.println(testCase.getStringOfTest());
		if (this.tested.containsKey(testCase)) {
			// System.out.println(this.tested.get(testCase));
			return this.tested.get(testCase);
		} else {
			testCase.setTestState(caseRunner.runTestCase(testCase));
			this.tested.put(testCase,
					testCase.testDescription() == TestCase.PASSED);
			executed.add(testCase);

			// System.out.println(testCase.testDescription() ==
			// TestCase.PASSED);
			return testCase.testDescription() == TestCase.PASSED;
		}
	}

	private TestCase Motivate_different(List<Integer> temp) {
		// TODO Auto-generated method stub

		Tuple tuple = new Tuple(temp.size(), testCase);
		int location = 0;
		for (int i : temp) {
			tuple.set(location, i);
			location++;
		}

		HashSet<TestCase> testCases = this.testedTupleTestCases.get(tuple);

		tuple = tuple.getReverseTuple();

		int[] test = this.gtc.getTestCase(tuple, testCases);

		TestCaseImplement newCase = new TestCaseImplement(test);

		// extraCases.addTest(newCase);
		return newCase;
	}

	private TestCase Motivate(List<Integer> temp) {
		// TODO Auto-generated method stub

		Tuple tuple = new Tuple(temp.size(), testCase);
		int location = 0;
		for (int i : temp) {
			tuple.set(location, i);
			location++;
		}
		tuple = tuple.getReverseTuple();

		int[] test = this.gtc.getTestCase(tuple);

		TestCaseImplement newCase = new TestCaseImplement(test);

		// extraCases.addTest(newCase);
		return newCase;
	}

	public Pa LocateFixeedParamBS(List<Integer> CFree, Tuple partBug) {
		// convert the List to a int[]
		int[] free = CovertTntegerToInt(CFree);

		// get a Candidate index
		Tuple freeTuple = new Tuple(free.length, testCase);

		freeTuple.setParamIndex(free);
		Tuple noCand = freeTuple.catComm(freeTuple, partBug);

		Tuple cand = noCand.getReverseTuple();
		int[] Ccand = cand.getParamIndex();
		List<Integer> U = new ArrayList<Integer>();
		U.addAll(CFree);

		Tuple determine = cand.catComm(cand, freeTuple);
		if (Ccand == null
				|| Ccand.length == 0
				|| (partBug.getDegree() > 0 && !this
						.testTuple(CovertTntToTnteger(determine.getParamIndex())))) {
			Tuple fixone = new Tuple(0, testCase);
			Pa pa = new Pa();
			pa.CFree = U;
			pa.fixdOne = fixone;
			return pa;
		}

		int low = 0;
		int high = Ccand.length - 1;
		int middle = (low + high) / 2;
		List<Integer> candList = this.CovertTntToTnteger(Ccand);
		while (low < high) {
			int[] lower = this.CovertTntegerToInt(candList.subList(low,
					middle + 1));
			Tuple Low = new Tuple(lower.length, testCase);
			Low.setParamIndex(lower);

			Tuple Temp = freeTuple.catComm(freeTuple, Low);
			if (this.testTuple(this.CovertTntToTnteger(Temp.getParamIndex()))) {
				high = middle;
				middle = (low + high) / 2;
			} else {
				low = middle + 1;
				middle = (low + high) / 2;
				freeTuple = Temp;
			}
		}
		Tuple fixone = new Tuple(1, testCase);
		fixone.set(0, new Integer(candList.get(low)));
		Pa pa = new Pa();
		pa.CFree = this.CovertTntToTnteger(freeTuple.getParamIndex());
		pa.fixdOne = fixone;
		return pa;
	}

	public Tuple Fic_BS(List<Integer> CTabu) {
		Tuple partBug = new Tuple(0, testCase);
		List<Integer> CFree = new ArrayList<Integer>();
		CFree.addAll(CTabu);
		while (true) {
			Pa pa = this.LocateFixeedParamBS(CFree, partBug);
			CFree = pa.CFree;
			Tuple newRelatedPartBug = pa.fixdOne;
			if (newRelatedPartBug.getDegree() == 0) {
				break;
			}
			partBug = partBug.catComm(partBug, newRelatedPartBug);
		}
		return partBug;
	}

	public boolean isContain(List<Integer> integers, int i) {
		for (Integer ii : integers) {
			if (ii.intValue() == i)
				return true;
		}
		return false;

	}

	public Tuple Fic_ofot(List<Integer> CTabu) {
		Tuple partBug = new Tuple(0, testCase);
		List<Integer> CFree = new ArrayList<Integer>();
		CFree.addAll(CTabu);
		
			while (true) {
				Pa pa = this.LocateFixedParam(CFree, partBug);
				CFree = pa.CFree;
				Tuple newRelatedPartBug = pa.fixdOne;
				if (newRelatedPartBug.getDegree() == 0) {
					break;
				}
				partBug = partBug.catComm(partBug, newRelatedPartBug);
			}
		// if(part)
		return partBug;
	}

	public void FicSingleMuOFOT() {
		List<Integer> CTabu = new ArrayList<Integer>();
		for (Tuple mfs : this.currentMFS) {
			if (this.testCase.containsOf(mfs)) {
				for (int i : mfs.getParamIndex()) {
					if (!isContain(CTabu, i)) {
						CTabu.add(i);
					}
				}
			}
		}

		while (true) {

			// System.out.println("start then identify then");
			if (CTabu.size() > 0 && testTuple(CTabu)) {
				break;
			}

			Tuple bug = Fic_ofot(CTabu);
			int i = 0;
			while (bug.getDegree() == 0) {
//				System.out.print("degree is zero and the cost is " );
//				int executeBefore = this.executed.size();
				bug = Fic_ofot(CTabu);
//				int executeAfter = this.executed.size();
//				System.out.println(executeAfter -  executeBefore);
				i ++;
				if(i > 5){
					break;
				}
			}
			/**
			 * handle *********************
			 */
//			if (bug.getDegree() == 0) {
//				System.out.println("bug zero");
//				break;
//			} else {
//				System.out.println("bug obtained!");
//			}

			this.bugs.add(bug);

			Tuple tuple = new Tuple(CTabu.size(), testCase);
			int[] tabu = CovertTntegerToInt(CTabu);
			tuple.setParamIndex(tabu);

			Tuple newCTabu = tuple.catComm(tuple, bug);
			CTabu.clear();
			CTabu.addAll(CovertTntToTnteger(newCTabu.getParamIndex()));

			// Tuple bug = Fic(CTabu);
			// if (bug.getDegree() == 0) {
			// Tuple fixone = new Tuple(1, testCase);
			// fixone.setParamIndex(new int[] { testCase.getLength() - 1 });
			// bug = fixone;
			// }
			// System.out.println("bug : " + bug.toString() + " bug degree : " +
			// bug.getDegree());

			// this.bugs.add(bug);

			// break;
		}
	}

//	public void FicSingleMuOFOTwithFeedBack() {
//		List<Integer> CTabu = new ArrayList<Integer>();
//		while (true) {
//
//			// System.out.println("start then idenity then");
//			if (CTabu.size() > 0 && testTuple(CTabu)) {
//				break;
//			}
//
//			Tuple bug = Fic(CTabu);
//			this.bugs.add(bug);
//			break;
//		}
//	}

	public void FicNOP() {
		List<Integer> CTabu = new ArrayList<Integer>();
		while (true) {

			// System.out.println("start then idenity then");
			if (CTabu.size() > 0 && testTuple(CTabu)) {
				break;
			}

			Tuple bug = Fic_BS(CTabu);
			// System.out.println("bug : " + bug.toString());
			if (bug.getDegree() == 0)
				break;
			this.bugs.add(bug);

			Tuple tuple = new Tuple(CTabu.size(), testCase);
			int[] tabu = CovertTntegerToInt(CTabu);
			tuple.setParamIndex(tabu);

			Tuple newCTabu = tuple.catComm(tuple, bug);
			CTabu.clear();
			CTabu.addAll(CovertTntToTnteger(newCTabu.getParamIndex()));

			// if(bug.getDegree() == testCase.getLength())
			// break;
		}
	}

	/**
	 * @return the extraCases
	 */
	public List<TestCase> getExecuted() {
		return executed;
	}

	public static void main(String[] args) {
		int[] wrong = new int[] { 1, 1, 1, 1, 1, 1, 1, 1 };
		TestCase wrongCase = new TestCaseImplement();
		wrongCase.setTestState(TestCase.FAILED);
		((TestCaseImplement) wrongCase).setTestCase(wrong);

		int[] param = new int[] { 3, 3, 3, 3, 3, 3, 3, 3 };

		Tuple bugModel = new Tuple(8, wrongCase);
		bugModel.set(0, 0);
		bugModel.set(1, 1);
		bugModel.set(2, 2);
		bugModel.set(3, 3);
		bugModel.set(4, 4);
		bugModel.set(5, 5);
		bugModel.set(6, 6);
		bugModel.set(7, 7);
		// bugModel.set(1, 4);

		// Tuple bugModel2 = new Tuple(1, wrongCase);
		// bugModel2.set(0, 3);
		//
		// Tuple bugModel3 = new Tuple(1, wrongCase);
		// bugModel3.set(0, 6);

		CaseRunner caseRunner = new CaseRunnerWithBugInject();
		((CaseRunnerWithBugInject) caseRunner).inject(bugModel);
		// ((CaseRunnerWithBugInject) caseRunner).inject(bugModel2);
		// ((CaseRunnerWithBugInject) caseRunner).inject(bugModel3);

		AETG_Constraints ac = new AETG_Constraints(new DataCenter(param, 2));

		FIC_Constraints fic = new FIC_Constraints(wrongCase, param, caseRunner,
				ac);
		fic.FicNOP();

		for (int i = 0; i < fic.getExecuted().size(); i++) {
			System.out.println(fic.getExecuted().get(i).getStringOfTest());
		}
		for (Tuple bug : fic.getBugs()) {
			// if (bug.equals(bugModel1) || bug.equals(bugModel2)) {
			// System.out.println("true");
			// }
			System.out.println(bug.toString());
		}
	}
}
